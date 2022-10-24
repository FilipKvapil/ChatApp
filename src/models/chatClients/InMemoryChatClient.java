package models.chatClients;

import models.Message;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InMemoryChatClient implements ChatClient{
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;

    private List<ActionListener> listenerLoggedUsersChanged = new ArrayList<>();
    private List<ActionListener> listenerMessagesChanged = new ArrayList<>();

    public InMemoryChatClient(){
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
        System.out.println("new Message - " + text);
        raiseEventMessagesChanged();
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        addSystemMessage(Message.USER_LOGGED_IN, loggedUser);
        System.out.println("User logged in" + userName);
        raiseEventLoggedUsersChanged();
    }

    @Override
    public void logout() {
        addSystemMessage(Message.USER_LOGGED_OUT, loggedUser);
        loggedUsers.remove(loggedUser);
        loggedUser = null;
        System.out.println("User logged out");
        raiseEventLoggedUsersChanged();
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

    @Override
    public void addActionListenerLoggedUsersChanged(ActionListener toAdd) {
        listenerLoggedUsersChanged.add(toAdd);
    }

    @Override
    public void addActionListenerMessagesChanged(ActionListener toAdd) {
        listenerMessagesChanged.add(toAdd);
    }

    private void raiseEventLoggedUsersChanged(){
        for(ActionListener al : listenerLoggedUsersChanged) {
            al.actionPerformed(new ActionEvent(this,1,"usersChanged"));
        }
    }

    private void raiseEventMessagesChanged(){
        for(ActionListener al : listenerMessagesChanged) {
            al.actionPerformed(new ActionEvent(this,1,"messagesChanged"));
        }
    }

    private void addSystemMessage(int type, String author){
        messages.add(new Message(type,author));
        raiseEventMessagesChanged();
    }
}
