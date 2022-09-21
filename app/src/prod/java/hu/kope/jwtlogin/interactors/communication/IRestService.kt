package hu.kope.jwtlogin.interactors.communication

import hu.kope.jwtlogin.interactors.communication.models.AuthenticationModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IRestService {

    enum class GRANT_TYPE {
        PASSWORD, REFRESH_TOKEN
    }

    @FormUrlEncoded
    @POST("/idp/api/v1/token")
    fun loginWithUserNameAndPassword(
        @Field("userName") userName: String,
        @Field("password") password: String,
        @Field("grant_type") grant_type: GRANT_TYPE,
        @Field("client_id") client_id: String
    ): Call<AuthenticationModel>

    @FormUrlEncoded
    @POST("/idp/api/v1/token")
    fun loginWithRefreshToken(
        @Field("refresh_token") refresh_token: String,
        @Field("grant_type") grant_type: GRANT_TYPE,
        @Field("client_id") client_id: String
    ): Call<AuthenticationModel>

}