package com.example.vocabmate.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.example.vocabmate.Activity.LearnRandomActivity;
import com.example.vocabmate.Activity.LearnTopicActivity;
import com.example.vocabmate.R;
import android.content.Intent;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.vocabmate.Activity.TopicListActivity;
import com.example.vocabmate.Activity.VocabularyTestActivity;

public class LearnFragment extends Fragment {

    private LinearLayout btnModeTopic, btnModeRandom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn, container, false);

        // Ánh xạ các nút
        btnModeTopic = view.findViewById(R.id.btnModeTopic);
        btnModeRandom = view.findViewById(R.id.btnModeRandom);

        // Cài đặt các sự kiện click
        btnModeTopic.setOnClickListener(v -> openTopicScreen());
        btnModeRandom.setOnClickListener(v -> openRandomMode());

        return view;
    }

    private void openTopicScreen() {
        // Mở màn hình TopicListActivity để chọn chủ đề
        Intent intent = new Intent(getActivity(), LearnTopicActivity.class);
        startActivity(intent);
    }

    private void openRandomMode() {
        // Mở màn hình VocabularyTestActivity với chế độ random
        Intent intent = new Intent(getActivity(), LearnRandomActivity.class);
        intent.putExtra("mode", "random");  // Chuyển thông tin chế độ học là random
        startActivity(intent);
    }
}

