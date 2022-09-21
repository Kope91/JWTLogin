package hu.kope.jwtlogin

import com.fasterxml.jackson.databind.ObjectMapper
import hu.kope.jwtlogin.interactors.authentication.AuthenticationInteractor
import hu.kope.jwtlogin.interactors.authentication.IAuthenticationInteractor
import hu.kope.jwtlogin.interactors.communication.BackendService
import hu.kope.jwtlogin.interactors.communication.IBackendService

import hu.kope.jwtlogin.interactors.jwt.IJWTInteractor
import hu.kope.jwtlogin.interactors.jwt.JWTInteractor
import hu.kope.jwtlogin.interactors.users.IUserInteractor
import hu.kope.jwtlogin.interactors.users.UserInteractor
import hu.kope.jwtlogin.presentations.home.HomePresenter
import hu.kope.jwtlogin.presentations.home.IHomePresenter
import hu.kope.jwtlogin.presentations.login.ILoginPresenter
import hu.kope.jwtlogin.presentations.login.LoginPresenter
import hu.kope.jwtlogin.repositories.databases.user.IUserModelRepository
import hu.kope.jwtlogin.repositories.databases.user.UserModelRepository
import hu.kope.jwtlogin.repositories.preferences.IPreferences
import hu.kope.jwtlogin.repositories.preferences.Preferences
import org.koin.dsl.module

object AppModule {

    fun getModule() = module {

        single {
            ObjectMapper()
        }

        //communication
        single<IBackendService> { BackendService() }

        //repositories
        single<IPreferences> { Preferences() }
        single<IUserModelRepository> { UserModelRepository() }

        //interactors
        single<IJWTInteractor> { JWTInteractor() }
        single<IUserInteractor> { UserInteractor() }
        single<IAuthenticationInteractor> { AuthenticationInteractor() }


        //presenters
        single<ILoginPresenter> { LoginPresenter() }
        single<IHomePresenter> { HomePresenter() }
    }

}