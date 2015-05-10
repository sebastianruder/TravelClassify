/**
 * Created by sebastian on 09/05/15.
 */

import java.io.*;
import java.util.*;

import cc.mallet.classify.*;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.types.*;

public class DocumentClassifier
{
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        String classifierFile = "/home/sebastian/git/Mallet/activities.classifier";
        String exampleFile = "/home/sebastian/git/Mallet/prog_example.txt";

        DocumentClassifier dc = new DocumentClassifier();
        Classifier classifier = dc.loadClassifier(new File(classifierFile));
        dc.printLabelings(classifier, new File(exampleFile));

    }

    public Classifier trainClassifier(InstanceList trainingInstances) {

        // Here we use a maximum entropy (ie polytomous logistic regression)
        //  classifier. Mallet includes a wide variety of classification
        //  algorithms, see the JavaDoc API for details.

        ClassifierTrainer trainer = new MaxEntTrainer();
        return trainer.train(trainingInstances);
    }

    public Classifier loadClassifier(File serializedFile)
            throws FileNotFoundException, IOException, ClassNotFoundException {

        // The standard way to save classifiers and Mallet data
        //  for repeated use is through Java serialization.
        // Here we load a serialized classifier from a file.

        Classifier classifier;

        ObjectInputStream ois =
                new ObjectInputStream (new FileInputStream (serializedFile));
        classifier = (Classifier) ois.readObject();
        ois.close();

        return classifier;
    }

    public void saveClassifier(Classifier classifier, File serializedFile)
            throws IOException {

        // The standard method for saving classifiers in
        //  Mallet is through Java serialization. Here we
        //  write the classifier object to the specified file.

        ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream (serializedFile));
        oos.writeObject (classifier);
        oos.close();
    }

    public List<Map.Entry<String, Double>> printLabelings(Classifier classifier, File file) throws IOException {

        // Create a new iterator that will read raw instance data from
        //  the lines of a file.
        // Lines should be formatted as:
        //
        //   [name] [label] [data ... ]
        //
        //  in this case, "label" is ignored.

        CsvIterator reader =
                new CsvIterator(new FileReader(file),
                        "(\\w+)\\s+(\\w+)\\s+(.*)",
                        3, 2, 1);  // (data, label, name) field indices

        // Create an iterator that will pass each instance through
        //  the same pipe that was used to create the training data
        //  for the classifier.
        Iterator instances =
                classifier.getInstancePipe().newIteratorFrom(reader);

        // Classifier.classify() returns a Classification object
        //  that includes the instance, the classifier, and the
        //  classification results (the labeling). Here we only
        //  care about the Labeling.

        Map<String, Double> scoreMap = new HashMap<String, Double>();
        while (instances.hasNext()) {
            Labeling labeling = classifier.classify(instances.next()).getLabeling();

            // print the labels with their weights in descending order (ie best first)

            for (int rank = 0; rank < labeling.numLocations(); rank++){
                String label = labeling.getLabelAtRank(rank).toString();
                double score = labeling.getValueAtRank(rank);
                if (scoreMap.containsKey(label)) {
                    scoreMap.put(label, scoreMap.get(label) + score);
                }
                else {
                    scoreMap.put(label, score);
                }

                System.out.print(label + ":" + score + " ");
            }

            System.out.println();
        }

        List<Map.Entry<String, Double>> list = sortByValue(scoreMap);
        for (Map.Entry<String, Double> entry : list) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        return list.subList(0, 5);

    }

    public static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return - (o1.getValue()).compareTo( o2.getValue() );
            }
        } );

        return list;
    }

}