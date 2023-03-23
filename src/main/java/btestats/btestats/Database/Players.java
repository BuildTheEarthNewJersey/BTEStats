package btestats.btestats.Database;


import com.google.common.collect.Iterators;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import btestats.btestats.Database.Parsers.PlayerData;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;
import java.util.Iterator;


public class Players {

    private MongoCollection<Document> collection;
    public Players(MongoDatabase database){
        this.collection= database.getCollection("players");
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
//        BasicDBObject whereQuery = new BasicDBObject();
//        whereQuery.put("_id", uuid);
//        Iterator<Document> it = this.collection.find(whereQuery).iterator();
//        int size = Iterators.size(it);
//        if (size == 0){
//            throw new IllegalArgumentException("No player found");
//        }
//        else if (size > 1){
//            throw new IllegalArgumentException("Duplicate player found");
//        }
//      Document result = it.next();

        Bson query = Filters.eq("_id", uuid);
        Document document = this.collection.find(query).first();
        if (document == null) {
            createPlayer(uuid);
            document = this.collection.find(query).first();
        }
        int blocksPlaced = (int) document.get("blocksPlaced");
        long lastLogin = (long) document.get("lastLogin");
        int loginStreak = (int) document.get("loginStreak");


        return new PlayerData(uuid, blocksPlaced, lastLogin, loginStreak);


    }
    public void updateBlocksPlaced(String uuid, int quantity){
        PlayerData player = getPlayer(uuid);
        int blocksPlaced = player.blocksPlaced+quantity;
        collection.updateOne(Filters.eq("_id", uuid), Updates.set("blocksPlaced", blocksPlaced));
    }

    public void updateLoginStreak(String uuid) {
        PlayerData player = getPlayer(uuid);
        int loginStreak = player.loginStreak + 1;
        collection.updateOne(Filters.eq("_id", uuid), Updates.set("loginStreak", loginStreak));
    }

    public void createPlayer(String uuid){
        Document player = new Document();
        long timeStamp = System.currentTimeMillis();
        player.append("_id", uuid);
        player.append("blocksPlaced", 0);
        player.append("lastLogin", timeStamp);
        player.append("loginStreak", 0);
        collection.insertOne(player);
    }


}


