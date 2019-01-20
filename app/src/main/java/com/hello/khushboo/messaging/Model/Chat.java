package com.hello.khushboo.messaging.Model;

public class Chat {

    private String Sender;
    private String receiver;
    private String message;
    private boolean isseen;


    public Chat(String sender, String receiver, String message,boolean isseen) {
        this.Sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
    }
    public Chat(){

    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
