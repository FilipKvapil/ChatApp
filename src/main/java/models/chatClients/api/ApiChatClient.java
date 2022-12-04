package models.chatClients.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.LocalDateTimeDeserializer;
import models.LocalDateTimeSerializer;
import models.Message;
import models.chatClients.ChatClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
//Třída na práci s webovou databazí
public class ApiChatClient implements ChatClient {
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;

    private final List<ActionListener> listenerLoggedUsersChanged = new ArrayList<>();
    private final List<ActionListener> listenerMessagesChanged = new ArrayList<>();
    private final String BASE_URL = "http://fimuhkpro22021.aspifyhost.cz";
    private String token;

    private final Gson gson;

    public ApiChatClient(){
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        //nové vláko - refreshuje uživatele a zprávy
        Runnable refreshData = ()->{
            Thread.currentThread().setName("RefreshData");
            try {
                while (true){
                    if(isAuthenticated()){
                        refreshLoggedUsers();
                        refreshMessages();
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        Thread refreshDataThread = new Thread(refreshData);
        refreshDataThread.start();
    }

    @Override
    public void sendMessage(String text) {
        try{
            SendMessageRequest msgRequest = new SendMessageRequest(token,text);
             String url = BASE_URL + "/api/Chat/SendMassage";
             HttpPost post = new HttpPost(url);

             String jsonBody = gson.toJson(msgRequest);
             StringEntity body = new StringEntity(
                     jsonBody,
                     "usf-8"
             );
             body.setContentType("application/json");
             post.setEntity(body);

             CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post);

             if(response.getStatusLine().getStatusCode()==204){
                 refreshMessages();
             }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void login(String userName) {
        try{
            String url = BASE_URL + "/api/Chat/Login";
            CloseableHttpResponse response = http(userName);

            if(response.getStatusLine().getStatusCode() == 200){
                token = EntityUtils.toString(response.getEntity());
                token = token.replace("\"","").trim();

                loggedUser = userName;
                System.out.println("User logged in" + userName);
                refreshLoggedUsers();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        try{
            String url = BASE_URL + "/api/Chat/Logout";

            CloseableHttpResponse response = http(token);

            if(response.getStatusLine().getStatusCode() == 204){
                token = null;
                loggedUser = null;
                loggedUsers.clear();
                System.out.println("User logged out");
                raiseEventLoggedUsersChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private CloseableHttpResponse http (String text){
        try {
            String url = BASE_URL + "/api/Chat/Logout";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(
                    "\"" + text + "\"",
                    "utf-8"
            );
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            return httpClient.execute(post);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
    private void refreshLoggedUsers (){
        try {
            String url = BASE_URL + "/api/Chat/GetLoggedUsers";
            HttpGet get = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200){
                String jsonBody = EntityUtils.toString(response.getEntity());
                loggedUsers = gson.fromJson(
                        jsonBody,
                        new TypeToken<ArrayList<String>>(){}.getType()
                );
                raiseEventLoggedUsersChanged();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void refreshMessages (){
        try {
            String url = BASE_URL + "/api/Chat/GetMessages";
            HttpGet get = new HttpGet(url);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200){
                String jsonBody = EntityUtils.toString(response.getEntity());
                messages = gson.fromJson(
                        jsonBody,
                        new TypeToken<ArrayList<Message>>(){}.getType()
                );
                raiseEventMessagesChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
