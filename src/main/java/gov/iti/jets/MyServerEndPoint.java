package gov.iti.jets;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import com.google.gson.Gson;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint(value = "/chatEndpoint")

public class MyServerEndPoint {

    private Session session;
    private static Set<Session> chatusersSet = new CopyOnWriteArraySet<>();

    @Inject
    ChatSessionController chatSessionController;

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        System.out.println("in onOpen, welcome!!");

        this.session = session;

        chatusersSet.add(session);

        String query = session.getQueryString();
        String username = query.split("&")[0].substring(query.split("&")[0].indexOf("=")+1);
        String gender = query.split("&")[1].substring(query.split("&")[1].indexOf("=")+1);
        System.out.println("open connection with user:" + username + "  " + gender);
        ChatSessionController.addUser(session.getId(),new UserInfo(username,gender));

        broadcast("online");
    }

    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("message recieved " + message);

        UserInfo sender = ChatSessionController.getUserInfo(session.getId());
        MessageDto messageDto = new MessageDto();
        messageDto.setUserName(sender.getUsername());
        messageDto.setGender(sender.getGender());
        messageDto.setContent(message);
        System.out.println("message :" + messageDto);
        chatusersSet.forEach(user->{
            try {
                user.getBasicRemote().sendText(new Gson().toJson(messageDto));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @OnClose
    public void close(Session session) throws IOException, EncodeException {
        // chatusersSet.remove(session);
        // MessageDto message = new MessageDto();
//        Map<String,String> chatusers = chatSessionController.getUsers();
//        String chatuser = chatusers.get(session.getId());
//        message.setUserName(chatuser);
//        chatusers.remove(chatuser);

        chatusersSet.remove(session);
        ChatSessionController.removeUser(session.getId());
        broadcast("offline");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("There has been an error with session " + session.getId());
    }

    private static void broadcast(String message)
            throws IOException, EncodeException {

        chatusersSet.forEach(session -> {
            synchronized (session) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

/*
package gov.iti.jets;

import java.io.Console;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.EncodeException;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint(value = "/chatEndpoint/{username}/{gender}")

public class MyServerEndPoint {
    private Session session;
    private static Set<Session> chatusersSet = new CopyOnWriteArraySet<>();

    @Inject
    ChatSessionController chatSessionController;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("gender") String gender)
            throws IOException, EncodeException {
        System.out.println("in onOpen, welcome!!");

        this.session = session;
        chatusersSet.add(session);

        System.out.println("user name: " + session.getId());

        System.out.println("user name: " + username);
        System.out.println("user gender: " + gender);

        UserInfo userInfo = new UserInfo(username, gender);
        ChatSessionController.addUser(session.getId(), userInfo);

        MessageDto message = new MessageDto();
        message.setUserName(username);
        message.setGender(gender);
        message.setContent("welcome ya " + username);
        Map<String, UserInfo> chatusers = chatSessionController.getUsers();

        System.out.println("welcome ya " + chatusers.get(session.getId()).getUsername());
        broadcast(message);
    }

    @OnMessage
    public void onMessage(String messageContent, Session session) throws EncodeException, IOException {
        System.out.println("onMessage");

        Map<String, UserInfo> chatusers = chatSessionController.getUsers();
        MessageDto message = new MessageDto();
        message.setUserName(chatusers.get(session.getId()).getUsername());
        message.setGender(chatusers.get(session.getId()).getGender());
        message.setContent(messageContent);
        broadcast(message);
    }

    @OnClose
    public void close(Session session) {
        chatusersSet.remove(session);
        MessageDto message = new MessageDto();
        Map<String, UserInfo> chatusers = chatSessionController.getUsers();
        UserInfo chatuser = chatusers.get(session.getId());
        message.setUserName(chatuser.getUsername());
        message.setGender(chatuser.getGender());
        chatusers.remove(session.getId());
        chatusersSet.remove(session.getId());
        message.setContent("Disconnected from server");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("There has been an error with session " + session.getId());
    }

    private static void broadcast(MessageDto message)
            throws IOException, EncodeException {

        chatusersSet.forEach(session -> {
            synchronized (session) {
                try {
                    session.getBasicRemote().sendObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
*/