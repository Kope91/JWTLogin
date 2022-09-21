package hu.kope.jwtlogin.repositories.databases

import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.kope.jwtlogin.models.UserModel
import hu.kope.jwtlogin.models.UserRoleEnum
import hu.kope.jwtlogin.repositories.databases.user.IUserModelRepository
import hu.kope.jwtlogin.repositories.databases.user.UserModelRepository
import org.junit.*
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class UserModelRepositoryTest {

    companion object {
        private val USER_ID = UUID.randomUUID().toString()
        private val USER_NAME = "Kope91"
        private val FULL_NAME = "GivenName FamiliyName"
        private val ROLE = UserRoleEnum.EDITOR

        private lateinit var userModelRepository: IUserModelRepository


        val userModel =
            UserModel(USER_ID, USER_NAME, FULL_NAME, ROLE)

        @BeforeClass
        @JvmStatic
        fun setup() {
            userModelRepository = UserModelRepository()
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            userModelRepository.deleteAllUserModel()
        }

    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        userModelRepository.deleteAllUserModel()
    }

    @Test
    fun test_delete_all_userModel() {
        val userId = UUID.randomUUID().toString()
        val userModel2 =
            UserModel(userId, USER_NAME, FULL_NAME, ROLE)

        userModelRepository.saveUserModel(userModel)
        userModelRepository.saveUserModel(userModel2)

        userModelRepository.deleteAllUserModel()

        Assert.assertNull(userModelRepository.getUserModelById(USER_ID))
        Assert.assertNull(userModelRepository.getUserModelById(userId))
    }

    @Test
    fun test_getUserModelById_not_exists_user() {
        Assert.assertNull(userModelRepository.getUserModelById(USER_ID))
    }

    @Test
    fun test_save_and_getUserModelById() {
        userModelRepository.saveUserModel(userModel)

        val savedUserModel = userModelRepository.getUserModelById(USER_ID)

        Assert.assertEquals(userModel, savedUserModel)
    }

    @Test
    fun test_delete_saved_userModel() {
        userModelRepository.saveUserModel(userModel)
        userModelRepository.deleteUserModelById(USER_ID)
        Assert.assertNull(userModelRepository.getUserModelById(USER_ID))
    }

}