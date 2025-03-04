package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.GameMetaData;

import java.util.List;

public class GameService extends Service {

    public GameService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        super(authDAO, userDAO, gameDAO);
    }

    public CreateGameResult createGame(CreateGameRequest request){
        String gameName = request.getGameName();

        String authToken = request.getAuthorization();
        this.authenticateWithToken(authToken);

        int gameID = gameDAO.addGame(gameName);
        return new CreateGameResult(200, gameID);

    }

    public ListGamesResult listGames(ListGamesRequest request){
        String authToken = request.getAuthorization();
        this.authenticateWithToken(authToken);

        List<GameMetaData> games = this.gameDAO.getAllGames();
        return new ListGamesResult(200, games);
    }

    public JoinGameResult joinGame(JoinGameRequest request){
        String authToken = request.getAuthorization();
        AuthData authData = this.authenticateWithToken(authToken);

        int gameID = request.getGameID();
        String playerColor = request.getPlayerColor();

        gameDAO.addUserToGame(gameID, playerColor, authData.username());
        return new JoinGameResult(200);
    }

}


