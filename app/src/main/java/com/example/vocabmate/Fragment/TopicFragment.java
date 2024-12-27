package com.example.vocabmate.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vocabmate.Adapter.TopicAdapter;
import com.example.vocabmate.Model.Topic;
import com.example.vocabmate.R;
import com.example.vocabmate.Service.ApiClient;
import com.example.vocabmate.Service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TopicFragment extends Fragment {

    private ListView topicListView;
    private TopicAdapter topicAdapter;
    private List<Topic> topicList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic, container, false);

        // Ánh xạ ListView
        topicListView = view.findViewById(R.id.lv_topics);

        // Gọi phương thức để lấy danh sách chủ đề
        fetchTopics();

        // Xử lý sự kiện khi người dùng nhấn vào một mục trong danh sách
        topicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy chủ đề đã chọn
                Topic selectedTopic = topicList.get(position);

                // Tạo Bundle và truyền dữ liệu
                Bundle bundle = new Bundle();
                bundle.putLong("topicId", selectedTopic.getTopicId());
                bundle.putString("topicName", selectedTopic.getTopicName());

                // Tạo Fragment VocabularyFragment và gán Bundle
                VocabularyFragment fragment = new VocabularyFragment();
                fragment.setArguments(bundle);

                // Chuyển sang VocabularyFragment
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment) // Chuyển sang Fragment mới
                        .addToBackStack(null) // Thêm vào stack để quay lại nếu cần
                        .commit();
            }
        });

        return view;
    }

    // Phương thức để gọi API và lấy danh sách các chủ đề
    private void fetchTopics() {
        Retrofit retrofit = ApiClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Topic>> call = apiService.getTopics();
        call.enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Nếu có dữ liệu trả về, cập nhật danh sách topicList
                    topicList = response.body();
                    // Khởi tạo TopicAdapter và thiết lập cho ListView
                    topicAdapter = new TopicAdapter(requireContext(), topicList);
                    topicListView.setAdapter(topicAdapter);
                } else {
                    // Nếu không có dữ liệu
                    Log.e("TopicFragment", "Không có dữ liệu chủ đề.");
                }
            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {
                // Xử lý lỗi khi gọi API không thành công
                Log.e("TopicFragment", "Gọi API thất bại", t);
            }
        });
    }
}
