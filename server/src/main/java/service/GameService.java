package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import server.service.exception.ResponseException;
import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import service.request.ListGamesRequest;
import service.result.CreateGameResult;
import service.result.JoinGameResult;
import service.result.ListGamesResult;

import java.util.List;

public class GameService extends Service {

    public GameService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        super(authDAO, userDAO, gameDAO);
    }

    /**
     * Creates a game in the database.
     * @param request the CreateGameRequest passed in by the handler.
     * @return a CreateGameResult with status code 200.
     */
    public CreateGameResult createGame(CreateGameRequest request){
        String authToken = request.getAuthorization();
        this.authenticateWithToken(authToken);

        String gameName = request.getGameName();
        if(gameName == null || gameName.isEmpty()){
            throw new ResponseException(400, "Error: bad request");
        }

        int gameID = gameDAO.addGame(gameName);
        return new CreateGameResult(200, gameID);

    }

    /**
     * Lists all the games in the database.
     * @param request the ListGamesRequest passed in by the handler.
     * @return a ListGamesResult with status code 200, and a list of all the games.
     */
    public ListGamesResult listGames(ListGamesRequest request){
        String authToken = request.getAuthorization();
        this.authenticateWithToken(authToken);

        GameMetaData[] games = this.gameDAO.getAllGames();
        return new ListGamesResult(200, games);
    }

    /**
     * Adds a user to a game in the database.
     * @param request the JoinGameRequest passed in by the handler.
     * @return a JoinGameResult with status code 200.
     */
    public JoinGameResult joinGame(JoinGameRequest request){
        String authToken = request.getAuthorization();
        AuthData authData = this.authenticateWithToken(authToken);

        int gameID = request.getGameID();
        String playerColor = request.getPlayerColor();

        // Make sure ID is valid
        GameData gameToJoin = gameDAO.getGame(gameID);

        if (gameToJoin == null){
            throw new ResponseException(400, "Error: bad request");
        }

        if (playerColor == null){
            throw new ResponseException(400, "Error: bad request");
        }

        switch (playerColor){
            case "WHITE":
                if (gameToJoin.whiteUsername() != null){
                    throw new ResponseException(403, "Error: already taken");
                }
                gameDAO.addUserToGame(gameID, playerColor, authData.username());
                return new JoinGameResult(200);
            case "BLACK":
                if (gameToJoin.blackUsername() != null){
                    throw new ResponseException(403, "Error: already taken");
                }
                gameDAO.addUserToGame(gameID, playerColor, authData.username());
                return new JoinGameResult(200);
            default:
                throw new ResponseException(400, "Error: bad request");
        }

    }
}


