package com.example.vocabmate.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vocabmate.Model.Topic;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class TopicListGActivity extends AppCompatActivity {

    private ListView topicListView;
    private List<Topic> topicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_topic);

        topicListView = findViewById(R.id.topicListView);

        fetchTopics();
    }

    private void fetchTopics() {
        Retrofit retrofit = ApiClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Topic>> call = apiService.getTopics();
        call.enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topicList = response.body();
                    displayTopics();
                } else {
                    Toast.makeText(TopicListGActivity.this, "Failed to load topics", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {
                Toast.makeText(TopicListGActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTopics() {
        ArrayAdapter<Topic> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, topicList);

        topicListView.setAdapter(adapter);

        topicListView.setOnItemClickListener((parent, view, position, id) -> {
            Topic selectedTopic = topicList.get(position);

            // Navigate to MainActivity với topicId
            Intent intent = new Intent(TopicListGActivity.this, FlashCardActivity.class);
            intent.putExtra("TOPIC_ID", selectedTopic.getTopicId());
            intent.putExtra("TOPIC_NAME", selectedTopic.getTopicName());
            intent.putExtra("TOPIC_IMG", selectedTopic.getTopicImg());
            startActivity(intent);
        });
    }
}
