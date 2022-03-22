package gov.iti.jets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatSessionController {


    private static Map<String, List<String>>  users = new HashMap<>();

    public ChatSessionController() {

    }

    public Map<String, String> getUsers() {
        return chatusers;
    }

    public void setUsers(Map<String, String> chatusers) {
        this.chatusers = chatusers;
    }
}
