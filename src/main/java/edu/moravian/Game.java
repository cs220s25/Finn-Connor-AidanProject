package edu.moravian;
import exceptions.GameAlreadyStartedException;
import exceptions.GameNotStartedException;
import exceptions.StorageException;

import java.util.*;

public class Game {
    private final GameStorage storage;
    private boolean gameWon = false;

    public Game(GameStorage storage) {
        this.storage = storage;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public String startGame(String playerName) throws StorageException {
        if (storage.getPlayerLocation(playerName) != null) {
            throw new GameAlreadyStartedException("A game is already in progress.");
        }

        storage.addLocation("entrance hall", "A grand entrance hall with faded wallpaper and a dusty chandelier hanging above. A big exit door stands here with two circular slots carved into it.");
        storage.addLocation("dining room", "A large dining table covered in cobwebs, with dusty chairs around it.");
        storage.addLocation("kitchen", "An old, dark kitchen with rusted pots and an eerie silence.");
        storage.addLocation("library", "Shelves lined with ancient, leather-bound books and a strange smell of decay.");
        storage.addLocation("study", "A small study with a worn desk and an old, flickering lamp.");
        storage.addLocation("upstairs hall", "A dimly lit hallway, with doors leading to various rooms and a faint creaking sound.");
        storage.addLocation("bedroom", "A neglected bedroom with a torn bedspread and an unsettling stillness in the air.");
        storage.addLocation("bathroom", "A grimy bathroom with a cracked mirror and an odor of mildew.");
        storage.addLocation("basement", "A cold, damp basement with an eerie echo, the floor covered in old wooden planks.");

        storage.connectLocations("entrance hall", "dining room");
        storage.connectLocations("entrance hall", "library");
        storage.connectLocations("entrance hall", "upstairs hall");
        storage.connectLocations("entrance hall", "basement");
        storage.connectLocations("dining room", "kitchen");
        storage.connectLocations("library", "study");
        storage.connectLocations("upstairs hall", "bedroom");
        storage.connectLocations("upstairs hall", "bathroom");
        storage.connectLocations("basement", "entrance hall");

        storage.setItem("kitchen", "knife");
        storage.setItem("bedroom", "lantern");
        storage.setItem("basement", "circular stone 2");
        storage.setItem("study", "circular stone 1");
        storage.setMonster("bathroom", true);
        storage.setMonster("basement", true);
        storage.setMonster("bedroom", true);

        storage.setPlayerLocation(playerName, "entrance hall");

        return "Game started. You awaken in a grand entrance hall of a creepy mansion. A big exit door stands here with two circular slots carved into it.";
    }

    public String exitGame(String playerName) throws StorageException {
        if (storage.getPlayerLocation(playerName) == null) {
            throw new GameNotStartedException("No game in progress.");
        }

        storage.removePlayerItems(playerName);
        storage.setPlayerLocation(playerName, null);
        storage.close();

        return "Game exited.";
    }

    public String movePlayer(String playerName, String locationName) throws StorageException {
        if (storage.getPlayerLocation(playerName) == null) {
            throw new GameNotStartedException("No game in progress.");
        }

        String currentLocation = storage.getPlayerLocation(playerName);
        Location destination = storage.getLocation(locationName);

        if (destination == null) {
            return "That location does not exist.";
        }

        Set<String> connectedLocations = storage.getLocation(currentLocation).getConnectedLocations();

        if (!connectedLocations.contains(locationName)) {
            return "You cannot move to " + locationName + " from " + currentLocation + ". The location is not connected.";
        }

        if ("basement".equals(locationName) && !storage.playerHasItem(playerName, "lantern")) {
            return "It's too dark to enter the basement without a light source.";
        }

        StringBuilder result = new StringBuilder();

        if (destination.hasMonster()) {
            boolean monsterDefeated = handleMonster(playerName, locationName, destination, result);
            if (!monsterDefeated) {
                return result.toString();
            }
        }

        storage.setPlayerLocation(playerName, locationName);
        result.append("You moved to ").append(locationName).append(".\nDescription: ").append(destination.getDescription());

        result.append(handleItem(destination));

        return result.toString();
    }

    private boolean handleMonster(String playerName, String locationName, Location destination, StringBuilder result) throws StorageException {
        String requiredWeapon = "knife";

        if (!storage.playerHasItem(playerName, requiredWeapon)) {
            result.append("A monster blocks your path! You need a ").append(requiredWeapon).append(" to defeat it.\n").append("You retreat back to ").append(storage.getPlayerLocation(playerName)).append(".");
            return false;
        } else {
            destination.setMonster(false);
            storage.setLocationMonster(locationName, false);
            result.append("A monster was here, but you defeated it with your ").append(requiredWeapon).append("!\n");
            return true;
        }
    }

    private String handleItem(Location destination) {
        if (destination.getItem() != null) {
            return "\nThere is an item here: " + destination.getItem() + ".";
        }
        return "";
    }

    public Set<String> getAvailableLocations(String playerName) throws StorageException {
        if (storage.getPlayerLocation(playerName) == null) {
            throw new GameNotStartedException("No game in progress.");
        }

        String locationName = storage.getPlayerLocation(playerName);
        Location location = storage.getLocation(locationName);
        return location.getConnectedLocations();
    }

    public String pickUpItem(String playerName) throws StorageException {
        if (storage.getPlayerLocation(playerName) == null) {
            throw new GameNotStartedException("No game in progress.");
        }

        String locationName = storage.getPlayerLocation(playerName);
        Location location = storage.getLocation(locationName);

        if (location.getItem() != null) {
            storage.addItemToPlayer(playerName, location.getItem());
            location.setItem(null);
            return "You picked up the item.";
        }
        return "No items to pick up here.";
    }

    public String useItem(String playerName, String itemName) throws StorageException {
        if (!storage.getPlayerLocation(playerName).equals("entrance hall")) {
            return "You can't use the " + itemName + " here.";
        }

        if (!itemName.equals("circular stone 1") && !itemName.equals("circular stone 2")) {
            return "You can't use the " + itemName + " here.";
        }

        storage.markStoneUsed(itemName, "entrance hall");
        storage.removeItemFromPlayer(playerName, itemName);

        if (storage.areAllStonesUsed("entrance hall")) {
            gameWon = true;
            return "You place the second stone in the exit door and start to feel a rumble. The double doors creak open, revealing the night sky beyond. You have escaped the mansion! Thanks for playing!";
        }

        return "You placed the " + itemName + " in the exit door.";
    }

    public String processUseItem(String playerName, String itemName) throws StorageException {
        String message = useItem(playerName, itemName);

        if (isGameWon()) {
            exitGame(playerName);
            return message;
        }
        return message;
    }

    public String getPlayerInventory(String playerName) throws StorageException {
        if (storage.getPlayerLocation(playerName) == null) {
            throw new GameNotStartedException("No game in progress.");
        }

        String items = storage.getPlayerItem(playerName);
        return items != null ? "Your inventory: " + items : "Your inventory is empty.";
    }
}