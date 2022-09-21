package hu.kope.jwtlogin.interactors.users

import hu.kope.jwtlogin.interactors.base.BaseInteractor
import hu.kope.jwtlogin.models.UserModel
import hu.kope.jwtlogin.repositories.databases.user.IUserModelRepository
import org.koin.core.component.inject


class UserInteractor : BaseInteractor(), IUserInteractor {

    private val userRepository: IUserModelRepository by inject()

    override fun saveUserModel(userModel: UserModel) {
        userRepository.saveUserModel(userModel)
    }

    override fun getUserById(userId: String): UserModel {
        return userRepository.getUserModelById(userId)!!
    }

    override fun deleteUserById(userId: String) {
        userRepository.deleteUserModelById(userId)
    }
}