package com.example.vocabmate.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vocabmate.Activity.FlashCardActivity;
import com.example.vocabmate.Activity.TestActivity;
import com.example.vocabmate.Activity.TopicListGActivity;
import com.example.vocabmate.R;

public class HomepageFragment extends Fragment {
    private EditText searchBar;
    private ImageView searchIcon;
    private TextView countStreak;
    private LinearLayout topicButton, cardButton, testButton, reminderButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        // Ánh xạ các view
        searchBar = view.findViewById(R.id.search_bar);
        searchIcon = view.findViewById(R.id.search_icon);
        countStreak = view.findViewById(R.id.count);
        topicButton = view.findViewById(R.id.topic);
        cardButton = view.findViewById(R.id.card);
        testButton = view.findViewById(R.id.test);
        reminderButton = view.findViewById(R.id.reminder);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int streakCount = sharedPreferences.getInt("streakCount", 1);  // Mặc định là 0 nếu không tìm thấy

        updateStreak(streakCount);
        // Cài đặt các sự kiện click
        setupClickListeners();

        return view;
    }

    private void setupClickListeners() {
        // Sự kiện tìm kiếm
        searchIcon.setOnClickListener(v -> performSearch());

        // Sự kiện các nút chức năng
        topicButton.setOnClickListener(v -> openTopicScreen());  // Khi nhấn nút "Chủ đề"
        cardButton.setOnClickListener(v -> openFlashcardScreen());
        testButton.setOnClickListener(v -> openTestScreen());
        reminderButton.setOnClickListener(v -> openReminderScreen());
    }

    private void performSearch() {
        String searchText = searchBar.getText().toString().trim();
        // TODO: Implement search functionality
        // Ví dụ: chuyển đến màn hình kết quả tìm kiếm
    }

    private void openTopicScreen() {
        TopicFragment topicFragment = new TopicFragment();
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.firstFragment, topicFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    private void openFlashcardScreen() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), TopicListGActivity.class);
            startActivity(intent);
        }
    }

    private void openTestScreen() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), TestActivity.class);
            startActivity(intent);
        }
    }

    private void openReminderScreen() {
        // TODO: Chuyển đến màn hình Tạo nhắc nhở
    }

    // Phương thức cập nhật streak (số ngày học liên tiếp)
    private void updateStreak(int streakCount) {
        countStreak.setText(String.format("%02d", streakCount));
    }
}
