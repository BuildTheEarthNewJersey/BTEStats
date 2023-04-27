package net.btenj.btestats.storage;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.btenj.btestats.storage.parsers.PlayerData;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.concurrent.ConcurrentHashMap;


public class Players implements Bufferable{

    private MongoCollection<Document> collection;

    private ConcurrentHashMap<String, PlayerData> playerBuffer;
    private static final String BLOCKS_PLACED_KEY = "blocksPlaced";
    private static final String LAST_LOGIN_KEY = "lastLogin";
    private static final String LOGIN_STREAK_KEY = "loginStreak";

    private ConcurrentHashMap<String, Integer> addBuffer;

    public Players(MongoDatabase database){
        this.collection= database.getCollection("players");
        playerBuffer = new ConcurrentHashMap<>();
        addBuffer = new ConcurrentHashMap<>();
    }




    /*TODO Implement:
    1. Add block place, remove block place function (increments db object and adds 1)
    1.5 adding blocksPlaced and removed blocksPlaced will first go through a queue, and after ever minute (or the queue gets full), the DB insert will then happen. This is to avoid overloading the DB. Also, add shutdown event to flush queue into DB.
    2. db collection name is "Players", and an object will look like:
    {
        uuid: string,
        blocksPlaced: int,
        lastLogin: Date,
        loginStreak: int,
        hoursPlayed: int
    }
    Note: EVERY DB CALL MUST FIRST GO THROUGH VALIDATION
     */
    private PlayerData getPlayer(String uuid){
        if (playerBuffer.containsKey(uuid)){
            return playerBuffer.get(uuid);
        }
        else{
            addPlayerToBuffer(uuid);
        }

        Bson query = Filters.eq("_id", uuid);
        Document document = this.collection.find(query).first();
        if (document == null) {
            addPlayerToDB(uuid);
            document = this.collection.find(query).first();
        }
        int blocksPlaced = (int) document.get(BLOCKS_PLACED_KEY);
        long lastLogin = (long) document.get(LAST_LOGIN_KEY);
        int loginStreak = (int) document.get(LOGIN_STREAK_KEY);


        return new PlayerData(uuid, blocksPlaced, lastLogin, loginStreak);
    }

    public PlayerData createPlayer(String uuid){
        return new PlayerData(uuid, 1, System.currentTimeMillis(), 0);
    }
    public void addPlayerToBuffer(String uuid){
        playerBuffer.put(uuid, createPlayer(uuid));
    }
    public void addPlayerToDB(String uuid){
        Document player = new Document();
        long timeStamp = System.currentTimeMillis();
        player.append("_id", uuid);
        player.append(BLOCKS_PLACED_KEY, 0);
        player.append(LAST_LOGIN_KEY, timeStamp);
        player.append(LOGIN_STREAK_KEY, 0);
        collection.insertOne(player);
    }

    public void updateBlocksPlaced(String uuid, int quantity){
        if(!this.addBuffer.containsKey((uuid))){
            this.addBuffer.put(uuid, quantity);
        }
        else{
            this.addBuffer.put(uuid, this.addBuffer.get(uuid)+quantity);
        }
    }
    @Override
    public void flush() {
        this.addBuffer.forEach((uuid, quantity) -> {
            PlayerData player = getPlayer(uuid);
            collection.updateOne(Filters.eq("_id", uuid), Updates.set(BLOCKS_PLACED_KEY, player.blocksPlaced + quantity));
        });

        this.addBuffer = new ConcurrentHashMap<>();
        this.playerBuffer = new ConcurrentHashMap<>();
    }
}


