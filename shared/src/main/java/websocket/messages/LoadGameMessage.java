package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage {
    private GameData game;
    private boolean observing;

    public LoadGameMessage(GameData game, Boolean observing){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.observing = observing;
    }

    public void setGame(GameData game){
        this.game = game;
    }

    public GameData getGame(){
        return this.game;
    }

    public boolean isObserving(){
        return observing;
    }

    @Override
    public boolean equals(Object o) {
        if(super.equals(o)){
            if (this == o) {
                return true;
            }
            if (!(o instanceof LoadGameMessage)) {
                return false;
            }
            LoadGameMessage that = (LoadGameMessage) o;
            return this.getServerMessageType() == that.getServerMessageType() && this.getGame().equals(that.getGame());
        }
        return false;
    }

    @Override
    public String toString(){
        return "LoadGameMessage(" +
                "gameID = " + this.game.gameID() + ", " +
                "whiteUsername = " + this.game.whiteUsername() + ", " +
                "blackUsername = " + this.game.blackUsername() +  ", " +
                "gameName = " + this.game.gameName() + ", " +
                "ChessGame not shown)";
    }
}
