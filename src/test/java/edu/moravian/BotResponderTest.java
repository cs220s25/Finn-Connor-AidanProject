package edu.moravian;
import exceptions.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BotResponderTest {

    private BotResponder botResponder;
    private Game game;

    @BeforeEach
    public void setUp() {
        MemoryStorage storage = new MemoryStorage();
        game = new Game(storage);
        botResponder = new BotResponder(game);
    }

    @Test
    public void testStartGame() {
        String response = botResponder.respond("player1", "!start");
        assertEquals("Game started. You awaken in a grand entrance hall of a creepy mansion. A big exit door stands here with two circular slots carved into it.", response);
    }

    @Test
    public void testStartGameWhenAlreadyStarted() {
        botResponder.respond("player1", "!start");
        String response = botResponder.respond("player1", "!start");
        assertEquals("Game already in progress", response);
    }

    @Test
    public void testExitGame() {
        botResponder.respond("player1", "!start");
        String response = botResponder.respond("player1", "!exit");
        assertEquals("Game exited.", response);
    }

    @Test
    public void testExitGameWhenNotStarted() {
        String response = botResponder.respond("player1", "!exit");
        assertEquals("Game not started", response);
    }

    @Test
    public void testMovePlayerWithInvalidLocation() {
        botResponder.respond("player1", "!start");
        String response = botResponder.respond("player1", "!move kitchen");
        assertEquals("You cannot move to kitchen from entrance hall. The location is not connected.", response);
    }

    @Test
    public void testMovePlayerWithValidLocation() {
        botResponder.respond("player1", "!start");
        String response = botResponder.respond("player1", "!move dining room");
        assertTrue(response.contains("You moved to dining room."));
    }

    @Test
    public void testPickUpItem() throws StorageException {
        botResponder.respond("player1", "!start");
        game.movePlayer("player1", "dining room");
        game.movePlayer("player1", "kitchen");
        String response = botResponder.respond("player1", "!pickup");
        assertEquals("You picked up the item.", response);
    }

    @Test
    public void testPickUpItemWhenNoItem() throws StorageException {
        botResponder.respond("player1", "!start");
        game.movePlayer("player1", "entrance hall");
        String response = botResponder.respond("player1", "!pickup");
        assertEquals("No items to pick up here.", response);
    }

    @Test
    public void testUseItemWhenNotInInventory() {
        botResponder.respond("player1", "!start");
        String response = botResponder.respond("player1", "!use knife");
        assertTrue(response.contains("You can't use"), response);
    }

    @Test
    public void testGetInventory() throws StorageException {
        botResponder.respond("player1", "!start");
        game.movePlayer("player1", "dining room");
        game.movePlayer("player1", "kitchen");
        botResponder.respond("player1", "!pickup");
        String response = botResponder.respond("player1", "!inventory");
        assertTrue(response.contains("knife"));
    }

    @Test
    public void testUnknownCommand() {
        String response = botResponder.respond("player1", "!unknownCommand");
        assertEquals("Unknown command. Available commands: !start, !exit, !move, !locations, !pickup, !use, !inventory.", response);
    }

    @Test
    public void testLocations() {
        botResponder.respond("player1", "!start");
        String response = botResponder.respond("player1", "!locations");
        assertTrue(response.contains("You can move to: basement, dining room, library, upstairs hall"));
    }
}