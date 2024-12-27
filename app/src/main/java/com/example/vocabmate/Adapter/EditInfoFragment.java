package com.example.vocabmate.Adapter;

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
import com.example.vocabmate.Model.Vocab;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import java.util.List;

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

        editFullName = view.findViewById(R.id.edit_name);
        editAddress = view.findViewById(R.id.edit_origin);
        editBirthday = view.findViewById(R.id.edit_birth);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        // Lấy dữ liệu từ arguments (nếu có)
        if (getArguments() != null) {
            editFullName.setText(getArguments().getString("fullName", ""));
            editAddress.setText(getArguments().getString("address", ""));
            editBirthday.setText(getArguments().getString("birthday", ""));
        }

        saveButton.setOnClickListener(v -> saveAccountInfo());
        cancelButton.setOnClickListener(v -> {
            // Quay lại màn trước đó
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void saveAccountInfo() {
        String fullName = editFullName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String birthday = editBirthday.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(address) || TextUtils.isEmpty(birthday)) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        AccountDTO updatedAccount = new AccountDTO();
        updatedAccount.setFullName(fullName);
        updatedAccount.setAddress(address);
        updatedAccount.setBirthday(birthday);



    }
}
