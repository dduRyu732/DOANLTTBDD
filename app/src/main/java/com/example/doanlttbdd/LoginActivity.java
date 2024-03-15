package com.example.doanlttbdd;

import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;

    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (isValidLogin(username, password)) {
                    // Lưu trạng thái đăng nhập
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    if (username.equals("admin") && password.equals("admin")) {
                        // Chuyển đến AdminActivity
                        Intent adminIntent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(adminIntent);
                    } else {
                        // Chuyển đến MainActivity
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                    }

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng Activity đăng nhập
                } else {
                    // Hiển thị thông báo lỗi đăng nhập
                    Toast.makeText(LoginActivity.this, "Tài khoản chưa được đăng ký hoặc thông tin đăng nhập không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến RegisterActivity
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

    }

    private boolean isValidLogin(String username, String password) {
        // Kiểm tra thông tin đăng nhập
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        return username.equals(savedUsername) && password.equals(savedPassword);
    }
}
