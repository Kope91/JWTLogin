package hu.kope.jwtlogin.presentations.base

import hu.kope.jwtlogin.repositories.preferences.IPreferences
import hu.kope.jwtlogin.views.base.IBaseActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BasePresenter : IBasePresenter, KoinComponent {

    val preferences: IPreferences by inject()

    lateinit var baseActivity: IBaseActivity


    override fun attachScreen(baseActivity: IBaseActivity) {
        this.baseActivity = baseActivity
    }

}