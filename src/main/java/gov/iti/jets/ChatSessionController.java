package gov.iti.jets;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatSessionController {


    private static Map<String, String>  users = new HashMap<>();

    public ChatSessionController() {

    }

    public String getUsers() {
        List<String> users = new ArrayList<>();
//        for();
        return new Gson().toJson(users);
    }

    public  static void addUser(String id, String data){
        users.put(id,data);
    }


//    public void setUsers(Map<String, String> chatusers) {
//        this.chatusers = chatusers;
//    }
}
