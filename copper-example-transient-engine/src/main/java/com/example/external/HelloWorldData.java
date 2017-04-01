package com.example.external;

import java.io.Serializable;

public class HelloWorldData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cid;
    private HelloWorldStatus status;
    private String sender;
    private String replier;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public HelloWorldStatus getStatus() {
        return status;
    }

    public void setStatus(HelloWorldStatus status) {
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReplier() {
        return replier;
    }

    public void setReplier(String replier) {
        this.replier = replier;
    }

}
