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
import com.example.vocabmate.Model.Vocab;
import com.example.vocabmate.R;

import java.util.List;

public class VocabularyAdapter extends ArrayAdapter<Vocab> {

    public VocabularyAdapter(@NonNull Context context, List<Vocab> vocabularies) {
        super(context, 0, vocabularies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_vocabulary_simple, parent, false);
        }

        Vocab vocabulary = getItem(position);

        TextView word = convertView.findViewById(R.id.vocabularyWord);
        ImageView vocabImg = convertView.findViewById(R.id.vocabularyImg);

        if (vocabulary != null) {
            word.setText(vocabulary.getVocab());
            Glide.with(getContext())
                    .load(vocabulary.getVocabImg())
                    .placeholder(R.drawable.learning_checked)
                    .into(vocabImg);
        }

        return convertView;
    }
}
