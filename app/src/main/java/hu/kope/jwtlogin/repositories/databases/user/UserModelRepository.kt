package hu.kope.jwtlogin.repositories.databases.user

import android.content.Context
import hu.kope.jwtlogin.models.UserModel
import hu.kope.jwtlogin.models.UserRoleEnum
import hu.kope.jwtlogin.repositories.databases.JWTAppDatabase
import hu.kope.jwtlogin.repositories.databases.user.model.RoomUserModel
import hu.kope.jwtlogin.repositories.databases.user.model.RoomUserRoleEnum
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserModelRepository : IUserModelRepository, KoinComponent {

    private val context: Context by inject()

    private var userModelDao: UserModelDao? = null
        get() {
            if (field == null) {
                val db = JWTAppDatabase.getDatabase(context) as JWTAppDatabase
                field = db.userModelDao()
            }
            return field
        }

    override fun saveUserModel(userModel: UserModel) {
        userModelDao!!.insertOrUpdateUserModel(userModel.toRoomModel())
    }

    override fun getUserModelById(userId: String): UserModel? {
        return userModelDao!!.getUserModel(userId).fromRoomModel()
    }

    override fun deleteUserModelById(userId: String) {
        userModelDao!!.deleteUserModelById(userId)
    }

    override fun deleteAllUserModel() {
        userModelDao!!.deleteAllUserModel()
    }
}

private fun RoomUserModel?.fromRoomModel(): UserModel? {
    if (this != null) {
        return UserModel(
            userId = userId,
            userName = userName,
            fullName = fullName,
            role = UserRoleEnum.valueOf(role.name)
        )
    }
    return null
}

private fun UserModel.toRoomModel(): RoomUserModel {
    return RoomUserModel(
        userId = userId,
        userName = userName,
        fullName = fullName,
        role = RoomUserRoleEnum.valueOf(role.name)
    )
}


