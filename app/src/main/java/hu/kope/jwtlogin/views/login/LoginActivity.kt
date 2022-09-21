package hu.kope.jwtlogin.views.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import hu.kope.jwtlogin.BuildConfig
import hu.kope.jwtlogin.databinding.ActivityLoginBinding
import hu.kope.jwtlogin.presentations.login.ILoginPresenter
import hu.kope.jwtlogin.views.base.BaseActivity
import hu.kope.jwtlogin.views.home.HomeActivity
import hu.kope.jwtlogin.views.login.models.AuthenticationResult
import hu.kope.jwtlogin.views.login.models.InputValidationResult
import hu.kope.jwtlogin.views.login.models.LoginCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class LoginActivity : BaseActivity(), ILoginActivity {

    private val presenter: ILoginPresenter by inject()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userName: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var login: Button
    private lateinit var loader: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachScreen(this)
        presenter.generateClientId()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userName = binding.username
        password = binding.password
        login = binding.login
        loader = binding.loading.loader

        setupInputFields()
        setupButtons()
        if (presenter.hasRefreshToken()) {
            loginWithRefreshToken()
        }
    }

    private fun setupButtons() {
        login.apply {
            setOnClickListener {
                hideKeyboard()
                loginWithUserNameAndPassword(
                    LoginCredential(
                        userName.text.toString(),
                        password.text.toString()
                    )
                )
            }
        }
    }


    private fun setupInputFields() {
        userName.apply {
            afterTextChanged {
                presenter.validateUserNameAndPassword(
                    LoginCredential(
                        userName.text.toString(),
                        password.text.toString()
                    )
                )
            }
        }
        password.apply {
            afterTextChanged {
                presenter.validateUserNameAndPassword(
                    LoginCredential(
                        userName.text.toString(),
                        password.text.toString()
                    )
                )
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        hideKeyboard()
                        loginWithUserNameAndPassword(
                            LoginCredential(
                                userName.text.toString(),
                                password.text.toString()
                            )
                        )
                    }
                }
                false
            }
        }

    }

    private fun loginWithRefreshToken() {
        showLoader()
        var result: AuthenticationResult?
        onIOThread {
            if (BuildConfig.USE_MOCK_SERVER) {
                withContext(Dispatchers.IO) {
                    Thread.sleep((1000..2000).random().toLong())
                }
            }
            result = suspendCoroutine {
                onMainThread {
                    it.resume(
                        presenter.loginWithRefreshToken()
                    )
                }
            }
            onMainThread { handleAuthenticationResult(result) }
        }
    }

    override fun showInputValidationResult(inputValidationResult: InputValidationResult) {
        inputValidationResult.usernameError?.let {
            userName.error = getString(inputValidationResult.usernameError!!)
        }
        inputValidationResult.passwordError?.let {
            password.error = getString(inputValidationResult.passwordError!!)
        }
        login.isEnabled = inputValidationResult.isDataValid
    }

    private fun loginWithUserNameAndPassword(loginCredential: LoginCredential) {
        showLoader()
        var result: AuthenticationResult?
        onIOThread {
            if (BuildConfig.USE_MOCK_SERVER) {
                withContext(Dispatchers.IO) {
                    Thread.sleep((1000..2000).random().toLong())
                }
            }
            result = suspendCoroutine {
                onIOThread {

                    it.resume(
                        presenter.loginWithUserNameAndPassword(loginCredential)
                    )
                }
            }
            onMainThread { handleAuthenticationResult(result) }
        }
    }

    private fun handleAuthenticationResult(result: AuthenticationResult?) {
        hideLoader()
        result?.let {
            if (it.authenticationFinishedWithSuccess) {
                navigateToHomeScreen()
            } else {
                showErrorText(result.authenticationError)
            }
        }
    }

    private fun showErrorText(text: Int?) {
        text?.let {
            val snackbar =
                Snackbar.make(binding.parentLayout, getString(text), Snackbar.LENGTH_LONG)
            snackbar.show()
        }


    }

    private fun navigateToHomeScreen() {
        val i = Intent(this, HomeActivity::class.java)
        this.startActivity(i)
    }


    private fun hideLoader() {
        loader.visibility = View.GONE
    }

    private fun showLoader() {
        loader.visibility = View.VISIBLE
    }
}


private fun AppCompatEditText.afterTextChanged(afterTextChanged: () -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}







