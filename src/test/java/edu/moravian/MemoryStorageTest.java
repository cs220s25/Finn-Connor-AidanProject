package edu.moravian;
import exceptions.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryStorageTest {

    private MemoryStorage memoryStorage;

    @BeforeEach
    public void setUp() {
        memoryStorage = new MemoryStorage();
    }

    @Test
    public void testAddLocation() {
        memoryStorage.addLocation("Town", "A peaceful town.");
        Location location = memoryStorage.getLocation("Town");

        assertNotNull(location);
        assertEquals("A peaceful town.", location.getDescription());
    }

    @Test
    public void testConnectLocations() {
        memoryStorage.addLocation("Town", "A peaceful town.");
        memoryStorage.addLocation("Forest", "A dense forest.");

        memoryStorage.connectLocations("Town", "Forest");

        Location town = memoryStorage.getLocation("Town");
        Location forest = memoryStorage.getLocation("Forest");

        assertTrue(town.getConnectedLocations().contains("Forest"));
        assertTrue(forest.getConnectedLocations().contains("Town"));
    }

    @Test
    public void testSetItem() {
        memoryStorage.addLocation("Town", "A peaceful town.");
        memoryStorage.setItem("Town", "Sword");

        Location location = memoryStorage.getLocation("Town");
        assertEquals("Sword", location.getItem());
    }

    @Test
    public void testSetMonster() {
        memoryStorage.addLocation("Forest", "A dense forest.");
        memoryStorage.setMonster("Forest", true);

        Location location = memoryStorage.getLocation("Forest");
        assertTrue(location.hasMonster());
    }

    @Test
    public void testSetLocationMonsterWithException() {
        assertThrows(StorageException.class, () -> memoryStorage.setLocationMonster("NonexistentLocation", true));
    }

    @Test
    public void testSetPlayerLocation() {
        memoryStorage.addLocation("Town", "A peaceful town.");
        memoryStorage.setPlayerLocation("Player1", "Town");

        String playerLocation = memoryStorage.getPlayerLocation("Player1");
        assertEquals("Town", playerLocation);
    }

    @Test
    public void testAddItemToPlayer() {
        memoryStorage.addItemToPlayer("Player1", "Sword");

        String playerItem = memoryStorage.getPlayerItem("Player1");
        assertEquals("Sword", playerItem);
    }

    @Test
    public void testPlayerHasItem() {
        memoryStorage.addItemToPlayer("Player1", "Sword");

        assertTrue(memoryStorage.playerHasItem("Player1", "Sword"));
        assertFalse(memoryStorage.playerHasItem("Player1", "Shield"));
    }

    @Test
    public void testRemovePlayerItems() {
        memoryStorage.addItemToPlayer("Player1", "Sword");
        memoryStorage.removePlayerItems("Player1");

        String playerItem = memoryStorage.getPlayerItem("Player1");
        assertNull(playerItem);
    }

    @Test
    public void testMarkStoneUsed() {
        memoryStorage.markStoneUsed("circular stone 1", "entrance hall");
        memoryStorage.markStoneUsed("circular stone 2", "entrance hall");
        assertTrue(memoryStorage.areAllStonesUsed("entrance hall"));
        memoryStorage.markStoneUsed("circular stone 1", "nonexistent room");
        assertFalse(memoryStorage.areAllStonesUsed("nonexistent room"));
    }

    @Test
    public void testAreAllStonesUsed() {
        memoryStorage.markStoneUsed("circular stone 1", "entrance hall");
        assertFalse(memoryStorage.areAllStonesUsed("entrance hall"));
        memoryStorage.markStoneUsed("circular stone 2", "entrance hall");
        assertTrue(memoryStorage.areAllStonesUsed("entrance hall"));
    }

    @Test
    public void testRemoveItemFromPlayer() {
        memoryStorage.addItemToPlayer("Player1", "knife");
        memoryStorage.removeItemFromPlayer("Player1", "knife");
        assertFalse(memoryStorage.playerHasItem("Player1", "knife"));
    }
}