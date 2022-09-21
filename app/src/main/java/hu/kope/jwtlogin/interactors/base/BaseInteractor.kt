package hu.kope.jwtlogin.interactors.base

import hu.kope.jwtlogin.repositories.preferences.IPreferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BaseInteractor : KoinComponent {

    val preferences: IPreferences by inject()



}