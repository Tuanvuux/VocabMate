package com.example.vocabmate.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.vocabmate.Model.Topic;
import com.example.vocabmate.R;

import java.util.List;


public class TopicAdapter extends ArrayAdapter<Topic> {

    public TopicAdapter(@NonNull Context context, List<Topic> gettopics) {
        super(context, 0, gettopics);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_topic, parent, false);
        }

        Topic topic = getItem(position);

        TextView topicName = convertView.findViewById(R.id.topicName);
        ImageView topicImg = convertView.findViewById(R.id.topicIcon);

        if (topic != null) {
            topicName.setText(topic.getTopicName());
            Glide.with(getContext())
                    .load(topic.getTopicImg())
                    .placeholder(R.drawable.vacation) // Ảnh placeholder nếu chưa tải xong
                    .into(topicImg);
        }
        return convertView;
    }
}

