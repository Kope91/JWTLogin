package hu.kope.jwtlogin.views.login

import hu.kope.jwtlogin.views.base.IBaseActivity
import hu.kope.jwtlogin.views.login.models.InputValidationResult

interface ILoginActivity : IBaseActivity {

    fun showInputValidationResult(inputValidationResult: InputValidationResult)
}