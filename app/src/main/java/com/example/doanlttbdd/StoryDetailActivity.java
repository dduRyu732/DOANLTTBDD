package com.example.doanlttbdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class StoryDetailActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewAuthor;
    private TextView textViewContent;
    private BottomNavigationView bottomNavigationView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        textViewContent = findViewById(R.id.textViewContent);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        databaseHelper = new DatabaseHelper(this);

        // Lấy dữ liệu chuyện từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("storyId")) {
            int storyId = intent.getIntExtra("storyId", -1);
            Story story = retrieveStoryFromSQLite(storyId);
            if (story != null) {
                Log.d("MyApp", "Thông tin câu chuyện: " + story.getTitle() + ", " + story.getAuthor() + ", " + story.getContent());
                textViewTitle.setText(story.getTitle());
                textViewAuthor.setText(story.getAuthor());
                textViewContent.setText(story.getContent());
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        Intent homeIntent = new Intent(StoryDetailActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        finish(); // Đóng Activity hiện tại
                        return true;
                    case R.id.menu_account:
                        Intent accountIntent = new Intent(StoryDetailActivity.this, LoginActivity.class);
                        startActivity(accountIntent);
                        finish(); // Đóng Activity hiện tại
                        return true;
                    case R.id.menu_setting:
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
    }

    private Story retrieveStoryFromSQLite(int storyId) {
        // Sử dụng databaseHelper để lấy chuyện từ SQLite dựa trên storyId
        // Trả về đối tượng Story tương ứng hoặc null nếu không tìm thấy
        return databaseHelper.getStory(storyId);
    }
}