package com.example.doanlttbdd;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextPhoneNumber;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonRegister;
    private EditText editTextEmail;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        editTextEmail = findViewById(R.id.editTextEmail);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();
                String email = editTextEmail.getText().toString();

                if (username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (isUsernameTaken(username)) {
                    Toast.makeText(RegisterActivity.this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    // Lưu thông tin đăng ký vào SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putString("phoneNumber", phoneNumber);
                    editor.putString("email", email);
                    if (!isValidEmail(email)) {
                        Toast.makeText(RegisterActivity.this, "Định dạng email không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    editor.apply();

                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng Activity đăng ký
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent);

                }
            }

        });

    }

    private boolean isUsernameTaken(String username) {
        String savedUsername = sharedPreferences.getString("username", "");
        return username.equals(savedUsername);
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.endsWith("@gmail.com");
    }

}