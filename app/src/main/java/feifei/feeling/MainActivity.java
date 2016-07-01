package feifei.feeling;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.view.ViewPropertyAnimator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import feifei.feeling.fab.FloatingActionButton;

public class MainActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar      mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    public FloatingActionButton getActionButton() {
        return actionButton;
    }

    @Bind(R.id.action_b)
    feifei.feeling.fab.FloatingActionButton actionButton;

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
        simpleDraweeView.setOnClickListener(v -> {
            mDrawerLayout.closeDrawers();
            startActivity(new Intent(this, UserCenter.class));
        });
        mNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.item_content:
                    mDrawerLayout.closeDrawers();
                    switch2News();
                    break;
                case R.id.item_images:
                    mDrawerLayout.closeDrawers();
                    switch2Images();
                    break;
                case R.id.item_about:
                    mDrawerLayout.closeDrawers();
                    switch2Weather();
                    break;
                default:
                    mDrawerLayout.closeDrawers();
                    switch2News();
                    break;
            }
            return false;
        });

    }


//    @Override
//    public void onEnterAnimationComplete() {
//        super.onEnterAnimationComplete();
//        newsFragment.setAdapter();
//        if (start) {
//            newsFragment.anim();
//        }
//    }

    //动画解决了
    boolean start = true;

    @Override
    protected void onRestart() {
        super.onRestart();
        start = false;
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

    int x = 0;

    public void switch2News() {
        x = 1;
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
            new android.os.Handler().postDelayed(() -> {
                newsFragment.refresh(false);
            }, 1000);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick(R.id.action_b)
    public void onClick() {
//        newsFragment.change();
        int[] startingLocation = new int[2];
        actionButton.getLocationOnScreen(startingLocation);
        startingLocation[0] += actionButton.getWidth() / 2;
        PublishActivity.startCameraFromLocation(startingLocation, this);
        overridePendingTransition(0, 0);
//        startActivityForResult(new Intent(this, PublishActivity.class), 5);
    }

    public void down() {
        ViewPropertyAnimator.animate(actionButton).translationY(actionButton.getHeight() * 2)
                .setInterpolator(new AccelerateInterpolator(1))
                .start();
    }

    public void up() {
        ViewPropertyAnimator.animate(actionButton).translationY(0)
                .setInterpolator(new DecelerateInterpolator(1))
                .start();
    }

    @Override
    public void onBackPressed() {
        if (x == 1) {
            if (newsFragment != null && !newsFragment.back()) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (x == 1) {
            if (newsFragment != null) {
                newsFragment.change();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
