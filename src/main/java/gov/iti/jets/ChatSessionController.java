
package gov.iti.jets;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatSessionController {
    private static Map<String, UserInfo> users = new HashMap<>();

    public ChatSessionController() {

    }

    public static String getUsersJson() {
        List<UserInfo> allUsers = new ArrayList<>();

        for (UserInfo user : users.values()) {
            System.out.println("user:" + user);
            allUsers.add(user);
        }
        return new Gson().toJson(allUsers);
    }

    public static void addUser(String id, UserInfo data) {
        users.put(id, data);
    }

    public void setUsers(Map<String, UserInfo> users) {
        this.users = users;
    }

    public Map<String, UserInfo> getUsers() {
        return users;
    }

    public static UserInfo getUserInfo(String id){
        return users.get(id);
    }

    public static void removeUser(String id){
        users.remove(id);
    }

}
