package hu.kope.jwtlogin.presentations.home

import hu.kope.jwtlogin.R
import hu.kope.jwtlogin.interactors.authentication.IAuthenticationInteractor
import hu.kope.jwtlogin.interactors.users.IUserInteractor
import hu.kope.jwtlogin.models.UserRoleEnum
import hu.kope.jwtlogin.presentations.base.BasePresenter
import hu.kope.jwtlogin.views.home.IHomeActivity
import org.koin.core.component.inject


class HomePresenter : BasePresenter(), IHomePresenter {
    private val userInteractor: IUserInteractor by inject()
    private val authenticationInteractor : IAuthenticationInteractor by inject()
    override fun fetchUserData() {
        val userModel = userInteractor.getUserById(preferences.loadUserId())
        showUserName(userModel.fullName)
        showRole(userModel.role)
    }

    override fun deleteUserData() {
        authenticationInteractor.deleteUserData()
    }

    private fun showUserName(userName: String) {
        (baseActivity as IHomeActivity).showUserName(userName)
    }

    private fun showRole(role: UserRoleEnum) {
        (baseActivity as IHomeActivity).showRole(getRole(role))
    }

    private fun getRole(role: UserRoleEnum): Int {
        return when (role) {
            UserRoleEnum.EDITOR -> R.string.text_role_editor
            UserRoleEnum.WRITER -> R.string.text_role_writer
            UserRoleEnum.DIRECTOR -> R.string.text_role_director
            UserRoleEnum.ACTOR -> R.string.text_role_actor
        }
    }
}