package feifei.feeling;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar      mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView        mNavigationView;

    NewsFragment newsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name,
                R.string.app_name);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(mNavigationView);
        switch2News();
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) mNavigationView.getHeaderView(0).findViewById(R.id.profile_image);
//        simpleDraweeView.setImageURI(Uri.parse("peizhifei.github.io/imgs/me/logo.jpg"));
        simpleDraweeView.setImageURI(Uri.parse("res:///" + R.drawable.logo));
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
//                         switchNavigation(menuItem.getItemId());
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                });
    }

    public void switch2News() {
        if (newsFragment == null) {
            newsFragment = new NewsFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, newsFragment).commit();
        mToolbar.setTitle(R.string.navigation_news);
    }

    public void switch2Images() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new NewsFragment()).commit();
        mToolbar.setTitle(R.string.navigation_images);
    }

    public void switch2Weather() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new NewsFragment()).commit();
        mToolbar.setTitle(R.string.navigation_weather);
    }

    public void switch2About() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new NewsFragment()).commit();
        mToolbar.setTitle(R.string.navigation_about);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            newsFragment.refresh();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.action_b)
    public void onClick() {
        startActivityForResult(new Intent(this, PublishActivity.class), 5);
    }
}
