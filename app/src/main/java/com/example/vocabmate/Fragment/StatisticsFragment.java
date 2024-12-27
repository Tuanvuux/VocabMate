package com.example.vocabmate.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vocabmate.Adapter.TopicsAdapter;
import com.example.vocabmate.Model.StatisticTopic;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StatisticsFragment extends Fragment {

    private TextView totalLearnedWordsTextView;
    private ListView topicsLayout; // Sử dụng ListView thay vì LinearLayout

    private int accountId; // Biến để lưu accountId

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate giao diện từ file XML
        View view = inflater.inflate(R.layout.thongke, container, false);

        // Lấy accountId từ SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        accountId = sharedPreferences.getInt("accountId", -1); // -1 nếu không tìm thấy

        if (accountId == -1) {
            // Nếu accountId không tồn tại, thông báo lỗi hoặc xử lý phù hợp
            totalLearnedWordsTextView.setText("Không tìm thấy thông tin người dùng");
            return view;
        }

        // Ánh xạ các view
        totalLearnedWordsTextView = view.findViewById(R.id.totalLearnedWordsTextView);
        topicsLayout = view.findViewById(R.id.topicsLayout); // Sử dụng ListView thay vì LinearLayout

        // Lấy tổng số từ đã học
        getTotalLearnedWords();

        // Lấy danh sách các chủ đề từ API
        getLearnedTopics();

        return view;
    }

    // Gọi API để lấy tổng số từ đã học
    private void getTotalLearnedWords() {
        Retrofit retrofit = ApiClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Integer> call = apiService.getTotalLearnedWords(accountId); // Sử dụng accountId từ SharedPreferences

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int totalLearnedWords = response.body();
                    totalLearnedWordsTextView.setText(String.format("%d", totalLearnedWords));
                } else {
                    totalLearnedWordsTextView.setText("Lỗi khi lấy dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                totalLearnedWordsTextView.setText("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // Gọi API để lấy danh sách các chủ đề đã học
    private void getLearnedTopics() {
        Retrofit retrofit = ApiClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<StatisticTopic>> call = apiService.getLearnedTopics(accountId); // Sử dụng accountId từ SharedPreferences

        call.enqueue(new Callback<List<StatisticTopic>>() {
            @Override
            public void onResponse(Call<List<StatisticTopic>> call, Response<List<StatisticTopic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<StatisticTopic> topics = response.body();
                    displayTopics(topics);
                } else {
                    totalLearnedWordsTextView.setText("Lỗi khi lấy dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<List<StatisticTopic>> call, Throwable t) {
                totalLearnedWordsTextView.setText("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // Hàm hiển thị danh sách các chủ đề lên giao diện
    private void displayTopics(List<StatisticTopic> topics) {
        // Sử dụng adapter để hiển thị danh sách các chủ đề lên ListView
        TopicsAdapter adapter = new TopicsAdapter(requireContext(), topics, accountId);
        topicsLayout.setAdapter(adapter);
    }
}
