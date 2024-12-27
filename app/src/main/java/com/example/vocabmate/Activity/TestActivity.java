package com.example.vocabmate.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.vocabmate.R;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kiemtratuvung);

        // Initialize buttons for choosing modes
        LinearLayout btnMode = findViewById(R.id.btnModeTopic);
        LinearLayout btnRandom = findViewById(R.id.btnModeRandom);

        // Handle button clicks
        btnMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình TopicListActivity
                Intent intent = new Intent(TestActivity.this, TopicListActivity.class);
                startActivity(intent);
            }
        });


        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to detailed vocabulary testing screen (Random mode)
                Intent intent = new Intent(TestActivity.this, VocabularyTestActivity.class);
                intent.putExtra("mode", "random");
                startActivity(intent);
            }
        });
    }
}