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
    private ApiClient apiClient;

    private String correctAnswer; // Câu trả lời đúng
    private int currentIndex = 0; // Chỉ số câu hỏi hiện tại
    private int score = 0; // Điểm số ban đầu
    private List<Vocab> vocabList = new ArrayList<>(); // Danh sách từ vựng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kiemtratuvungchitiet);

        // Khởi tạo các thành phần giao diện
        textViewQuestion = findViewById(R.id.textViewQuestion);
        editTextAnswer = findViewById(R.id.editTextAnswer);
        btnCheckAnswer = findViewById(R.id.btnCheckAnswer);
        btnSkip = findViewById(R.id.btnSkip);

        // Lấy dữ liệu từ API
        fetchVocabData();

        // Xử lý khi nhấn "Kiểm tra đáp án"
        btnCheckAnswer.setOnClickListener(v -> {
            String userAnswer = editTextAnswer.getText().toString().trim();
            if (!userAnswer.isEmpty()) {
                if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                    score++; // Tăng điểm nếu trả lời đúng
                    Toast.makeText(this, "Câu trả lời đúng!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Câu trả lời sai. Đáp án đúng: " + correctAnswer, Toast.LENGTH_SHORT).show();
                }

                // Điều hướng sang câu hỏi tiếp theo hoặc hoàn thành bài kiểm tra
                if (currentIndex < vocabList.size() - 1) {
                    currentIndex++;
                    displayQuestion(vocabList.get(currentIndex));
                } else {
                    displayFinalScore(); // Hiển thị điểm số cuối cùng
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập câu trả lời", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý khi nhấn "Bỏ qua"
        btnSkip.setOnClickListener(v -> {
            if (currentIndex < vocabList.size() - 1) {
                currentIndex++;
                displayQuestion(vocabList.get(currentIndex));
            } else {
                displayFinalScore(); // Hiển thị điểm số cuối cùng
            }
        });
    }

    // Phương thức lấy dữ liệu từ API
    private void fetchVocabData() {

        ApiService vocabApi = apiClient.getClient().create(ApiService.class);

        vocabApi.getTest().enqueue(new Callback<List<Vocab>>() {
            @Override
            public void onResponse(Call<List<Vocab>> call, Response<List<Vocab>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    vocabList = response.body();
                    if (!vocabList.isEmpty()) {
                        currentIndex = 0;
                        displayQuestion(vocabList.get(currentIndex)); // Hiển thị câu hỏi đầu tiên
                    } else {
                        Toast.makeText(VocabularyTestActivity.this, "Không có dữ liệu từ API!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Failed to fetch vocab data: " + response.message());
                    Toast.makeText(VocabularyTestActivity.this, "Lỗi API: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Vocab>> call, Throwable t) {
                Log.e("API Error", "Failed: " + t.getMessage());
                Toast.makeText(VocabularyTestActivity.this, "Không thể kết nối API!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hiển thị câu hỏi
    private void displayQuestion(Vocab vocab) {
        textViewQuestion.setText("What is the vocab for \"" + vocab.getMeaning() + "\"?");
        correctAnswer = vocab.getVocab(); // Lấy từ vựng đúng
        editTextAnswer.setText(""); // Reset câu trả lời
    }

    // Phương thức hiển thị điểm số cuối cùng
    private void displayFinalScore() {
        Toast.makeText(this, "Bạn đã hoàn thành bài kiểm tra! Điểm của bạn: " + score + "/" + vocabList.size(), Toast.LENGTH_LONG).show();
        // Có thể chuyển hướng sang màn hình kết quả hoặc hoạt động khác nếu cần
    }
}