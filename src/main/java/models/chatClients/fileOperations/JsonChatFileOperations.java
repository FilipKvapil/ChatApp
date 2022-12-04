package models.chatClients.fileOperations;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.LocalDateTimeDeserializer;
import models.LocalDateTimeSerializer;
import models.Message;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonChatFileOperations implements ChatFileOperations {
    private final Gson gson;
    //název souboru
    private static final String MESSAGE_FILE = "./messages.json";

    public JsonChatFileOperations (){
        //inicializace gson
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
    }

    @Override
    public void writeMessages(List<Message> messages) {
        String jsonText = gson.toJson(messages);
        System.out.println(jsonText);
        try {
            FileWriter writer = new FileWriter(MESSAGE_FILE);
            writer.write(jsonText);
            //odstranění všech dat v zapisovači
            writer.flush();
            //zavření souboru
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> readMessages() {
        try {
            FileReader reader = new FileReader(MESSAGE_FILE);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                jsonText.append(line);
            }
            reader.close();

            Type targetType = new TypeToken<ArrayList<Message>>(){}.getType();
            List<Message> messages = gson.fromJson(jsonText.toString(),targetType);

            return messages;
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
