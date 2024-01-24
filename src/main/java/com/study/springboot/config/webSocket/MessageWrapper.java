package com.study.springboot.config.webSocket;

public class MessageWrapper {
    private String content;
    private String timestamp;
    private String userName;

    public MessageWrapper(String content, String timestamp, String userName) {
        this.content = content;
        this.timestamp = timestamp;
        this.userName = userName;
    }

    // 게터, 세터 등을 추가할 수 있습니다.

    // 예를 들어, 아래와 같이 게터를 추가할 수 있습니다.
    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUserName() {
        return userName;
    }
}

