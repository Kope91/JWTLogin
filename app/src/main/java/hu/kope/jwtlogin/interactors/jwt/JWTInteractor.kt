package hu.kope.jwtlogin.interactors.jwt

import hu.kope.jwtlogin.interactors.base.BaseInteractor
import hu.kope.jwtlogin.interactors.communication.exceptions.RefreshTokenExpired
import hu.kope.jwtlogin.models.UserModel
import hu.kope.jwtlogin.models.UserRoleEnum
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*


class JWTInteractor : BaseInteractor(), IJWTInteractor {

    override fun createRefreshToken(): String {
        return Jwts.builder()
            .setExpiration(createExpirationDate(TOKEN_EXPIRATION_IN_SECONDS))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY.toByteArray())
            .compact()
    }

    override fun createJWT(userModel: UserModel, expiresInSeconds: Long): String {
        return Jwts.builder().claim(CLAIM_USER_ID, userModel.userId)
            .claim(CLAIM_USER_NAME, userModel.userName)
            .claim(CLAIM_FULL_NAME, userModel.fullName)
            .claim(CLAIM_ROLE, userModel.role.toString().lowercase())
            .setExpiration(createExpirationDate(expiresInSeconds))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY.toByteArray())
            .compact()
    }

    private fun createExpirationDate(expiresInSeconds: Long): Date? {
        return Date.from(Date().toInstant().plusSeconds(expiresInSeconds))
    }

    override fun decodeJWT(JWTToken: String): UserModel {
        val claims: Claims = parseJWTToken(JWTToken)
        return UserModel(
            getClaimByName(claims, CLAIM_USER_ID),
            getClaimByName(claims, CLAIM_USER_NAME),
            getClaimByName(claims, CLAIM_FULL_NAME),
            UserRoleEnum.valueOf(getClaimByName(claims, CLAIM_ROLE).uppercase())
        )
    }


    private fun parseJWTToken(JWTToken: String): Claims {
        return Jwts.parser().setSigningKey(SECRET_KEY.toByteArray()).setAllowedClockSkewSeconds(5)
            .parseClaimsJws(JWTToken).body
    }

    private fun getClaimByName(claims: Claims, claimName: String): String {
        return claims[claimName, String::class.java]
    }

    override fun JWTTokenIsExpired(JWTToken: String): Boolean {
        return try {
            val claims: Claims = parseJWTToken(JWTToken)
            claims.expiration.before(Date())
        } catch (e: ExpiredJwtException) {
            throw RefreshTokenExpired()
        }
    }

    companion object {
        const val TOKEN_EXPIRATION_IN_SECONDS: Long = 119
        const val SECRET_KEY = "secret_key"
        const val CLAIM_USER_ID = "idp:user_id"
        const val CLAIM_USER_NAME = "idp:user_name"
        const val CLAIM_FULL_NAME = "idp:fullname"
        const val CLAIM_ROLE = "role"
    }

}