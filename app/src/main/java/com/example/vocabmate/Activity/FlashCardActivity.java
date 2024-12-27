package com.example.vocabmate.Activity;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vocabmate.Model.Vocab;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


// Trong FlashCardActivity.java
public class FlashCardActivity extends AppCompatActivity {
    private TextView vocabTextView, meaningTextView, transcriptionTextView, exampleTextView;
    private ImageView topicImageView, vocabImageView;
    private TextView topicNameTextView;
    private ImageView leftArrowButton, rightArrowButton, backArrowButton, nextArrowButton;
    private List<Vocab> vocabList;
    private int currentIndex = 0;
    private boolean isFrontVisible = true; // Biến trạng thái mặt trước

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard);

        // Khởi tạo các thành phần giao diện
        vocabTextView = findViewById(R.id.vocabTextView);
        meaningTextView = findViewById(R.id.meaningTextView);
        transcriptionTextView = findViewById(R.id.transcriptionTextView);
        exampleTextView = findViewById(R.id.exampleTextView);
        vocabImageView = findViewById(R.id.vocabImageView);
        topicImageView = findViewById(R.id.topicImageView);
        topicNameTextView = findViewById(R.id.topicNameTextView);
        leftArrowButton = findViewById(R.id.leftArrowButton);
        rightArrowButton = findViewById(R.id.rightArrowButton);
        backArrowButton = findViewById(R.id.backArrowButton);

        View flashcardContainer = findViewById(R.id.flashcardContainer);
        flashcardContainer.setOnClickListener(v -> flipFlashcard());

        // Nhận thông tin từ Intent
        int topicId = getIntent().getIntExtra("TOPIC_ID", -1);
        String topicName = getIntent().getStringExtra("TOPIC_NAME");
        String topicImg = getIntent().getStringExtra("TOPIC_IMG");

        // Hiển thị tên topic và hình ảnh
        if (topicName != null && topicImg != null) {
            topicNameTextView.setText(topicName);
            Glide.with(this).load(topicImg).into(topicImageView);  // Sử dụng Glide để tải hình ảnh
        }

        if (topicId != -1) {
            fetchVocabByTopic(topicId);
        }

        leftArrowButton.setOnClickListener(v -> navigateVocab(-1));
        rightArrowButton.setOnClickListener(v -> navigateVocab(1));

        backArrowButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TopicListGActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Lấy từ vựng theo chủ đề
    private void fetchVocabByTopic(int topicId) {
        Retrofit retrofit = ApiClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Vocab>> call = apiService.getVocabByTopic(topicId);
        call.enqueue(new Callback<List<Vocab>>() {
            @Override
            public void onResponse(Call<List<Vocab>> call, Response<List<Vocab>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    vocabList = response.body();
                    displayVocab(currentIndex);
                } else {
                    Toast.makeText(FlashCardActivity.this, "Failed to load vocab", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Vocab>> call, Throwable t) {
                Toast.makeText(FlashCardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hiển thị từ vựng
    private void displayVocab(int index) {
        if (vocabList != null && !vocabList.isEmpty()) {
            Vocab vocab = vocabList.get(index);
            vocabTextView.setText(vocab.getVocab());
            transcriptionTextView.setText(vocab.getTranscription());
            exampleTextView.setText(vocab.getExample());
            meaningTextView.setText(vocab.getMeaning());

            Glide.with(this)
                    .load(vocab.getVocabImg())  // Image URL from Vocab object
                    .placeholder(R.drawable.placeholder_image)  // Placeholder image while loading
                    .into(vocabImageView);  // Set image into ImageView

            if (!isFrontVisible) {
                flipFlashcard();
            }
        }
    }

    // Điều hướng qua các từ vựng
    private void navigateVocab(int direction) {
        if (vocabList != null && !vocabList.isEmpty()) {
            currentIndex = (currentIndex + direction + vocabList.size()) % vocabList.size();
            displayVocab(currentIndex);
        }
    }

    // Lật thẻ flashcard
    private void flipFlashcard() {
        View frontCard = findViewById(R.id.frontCard);
        View backCard = findViewById(R.id.backCard);

        AnimatorSet flipOutAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_out);
        AnimatorSet flipInAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_in);

        if (isFrontVisible) {
            flipOutAnimator.setTarget(frontCard);
            flipInAnimator.setTarget(backCard);
            backCard.setVisibility(View.VISIBLE);
            frontCard.setVisibility(View.INVISIBLE);
        } else {
            flipOutAnimator.setTarget(backCard);
            flipInAnimator.setTarget(frontCard);
            frontCard.setVisibility(View.VISIBLE);
            backCard.setVisibility(View.INVISIBLE);
        }

        flipOutAnimator.start();
        flipInAnimator.start();

        isFrontVisible = !isFrontVisible;
    }
}

