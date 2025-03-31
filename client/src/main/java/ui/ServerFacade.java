package ui;

import com.google.gson.Gson;
import exceptions.ServerFacadeException;
import model.AuthData;
import model.GameMetaData;
import model.UserData;
import server.service.exception.ResponseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {

    private String serverURL;

    public ServerFacade(String serverURL){
        this.serverURL = serverURL;
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */

    public boolean clear(){
        try {
            String path = "/db";
            this.makeHttpRequest("DELETE", path, null, null, null);
            return true;
        } catch (Exception exception){
            return false;
        }
    }

    /**
     * Accesses the register endpoint.
     * @return the user's AuthData
     */
    public AuthData register(String username, String password, String email) throws ServerFacadeException {
        String path = "/user";
        UserData userData = new UserData(username, password, email);
        AuthData authData = this.makeHttpRequest("POST", path, userData, null, AuthData.class);
        return authData;
    }
    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public AuthData login(String username, String password) throws ServerFacadeException {
        String path = "/session";

        record UserCredentials(String username, String password){}
        UserCredentials credentials = new UserCredentials(username, password);

        AuthData authData = this.makeHttpRequest("POST", path, credentials, null, AuthData.class);
        return authData;
    }
    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public boolean logout(String authToken) throws ServerFacadeException {
        String path = "/session";

        record UserCredentials(String username, String password){}
        HashMap<String, String> headers = new HashMap<>(1);
        headers.put("authorization", authToken);

        this.makeHttpRequest("DELETE", path, null, headers, AuthData.class);
        return true;
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public GameMetaData[] listGames(String authToken) throws ServerFacadeException {
        String path = "/game";

        HashMap<String, String> headers = new HashMap<>(1);
        headers.put("authorization", authToken);

        record GamesMap(GameMetaData[] games){}


        GamesMap gamesMap = this.makeHttpRequest("GET", path, null, headers, GamesMap.class);
        return gamesMap.games();
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public int createGame(String gameName, String authToken) throws ServerFacadeException {
        String path = "/game";

        HashMap<String, String> headers = new HashMap<>(1);
        headers.put("authorization", authToken);

        record GameNameContainer(String gameName){}
        record GameIDContainer(String gameID){}

        GameNameContainer gameToCreate = new GameNameContainer(gameName);

        GameIDContainer gameID = this.makeHttpRequest("POST", path, gameToCreate, headers, GameIDContainer.class);
        return Integer.parseInt(gameID.gameID());
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public void joinGame(String playerColor, int gameID, String authToken) throws ServerFacadeException {
        String path = "/game";

        HashMap<String, String> headers = new HashMap<>(1);
        headers.put("authorization", authToken);

        record JoinGameInfo(String playerColor, int gameID){}
        //record GameIDContainer(String gameID){}

        JoinGameInfo joinGameInfo = new JoinGameInfo(playerColor, gameID);

        this.makeHttpRequest("PUT", path, joinGameInfo, headers, null);
    }

    private <T> T makeHttpRequest(String method,
                                  String path,
                                  Object request,
                                  HashMap<String, String> headers,
                                  Class<T> responseClass)
            throws ServerFacadeException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            this.writeBody(request, headers, http);
            http.connect();
            this.throwIfNotSuccessful(http);

            return this.readBody(http, responseClass);
        } catch (ResponseException exception){
            throw new ServerFacadeException(exception.getStatusCode(), exception.getMessage());
        } catch (URISyntaxException | IOException exception){
            throw new ServerFacadeException(500, exception.getMessage());
        }
    }

    private void writeBody(Object request, HashMap<String, String> headers, HttpURLConnection http) throws IOException {
        if (headers != null){
            // Add all the headers specified
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                http.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        if (request != null){
            http.addRequestProperty("Content-Type", "application/json");
            String requestJSON = new Gson().toJson(request);
            try (OutputStream requestBody = http.getOutputStream()){
                requestBody.write(requestJSON.getBytes());
            }
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0){
            try (InputStream responseBody = http.getInputStream()){
                InputStreamReader reader = new InputStreamReader(responseBody);
                if(responseClass != null){
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ServerFacadeException {
        int status = http.getResponseCode();
        if (!this.isSuccessful(status)){
            try (InputStream responseError = http.getErrorStream()){
                if (responseError != null){
                    ResponseException exception = ResponseException.fromJson(responseError);
                    throw new ServerFacadeException(status, exception.getMessage());
                }
            }
        }
    }

    private boolean isSuccessful(int status){
        return status / 100 == 2; // Any code in 200 to 299 range is successful
    }
}

