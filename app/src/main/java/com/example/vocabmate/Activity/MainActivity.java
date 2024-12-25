package com.example.vocabmate.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;
import com.example.vocabmate.Model.StatisticTopic;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends Activity {

    private TextView totalLearnedWordsTextView;
    private LinearLayout topicsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongke);  // Sử dụng layout thongke.xml

        // Ánh xạ các view
        totalLearnedWordsTextView = findViewById(R.id.totalLearnedWordsTextView);
        topicsLayout = findViewById(R.id.topicsLayout);

        // Lấy tổng số từ đã học
        getTotalLearnedWords();

        // Lấy danh sách các chủ đề từ API
        getLearnedTopics();
    }

    // Gọi API để lấy tổng số từ đã học
    private void getTotalLearnedWords() {
        Retrofit retrofit = ApiClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Integer> call = apiService.getTotalLearnedWords(1); // userId = 1

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int totalLearnedWords = response.body();
                    totalLearnedWordsTextView.setText(String.format("Tổng số từ đã học: %d", totalLearnedWords));
                } else {
                    totalLearnedWordsTextView.setText("Lỗi khi lấy dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                totalLearnedWordsTextView.setText("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // Gọi API để lấy danh sách các chủ đề đã học
    private void getLearnedTopics() {
        Retrofit retrofit = ApiClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<StatisticTopic>> call = apiService.getLearnedTopics(1); // accountId = 1

        call.enqueue(new Callback<List<StatisticTopic>>() {
            @Override
            public void onResponse(Call<List<StatisticTopic>> call, Response<List<StatisticTopic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<StatisticTopic> topics = response.body();
                    displayTopics(topics);
                } else {
                    TextView errorTextView = new TextView(MainActivity.this);
                    errorTextView.setText("Lỗi khi lấy dữ liệu");
                    topicsLayout.addView(errorTextView);
                }
            }

            @Override
            public void onFailure(Call<List<StatisticTopic>> call, Throwable t) {
                TextView errorTextView = new TextView(MainActivity.this);
                errorTextView.setText("Lỗi kết nối: " + t.getMessage());
                topicsLayout.addView(errorTextView);
            }
        });
    }

    // Hàm hiển thị danh sách các chủ đề lên giao diện
    private void displayTopics(List<StatisticTopic> topics) {
        for (StatisticTopic topic : topics) {
            TextView topicTextView = new TextView(this);
            topicTextView.setText(String.format("%s: %d/%d", topic.getTopicName(), topic.getLearnedCount(), topic.getTotalCount()));
            topicTextView.setPadding(16, 16, 16, 16);

            topicsLayout.addView(topicTextView);
        }
    }
}
