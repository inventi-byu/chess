package websocket;

import javax.websocket.Session;
import java.io.IOException;

public class Connection {
    public String username;
    public Session session;

    public Connection(String username, Session session){
        this.username = username;
        this.session = session;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setSession(Session session){
        this.session = session;
    }

    public Session getSession(){
        return this.session;
    }

    public void send(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

}
