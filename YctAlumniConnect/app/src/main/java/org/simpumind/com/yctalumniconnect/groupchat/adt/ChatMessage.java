package org.simpumind.com.yctalumniconnect.groupchat.adt;

/**
 * Created by simpumind on 1/11/16.
 */
public class ChatMessage {

    private String username;
    private String message;
    private long timeStamp;

    public boolean left;

    public ChatMessage(boolean left, String username, String message, long timeStamp){
        this.username  = username;
        this.message   = message;
        this.timeStamp = timeStamp;
        this.left = left;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
