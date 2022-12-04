package models.chatClients.fileOperations;

import models.Message;

import java.util.List;
public class CSVChatFileOperations implements ChatFileOperations{

    private static final String MESSAGE_FILE = "./messages.csv";


    @Override
    public void writeMessages(List<Message> messages) {

    }

    @Override
    public List<Message> readMessages() {
        return null;
    }
}
