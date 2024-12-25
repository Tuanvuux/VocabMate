package com.example.vocabmate.Model;

public class Topic {
    private int topicId;
    private String topicName;
    private String topicStrans; // Mô tả (dịch)
    private String topicImg;    // Đường dẫn ảnh

    // Getter và Setter
    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicStrans() {
        return topicStrans;
    }

    public void setTopicStrans(String topicStrans) {
        this.topicStrans = topicStrans;
    }

    public String getTopicImg() {
        return topicImg;
    }

    public void setTopicImg(String topicImg) {
        this.topicImg = topicImg;
    }
}
