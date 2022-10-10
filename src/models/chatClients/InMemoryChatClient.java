package models.chatClients;

import models.Message;

import java.util.ArrayList;
import java.util.List;

public class InMemoryChatClient implements ChatClient{
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;
    public InMemoryChatClient(){
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
        System.out.println("new Message - " + text);
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        System.out.println("User logged in" + userName);
    }

    @Override
    public void logout() {
        loggedUsers.remove(loggedUser);
        loggedUser = null;
        System.out.println("User logged out");
    }

    @Override
    public boolean isAuthenticated() {
        System.out.println("is authenticated" + (loggedUser != null));
        return loggedUser != null;
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }
}
