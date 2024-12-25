package com.example.vocabmate.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vocabmate.Model.AccountDTO;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView errorTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ view
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        errorTextView = findViewById(R.id.errorTextView);

        // Xử lý sự kiện đăng nhập
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    errorTextView.setText("Vui lòng nhập đầy đủ thông tin");
                    errorTextView.setVisibility(View.VISIBLE);
                } else {
                    login(username, password);
                }
            }
        });
    }

    private void login(String username, String password) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<AccountDTO> call = apiService.login(username, password);

        call.enqueue(new Callback<AccountDTO>() {
            @Override
            public void onResponse(Call<AccountDTO> call, Response<AccountDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountDTO account = response.body();

                    // Chuyển đến màn hình chính hoặc hiển thị thông tin tài khoản
                    Toast.makeText(LoginActivity.this, "Chào " + account.getFullName(), Toast.LENGTH_SHORT).show();

                    // Ví dụ: Chuyển sang màn hình chính
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 404) {
                    errorTextView.setText("Tài khoản không tồn tại");
                    errorTextView.setVisibility(View.VISIBLE);
                } else if (response.code() == 401) {
                    errorTextView.setText("Sai mật khẩu");
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AccountDTO> call, Throwable t) {
                errorTextView.setText("Lỗi kết nối: " + t.getMessage());
                errorTextView.setVisibility(View.VISIBLE);
            }
        });
    }
}
