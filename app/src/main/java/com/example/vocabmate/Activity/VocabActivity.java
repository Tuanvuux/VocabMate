package com.example.vocabmate.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vocabmate.Adapter.VocabularyAdapter;
import com.example.vocabmate.Model.Vocab;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabActivity extends Activity {

    private ListView vocabListView;
    private int accountId;
    private int topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab);

        vocabListView = findViewById(R.id.vocabListView);

        // Lấy accountId và topicId từ Intent
        accountId = getIntent().getIntExtra("accountId", -1);
        topicId = getIntent().getIntExtra("topicId", -1);

        // Gọi API để lấy danh sách từ vựng
        getLearnedVocabByTopicAndAccount(accountId, topicId);
    }

    private void getLearnedVocabByTopicAndAccount(int accountId, int topicId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Vocab>> call = apiService.getLearnedVocabByTopicAndAccount(accountId, topicId);

        call.enqueue(new Callback<List<Vocab>>() {
            @Override
            public void onResponse(Call<List<Vocab>> call, Response<List<Vocab>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Vocab> vocabs = response.body();
                    displayVocabs(vocabs);
                } else {
                    // Xử lý lỗi từ API
                    String errorMessage = "Lỗi khi lấy dữ liệu: " + response.message();
                    Toast.makeText(VocabActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Vocab>> call, Throwable t) {
                // Xử lý lỗi kết nối
                String errorMessage = "Lỗi kết nối: " + t.getMessage();
                Toast.makeText(VocabActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                t.printStackTrace(); // In chi tiết lỗi ra logcat
            }
        });
    }

    private void displayVocabs(List<Vocab> vocabs) {
        VocabularyAdapter adapter = new VocabularyAdapter(this, vocabs);
        vocabListView.setAdapter(adapter);
    }
}