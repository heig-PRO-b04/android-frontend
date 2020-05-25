package ch.heigvd.pro.b04.android.Poll

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PollViewModelFactory(
    private val application: Application,
    private val idModerator: Int,
    private val idPoll: Int,
    private val token: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val constructors = modelClass.declaredConstructors
        return constructors[0].newInstance(application, idModerator, idPoll, token) as T
    }
}
