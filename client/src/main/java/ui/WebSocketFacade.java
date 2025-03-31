package ui;

import chess.ChessGame;
import chess.ChessPosition;
import exceptions.ServerFacadeException;
import model.GameData;

public class WebSocketFacade {

    private String webSocketURL;

    public WebSocketFacade(String webSocketURL){
        this.webSocketURL = webSocketURL;
    }

    public void observeGame(String gameID, String authToken) throws ServerFacadeException {
        return;
        // Not implemented until phase 6
    }

    public GameData makeMove(ChessGame.TeamColor teamColor, ChessPosition start, ChessPosition end) throws ServerFacadeException {
        throw new ServerFacadeException(0, "Not implemented");
    }

    public void leaveGame(String authToken, String username) throws ServerFacadeException {
        throw new ServerFacadeException(0, "Not implemented.");
    }

    public void resignGame(String authToken, String username) throws ServerFacadeException {
        throw new ServerFacadeException(0, "Not implemented.");
    }
}
