package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage {
    private GameData game;

    public LoadGameMessage(GameData game){
        super(ServerMessageType.LOAD_GAME);
        this.game = message;
    }

    public void setMessage(String message){
        this.game = message;
    }

    public String getMessage(){
        return this.game;
    }

    @Override
    public boolean equals(Object o) {
        if(super.equals(o)){
            if (this == o) {
                return true;
            }
            if (!(o instanceof NotificationMessage)) {
                return false;
            }
            NotificationMessage that = (NotificationMessage) o;
            return this.getServerMessageType() == that.getServerMessageType() && this.getMessage().equals(that.getMessage());
        }
        return false;
    }

    @Override
    public String toString(){
        return "NotificationMessage(" + this.getMessage() + ")";
    }
}
