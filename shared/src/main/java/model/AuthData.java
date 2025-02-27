package model;

/**
 * A record for storing a user's authentication data
 * @param authToken the authorization token of the user
 * @param username the username with whom to associate the authToken
 */
public record AuthData(String authToken, String username) { }
