package ui;

import chess.ChessBoard;
import com.google.gson.Gson;
import exceptions.ServerFacadeException;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import model.UserData;
import server.service.exception.ResponseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {

    private String serverURL;

    public ServerFacade(){
        this.serverURL = "http//localhost:8080";
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public boolean clear() throws ServerFacadeException {
        String path = "/db";
        this.makeHttpRequest("DELETE", path, null, null, null);
        return true;
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

        throw new ServerFacadeException(500, "Not implemented.");
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public GameMetaData[] listGames() throws ServerFacadeException {
        throw new ServerFacadeException(500, "Not implemented.");
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public int createGame(int gameID) throws ServerFacadeException {
        throw new ServerFacadeException(500, "Not implemented.");
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public GameData joinGame(String playerColor, int gameID) throws ServerFacadeException {
        throw new ServerFacadeException(500, "Not implemented.");
    }

    public ChessBoard observe(String gameID) throws ServerFacadeException {
        throw new ServerFacadeException(500, "Not implemented.");
    }

    public <T> T makeHttpRequest(String method, String path, Object request, HashMap<String, String> headers, Class<T> responseClass) throws ServerFacadeException {
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
        } catch (Exception exception){
            throw new ServerFacadeException(500, exception.getMessage());
        }
    }

    private void writeBody(Object request, HashMap<String, String> headers, HttpURLConnection http) throws IOException {
        if (request != null){
            http.addRequestProperty("Content-Type", "application/json");
            if (!headers.isEmpty())
                // Add all the headers specified
                for (Map.Entry<String, String> entry : headers.entrySet()){
                    http.addRequestProperty(entry.getKey(), entry.getValue());
                }
            String requestJSON = new Gson().toJson(request);
            try (OutputStream requestBody = http.getOutputStream()){
                requestBody.write(requestJSON.getBytes());
            }
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < -1){
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
                    throw new ServerFacadeException(exception.getStatusCode(), exception.getMessage());
                }
            }
        }
    }

    private boolean isSuccessful(int status){
        return status / 100 == 2; // Any code in 200 to 299 range is successful
    }
}

