package model;
import chess.*;

/**
 * A record for storing a game's data.
 * @param gameID the game id
 * @param whiteUsername the username of the white player
 * @param blackUsername the username of the black player
 * @param gameName the name of the game
 * @param game the ChessGame object
 */
public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}

// Do I need to import all of chess for the `game` field to work correctly?