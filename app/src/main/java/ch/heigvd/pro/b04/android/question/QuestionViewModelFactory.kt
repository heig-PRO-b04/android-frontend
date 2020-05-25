package ch.heigvd.pro.b04.android.question

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ch.heigvd.pro.b04.android.datamodel.Question

class QuestionViewModelFactory(
    private val application: Application,
    private val question : Question,
    private val token : String
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val constructors = modelClass.declaredConstructors
        return constructors[0].newInstance(application, question, token) as T
    }
}
