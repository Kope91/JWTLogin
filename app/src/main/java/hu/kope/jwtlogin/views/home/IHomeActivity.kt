package hu.kope.jwtlogin.views.home

import hu.kope.jwtlogin.views.base.IBaseActivity

interface IHomeActivity : IBaseActivity {
    fun showUserName(userName : String)

    fun showRole(role : Int)
}