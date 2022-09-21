package hu.kope.jwtlogin.interactors.users

import hu.kope.jwtlogin.models.UserModel

interface IUserInteractor {
    fun saveUserModel(userModel: UserModel)
    fun getUserById(userId: String): UserModel
    fun deleteUserById(userId: String)
}