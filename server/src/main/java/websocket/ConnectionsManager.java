package websocket;

import com.google.gson.Gson;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.ArrayList;

public class ConnectionsManager {

    private ArrayList<Connection> connections;
    private ArrayList<String> currentUsers;

    public ConnectionsManager(){
        this.connections = new ArrayList<Connection>();
        this.currentUsers = new ArrayList<String>();
    }

    public void addConnection(Connection connection){
        this.connections.add(connection);
        this.currentUsers.add(connection.getUsername());
    }

    public void setConnections(ArrayList<Connection> connections){
        this.connections = connections;
    }

    public ArrayList<Connection> getConnections(){
        return this.connections;
    }

    public void notifyAllUsers(NotificationMessage notification) throws IOException {
        this.notify(this.currentUsers.toArray(new String[0]), notification);
    }

    public void notifyAllExcept(String exclude, NotificationMessage notification) throws IOException {
        ArrayList<String> allUsersExcept = new ArrayList<String>(this.currentUsers);
        allUsersExcept.remove(exclude);
        this.notify(allUsersExcept.toArray(new String[0]), notification);
    }

    public void notifyAllExcept(String[] excludes, NotificationMessage notification) throws IOException {
        ArrayList<String> allUsersExcept = new ArrayList<String>(this.currentUsers);

        for (String user : excludes){
            try {
                allUsersExcept.remove(user);
            } catch (Exception exception){}
        }

        this.notify(allUsersExcept.toArray(new String[0]), notification);
    }

    public void notify(String[] usernames, NotificationMessage notification) throws IOException {
        for (String user : usernames){
            this.notify(user, notification);
        }
    }

    public void notify(String username, NotificationMessage notification) throws IOException {
        Connection connection = this.getConnectionFromUsername(username);
        if(connection == null){
            // TODO: Do nothing or do something?
            return;
        }
        if (connection.session.isOpen()){
            connection.send(new Gson().toJson(notification));
        } else {
            this.removeConnection(connection);
        }
    }

    private Connection getConnectionFromUsername(String username){
        Connection connectionToReturn = null;
        for (Connection connection : this.connections){
            if(connection.getUsername().equals(username)){
                connectionToReturn = connection;
                break;
            }
        }
        return connectionToReturn;
    }

    public void removeConnection(Connection connection){
        this.connections.remove(connection);
        this.currentUsers.remove(connection.username);
    }
}
