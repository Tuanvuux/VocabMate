package com.example.vocabmate.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vocabmate.Model.Topic;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicListActivity extends AppCompatActivity {
    private LinearLayout topicContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);

        topicContainer = findViewById(R.id.topicContainer);

        // Gọi API để lấy danh sách chủ đề
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Topic>> call = apiService.getTopics();

        call.enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayTopics(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {
                Toast.makeText(TopicListActivity.this, "Lỗi khi tải danh sách chủ đề", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTopics(List<Topic> topics) {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Topic topic : topics) {
            View view = inflater.inflate(R.layout.item_topic, topicContainer, false);

            // Gắn dữ liệu vào item
            ImageView topicImage = view.findViewById(R.id.topicImage);
            TextView topicName = view.findViewById(R.id.topicName);
            TextView topicStrans = view.findViewById(R.id.topicStrans);

            topicName.setText(topic.getTopicName());
            topicStrans.setText(topic.getTopicStrans());
            Glide.with(this).load(topic.getTopicImg()).into(topicImage);

            // Xử lý khi nhấn vào chủ đề
            view.setOnClickListener(v -> {
                Intent intent = new Intent(TopicListActivity.this, VocabularyTestActivity.class);
                intent.putExtra("topicId", topic.getTopicId());
                startActivity(intent);
            });

            // Thêm view vào container
            topicContainer.addView(view);
        }
    }
}
