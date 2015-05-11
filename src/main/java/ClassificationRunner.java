import cc.mallet.classify.Classifier;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * Class to run classification on threads.
 *
 * Created by sebastian on 10/05/15.
 */
public class ClassificationRunner implements Runnable {
    private Thread t;
    private String threadName; // userid
    private MongoDBHandler handler;
    private DocumentClassifier docClassifier;
    private Classifier classifier;

    public ClassificationRunner(String name){
        threadName = name;
        System.out.println("Creating " +  threadName );
    }

    public void run() {
        runClassification();
    }

    public List<Map.Entry<String, Double>> runClassification() {
        System.out.println("Running " +  threadName );
        try {
            handler = new MongoDBHandler();
        }
        catch (UnknownHostException ex) {
            System.out.println("Unknown host.");
        }
        docClassifier = new DocumentClassifier();
        String classifierFile = "activities.classifier";
        try {
            classifier = docClassifier.loadClassifier(new File(classifierFile));
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Classifier couldn't be loaded.");
        }
        catch (IOException ex) {
            System.out.println("Classifier file not found.");
        }
        System.out.println("Classifier loaded.");
        List<Map.Entry<String, Double>> list = null;
        try {
            list = classifyPosts(threadName);
        }
        catch (IOException ex) {
            System.out.println("File not found");
        }

        for (Map.Entry<String, Double> entry : list) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        return list;
    }

    /**
     * Method to classify a user's posts given her id.
     * @param userId the user id
     * @return
     * @throws IOException
     */
    private List<Map.Entry<String, Double>> classifyPosts(String userId) throws IOException {
        List<String> posts = handler.getPosts(userId);
        String tempFile = String.format("%s.temp", userId);
        PrintWriter writer = new PrintWriter(tempFile, "UTF-8");
        for (String post : posts) {
            writer.printf("0\t\t%s", post);
        }

        writer.close();

        List<Map.Entry<String, Double>> list = docClassifier.printLabelings(classifier, new File(tempFile));
        File file = new File(tempFile);
        if (file.delete()) {
            System.out.println("File deleted successfully.");
        }
        else {
            System.out.println("File couldn't be deleted.");
        }

        handler.saveUserActivities(list, userId);
        return list;
    }

    /**
     * Starts a thread.
     */
    public void start ()
    {
        if (t == null)
        {
            t = new Thread (this, threadName);
            t.start ();
        }
    }

}
