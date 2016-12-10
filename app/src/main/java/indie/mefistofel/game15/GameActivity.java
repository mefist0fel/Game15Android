package indie.mefistofel.game15;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IFragmentInteractionListener {

    private Fragment mCurrentFragment;
    private static final String TAG_CURRENT_FRAGMENT = "current_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mCurrentFragment = fragmentManager.findFragmentByTag(TAG_CURRENT_FRAGMENT);
        if (mCurrentFragment == null) {
            shuffleNewGame();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_shuffle) {
            shuffleNewGame();
        } else if (id == R.id.nav_info) {
            onFragmentInteraction(Action.info);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Action action) {
        switch (action) {
            case win:
                showFragment(new WinFragment());
                break;
            case shuffle:
                shuffleNewGame();
                break;
            case info:
                showFragment(new InfoFragment(), true);
                break;
        }
    }

    private void shuffleNewGame() {
        FieldData.getInstance().shuffleFields();
        showFragment(new GameFragment());
    }

    void showFragment(Fragment fragment) {
        showFragment(fragment, false);
    }

    void showFragment(Fragment fragment, boolean addToBackStack) {
        mCurrentFragment = fragment;
        FragmentManager myFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, TAG_CURRENT_FRAGMENT);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}
