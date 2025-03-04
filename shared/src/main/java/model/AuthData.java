package model;

/**
 * A record for storing a user's authentication data
 * @param username the username with whom to associate the authToken
 * @param authToken the authorization token of the user
 */
public record AuthData(String username, String authToken) { }
