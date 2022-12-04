package models.database;

import models.Message;

import java.util.List;
//interface pro práci s lokální databází
public interface DatabaseOperations {
    void addMesages(Message message);
    List<Message> getMessage();
}
