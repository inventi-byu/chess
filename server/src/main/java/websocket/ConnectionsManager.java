package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionsManager {

    private ArrayList<Connection> connections;

    public ConnectionsManager(){
        this.connections = new ArrayList<Connection>();
    }

    public void addConnection(Connection connection){
        this.connections.add(connection);
    }

    public void setConnections(ArrayList<Connection> connections){
        this.connections = connections;
    }

    public ArrayList<Connection> getConnections(){
        return this.connections;
    }

    public void notifyExcept(Session[] actors, Session exclude, ServerMessage notification) throws IOException{
        ArrayList<Session> allActorsExcept = new ArrayList<Session>(List.of(actors));
        allActorsExcept.remove(exclude);

        this.notify(allActorsExcept.toArray(new Session[0]), notification);
    }

    public void notify(Session[] actors, ServerMessage notification) throws IOException {
        for (Session actor : actors){
            this.notify(actor, notification);
        }
    }

    public void notify(Session session, ServerMessage notification) throws IOException {
        Connection connection = this.getConnectionFromSession(session);
        if(connection == null){
            // TODO: Do nothing or do something?
            return;
        }
        if (connection.session.isOpen()){
            connection.send(new Gson().toJson(notification));
        } else {
            this.removeConnection(username);
        }
    }

    private Connection getConnectionFromSession(Session session){
        Connection connectionToReturn = null;
        for (Connection connection : this.connections){
            try {
                if (connection.getSession().equals(session)) {
                    connectionToReturn = connection;
                    break;
                }
            } catch (NullPointerException exception){
                continue;
            }
        }
        return connectionToReturn;
    }

    public void removeConnection(String username){
        Connection connection = getConnectionFromUsername(username);
        this.connections.remove(connection);
    }
}
