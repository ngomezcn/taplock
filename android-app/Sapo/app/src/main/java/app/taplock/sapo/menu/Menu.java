package app.taplock.sapo.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import app.taplock.sapo.HelpFragment;
import app.taplock.sapo.NotificationsFragment;
import app.taplock.sapo.ProfileFragment;
import app.taplock.sapo.R;
import app.taplock.sapo.home.HomeFragment;

public class Menu extends AppCompatActivity {
    BottomNavigationView btnNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnNav = findViewById(R.id.bottom_navigation);
        btnNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, new HomeFragment()).commit();

        BadgeDrawable badge_notifications = btnNav.getOrCreateBadge(R.id.notification);

        badge_notifications.setBackgroundColor(Color.RED);
        badge_notifications.setBadgeTextColor(Color.YELLOW);
        badge_notifications.setMaxCharacterCount(3);
        badge_notifications.setNumber(3);
        badge_notifications.setVisible(true);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.home:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.profile:
                    selectedFragment = new ProfileFragment();
                    break;

                case R.id.notification:
                    selectedFragment = new NotificationsFragment();
                    btnNav.getOrCreateBadge(R.id.notification).clearNumber();
                    btnNav.getOrCreateBadge(R.id.notification).setVisible(false);
                    break;

                case R.id.help:
                    selectedFragment = new HelpFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_layout
                    ,selectedFragment).commit();

            return true;
        }
    };


}