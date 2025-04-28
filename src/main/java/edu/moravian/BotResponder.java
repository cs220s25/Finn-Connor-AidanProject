package edu.moravian;
import exceptions.GameAlreadyStartedException;
import exceptions.GameNotStartedException;
import exceptions.StorageException;

import java.util.Arrays;

public class BotResponder {
    private final Game game;

    public BotResponder(Game game) {
        this.game = game;
    }

    public String respond(String username, String message) {
        String[] args = message.split(" ");
        String command = args[0].toLowerCase();

        try {
            if (command.equals("!start")) {
                return game.startGame(username);
            } else if (command.equals("!exit")) {
                return game.exitGame(username);
            } else if (command.equals("!move")) {
                if (args.length < 2) return "Usage: !move <location>";
                return game.movePlayer(username, String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
            } else if (command.equals("!locations")) {
                return "You can move to: " + String.join(", ", game.getAvailableLocations(username));
            } else if (command.equals("!pickup")) {
                return game.pickUpItem(username);
            } else if (command.equals("!use")) {
                if (args.length < 2) return "Usage: !use <item>";
                String itemName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                return game.processUseItem(username, itemName);
            } else if (command.equals("!inventory")) {
                return game.getPlayerInventory(username);
            } else if (command.equals("!help")) {
                return getHelpMessage();
            } else {
                return "Unknown command. Available commands: !start, !exit, !move, !locations, !pickup, !use, !inventory.";
            }
        } catch (GameAlreadyStartedException | GameNotStartedException | StorageException e) {
            return e.getMessage();
        }
    }

    private String getHelpMessage() {
        return String.join("\n",
                "!start - Start the game",
                "!locations - View available locations",
                "!move <location> - Move to a different location",
                "!pickup - Pick up an item from the current location",
                "!use <item> - Use an item in your inventory",
                "!inventory - View your inventory",
                "!exit - Exit the game"
        );
    }
}
