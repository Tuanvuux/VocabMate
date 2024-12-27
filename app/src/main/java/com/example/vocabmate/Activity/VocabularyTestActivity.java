package com.example.vocabmate.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vocabmate.Model.Vocab;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabularyTestActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private EditText editTextAnswer;
    private Button btnCheckAnswer, btnSkip;
    private String mode;
    private int topicId;
    private String correctAnswer;
    private int currentIndex = 0;
    private int score = 0;
    private List<Vocab> vocabList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kiemtratuvungchitiet);

        // Khởi tạo các thành phần giao diện
        textViewQuestion = findViewById(R.id.textViewQuestion);
        editTextAnswer = findViewById(R.id.editTextAnswer);
        btnCheckAnswer = findViewById(R.id.btnCheckAnswer);
        btnSkip = findViewById(R.id.btnSkip);

        // Lấy thông tin từ Intent
        mode = getIntent().getStringExtra("mode");
        topicId = getIntent().getIntExtra("topicId", -1);

        // Xử lý chế độ test
        if ("random".equals(mode)) {
            fetchRandomVocabData();
        } else if ("topic".equals(mode) && topicId != -1) {
            fetchVocabDataByTopic(topicId);
        } else {
            Toast.makeText(this, "Chế độ hoặc chủ đề không hợp lệ!", Toast.LENGTH_SHORT).show();
            Log.e("VocabularyTestActivity", "Invalid mode or topicId");
        }

        // Xử lý khi nhấn "Kiểm tra đáp án"
        btnCheckAnswer.setOnClickListener(v -> {
            String userAnswer = editTextAnswer.getText().toString().trim();
            if (!userAnswer.isEmpty()) {
                if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                    score++;
                    Toast.makeText(this, "Câu trả lời đúng!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Sai. Đáp án: " + correctAnswer, Toast.LENGTH_SHORT).show();
                }
                nextQuestion();
            } else {
                Toast.makeText(this, "Vui lòng nhập câu trả lời", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý khi nhấn "Bỏ qua"
        btnSkip.setOnClickListener(v -> nextQuestion());
    }

    private void nextQuestion() {
        if (currentIndex < vocabList.size() - 1) {
            currentIndex++;
            displayQuestion(vocabList.get(currentIndex));
        } else {
            displayFinalScore();
        }
    }

    private void fetchRandomVocabData() {
        ApiService vocabApi = ApiClient.getClient().create(ApiService.class);
        vocabApi.getTest().enqueue(new Callback<List<Vocab>>() {
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

    private void fetchVocabDataByTopic(int topicId) {
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
        textViewQuestion.setText(vocab.getMeaning());
        correctAnswer = vocab.getVocab();
        editTextAnswer.setText("");
    }

    private void displayFinalScore() {
        Toast.makeText(this, "Hoàn thành! Điểm: " + score + "/" + vocabList.size(), Toast.LENGTH_LONG).show();
    }

    private void showEmptyDataToast() {
        Toast.makeText(this, "Không có dữ liệu từ API!", Toast.LENGTH_SHORT).show();
    }

    private void showApiError(String message) {
        Log.e("API Error", "Failed: " + message);
        Toast.makeText(this, "Lỗi API: " + message, Toast.LENGTH_SHORT).show();
    }

    private void showApiFailure(String error) {
        Log.e("API Failure", "Error: " + error);
        Toast.makeText(this, "Không thể kết nối API!", Toast.LENGTH_SHORT).show();
    }
}
