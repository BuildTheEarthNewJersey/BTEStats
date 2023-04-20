package btestats.btestats.Database;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import btestats.btestats.Database.Parsers.PlayerData;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.HashMap;


public class Players implements Bufferable{

    private MongoCollection<Document> collection;

    private HashMap<String, PlayerData> playerBuffer;

    private final Object BUFFER_MUX = new Object();

    private HashMap<String, Integer> addBuffer;

    public Players(MongoDatabase database){
        this.collection= database.getCollection("players");
        playerBuffer = new HashMap<>();
        addBuffer = new HashMap<>();
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
        Bson query = Filters.eq("_id", uuid);
        Document document = this.collection.find(query).first();
        if (document == null) {
            addPlayerToDB(uuid);
            document = this.collection.find(query).first();
        }
        int blocksPlaced = (int) document.get("blocksPlaced");
        long lastLogin = (long) document.get("lastLogin");
        int loginStreak = (int) document.get("loginStreak");


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
        player.append("blocksPlaced", 0);
        player.append("lastLogin", timeStamp);
        player.append("loginStreak", 0);
        collection.insertOne(player);
    }


    public void updateBlocksPlaced(String uuid, int quantity){

        synchronized (BUFFER_MUX){
            this.addBuffer.put(uuid, quantity);
        }

//        PlayerData player = getPlayer(uuid);
//        int blocksPlaced = player.blocksPlaced+quantity;
//        collection.updateOne(Filters.eq("_id", uuid), Updates.set("blocksPlaced", blocksPlaced));
    }
    @Override
    public void flush() {
        synchronized (BUFFER_MUX){
            this.addBuffer.forEach((uuid, quantity) -> {
                PlayerData player = getPlayer(uuid);
                int blocksPlaced = player.blocksPlaced+quantity;
                collection.updateOne(Filters.eq("_id", uuid), Updates.set("blocksPlaced", blocksPlaced));
            });
        }
    }
}


