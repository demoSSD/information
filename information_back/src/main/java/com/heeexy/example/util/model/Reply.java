package com.heeexy.example.util.model;

import java.util.Date;

public class Reply {
    int id;
    int question_id;
    String reply_id;
    String content;
    Date created_time;
    int accept;
    int delete_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }

    public int getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(int delete_status) {
        this.delete_status = delete_status;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", question_id=" + question_id +
                ", reply_id='" + reply_id + '\'' +
                ", content='" + content + '\'' +
                ", created_time=" + created_time +
                ", accept=" + accept +
                ", delete_status=" + delete_status +
                '}';
    }
}
