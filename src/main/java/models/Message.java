package models;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

public class Message {
    @Expose (serialize = true,deserialize = true)
    private String author;
    @Expose (serialize = true,deserialize = true)
    private String text;
    @Expose (serialize = false,deserialize = false)
    private LocalDateTime time;
    @Expose (serialize = true,deserialize = true)
    private String created;
    public static final int USER_LOGGED_IN = 1;
    public static final int USER_LOGGED_OUT = 2;

    private static final String AUTHOR_SYSTEM = "SYSTEM";

    public Message(String author, String text) {
        this.author = author;
        this.text = text;
        this.time = LocalDateTime.now();
        this.created = time.getHour() + ":" + time.getMinute() + "-" ;
    }

    public Message(int type, String username){
        this.author = AUTHOR_SYSTEM;
        this.time = LocalDateTime.now();
        this.created = time.getHour() + ":" + time.getMinute();
        if(type == USER_LOGGED_IN){
            text = username + " has joined the chat\n";
        }else if(type == USER_LOGGED_OUT){
            text = username + " has leaved the chat\n";
        }
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getCreated() {
        return created;
    }

    @Override
    public String toString() {
        if(author.equalsIgnoreCase(AUTHOR_SYSTEM))
            return text;

        String s = author + " ["+created+"]\n";
        s += text + "\n";
        return s;
    }
}
