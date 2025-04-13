package edu.moravian;
import exceptions.GameAlreadyStartedException;
import exceptions.GameNotStartedException;
import exceptions.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private MemoryStorage storage;
    private Game game;
    private final String playerName = "player1";

    @BeforeEach
    void setUp() {
        storage = new MemoryStorage();
        game = new Game(storage);
    }

    @Test
    void testStartGameSuccessfully() throws StorageException {
        game.startGame(playerName);
        assertEquals("entrance hall", storage.getPlayerLocation(playerName));
        assertNotNull(storage.getLocation("entrance hall"));
        assertNotNull(storage.getLocation("dining room"));
    }

    @Test
    void testStartGameAlreadyStarted() throws StorageException {
        game.startGame(playerName);
        assertThrows(GameAlreadyStartedException.class, () -> game.startGame(playerName));
    }

    @Test
    void testExitGameNotStarted() {
        assertThrows(GameNotStartedException.class, () -> game.exitGame(playerName));
    }

    @Test
    void testMovePlayerToConnectedLocation() throws StorageException {
        game.startGame(playerName);
        String result = game.movePlayer(playerName, "dining room");
        assertEquals("dining room", storage.getPlayerLocation(playerName));
        assertTrue(result.contains("You moved to dining room."));
    }

    @Test
    void testMovePlayerToUnconnectedLocation() throws StorageException {
        game.startGame(playerName);
        String result = game.movePlayer(playerName, "study");
        assertEquals("You cannot move to study from entrance hall. The location is not connected.", result);
        assertEquals("entrance hall", storage.getPlayerLocation(playerName));
    }

    @Test
    void testMovePlayerWithMonsterWithoutWeapon() throws StorageException {
        game.startGame(playerName);
        game.movePlayer(playerName, "upstairs hall");
        String result = game.movePlayer(playerName, "bathroom");
        assertTrue(result.contains("A monster blocks your path!"));
        assertEquals("upstairs hall", storage.getPlayerLocation(playerName));
    }

    @Test
    void testMovePlayerWithMonsterWithWeapon() throws StorageException {
        game.startGame(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "kitchen");
        game.pickUpItem(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "entrance hall");
        game.movePlayer(playerName, "upstairs hall");
        String result = game.movePlayer(playerName, "bathroom");
        assertTrue(result.contains("You moved to bathroom."));
        assertEquals("bathroom", storage.getPlayerLocation(playerName));
    }

    @Test
    void testPickUpItem() throws StorageException {
        game.startGame(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "kitchen");
        String result = game.pickUpItem(playerName);
        assertEquals("You picked up the item.", result);
        assertTrue(storage.playerHasItem(playerName, "knife"));
    }

    @Test
    void testPickUpItemNoItem() throws StorageException {
        game.startGame(playerName);
        String result = game.pickUpItem(playerName);
        assertEquals("No items to pick up here.", result);
    }

    @Test
    public void testPickUpMultipleItems() throws StorageException {
        game.startGame(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "kitchen");
        game.pickUpItem(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "entrance hall");
        game.movePlayer(playerName, "upstairs hall");
        game.movePlayer(playerName, "bedroom");
        game.pickUpItem(playerName);
        String inventory = game.getPlayerInventory(playerName);
        assertTrue(inventory.contains("lantern"));
        assertTrue(inventory.contains("knife"));
    }

    @Test
    void testUseItemSuccessfully() throws StorageException {
        game.startGame(playerName);
        game.movePlayer(playerName, "library");
        game.movePlayer(playerName, "study");
        game.pickUpItem(playerName);
        game.movePlayer(playerName, "library");
        game.movePlayer(playerName, "entrance hall");
        String result = game.useItem(playerName, "circular stone 1");
        assertEquals("You placed the circular stone 1 in the exit door.", result);
    }

    @Test
    void testUseItemNotInInventory() throws StorageException {
        game.startGame(playerName);
        String result = game.useItem(playerName, "knife");
        assertTrue(result.contains("You can't use"), result);
    }

    @Test
    void testGetAvailableLocations() throws StorageException {
        game.startGame(playerName);
        Set<String> availableLocations = game.getAvailableLocations(playerName);
        assertEquals(Set.of("dining room", "library", "upstairs hall", "basement"), availableLocations);
    }

    @Test
    void testGetPlayerInventory() throws StorageException {
        game.startGame(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "kitchen");
        game.pickUpItem(playerName);
        String inventory = game.getPlayerInventory(playerName);
        assertTrue(inventory.contains("knife"));
    }

    @Test
    void testMoveToRoomWithMonster() throws StorageException {
        game.startGame(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "kitchen");
        game.pickUpItem(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "entrance hall");
        game.movePlayer(playerName, "upstairs hall");
        game.movePlayer(playerName, "bedroom");
        Set<String> availableLocations = game.getAvailableLocations(playerName);
        assertEquals(Set.of("upstairs hall"), availableLocations);
    }

    @Test
    void testItemAfterDefeatingMonster() throws StorageException {
        game.startGame(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "kitchen");
        game.pickUpItem(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "entrance hall");
        game.movePlayer(playerName, "upstairs hall");
        String result = game.movePlayer(playerName, "bedroom");
        assertTrue(result.contains("There is an item here: lantern."));
    }

    @Test
    public void testReEnteringMonsterRoom() throws StorageException {
        game.startGame(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "kitchen");
        game.pickUpItem(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "entrance hall");
        game.movePlayer(playerName, "upstairs hall");
        game.movePlayer(playerName, "bedroom");
        game.movePlayer(playerName, "upstairs hall");
        String result = game.movePlayer(playerName, "bedroom");
        assertTrue(result.contains("You moved to bedroom."));
        assertFalse(result.contains("A monster was here"));
    }

    @Test
    void testMoveToBasementWithoutLantern() throws StorageException {
        game.startGame(playerName);
        String result = game.movePlayer(playerName, "basement");
        assertEquals("It's too dark to enter the basement without a light source.", result);
    }

    @Test
    public void testPickUpLanternAndEnterBasement() throws StorageException {
        game.startGame(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "kitchen");
        game.pickUpItem(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "entrance hall");
        game.movePlayer(playerName, "upstairs hall");
        game.movePlayer(playerName, "bedroom");
        game.pickUpItem(playerName);
        game.movePlayer(playerName, "upstairs hall");
        game.movePlayer(playerName, "entrance hall");
        String result = game.movePlayer(playerName, "basement");
        assertTrue(result.contains("You moved to basement."));
    }

    @Test
    void testWinGame() throws StorageException {
        game.startGame(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "kitchen");
        game.pickUpItem(playerName);
        game.movePlayer(playerName, "dining room");
        game.movePlayer(playerName, "entrance hall");
        game.movePlayer(playerName, "library");
        game.movePlayer(playerName, "study");
        game.pickUpItem(playerName);
        game.movePlayer(playerName, "library");
        game.movePlayer(playerName, "entrance hall");
        game.movePlayer(playerName, "basement");
        game.pickUpItem(playerName);
        game.movePlayer(playerName, "entrance hall");
        game.useItem(playerName, "circular stone 1");
        String result = game.useItem(playerName, "circular stone 2");
        assertTrue(result.contains("escaped"));
    }

    @Test
    void testRemoveItemFromInventoryAfterUse() throws StorageException {
        game.startGame(playerName);
        game.movePlayer(playerName, "library");
        game.movePlayer(playerName, "study");
        game.pickUpItem(playerName);
        game.movePlayer(playerName, "library");
        game.movePlayer(playerName, "entrance hall");
        game.useItem(playerName, "circular stone 1");
        String result = game.getPlayerInventory(playerName);
        assertFalse(result.contains("circular stone 1"));
    }
}