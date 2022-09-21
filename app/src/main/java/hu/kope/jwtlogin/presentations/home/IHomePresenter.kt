package hu.kope.jwtlogin.presentations.home

import hu.kope.jwtlogin.presentations.base.IBasePresenter

interface IHomePresenter : IBasePresenter {

    fun fetchUserData()
    fun deleteUserData()

}