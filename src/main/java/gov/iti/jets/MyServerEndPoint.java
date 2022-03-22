package gov.iti.jets;

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

        Map<String, String> parameters =   session.getPathParameters();
        ChatSessionController.addUser(session.getId(), parameters.get("username") +"+"+ parameters.get("gender"));

//        session.getBasicRemote();
        broadcast("online");
    }

    @OnMessage
    public void onMessage(String message, Session session){

    }



    @OnClose
    public void close(Session session) {
        chatusersSet.remove(session);
        MessageDto message = new MessageDto();
//        Map<String,String> chatusers = chatSessionController.getUsers();
//        String chatuser = chatusers.get(session.getId());
//        message.setUserName(chatuser);
//        chatusers.remove(chatuser);
        message.setContent("Disconnected from server");
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
