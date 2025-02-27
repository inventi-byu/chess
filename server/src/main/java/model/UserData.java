package model;

/**
 * A record for storing a user's data
 * @param username the username
 * @param password the password
 * @param email the user's email
 */
public record UserData(String username, String password, String email) {}
