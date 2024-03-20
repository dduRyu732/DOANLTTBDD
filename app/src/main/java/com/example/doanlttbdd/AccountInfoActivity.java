package com.example.doanlttbdd;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountInfoActivity extends AppCompatActivity {

    private TextView textViewAccountName;
    private TextView textViewPassword;
    private TextView textViewPhoneNumber;
    private Button buttonResetPassword;
    private TextView textViewResetPassword;
    private  TextView textViewEmail;
    private  BottomNavigationView bottomNavigationView;

    private boolean isResetPasswordVisible = false;

    private EditText editTextNewPassword;
    private Button buttonSavePassword;
    private Dialog changePasswordDialog;
    private Button buttonAddStory;
    private SharedPreferences sharedPreferences;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        textViewAccountName = findViewById(R.id.textViewAccountName);
        textViewPassword = findViewById(R.id.textViewPassword);
        textViewPhoneNumber = findViewById(R.id.textViewPhoneNumber);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
        textViewResetPassword = findViewById(R.id.textViewResetPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        buttonSavePassword = findViewById(R.id.buttonSavePassword);
        textViewEmail = findViewById(R.id.textViewEmail);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        buttonAddStory = findViewById(R.id.buttonAddStory);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            String username = sharedPreferences.getString("username", "");
            if (username.equals("admin")) {
                buttonAddStory.setVisibility(View.VISIBLE); // Hiển thị nút "Thêm truyện"
            } else {
                buttonAddStory.setVisibility(View.GONE); // Ẩn nút "Thêm truyện"
            }
        } else {
            buttonAddStory.setVisibility(View.GONE); // Ẩn nút "Thêm truyện"
        }
        buttonAddStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddStory(); // Gọi phương thức để chuyển đến AdminActivity
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        // Chuyển đến MainActivity (trang chủ)

                        Intent homeIntent = new Intent(AccountInfoActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        finish(); // Đóng Activity hiện tại


                        return true;
                    case R.id.menu_account:
                        // Chuyển đến LoginActivity (màn hình đăng nhập)
                        Intent accountIntent = new Intent(AccountInfoActivity.this, LoginActivity.class);
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

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String accountName = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        String phoneNumber = sharedPreferences.getString("phoneNumber", "");
        String email = sharedPreferences.getString("email","");
        textViewAccountName.setText("Tên Tài Khoản: " +accountName);
        textViewPassword.setText("Mật Khẩu: "+password);
        textViewPhoneNumber.setText("Số Điện Thoại: "+phoneNumber);
        textViewEmail.setText("Email: "+email);
        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isResetPasswordVisible = !isResetPasswordVisible;
                textViewResetPassword.setVisibility(isResetPasswordVisible ? View.VISIBLE : View.GONE);

                if (isResetPasswordVisible) {
                    showChangePasswordDialog();
                } else {
                    hideChangePasswordDialog();
                }
            }
        });
    }

    private void showChangePasswordDialog() {
        changePasswordDialog = new Dialog(this);
        changePasswordDialog.setContentView(R.layout.dialog_change_password);
        changePasswordDialog.setCancelable(true);

        EditText editTextCurrentPassword = changePasswordDialog.findViewById(R.id.editTextCurrentPassword);
        EditText editTextNewPassword = changePasswordDialog.findViewById(R.id.editTextNewPassword);
        Button buttonSavePassword = changePasswordDialog.findViewById(R.id.buttonSavePassword);

        buttonSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = editTextCurrentPassword.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();

                // Kiểm tra mật khẩu hiện tại có khớp với mật khẩu trong SharedPreferences

                // Nếu khớp, lưu mật khẩu mới vào SharedPreferences
                saveNewPassword(newPassword);

                // Ẩn dialog và cập nhật TextView "Mật khẩu"
                hideChangePasswordDialog();
                textViewPassword.setText(newPassword);
            }
        });

        changePasswordDialog.show();
    }

    private void hideChangePasswordDialog() {
        if (changePasswordDialog != null && changePasswordDialog.isShowing()) {
            changePasswordDialog.dismiss();
            changePasswordDialog = null;
        }
    }

    private void saveNewPassword(String newPassword) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", newPassword);
        editor.apply();
    }
    private void navigateToAddStory() {
        Intent intent = new Intent(AccountInfoActivity.this, AdminActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Kết thúc Activity hiện tại khi người dùng chọn nút home/back
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}