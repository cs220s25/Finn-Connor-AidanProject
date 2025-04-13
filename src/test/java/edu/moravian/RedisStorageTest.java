package edu.moravian;
import com.github.fppt.jedismock.RedisServer;
import exceptions.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RedisStorageTest {
    private RedisStorage redisStorage;

    @BeforeEach
    public void setUp() throws IOException, StorageException {
        RedisServer server = RedisServer.newRedisServer();
        server.start();
        redisStorage = new RedisStorage(server.getHost(), server.getBindPort());
    }

    @Test
    public void testAddLocation() throws Exception {
        redisStorage.addLocation("forest", "A dense forest");
        Location location = redisStorage.getLocation("forest");
        assertNotNull(location);
        assertEquals("A dense forest", location.getDescription());
    }

    @Test
    public void testConnectLocations() throws Exception {
        redisStorage.addLocation("forest", "A dense forest");
        redisStorage.addLocation("cave", "A dark cave");
        redisStorage.connectLocations("forest", "cave");

        Location forest = redisStorage.getLocation("forest");
        assertTrue(forest.getConnectedLocations().contains("cave"));

        Location cave = redisStorage.getLocation("cave");
        assertTrue(cave.getConnectedLocations().contains("forest"));
    }

    @Test
    public void testSetAndGetItemInLocation() throws Exception {
        redisStorage.addLocation("cabin", "A cozy cabin");
        redisStorage.setItem("cabin", "key");

        Location cabin = redisStorage.getLocation("cabin");
        assertEquals("key", cabin.getItem());
    }

    @Test
    public void testSetAndCheckMonsterInLocation() throws Exception {
        redisStorage.addLocation("tower", "A tall tower");
        redisStorage.setMonster("tower", true);

        Location tower = redisStorage.getLocation("tower");
        assertTrue(tower.hasMonster());
    }

    @Test
    public void testSetAndGetPlayerLocation() throws Exception {
        redisStorage.setPlayerLocation("player1", "forest");
        assertEquals("forest", redisStorage.getPlayerLocation("player1"));
    }

    @Test
    public void testAddItemToPlayerInventory() throws Exception {
        redisStorage.addItemToPlayer("player1", "sword");
        assertTrue(redisStorage.playerHasItem("player1", "sword"));
    }

    @Test
    public void testRemovePlayerItems() throws Exception {
        redisStorage.addItemToPlayer("player1", "shield");
        redisStorage.removePlayerItems("player1");
        assertFalse(redisStorage.playerHasItem("player1", "shield"));
    }

    @Test
    public void testGetNonexistentLocationReturnsNull() throws Exception {
        assertNull(redisStorage.getLocation("nonexistent"));
    }

    @Test
    public void testRemoveItemFromPlayer() throws Exception {
        redisStorage.addItemToPlayer("player1", "knife");
        assertTrue(redisStorage.playerHasItem("player1", "knife"));
        redisStorage.removeItemFromPlayer("player1", "knife");
        assertFalse(redisStorage.playerHasItem("player1", "knife"));
    }

    @Test
    public void testRemoveItemFromPlayerWhenItemDoesNotExist() throws Exception {
        assertFalse(redisStorage.playerHasItem("player1", "knife"));
        redisStorage.removeItemFromPlayer("player1", "knife");
        assertFalse(redisStorage.playerHasItem("player1", "knife"));
    }

    @Test
    public void testAllStonesUsed() throws Exception {
        redisStorage.addLocation("entrance hall", "An entrance hall");
        redisStorage.markStoneUsed("circular stone 1", "entrance hall");
        redisStorage.markStoneUsed("circular stone 2", "entrance hall");
        assertTrue(redisStorage.areAllStonesUsed("entrance hall"));
    }

    @Test
    public void testMarkStoneUsedMultipleTimes() throws Exception {
        redisStorage.addLocation("entrance hall", "An entrance hall");
        redisStorage.markStoneUsed("circular stone 1", "entrance hall");
        redisStorage.markStoneUsed("circular stone 1", "entrance hall");
        assertFalse(redisStorage.areAllStonesUsed("entrance hall"));
    }

    @Test
    public void testClose() {
        assertDoesNotThrow(() -> redisStorage.close());
    }
}