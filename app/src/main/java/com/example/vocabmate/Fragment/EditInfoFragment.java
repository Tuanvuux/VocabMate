package com.example.vocabmate.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vocabmate.Model.AccountDTO;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditInfoFragment extends Fragment {

    private EditText editFullName, editAddress, editBirthday;
    private Button saveButton, cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_info, container, false);

        // Khởi tạo các thành phần giao diện
        editFullName = view.findViewById(R.id.edit_name);
        editAddress = view.findViewById(R.id.edit_origin);
        editBirthday = view.findViewById(R.id.edit_birth);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        // Lấy thông tin người dùng từ API
        fetchAccountInfo();

        // Xử lý sự kiện nút Lưu
        saveButton.setOnClickListener(v -> saveAccountInfo());

        // Xử lý sự kiện nút Hủy
        cancelButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void fetchAccountInfo() {
        // Lấy accountId từ SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int accountId = sharedPreferences.getInt("accountId", -1);

        if (accountId == -1) {
            Toast.makeText(getContext(), "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi API để lấy thông tin tài khoản
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAccountById(accountId).enqueue(new Callback<AccountDTO>() {
            @Override
            public void onResponse(Call<AccountDTO> call, Response<AccountDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountDTO account = response.body();
                    editFullName.setText(account.getFullName());
                    editAddress.setText(account.getAddress());
                    editBirthday.setText(account.getBirthday());
                } else {
                    Toast.makeText(getContext(), "Không thể lấy thông tin tài khoản!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAccountInfo() {
        String fullName = editFullName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String birthday = editBirthday.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(address) || TextUtils.isEmpty(birthday)) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int accountId = sharedPreferences.getInt("accountId", -1);

        if (accountId == -1) {
            Toast.makeText(getContext(), "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
            return;
        }

        AccountDTO updatedAccount = new AccountDTO();
        updatedAccount.setAccountId(accountId); // Nếu API cần ID trong body
        updatedAccount.setFullName(fullName);
        updatedAccount.setAddress(address);
        updatedAccount.setBirthday(birthday);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.updateAccount(accountId, updatedAccount).enqueue(new Callback<AccountDTO>() {
            @Override
            public void onResponse(Call<AccountDTO> call, Response<AccountDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
