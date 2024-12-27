package com.example.vocabmate.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.vocabmate.Model.AccountDTO;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import android.content.Intent;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import android.view.View;

import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {
    EditText etFullName, etOrigin, etBirth, etRegisterUsername, etRegisterPassword;
    Button btnRegister;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        etFullName = findViewById(R.id.etFullName);
        etOrigin = findViewById(R.id.etOrigin);
        etBirth = findViewById(R.id.etBirth);
        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = etFullName.getText().toString();
                String address = etOrigin.getText().toString();
                String birthday = etBirth.getText().toString();
                String username = etRegisterUsername.getText().toString();
                String password = etRegisterPassword.getText().toString();

                if (!fullName.isEmpty() && !address.isEmpty() && !birthday.isEmpty() &&
                        !username.isEmpty() && !password.isEmpty()) {
                    // Gọi API để đăng ký
                    registerAccount(fullName, address, birthday, username, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerAccount(String fullName, String address, String birthday, String username, String password) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        AccountDTO newAccount = new AccountDTO();
        newAccount.setFullName(fullName);
        newAccount.setAddress(address);
        newAccount.setBirthday(birthday);
        newAccount.setUserName(username);
        newAccount.setPassword(password);

        // Thực hiện gọi API
        apiService.registerAccount(newAccount).enqueue(new Callback<AccountDTO>() {
            @Override
            public void onResponse(Call<AccountDTO> call, Response<AccountDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountDTO> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối API!", Toast.LENGTH_SHORT).show();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}