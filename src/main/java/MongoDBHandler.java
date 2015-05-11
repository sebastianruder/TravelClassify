import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.*;

/**
 * Class to handle access to the travel database.
 */
public class MongoDBHandler {

    public static DB db;
    public static DBCollection postColl;
    public static DBCollection activityColl;

    /**
     * Initializes MongoDB connection to our database and sets the collections
     * that we'll use.
     * @throws UnknownHostException
     */
    public MongoDBHandler() throws UnknownHostException {
        ServerAddress address = new ServerAddress("ds037551.mongolab.com", 37551);
        MongoCredential credential = MongoCredential.createCredential("holiday-radar", "holiday-radar", new char[] {'h', 'o', 'l', 'i', 'd', 'a', 'y'});
        MongoClient mongoClient = new MongoClient(address, Arrays.asList(credential));
        this.db = mongoClient.getDB( "holiday-radar" );
        this.postColl = this.db.getCollection("social_data");
        this.activityColl = this.db.getCollection("activities");
    }

    /**
     * Retrieve a user's posts from the data base.
     * @param userId
     */
    public List<String> getPosts(String userId) {
        List<String> posts = new ArrayList<String>();

        BasicDBObject query = new BasicDBObject("user_id", userId);
        DBCursor cursor = postColl.find(query);
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

    /**
     * Save a user's activities to the database.
     * @param list a list of activities and their associated scores
     * @param userId the user id
     */
    public void saveUserActivities(List<Map.Entry<String, Double>> list, String userId) {
        BasicDBObject doc = new BasicDBObject("user_id", userId);
        for (Map.Entry<String, Double> entry : list) {
            doc.append(entry.getKey(), entry.getValue());
        }

        activityColl.insert(doc);
    }

    /**
     * Main method to establish a connection to the database and input two example items which
     * will then be retrieved.
     * @param args
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {

        MongoDBHandler dbHandler = new MongoDBHandler();
        BasicDBObject doc = new BasicDBObject("user_id", "123")
                .append("post", "I spend all day listening to music. The Beatles are awesome.");
        postColl.insert(doc);
        doc = new BasicDBObject("user_id", "123")
                .append("post", "My mum made me a sandwich. I'm going to track racing now.");
        postColl.insert(doc);

        dbHandler.getPosts("123");
    }

}

