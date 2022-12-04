package models.chatClients;

import models.Message;
import models.chatClients.fileOperations.ChatFileOperations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

//Třída pro práci se ukládáním do lokálního souboru

public class FileChatClient implements ChatClient{
    private String loggedUser;
    //aktuální
    private final List<String> loggedUsers;
    private final List<Message> messages;
    //historie
    private final List<ActionListener> listenerLoggedUsersChanged = new ArrayList<>();
    private final List<ActionListener> listenerMessagesChanged = new ArrayList<>();

    ChatFileOperations chatFileOperations;

    public FileChatClient(ChatFileOperations chatFileOperations){
        loggedUsers = new ArrayList<>();
        this.chatFileOperations = chatFileOperations;
        messages = chatFileOperations.readMessages();
    }
    //přidání nové zprávy a zavolání metody na vypsání
    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
        System.out.println("new Message - " + text);
        chatFileOperations.writeMessages(messages);
        raiseEventMessagesChanged();
    }
    //přihlášení uživatele
    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        addSystemMessage(Message.USER_LOGGED_IN, loggedUser);
        System.out.println("User logged in" + userName);
        raiseEventLoggedUsersChanged();
    }
    //odhlášení uživatele
    @Override
    public void logout() {
        addSystemMessage(Message.USER_LOGGED_OUT, loggedUser);
        loggedUsers.remove(loggedUser);
        loggedUser = null;
        System.out.println("User logged out");
        raiseEventLoggedUsersChanged();
    }
    //vrátí zda je uživatel přihlášen
    @Override
    public boolean isAuthenticated() {
        System.out.println("is authenticated" + (loggedUser != null));
        return loggedUser != null;
    }
    //vrátí přihlášené uživatele
    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }
    //vrátí zprávy
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
        chatFileOperations.writeMessages(messages);
        raiseEventMessagesChanged();
    }
}
