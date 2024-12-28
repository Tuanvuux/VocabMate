package com.example.vocabmate.Activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vocabmate.Model.LearnHistory;
import com.example.vocabmate.Model.Vocab;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LearnVocabByTopicActivity extends AppCompatActivity {

    private TextView textViewQuestion, textViewMeaning, textViewExample, textViewTranscription;
    private Button btnSkip;
    private ImageView vocabImage;
    private int currentIndex = 0;
    private List<Vocab> vocabList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_learnvocab); // Sử dụng layout mới

        // Khởi tạo các thành phần giao diện
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewMeaning = findViewById(R.id.textViewMeaning);
        textViewExample = findViewById(R.id.textViewExample);
        textViewTranscription = findViewById(R.id.textViewTranscription);
        vocabImage = findViewById(R.id.vocabImage);
        btnSkip = findViewById(R.id.btnSkip);

        // Lấy thông tin từ Intent
        String mode = getIntent().getStringExtra("mode");


        if ("topic".equals(mode)) {
            fetchTopicVocabData();
        } else {
            Toast.makeText(this, "Chế độ không hợp lệ!", Toast.LENGTH_SHORT).show();
        }

        // Cài đặt sự kiện cho nút "Next"
        btnSkip.setOnClickListener(v -> skipQuestion());
    }

    private void fetchTopicVocabData() {
        int topicId = getIntent().getIntExtra("topicId",1);
        ApiService vocabApi = ApiClient.getClient().create(ApiService.class);
        vocabApi.getVocabsByTopic(topicId).enqueue(new Callback<List<Vocab>>() {
            @Override
            public void onResponse(Call<List<Vocab>> call, Response<List<Vocab>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    vocabList = response.body();
                    if (!vocabList.isEmpty()) {
                        displayQuestion(vocabList.get(currentIndex));
                    } else {
                        showEmptyDataToast();
                    }
                } else {
                    showApiError(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Vocab>> call, Throwable t) {
                showApiFailure(t.getMessage());
            }
        });
    }

    private void displayQuestion(Vocab vocab) {
        // Hiển thị thông tin từ vựng
        textViewQuestion.setText(vocab.getVocab()); // Hiển thị từ vựng
        textViewMeaning.setText(vocab.getMeaning()); // Hiển thị nghĩa
        textViewExample.setText(vocab.getExample()); // Hiển thị ví dụ
        textViewTranscription.setText(vocab.getTranscription()); // Hiển thị phiên âm

        // Hiển thị ảnh từ vựng (nếu có)
        if (vocab.getVocabImg() != null && !vocab.getVocabImg().isEmpty()) {
            Glide.with(LearnVocabByTopicActivity.this)
                    .load(vocab.getVocabImg())
                    .into(vocabImage); // Dùng Glide để load ảnh
        } else {
            vocabImage.setImageResource(R.drawable.placeholder_image); // Nếu không có ảnh, dùng ảnh mặc định
        }
    }

    private void skipQuestion() {
        Vocab currentVocab = vocabList.get(currentIndex);
        SharedPreferences sharedPreferences = LearnVocabByTopicActivity.this.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int accountId = sharedPreferences.getInt("accountId", -1);

        // Tạo đối tượng LearnHistory với accountId và vocabId
        LearnHistory learnHistory = new LearnHistory();
        learnHistory.setAccountId(accountId);  // Thay thế với accountId của người dùng (lấy từ session hoặc dữ liệu người dùng)
        learnHistory.setVocabId(currentVocab.getVocabId()); // Lấy vocabId của từ hiện tại

        // Gửi yêu cầu POST để lưu lịch sử học
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.saveLearnHistory(learnHistory).enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body()) {
                        Toast.makeText(LearnVocabByTopicActivity.this, "", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LearnVocabByTopicActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LearnVocabByTopicActivity.this, " " + response.message(), Toast.LENGTH_SHORT).show();
                }

                // Chuyển sang từ tiếp theo dù thành công hay thất bại
                currentIndex++;
                if (currentIndex < vocabList.size()) {
                    displayQuestion(vocabList.get(currentIndex)); // Hiển thị từ tiếp theo
                } else {
                    Toast.makeText(LearnVocabByTopicActivity.this, "Bạn đã hoàn thành bài học!", Toast.LENGTH_SHORT).show();
                    finish();  // Kết thúc khi học hết từ
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(LearnVocabByTopicActivity.this, "Không thể kết nối API!", Toast.LENGTH_SHORT).show();
            }

        });
    }



    private void showEmptyDataToast() {
        Toast.makeText(this, "Không có dữ liệu từ API!", Toast.LENGTH_SHORT).show();
    }

    private void showApiError(String message) {
        Toast.makeText(this, "Lỗi API: " + message, Toast.LENGTH_SHORT).show();
    }

    private void showApiFailure(String error) {
        Toast.makeText(this, "Không thể kết nối API!", Toast.LENGTH_SHORT).show();
    }
}
