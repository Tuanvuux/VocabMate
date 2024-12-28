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
    // thong so tu da hoc
    @GET("learned/total/{userId}")
    Call<Integer> getTotalLearnedWords(@Path("userId") int userId);

    // thong ke tu da hoc theo chu de
    @GET("learned/topic")
    Call<List<StatisticTopic>> getLearnedTopics(@Query("accountId") int accountId);

    //dang nhap
    @POST("login")
    Call<AccountDTO> login(@Query("username") String username, @Query("password") String password);

    //kiem tra tu vung ngau nhien
    @GET("vocab/test")
    Call<List<Vocab>> getTest();

    //Kiem tra tu vung theo chu de
    @GET("vocab/testTopic/{topicId}")
    Call<List<Vocab>> getVocabsByTopic(@Path("topicId") int topicId);

    // tu vung theo id
    @GET("vocab/{vocabId}")
    Call<Vocab> getVocabById(@Path("vocabId") int vocabId);

    // all topic
    @GET("topic")
    Call<List<Topic>> getTopics();

    // tu vung da hoc theo chu de
    @GET("learned/vocabLearnedByTopic")
    Call<List<Vocab>> getLearnedVocabByTopicAndAccount(
            @Query("accountId") int accountId,
            @Query("topicId") int topicId
    );
    //tu vung theo chu de
    @GET("vocab/topic/{topicId}")
    Call<List<Vocab>> getVocabByTopic(@Path("topicId") int topicId);
    // dk account
    @POST("accounts/register")
    Call<AccountDTO> registerAccount(@Body AccountDTO newAccount);

    // API lấy thông tin tài khoản theo accountId
    @GET("accounts/{accountId}")
    Call<AccountDTO> getAccountById(@Path("accountId") long accountId);

    // API cập nhật thông tin tài khoản theo accountId
    @POST("accounts/{accountId}")
    Call<AccountDTO> updateAccount(@Path("accountId") long accountId, @Body AccountDTO updatedAccount);

    // luu tu vung da hoc
    @POST("learned/vocabLearn")
    Call<Boolean> saveLearnHistory(@Body LearnHistory learnHistory);





}
