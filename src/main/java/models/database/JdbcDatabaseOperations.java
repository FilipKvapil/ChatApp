package models.database;

import models.Message;

import java.sql.*;
import java.util.List;

public class JdbcDatabaseOperations implements DatabaseOperations{
    private final Connection connection;

    public JdbcDatabaseOperations(String driver , String url) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        this.connection = DriverManager.getConnection(url);
    }


    @Override
    public void addMesages(Message message){
        try {
        String sql =
                "INSERT INTO ChatMessages (author, text, created)"
                + "VALUES ("
                +"'" + message.getAuthor()+"',"
                        +"'" + message.getText()+"',"
                        +"'" + Timestamp.valueOf((message.getCreated()))+"'"
                +");";
        Statement statement;
        statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //TODO: dodělat
    @Override
    public List<Message> getMessage() {
        return null;
    }
}
