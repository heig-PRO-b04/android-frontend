package ch.heigvd.pro.b04.android.Poll

import android.app.Application
import androidx.lifecycle.MutableLiveData
import ch.heigvd.pro.b04.android.Datamodel.Question
import ch.heigvd.pro.b04.android.Network.RequestsViewModel

class PollViewModel(application: Application,
                    idModerator : Int,
                    idPoll : Int,
                    token : String
) : RequestsViewModel(application, idModerator, idPoll, token) {
    val questionToView : MutableLiveData<Question> = MutableLiveData()

    fun goToQuestion(question: Question) {
        questionToView.postValue(question)
    }

}
