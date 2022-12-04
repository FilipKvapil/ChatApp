package models.chatClients.fileOperations;

import models.Message;

import java.util.List;

//interface pro práci se soubory
public interface ChatFileOperations {
    //ulož do soubpru zprávu
    void writeMessages(List<Message> messages);
    //přečti ze souboru zprávu
    List<Message> readMessages();
}
