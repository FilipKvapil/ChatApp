package models.chatClients;

import models.Message;

import java.awt.event.ActionListener;
import java.util.List;
//interface pro ChatClienta
public interface ChatClient {
    //odesílaná zpráva
    void sendMessage(String text);
    //přihlášení (jméno)
    void login(String userName);
    //odhlášení uživatele
    void logout();
    //vrátí tru/false když je přihlášený
    boolean isAuthenticated();
    //přihlášení úživatelé
    List<String> getLoggedUsers();
    //zprávy
    List<Message> getMessages();
    void addActionListenerLoggedUsersChanged(ActionListener toAdd);

    void addActionListenerMessagesChanged(ActionListener toAdd);
}
