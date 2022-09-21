package hu.kope.jwtlogin.repositories.databases.user.model

import androidx.room.Entity

@Entity(tableName = "UserModel", primaryKeys = ["userId"])
data class RoomUserModel(
    val userId: String,
    val userName: String,
    val fullName: String,
    val role: RoomUserRoleEnum
)