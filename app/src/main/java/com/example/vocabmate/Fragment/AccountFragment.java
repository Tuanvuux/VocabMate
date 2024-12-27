package com.example.vocabmate.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vocabmate.Activity.LoginActivity;
import com.example.vocabmate.Adapter.EditInfoFragment;
import com.example.vocabmate.Model.AccountDTO;
import com.example.vocabmate.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AccountFragment extends Fragment {

    private ImageView profileImage;
    private TextView fullNameTextView, addressTextView, birthdayTextView, editInfo;
    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Khởi tạo View
        profileImage = view.findViewById(R.id.profile_image);
        fullNameTextView = view.findViewById(R.id.full_name);
        addressTextView = view.findViewById(R.id.address);
        birthdayTextView = view.findViewById(R.id.birthday);
        logoutButton = view.findViewById(R.id.logoutButton);
        editInfo = view.findViewById(R.id.editInfo);

        // Gọi API để lấy dữ liệu
        fetchUserInfo();
        //click editInfo
        editInfo.setOnClickListener(v -> {
            // Thay vì Intent, sử dụng FragmentTransaction
            EditInfoFragment editInfoFragment = new EditInfoFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, editInfoFragment)  // R.id.fragment_container là ID của container chứa các Fragment trong Activity
                    .addToBackStack(null)  // Nếu muốn có thể quay lại fragment trước đó
                    .commit();
        });


        logoutButton.setOnClickListener(v -> {
            // Navigate to login screen and clear back stack
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        return view;
    }

    private void fetchUserInfo() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        String username = sharedPreferences.getString("username", null);
        String pw = sharedPreferences.getString("pw", null);

        Retrofit retrofit = ApiClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.login(username, pw).enqueue(new Callback<AccountDTO>() {
            @Override
            public void onResponse(Call<AccountDTO> call, Response<AccountDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountDTO account = response.body();

                    // Hiển thị thông tin người dùng
                    if (fullNameTextView != null) {
                        fullNameTextView.setText("Họ và tên: " + account.getFullName());
                    }
                    if (addressTextView != null) {
                        addressTextView.setText("Địa chỉ: " + account.getAddress());
                    }
                    if (birthdayTextView != null) {
                        birthdayTextView.setText("Ngày sinh: " + account.getBirthday());
                    }
                    if (profileImage != null) {
                        Glide.with(requireContext())
                                .load(account.getImage())
                                .circleCrop()
                                .into(profileImage);
                    }
                } else {
                    Log.e("AccountFragment", "API response failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AccountDTO> call, Throwable t) {
                Log.e("AccountFragment", "API call failed: " + t.getMessage());
            }
        });
    }


}