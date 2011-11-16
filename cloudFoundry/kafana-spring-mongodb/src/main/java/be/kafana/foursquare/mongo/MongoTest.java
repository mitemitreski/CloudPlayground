package be.kafana.foursquare.mongo;

import java.net.UnknownHostException;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;


public class MongoTest {

  public static void main(String[] args) throws UnknownHostException, MongoException {

    Mongo m = new Mongo("localhost");

    for (String s : m.getDatabaseNames()) {
      System.out.println(s);
    }

    DB db = m.getDB("db");

    DBCollection col = db.createCollection("dado", null);

    BasicDBObject info = new BasicDBObject();

 
    
    info.put("x", 203);
    info.put("y", 102);


    col.insert(info);
    Set<String> colls = db.getCollectionNames();
    System.out.println("--------------------------");
    for (String s : colls) {
      System.out.println(s);
    }

  }
}
