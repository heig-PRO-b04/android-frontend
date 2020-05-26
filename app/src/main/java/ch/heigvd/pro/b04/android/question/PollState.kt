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

// MODEL

data class Model(
        val poll: Poll,
        var current: Question,
        val token: String,
        var map: MutableMap<Question, List<FetchedAnswer>>
)

data class FetchedAnswer(
        var timestamp: Long,
        var answer: Answer
)

// EVENTS

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

// VIEWMODEL

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
    private val buffer = BroadcastChannel<Event>(Channel.Factory.UNLIMITED)

    private val events = merge(
            buffer.asFlow(),
            reloadAllQuestionsPeriodically(),
            reloadAnswersPeriodically(),
            markAnswerChecked(votes),
            moveToNext.map { Event.MoveToNext },
            moveToPrevious.map { Event.MoveToPrevious }
    )

    val data = flow {
        var current = Model(
                poll,
                question,
                token,
                mutableMapOf<Question, List<FetchedAnswer>>()
        )
        events.collect { event ->
            val (next, actions) = transform(current, event)
            scope.launch { actions.collect { event -> buffer.send(event) } }
            current = next
            emit(next)
        }
    }

    //val question: Flow<Question> = data
    //        .map { (_, question, _) -> question }

    //val answers: Flow<List<Answer>> = data
    //        .map { (_, question, _, answers) -> answers[question] ?: emptyList() }
    //        .map { list -> list.map { (_, answer) -> answer } }
}

suspend fun transform(data: Model, event: Event): Pair<Model, Flow<Event>> {
    return when (event) {
        is Event.NoOp -> data to emptyFlow()
        is Event.MoveToNext -> {
            val nextCurrent = data.map.keys
                    .filter { it.indexInPoll > data.current.indexInPoll }
                    .minBy { it.indexInPoll }
                    ?: data.current
            data.copy(current = nextCurrent) to emptyFlow()
        }
        is Event.MoveToPrevious -> {
            val nextCurrent = data.map.keys
                    .filter { it.indexInPoll < data.current.indexInPoll }
                    .maxBy { it.indexInPoll }
                    ?: data.current
            data.copy(current = nextCurrent) to emptyFlow()
        }
        is Event.SetVote -> {
            val fetched = data.map[data.current]?.first { it.answer.idAnswer == event.answer.idAnswer }
            fetched?.answer?.toggle()
            fetched?.timestamp = System.currentTimeMillis()
            data to emptyFlow()
        }
        is Event.GotQuestions -> {
            val updated = mutableMapOf<Question, List<FetchedAnswer>>()
            for ((question, values) in data.map) {
                val update = event.questions.firstOrNull { it.idQuestion == question.idQuestion }
                if (update != null) {
                    updated[update] = values
                }
            }
            for (question in event.questions) {
                if (!updated.containsKey(question))
                    updated[question] = emptyList()
            }
            data.map = updated
            data to emptyFlow()
        }
        is Event.GotAnswers -> {
            val answers = data.map[event.question] ?: emptyList()
            val updated = mutableListOf<FetchedAnswer>()
            for (answer in answers) {
                val update = event.answers.firstOrNull { it.answer.idAnswer == answer.answer.idAnswer }
                if (update != null && update.timestamp + 7500L >= answer.timestamp ) {
                    updated.add(update)
                }
            }
            for (update in event.answers) {
                if (!updated.contains(update)) {
                    updated.add(update)
                }
            }
            data.map[event.question] = updated
            data to emptyFlow()
        }
        is Event.RefreshQuestions -> {
            val events =
                    flow { emit(RockinAPI.getQuestionsSuspending(data.poll, data.token)) }
                            .keepBody()
                            .map { Event.GotQuestions(it) }
            data to events
        }
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

fun reloadAllQuestionsPeriodically() = flow<Event> {
    do {
        emit(Event.RefreshQuestions)
        delay(1000)
    } while (true)
}

@ExperimentalCoroutinesApi
fun markAnswerChecked(state: Flow<Answer>): Flow<Event> {
    return state.filterNotNull()
            .map { answer -> Event.SetVote(answer) }
}

@ExperimentalCoroutinesApi
fun reloadAnswersPeriodically() = flow<Event> {
    do {
        emit(Event.RefreshCurrentAnswers)
        delay(1000)
    } while (true)
}