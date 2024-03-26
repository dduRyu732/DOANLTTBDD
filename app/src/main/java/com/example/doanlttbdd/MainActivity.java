package com.example.doanlttbdd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView storyListView;
    private DatabaseHelper databaseHelper;
    private ImageButton buttonAccountInfo;

    private ViewFlipper viewFlipper;
    private List<Story> storyList;
    private StoryDetailActivity storyListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFlipper = findViewById(R.id.viewflipper);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        storyList = databaseHelper.getAllStories();
        storyListView = findViewById(R.id.list_view_stories);
        StoryListAdapter storyListAdapter = new StoryListAdapter(MainActivity.this, storyList);
        storyListView.setAdapter(storyListAdapter);
        Log.d("MyApp", "Story details: " + Story.toString());


        buttonAccountInfo = findViewById(R.id.buttonAccountInfo);
        buttonAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInfo = new Intent(MainActivity.this, AccountInfoActivity.class);
                startActivity(intentInfo);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        // Không cần chuyển đến MainActivity vì đã ở đó
                        return true;
                    case R.id.menu_account:
                        // Chuyển đến LoginActivity (màn hình đăng nhập)
                        Intent accountIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(accountIntent);
                        finish(); // Đóng Activity hiện tại
                        return true;
                    case R.id.menu_setting:
                        // Chỉnh đổi chế độ ban đêm/ngày
                        int nightMode = AppCompatDelegate.getDefaultNightMode();
                        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        }
                        recreate();
                        return true;
                }
                return false;
            }
        });
        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story selectedStory = storyListAdapter.getItem(position);
                int storyId = selectedStory.getId();
                Log.d("MyApp", "storyId: " + storyId);

                Intent intent = new Intent(MainActivity.this, StoryDetailActivity.class);
                intent.putExtra("storyId", selectedStory.getId());
                Log.d("MyApp", "storyId được truyền: " + selectedStory.getId());
                startActivity(intent);
            }
        });
    }
    }

    class StoryListAdapter extends ArrayAdapter<Story> {
        private List<Story> storyList;
        private Context context;

        public StoryListAdapter(Context context, List<Story> storyList) {
            super(context, 0, storyList);
            this.context = context;
            this.storyList = storyList;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.list_item_story, parent, false);
            }

            Story story = storyList.get(position);
            TextView textViewTitle = view.findViewById(R.id.textViewTitle);
            TextView textViewAuthor = view.findViewById(R.id.textViewAuthor);
            TextView textViewDescription = view.findViewById(R.id.textViewDescription);

            textViewTitle.setText(story.getTitle());
            textViewAuthor.setText(story.getAuthor());
            textViewDescription.setText(story.getDescription());

            return view;
        }

        @Override
        public int getCount() {
            return storyList != null ? storyList.size() : 0;
        }

        public Story getItem(int position) {
            return storyList.get(position);
        }
        
    }
