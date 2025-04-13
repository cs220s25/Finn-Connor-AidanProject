package edu.moravian;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import exceptions.StorageException;

import java.util.Map;

public class RedisStorage implements GameStorage {
    private final Jedis jedis;

    public RedisStorage(String hostname, int port) throws StorageException {
        try {
            jedis = new Jedis(hostname, port);
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public void addLocation(String name, String description) throws StorageException {
        try {
            jedis.hset("location:" + name, "description", description);
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public void connectLocations(String from, String to) throws StorageException {
        try {
            jedis.sadd("location:" + from + ":connections", to);
            jedis.sadd("location:" + to + ":connections", from);
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public void setItem(String locationName, String item) throws StorageException {
        try {
            jedis.hset("location:" + locationName, "item", item);
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public void setMonster(String locationName, boolean hasMonster) throws StorageException {
        try {
            jedis.hset("location:" + locationName, "monster", String.valueOf(hasMonster));
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public void setLocationMonster(String locationName, boolean hasMonster) throws StorageException {
        try {
            jedis.hset("location:" + locationName, "monster", String.valueOf(hasMonster));
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public Location getLocation(String name) throws StorageException {
        try {
            Map<String, String> data = jedis.hgetAll("location:" + name);
            if (data.isEmpty()) {
                return null;
            }

            Location location = new Location(data.get("description"));

            location.setConnectedLocations(jedis.smembers("location:" + name + ":connections"));

            if (data.containsKey("item")) {
                location.setItem(data.get("item"));
            }

            if (data.containsKey("monster")) {
                location.setMonster(Boolean.parseBoolean(data.get("monster")));
            }

            return location;
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public void setPlayerLocation(String playerName, String locationName) throws StorageException {
        try {
            if (locationName == null || locationName.isEmpty()) {
                jedis.hdel("player:" + playerName, "location");
            } else {
                jedis.hset("player:" + playerName, "location", locationName);
            }
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public String getPlayerLocation(String playerName) throws StorageException {
        try {
            return jedis.hget("player:" + playerName, "location");
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public void addItemToPlayer(String playerName, String item) throws StorageException {
        try {
            jedis.sadd("player:" + playerName + ":inventory", item);
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public boolean playerHasItem(String playerName, String item) throws StorageException {
        try {
            return jedis.sismember("player:" + playerName + ":inventory", item);
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public String getPlayerItem(String playerName) throws StorageException {
        try {
            return jedis.smembers("player:" + playerName + ":inventory").toString();
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public void removePlayerItems(String playerName) throws StorageException {
        try {
            jedis.del("player:" + playerName + ":inventory");
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public void removeItemFromPlayer(String playerName, String item) throws StorageException {
        try {
            jedis.srem("player:" + playerName + ":inventory", item);
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public void markStoneUsed(String stone, String room) throws StorageException {
        try {
            if (room.equals("entrance hall")) {
                jedis.hset("room:" + room, stone, "true");
            }
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public boolean areAllStonesUsed(String room) throws StorageException {
        try {
            if (room.equals("entrance hall")) {
                String stone1Status = jedis.hget("room:" + room, "circular stone 1");
                String stone2Status = jedis.hget("room:" + room, "circular stone 2");
                return "true".equals(stone1Status) && "true".equals(stone2Status);
            }
            return false;
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }

    @Override
    public void close() throws StorageException {
        try {
            jedis.flushAll();
            jedis.close();
        } catch (JedisException e) {
            throw new StorageException("Failed to connect to Redis server");
        }
    }
}