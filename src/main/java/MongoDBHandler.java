import com.mongodb.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MongoDBHandler {

    public static DB db;
    public static DBCollection coll;

    public MongoDBHandler() throws UnknownHostException {
        // To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
        // if it's a member of a replica set:
        ServerAddress address = new ServerAddress("ds037551.mongolab.com", 37551);
        MongoCredential credential = MongoCredential.createCredential("holiday-radar", "holiday-radar", new char[] {'h', 'o', 'l', 'i', 'd', 'a', 'y'});
        MongoClient mongoClient = new MongoClient(address, Arrays.asList(credential));
        this.db = mongoClient.getDB( "holiday-radar" );
        this.coll = this.db.getCollection("social_data");
    }

    /**
     * Retrieve a user's posts from the data base.
     * @param userId
     */
    public List<String> getPosts(String userId) {
        List<String> posts = new ArrayList<String>();

        BasicDBObject query = new BasicDBObject("user_id", userId);
        DBCursor cursor = coll.find(query);
        try {
            while (cursor.hasNext()) {
                DBObject next = cursor.next();
                String post = next.get("post").toString();
                posts.add(post);
                System.out.println(post);
            }
        } finally {
            cursor.close();
        }

        return posts;
    }

    public static void main(String[] args) throws UnknownHostException {

        MongoDBHandler dbHandler = new MongoDBHandler();
        /*BasicDBObject doc = new BasicDBObject("user_id", "123")
                .append("post", "I like swimming and going shopping, especially for handbags.");
        coll.insert(doc);
        doc = new BasicDBObject("user_id", "123")
                .append("post", "I go mountain climbing a lot.");
        coll.insert(doc);
*/
        dbHandler.getPosts("123");
    }

}

