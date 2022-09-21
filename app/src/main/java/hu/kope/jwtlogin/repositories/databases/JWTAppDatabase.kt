package hu.kope.jwtlogin.repositories.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.kope.jwtlogin.repositories.databases.user.UserModelDao
import hu.kope.jwtlogin.repositories.databases.user.model.RoomUserModel

@Database(
    version = 1,
    exportSchema = false,
    entities = [RoomUserModel::class]
)

abstract class JWTAppDatabase : RoomDatabase() {
    companion object {

        val DATABASE_NAME = "JWTLoginApp.sqlite"
        private var INSTANCE: JWTAppDatabase? = null
        fun getDatabase(context: Context): JWTAppDatabase? {
            if (INSTANCE == null) {
                synchronized(JWTAppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            JWTAppDatabase::class.java,
                            DATABASE_NAME
                        )
                            .setJournalMode(JournalMode.TRUNCATE)
                            .allowMainThreadQueries()
                            .build()

                    }
                }
            }
            return INSTANCE
        }
    }

    abstract fun userModelDao(): UserModelDao
}