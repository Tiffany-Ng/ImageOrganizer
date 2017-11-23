package ManageImage;

import guiView.Main;

import java.io.*;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 * TagManager represents a pool of unique tags
 * <p>TagManager's tags can be taken saved to or loaded from tags.ser</p>
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class TagManager implements Serializable {

    /**
     * the tags that a ManageImage.TagManager stores
     */
    private static LinkedList<String> tags = new LinkedList<>();

    /**
     * Loads tags from tags.ser
     * <p>
     * Adapted: http://www.avajava.com/tutorials/lessons/how-do-i-write-an-object-to-a-file-and-read-it-back.html
     * Date: Nov 9, 2017
     * </p>
     */
    @SuppressWarnings("unchecked")
    public static void load() {
        try {

            FileInputStream inputStream = new FileInputStream("tags.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            tags = (java.util.LinkedList<String>) objectInputStream.readObject();
            objectInputStream.close();

        } catch (FileNotFoundException e) {
            Main.logger.log(Level.SEVERE, "Serializable file path not found", e);
        } catch (IOException e) {
            Main.logger.log(Level.SEVERE, "Improper file reading", e);
        } catch (ClassNotFoundException e) {
            Main.logger.log(Level.SEVERE, "Improper class path", e);
        }
    }

    /**
     * Saves tags to tags.ser
     * <p>
     * Adapted from: http://www.avajava.com/tutorials/lessons/how-do-i-write-an-object-to-a-file-and-read-it-back.html
     * Date: Nov 9, 2017
     * </p>
     */
    public static void save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("tags.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(tags);
            objectOutputStream.close();

            tags.clear();
        } catch (FileNotFoundException e) {
            System.out.println("No file");

        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    /**
     * Returns the global tags
     *
     * @return LinkedList<String> the global tags
     */
    public static LinkedList<String> getTags() {
        return tags;
    }

    /**
     * Adds tag to tagManager if tag is not yet added
     *
     * @param tag the tag to be added to ManageImage.TagManager
     */
    static void add(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }
}
