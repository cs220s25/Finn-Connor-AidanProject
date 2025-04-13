package edu.moravian;
import exceptions.StorageException;

import java.util.*;

public class MemoryStorage implements GameStorage {
    private final Map<String, Location> locations = new HashMap<>();
    private final Map<String, String> playerLocations = new HashMap<>();
    private final Map<String, Set<String>> playerItems = new HashMap<>();
    private Map<String, Boolean> stonesUsedInRoom = new HashMap<>();

    public MemoryStorage() {
        stonesUsedInRoom.put("circular stone 1", false);
        stonesUsedInRoom.put("circular stone 2", false);
    }

    @Override
    public void addLocation(String name, String description) {
        locations.put(name, new Location(description));
    }

    @Override
    public void connectLocations(String from, String to) {
        Location fromLocation = locations.get(from);
        Location toLocation = locations.get(to);

        if (fromLocation != null && toLocation != null) {
            fromLocation.addConnectedLocation(to);
            toLocation.addConnectedLocation(from);
        }
    }

    @Override
    public void setItem(String locationName, String item) {
        Location location = locations.get(locationName);
        if (location != null) {
            location.setItem(item);
        }
    }

    @Override
    public void setMonster(String locationName, boolean hasMonster) {
        Location location = locations.get(locationName);
        if (location != null) {
            location.setMonster(hasMonster);
        }
    }

    @Override
    public void setLocationMonster(String locationName, boolean hasMonster) throws StorageException {
        Location location = locations.get(locationName);
        if (location == null) {
            throw new StorageException("Location not found: " + locationName);
        }
        location.setMonster(hasMonster);
    }

    @Override
    public Location getLocation(String name) {
        return locations.get(name);
    }

    @Override
    public void setPlayerLocation(String playerName, String locationName) {
        playerLocations.put(playerName, locationName);
    }

    @Override
    public String getPlayerLocation(String playerName) {
        return playerLocations.get(playerName);
    }

    @Override
    public void addItemToPlayer(String playerName, String item) {
        playerItems.computeIfAbsent(playerName, k -> new HashSet<>()).add(item);
    }

    @Override
    public boolean playerHasItem(String playerName, String item) {
        return playerItems.containsKey(playerName) && playerItems.get(playerName).contains(item);
    }

    @Override
    public String getPlayerItem(String playerName) {
        if (!playerItems.containsKey(playerName)) return null;
        return String.join(", ", playerItems.get(playerName));
    }

    @Override
    public void removePlayerItems(String playerName) {
        playerItems.remove(playerName);
    }

    @Override
    public void removeItemFromPlayer(String playerName, String item) {
        if (playerItems.containsKey(playerName)) {
            playerItems.get(playerName).remove(item);
        }
    }

    public void markStoneUsed(String stone, String room) {
        if (room.equals("entrance hall") && stonesUsedInRoom.containsKey(stone)) {
            stonesUsedInRoom.put(stone, true);
        }
    }

    public boolean areAllStonesUsed(String room) {
        return room.equals("entrance hall") &&
                stonesUsedInRoom.getOrDefault("circular stone 1", false) &&
                stonesUsedInRoom.getOrDefault("circular stone 2", false);
    }

    @Override
    public void close() {
    }
}
