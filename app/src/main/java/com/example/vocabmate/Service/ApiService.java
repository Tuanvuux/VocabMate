package com.example.vocabmate.Service;

import com.example.vocabmate.Model.AccountDTO;
import com.example.vocabmate.Model.StatisticTopic;
import com.example.vocabmate.Model.Topic;
import com.example.vocabmate.Model.Vocab;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("learned/total/{userId}")
    Call<Integer> getTotalLearnedWords(@Path("userId") int userId);

    @GET("learned/topic")
    Call<List<StatisticTopic>> getLearnedTopics(@Query("accountId") int accountId);

    @POST("login")
    Call<AccountDTO> login(@Query("username") String username, @Query("password") String password);

    @GET("vocab/test")
    Call<List<Vocab>> getTest();

    @GET("vocab/testTopic/{topicId}")
    Call<List<Vocab>> getVocabsByTopic(@Path("topicId") int topicId);

    @GET("vocab/{vocabId}")
    Call<Vocab> getVocabById(@Path("vocabId") int vocabId);

    @GET("topic")
    Call<List<Topic>> getTopics();

    @GET("learned/vocabLearnedByTopic")
    Call<List<Vocab>> getLearnedVocabByTopicAndAccount(
            @Query("accountId") int accountId,
            @Query("topicId") int topicId
    );

    @GET("vocab/topic/{topicId}")
    Call<List<Vocab>> getVocabByTopic(@Path("topicId") int topicId);

}
