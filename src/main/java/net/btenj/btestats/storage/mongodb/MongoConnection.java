package net.btenj.btestats.storage.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
  public static final String MONGO_ID_STRING = "_id";
  private final String mongoUri;

  public MongoConnection(String mongoUri) {
    this.mongoUri = mongoUri;
  }

  public MongoDatabase getConnection() {
    MongoClient client = MongoClients.create(mongoUri);
    return client.getDatabase("BTEstats");
  }
}
