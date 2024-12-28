package com.example.vocabmate.Service;

import com.example.vocabmate.Model.AccountDTO;
import com.example.vocabmate.Model.LearnHistory;
import com.example.vocabmate.Model.StatisticTopic;
import com.example.vocabmate.Model.Topic;
import com.example.vocabmate.Model.Vocab;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @POST("accounts/register")
    Call<AccountDTO> registerAccount(@Body AccountDTO newAccount);

    // API lấy thông tin tài khoản theo accountId
    @GET("accounts/{accountId}")
    Call<AccountDTO> getAccountById(@Path("accountId") long accountId);

    // API cập nhật thông tin tài khoản theo accountId
    @POST("accounts/{accountId}")
    Call<AccountDTO> updateAccount(@Path("accountId") long accountId, @Body AccountDTO updatedAccount);

    @POST("learned/vocabLearn")
    Call<Boolean> saveLearnHistory(@Body LearnHistory learnHistory);





}
