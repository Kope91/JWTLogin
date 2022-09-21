package hu.kope.jwtlogin.repositories.databases.user

import androidx.room.*
import hu.kope.jwtlogin.repositories.databases.user.model.RoomUserModel

@Dao
interface UserModelDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateUserModel(userModel: RoomUserModel)

    @Transaction
    @Query("DELETE FROM UserModel")
    fun deleteAllUserModel()

    @Transaction
    @Query("DELETE FROM UserModel WHERE userId == :userId")
    fun deleteUserModelById(userId: String)

    @Transaction
    @Query("SELECT * FROM UserModel WHERE userId == :userId")
    fun getUserModel(userId: String): RoomUserModel?

}