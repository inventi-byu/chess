package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
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
}
