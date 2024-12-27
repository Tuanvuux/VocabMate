package com.example.vocabmate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vocabmate.Activity.VocabActivity;
import com.example.vocabmate.Model.StatisticTopic;
import com.example.vocabmate.R;

import java.util.List;

public class TopicsAdapter extends ArrayAdapter<StatisticTopic> {
    private int accountId;

    public TopicsAdapter(Context context, List<StatisticTopic> topics, int accountId) {
        super(context, 0, topics);
        this.accountId = accountId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StatisticTopic topic = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.topic_item, parent, false);
        }

        TextView topicNameTextView = convertView.findViewById(R.id.topicNameTextView);
        TextView topicStatsTextView = convertView.findViewById(R.id.topicStatsTextView);

        topicNameTextView.setText(topic.getTopicName());
        topicStatsTextView.setText(String.format("%d/%d", topic.getLearnedCount(), topic.getTotalCount()));

        // Xử lý sự kiện khi nhấn vào một chủ đề
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), VocabActivity.class);
            intent.putExtra("accountId", accountId);
            intent.putExtra("topicId", topic.getTopicId());
            getContext().startActivity(intent);
        });

        return convertView;
    }
}