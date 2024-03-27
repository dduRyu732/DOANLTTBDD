package com.example.doanlttbdd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView storyListView;
    private DatabaseHelper databaseHelper;
    private ImageButton buttonAccountInfo;

    private ViewFlipper viewFlipper;
    private List<Story> storyList;
    private StoryListAdapter storyListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        SearchView searchView = appBarLayout.findViewById(R.id.search_view);

        viewFlipper = findViewById(R.id.viewflipper);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        storyList = databaseHelper.getAllStories();
        storyListView = findViewById(R.id.list_view_stories);
        storyListAdapter = new StoryListAdapter(MainActivity.this, storyList);
        storyListView.setAdapter(storyListAdapter);

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
                long storyId = selectedStory.getId();
                Log.d("MyApp", "storyId: " + storyId);

                Intent intent = new Intent(MainActivity.this, StoryDetailActivity.class);
                intent.putExtra("storyId", storyId);
                Log.d("MyApp", "storyId được truyền: " + storyId);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Thực hiện tìm kiếm theo từ khóa mới trong danh sách truyện và cập nhật giao diện người dùng
                List<Story> searchResults = searchInStoryList(newText);
                updateSearchResults(searchResults);
                return true;
            }
        });
    }

    private void performSearch(String query) {
        // Thực hiện tìm kiếm trong danh sách truyện và cập nhật giao diện người dùng
        List<Story> searchResults = searchInStoryList(query);
        updateSearchResults(searchResults);
    }

    private List<Story> searchInStoryList(String query) {
        List<Story> results = new ArrayList<>();
        for (Story story : storyList) {
            if (story.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    story.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                    story.getDescription().toLowerCase().contains(query.toLowerCase())) {
                results.add(story);
            }
        }
        return results;
    }

    private void updateSearchResults(List<Story> searchResults) {
        storyListAdapter = new StoryListAdapter(MainActivity.this, searchResults);
        storyListView.setAdapter(storyListAdapter);
    }

    private class StoryListAdapter extends ArrayAdapter<Story> {
        private Context context;
        private List<Story> stories;

        public StoryListAdapter(Context context, List<Story> stories) {
            super(context, 0, stories);
            this.context = context;
            this.stories = stories;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_story, parent, false);
            }

            Story currentStory = stories.get(position);

            TextView titleTextView = convertView.findViewById(R.id.textViewTitle);
            TextView authorTextView = convertView.findViewById(R.id.textViewAuthor);
            TextView descriptionTextView = convertView.findViewById(R.id.textViewDescription);

            titleTextView.setText(currentStory.getTitle());
            authorTextView.setText(currentStory.getAuthor());
            descriptionTextView.setText(currentStory.getDescription());

            return convertView;
        }
    }
}