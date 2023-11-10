package indie.mefistofel.game15

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import indie.mefistofel.game15.IFragmentInteractionListener.*

class GameActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    IFragmentInteractionListener {
    private var mCurrentFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView: NavigationView = this.findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val fragmentManager: FragmentManager = supportFragmentManager
        mCurrentFragment = fragmentManager.findFragmentByTag(TAG_CURRENT_FRAGMENT)
        if (mCurrentFragment == null) {
            shuffleNewGame()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mCurrentFragment is IFragmentKey) {
            (mCurrentFragment as IFragmentKey?)!!.OnKeyDown(keyCode)
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.nav_shuffle) {
            shuffleNewGame()
        } else if (id == R.id.nav_info) {
            onFragmentInteraction(Action.info)
        }
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction(action: Action) {
        when (action) {
            Action.win -> showFragment(WinFragment())
            Action.shuffle -> shuffleNewGame()
            Action.info -> showFragment(InfoFragment(), true)
        }
    }

    private fun shuffleNewGame() {
        FieldData.getInstance().shuffleFields()
        showFragment(GameFragment())
    }

    fun showFragment(fragment: Fragment) {
        showFragment(fragment, false)
    }

    fun showFragment(fragment: Fragment, addToBackStack: Boolean) {
        mCurrentFragment = fragment
        val myFragmentManager: FragmentManager = getSupportFragmentManager()
        val fragmentTransaction: FragmentTransaction = myFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment, TAG_CURRENT_FRAGMENT)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    companion object {
        private const val TAG_CURRENT_FRAGMENT = "current_fragment"
    }
}