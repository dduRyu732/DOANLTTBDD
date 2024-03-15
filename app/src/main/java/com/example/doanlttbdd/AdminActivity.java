package com.example.doanlttbdd;

import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextAuthor;
    private EditText editTextDescription;
    private Button buttonAdd;

    private BottomNavigationView bottomNavigationView;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonAdd = findViewById(R.id.buttonAdd);

        databaseHelper = new DatabaseHelper(this);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String author = editTextAuthor.getText().toString();
                String description = editTextDescription.getText().toString();

                long result = databaseHelper.insertBook(title, author, description);

                if (result != -1) {
                    makeText(AdminActivity.this, "?", Toast.LENGTH_SHORT).show();makeText(AdminActivity.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                    editTextTitle.setText("");
                    editTextAuthor.setText("");
                    editTextDescription.setText("");
                } else {
                    makeText(AdminActivity.this, "Failed to add book", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        // Chuyển đến MainActivity (trang chủ)

                            Intent homeIntent = new Intent(AdminActivity.this, MainActivity.class);
                            startActivity(homeIntent);
                            finish(); // Đóng Activity hiện tại


                        return true;
                    case R.id.menu_account:
                        // Chuyển đến LoginActivity (màn hình đăng nhập)
                        Intent accountIntent = new Intent(AdminActivity.this, LoginActivity.class);
                        startActivity(accountIntent);
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
    }
}