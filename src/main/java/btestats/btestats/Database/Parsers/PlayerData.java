package btestats.btestats.Database.Parsers;


import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class PlayerData {

    private String uuid;
    public int blocksPlaced;
    public long lastLogin;
    public int loginStreak;

    public PlayerData(String uuid, int blocksPlaced, long lastLogin, int loginStreak){
        if(!(uuid.equals(UUID.fromString(uuid).toString()))){
            throw new IllegalArgumentException("Invalid UUID");
        }
        Instant timestamp = Instant.now();
        this.uuid = uuid; //TODO: change this accordingly
        this.blocksPlaced = blocksPlaced;
        this.lastLogin = lastLogin;
        this.loginStreak = loginStreak;

    }

}