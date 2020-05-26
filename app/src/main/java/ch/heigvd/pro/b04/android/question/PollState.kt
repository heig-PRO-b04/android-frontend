package ch.heigvd.pro.b04.android.question

import ch.heigvd.pro.b04.android.datamodel.Answer
import ch.heigvd.pro.b04.android.datamodel.Poll
import ch.heigvd.pro.b04.android.datamodel.Question
import ch.heigvd.pro.b04.android.network.RockinAPI
import ch.heigvd.pro.b04.android.network.keepBody
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

/**
 * How long the user's vote is considered as more relevant than the server value.
 */
const val GRACE_DELAY_IN_MILLIS = 7500L

/**
 * The general refresh frequency. The smaller the delay, the more real-time-ish the app.
 */
const val FRESH_DELAY_IN_MILLIS = 1000L

/**
 * A type alias for a type which has a sequence of states with an identifier different for two
 * strictly consecutive sequence items.
 */
typealias Sequenced<T> = Pair<T, Int>

/**
 * A data class representing the [Model] of the currently displayed poll. The [map] values will
 * contain a best-effort guess of what the server state is, and takes into consideration the user
 * for the last [GRACE_DELAY_IN_MILLIS].
 */
data class Model(
        val poll: Poll,
        var current: Question,
        val token: String,
        var map: MutableMap<Question, List<FetchedAnswer>>
)

/**
 * A data class representing an [Answer], as well as a freshness stamp indicating when it was
 * fetched from the server.
 *
 * @see GRACE_DELAY_IN_MILLIS
 */
data class FetchedAnswer(
        var timestamp: Long,
        var answer: Answer
)

/**
 * A sealed class representing all the different events that might be triggered by the model. These
 * events might be launched based on user triggers (such as voting for an answer), or recurrent
 * triggers, like when the user loads the application.
 */
sealed class Event {

    object NoOp : Event()

    // User events.
    object MoveToNext : Event()
    object MoveToPrevious : Event()
    class SetVote(val answer: Answer) : Event()

    // Data events.
    class GotQuestions(val questions: List<Question>) : Event()
    class GotAnswers(val question: Question, val answers: List<FetchedAnswer>) : Event()

    // Refresh events.
    object RefreshQuestions : Event()
    object RefreshCurrentAnswers : Event()
}

/**
 * A class representing the state model of the poll and its different questions. The architecture is
 * very inspired by the Elm Architecture : a Model gets updated through Messages, and the view is
 * notified of Model changes to render the right data.
 *
 * @param scope A [CoroutineScope] to execute some flows in.
 * @param poll The [Poll] to display.
 * @param question The selected [Question] at start.
 * @param token The user token.
 *
 * @param moveToNext A [Flow] that emits when the user clicks the next button.
 * @param moveToPrevious A [Flow] that emits when the user clicks the previous button.
 * @param votes A [Flow] that emits when the user votes for an answer.
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class PollState(
        // Scope
        scope: CoroutineScope,

        // Data.
        poll: Poll,
        question: Question,
        token: String,

        // User actions.
        moveToNext: Flow<Unit>,
        moveToPrevious: Flow<Unit>,
        votes: Flow<Answer>
) {

    /**
     * A [BroadcastChannel] that consists of a buffer of all the [Event]s that have not been
     * processed by the main loop yet.
     */
    private val buffer = BroadcastChannel<Event>(Channel.Factory.BUFFERED)

    /**
     * A [MutableStateFlow] that acts as a single source of truth for all the [Flow]s that are
     * exposed outside of the [PollState]. A [Sequenced] [Model] is required, since the instance
     * does not change otherwise.
     *
     * Using a [MutableStateFlow] avoids duplicate [Flow]s, since [Flow]s are cold by default.
     */
    private val innerState: MutableStateFlow<Sequenced<Model>> = MutableStateFlow(Pair(Model(
            poll,
            question,
            token,
            mutableMapOf<Question, List<FetchedAnswer>>()
    ), 0))

    /**
     * The model that is exposed to the outside world directly. This might be hidden in the future,
     * to avoid unwanted mutable state modifications.
     */
    @Deprecated(level = DeprecationLevel.WARNING, message = "Please do not work with the internal mutable state directly")
    val data = innerState.map { it.first }

    /**
     * A [Flow] of all the [Event]s that need to be processed by the pipeline.
     */
    private val events = merge(
            buffer.asFlow(),
            reloadAllQuestionsPeriodically(),
            reloadAnswersPeriodically(),
            markAnswerChecked(votes),
            moveToNext.map { Event.MoveToNext },
            moveToPrevious.map { Event.MoveToPrevious }
    )

    /**
     * How many answers are currently checked.
     */
    val nbCheckedAnswer: Flow<Int> = data.map {
        it.map[it.current]?.map { it.answer }?.filter { it.isChecked }?.count() ?: 0
    }

    init {
        scope.launch {
            events.collect { event ->
                val (current, number) = innerState.value
                val (next, actions) = transform(current, event)
                scope.launch { actions.collect { event -> buffer.send(event) } }
                innerState.value = next to (number + 1)
            }
        }
    }
}

/**
 * Following the Elm Architecture design, a [Model] is updated with an [Event], and results in a
 * tuple of a [Model] with updated content, and a [Flow] of [Event] that might be triggered later.
 *
 * @param data The current state.
 * @param event The processed event.
 *
 * @return A [Pair] of a [Model] and a [Flow] of [Event].
 */
private suspend fun transform(data: Model, event: Event): Pair<Model, Flow<Event>> {
    return when (event) {
        is Event.NoOp -> data to emptyFlow()
        // Get the current question, and move to the next one (based on index)
        is Event.MoveToNext -> {
            val nextCurrent = data.map.keys
                    .filter { it.indexInPoll > data.current.indexInPoll }
                    .minBy { it.indexInPoll }
                    ?: data.current
            data.copy(current = nextCurrent) to flowOf(Event.RefreshCurrentAnswers)
        }
        // Get the current question, and move to the previous one (based on index)
        is Event.MoveToPrevious -> {
            val nextCurrent = data.map.keys
                    .filter { it.indexInPoll < data.current.indexInPoll }
                    .maxBy { it.indexInPoll }
                    ?: data.current
            data.copy(current = nextCurrent) to flowOf(Event.RefreshCurrentAnswers)
        }
        // Vote for a certain answer, persist the state locally, then inform the server. Reset the
        // grace period
        is Event.SetVote -> {
            val fetched = data.map[data.current]?.first { it.answer.idAnswer == event.answer.idAnswer }
            fetched?.answer?.toggle()
            fetched?.timestamp = System.currentTimeMillis()
            val effect = flow {
                emit(fetched?.answer?.let {
                    RockinAPI.voteForAnswerSuspending(it, data.token)
                })
            }.map { Event.NoOp }
            data to effect
        }
        // Update the list of displayed questions
        is Event.GotQuestions -> {
            val updated = mutableMapOf<Question, List<FetchedAnswer>>()
            for ((question, values) in data.map) {
                val update = event.questions.firstOrNull { it.idQuestion == question.idQuestion }
                if (update != null) {
                    updated[update] = values
                }
            }
            for (question in event.questions) {
                if (!updated.keys.any { it.idQuestion == question.idQuestion })
                    updated[question] = emptyList()
            }
            data.map = updated
            data to emptyFlow()
        }
        // Update the list of displayed answers
        is Event.GotAnswers -> {
            val answers = data.map[event.question] ?: emptyList()
            val updated = mutableListOf<FetchedAnswer>()
            for (answer in answers) {
                val update = event.answers.firstOrNull { it.answer.idAnswer == answer.answer.idAnswer }
                if (update != null && update.timestamp + GRACE_DELAY_IN_MILLIS >= answer.timestamp) {
                    updated.add(update)
                }
            }
            for (update in event.answers) {
                if (!updated.any { it.answer.idAnswer == update.answer.idAnswer }) {
                    updated.add(update)
                }
            }
            data.map[event.question] = updated
            data to emptyFlow()
        }
        // Ask the runtime to refresh the list of all the questions
        is Event.RefreshQuestions -> {
            val events =
                    flow { emit(RockinAPI.getQuestionsSuspending(data.poll, data.token)) }
                            .keepBody()
                            .map { Event.GotQuestions(it) }
            data to events
        }
        // Ask the runtime to refresh the list of the answers for the current question
        is Event.RefreshCurrentAnswers -> {
            val now = System.currentTimeMillis()
            val events = flow { emit(RockinAPI.getAnswersSuspending(data.current, data.token)) }
                    .keepBody()
                    .map { answers -> data.current to answers.map { answer -> FetchedAnswer(now, answer) } }
                    .map { (q, a) -> Event.GotAnswers(q, a) }
            data to events
        }
    }
}

/**
 * A [Flow] that emits regularly to get a list of all the questions.
 */
fun reloadAllQuestionsPeriodically() = flow<Event> {
    do {
        emit(Event.RefreshQuestions)
        delay(FRESH_DELAY_IN_MILLIS)
    } while (true)
}

/**
 * A [Flow] that transforms the checked [Answer] instances into a [Flow] of [Event].
 */
@ExperimentalCoroutinesApi
fun markAnswerChecked(state: Flow<Answer>): Flow<Event> {
    return state.map { answer -> Event.SetVote(answer) }
}

/**
 * A [Flow] that emits regularly to refresh the currently displayed question.
 */
@ExperimentalCoroutinesApi
fun reloadAnswersPeriodically() = flow<Event> {
    do {
        emit(Event.RefreshCurrentAnswers)
        delay(FRESH_DELAY_IN_MILLIS)
    } while (true)
}