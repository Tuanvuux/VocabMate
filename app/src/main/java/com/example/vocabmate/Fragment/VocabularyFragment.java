package com.example.vocabmate.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.vocabmate.Adapter.VocabularyAdapter;
import com.example.vocabmate.Model.Vocab;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VocabularyFragment extends Fragment {

    private ListView vocabularyListView;
    private VocabularyAdapter vocabularyAdapter;
    private List<Vocab> vocabularyList = new ArrayList<>();
    private int topicId;
    private String topicName; // Biến để hiển thị tiêu đề chủ đề

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary, container, false);

        // Lấy dữ liệu từ Bundle
        if (getArguments() != null) {
            topicId = getArguments().getInt("topicId", -1); // Lấy topicId với giá trị mặc định là -1
            topicName = getArguments().getString("topicName", ""); // Lấy tên chủ đề
        }

        // Kiểm tra và thiết lập tiêu đề chủ đề
        TextView titleTextView = view.findViewById(R.id.tv_topic_title);
        if (topicName != null && !topicName.isEmpty()) {
            titleTextView.setText(topicName);
        } else {
            titleTextView.setText("Từ vựng");
        }

        // Ánh xạ ListView
        vocabularyListView = view.findViewById(R.id.lv_vocabularies);

        // Gọi API nếu topicId hợp lệ
        if (topicId != -1) {
            fetchVocabularyByTopic();
        } else {
            Log.e("VocabularyFragment", "topicId không hợp lệ");
        }

        return view;
    }

    private void fetchVocabularyByTopic() {
        Retrofit retrofit = ApiClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Vocab>> call = apiService.getVocabsByTopic(topicId);
        call.enqueue(new Callback<List<Vocab>>() {
            @Override
            public void onResponse(Call<List<Vocab>> call, Response<List<Vocab>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Cập nhật danh sách từ vựng và hiển thị
                    vocabularyList = response.body();
                    vocabularyAdapter = new VocabularyAdapter(requireContext(), vocabularyList);
                    vocabularyListView.setAdapter(vocabularyAdapter);

                    // Gắn sự kiện click cho từng item
                    vocabularyListView.setOnItemClickListener((parent, view, position, id) -> {
                        Vocab selectedVocabulary = vocabularyList.get(position);
                        showVocabularyDetailsDialog(requireContext(), selectedVocabulary);
                    });
                } else {
                    Log.e("VocabularyFragment", "Không có dữ liệu từ vựng.");
                }
            }

            @Override
            public void onFailure(Call<List<Vocab>> call, Throwable t) {
                Log.e("VocabularyFragment", "Gọi API thất bại", t);
            }
        });
    }

    private void showVocabularyDetailsDialog(Context context, Vocab vocabulary) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_vocabulary_details, null);

        // Ánh xạ các thành phần trong dialog
        TextView word = dialogView.findViewById(R.id.dialogVocabularyWord);
        TextView meaning = dialogView.findViewById(R.id.dialogVocabularyMeaning);
        TextView transcription = dialogView.findViewById(R.id.dialogVocabularyTranscription);
        TextView example = dialogView.findViewById(R.id.dialogVocabularyExample);
        ImageView image = dialogView.findViewById(R.id.dialogVocabularyImage);

        // Gán dữ liệu từ vựng vào các thành phần
        word.setText(vocabulary.getVocab());
        meaning.setText(vocabulary.getMeaning());
        transcription.setText(vocabulary.getTranscription());
        example.setText(vocabulary.getExample());
        Glide.with(context)
                .load(vocabulary.getVocabImg())
                .placeholder(R.drawable.learning_checked)
                .into(image);

        // Hiển thị dialog
        builder.setView(dialogView);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
