package hu.kope.jwtlogin.views.home

import android.os.Bundle
import androidx.appcompat.widget.AppCompatTextView
import hu.kope.jwtlogin.databinding.ActivityHomeBinding
import hu.kope.jwtlogin.presentations.home.IHomePresenter
import hu.kope.jwtlogin.views.base.BaseActivity
import org.koin.core.component.inject

class HomeActivity : BaseActivity(), IHomeActivity {
    private val presenter: IHomePresenter by inject()
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userName: AppCompatTextView
    private lateinit var role: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachScreen(this)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = binding.username
        role = binding.role

        presenter.fetchUserData()
    }

    override fun showUserName(userName: String) {
        this.userName.text = userName
    }

    override fun showRole(role: Int) {
        this.role.text = getString(role)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.deleteUserData()
    }
}