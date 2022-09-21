package hu.kope.jwtlogin.views.base

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent


open class BaseActivity : AppCompatActivity(), IBaseActivity, KoinComponent {
    fun onMainThread(function: suspend () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            function()
        }
    }

    fun onIOThread(function: suspend () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            function()
        }
    }

    fun hideKeyboard() {
        val imm: InputMethodManager =
            this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = this.currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }
}