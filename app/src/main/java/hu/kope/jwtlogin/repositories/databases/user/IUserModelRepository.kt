package hu.kope.jwtlogin.repositories.databases.user

import hu.kope.jwtlogin.models.UserModel

interface IUserModelRepository {

    fun saveUserModel(userModel: UserModel)

    fun getUserModelById(userId: String): UserModel?

    fun deleteUserModelById(userId: String)

    fun deleteAllUserModel()

}