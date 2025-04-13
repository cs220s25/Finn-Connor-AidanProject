package edu.moravian;
import exceptions.StorageException;

public interface GameStorage {
    void addLocation(String name, String description) throws StorageException;
    void connectLocations(String from, String to) throws StorageException;
    void setItem(String locationName, String item) throws StorageException;
    void setMonster(String locationName, boolean hasMonster) throws StorageException;
    void setLocationMonster(String locationName, boolean hasMonster) throws StorageException;
    Location getLocation(String name) throws StorageException;
    void setPlayerLocation(String playerName, String locationName) throws StorageException;
    String getPlayerLocation(String playerName) throws StorageException;
    void addItemToPlayer(String playerName, String item) throws StorageException;
    boolean playerHasItem(String playerName, String item) throws StorageException;
    String getPlayerItem(String playerName)throws StorageException;
    void removePlayerItems(String playerName) throws StorageException;
    void removeItemFromPlayer(String playerName, String item) throws StorageException;
    void markStoneUsed(String stone, String room) throws StorageException;
    boolean areAllStonesUsed(String room) throws StorageException;
    void close() throws StorageException;
}
