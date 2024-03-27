package com.example.doanlttbdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import java.util.ArrayList;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class StoryDetailActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewAuthor;
    private TextView textViewContent;
    private BottomNavigationView bottomNavigationView;
    private DatabaseHelper databaseHelper;
    private EditText commentInput;
    private ListView commentList;
    private Button submit_comment;
    private ArrayAdapter<String> adapter;

    private SharedPreferences sharedPreferences;


    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        textViewContent = findViewById(R.id.textViewContent);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        EditText commentInput = findViewById(R.id.comment_input);
        ListView commentList = findViewById(R.id.comment_list);
        submit_comment = findViewById(R.id.submit_comment);
        databaseHelper = new DatabaseHelper(this);

        ArrayList<String> comments = new ArrayList<>();
        adapter = new ArrayAdapter<>(StoryDetailActivity.this,
                android.R.layout.simple_list_item_1, comments);
        commentList.setAdapter(adapter);
        submit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
                if (isLoggedIn) {
                    // Giữ nguyên logic thêm bình luận khi người dùng đã đăng nhập
                    String commentText = commentInput.getText().toString().trim();
                    if (!commentText.isEmpty()) {
                        // Thực hiện thêm bình luận vào database hoặc nơi lưu trữ khác
                        // Cập nhật adapter để hiển thị bình luận mới
                    } else {
                        Toast.makeText(StoryDetailActivity.this, "Vui lòng nhập bình luận ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Yêu cầu người dùng đăng nhập
                    Toast.makeText(StoryDetailActivity.this, "Vui lòng đăng nhập để bình luận", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Giả lập nhấp vào nút submit
                    submit_comment.performClick();
                    return true;
                }
                return false;
            }
        });



        // Lấy dữ liệu chuyện từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("storyId")) {
            int storyId = (int) intent.getLongExtra("storyId", -1);
            Story story = retrieveStoryFromSQLite(storyId);
            if (story != null) {
                Log.d("StoryDetail", "Thông tin câu chuyện: " + story.getTitle() + ", " + story.getAuthor() + ", " + story.getContent());
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