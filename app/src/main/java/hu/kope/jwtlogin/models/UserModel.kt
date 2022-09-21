package hu.kope.jwtlogin.models

data class UserModel(
    val userId: String,
    val userName: String,
    val fullName: String,
    val role: UserRoleEnum
)