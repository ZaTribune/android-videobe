package com.shadow.videobe;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.shadow.videobe.adapters.AllAdapter;
import com.shadow.videobe.adapters.ViewPagerAdapter;
import com.shadow.videobe.data.entities.Video;
import com.shadow.videobe.editing.EditingFragment;
import com.shadow.videobe.editing.VideoProvider;
import com.shadow.videobe.home.HomeFragment;
import com.shadow.videobe.home.HomeVideosListener;
import com.shadow.videobe.player.PlayerFragment;
import com.shadow.videobe.tools.ToolsFragment;
import com.shadow.videobe.utils.PermissionModel;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener
        , ToolsFragment.ToolsFragmentListener
        , HomeVideosListener {
    private int[] tabLayoutDrawables = new int[]{R.drawable.album,R.drawable.toys};
    private int[] drawerLayoutDrawables = new int[]{R.drawable.settings,R.drawable.statistics,R.drawable.history,R.drawable.about};
    private DrawerLayout mDrawerLayout;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout=findViewById(R.id.mDrawerLayout);
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        ListView lv_drawer = findViewById(R.id.lv_drawer);
        lv_drawer.setAdapter(new AllAdapter(MainActivity.this
                ,drawerLayoutDrawables
                ,getResources().getStringArray(R.array.main_menu_options)));
        lv_drawer.setOnItemClickListener(this);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
//      ActiveMainActivityBinding activityBinding=DataBindingUtil.
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(HomeFragment.newInstance("Hello",4), "Home");
        viewPagerAdapter.addFragment(ToolsFragment.newInstance(2), "Tools");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab tab;
        for(int i=0;i<tabLayout.getTabCount();i++){
            tab=tabLayout.getTabAt(i);
            assert tab != null;
            tab.setIcon(tabLayoutDrawables[i]);
        }
        tabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.yellow), PorterDuff.Mode.SRC_IN);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.yellow), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onVideoDeleted(String video) {
    }

    @Override
    public void onVideoCreated(String video) {
        HomeFragment homeFragment= (HomeFragment) viewPagerAdapter.getItem(0);
        homeFragment.getHomeVideosAdapter().refresh();//to view the video to home screen
    }

    @Override
    public void playVideo(String videoPath) {
        PlayerFragment playerFragment= (PlayerFragment) getSupportFragmentManager().findFragmentByTag(PlayerFragment.class.getName());
        if(playerFragment==null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, PlayerFragment.newInstance(videoPath,0),PlayerFragment.class.getName())
                    .addToBackStack(PlayerFragment.class.getName())
                    .commit();
        else {
            playerFragment.play(videoPath);
        }
    }

    public void controlDrawer(View view){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        }else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        System.out.println("Clicked");
        switch (position){
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
        }
    }

    @Override
    public void onToolsInteraction(String tool) {
     //todo use these instead of
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) {
            Toast.makeText(MainActivity.this,"Please provide permissions before using the App.",Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
