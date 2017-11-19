package ManageImage;

import GUI.Main;

import java.io.*;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 * ManageImage.TagManager represents a pool of unique tags
 */
public class TagManager implements Serializable {

    /**
     * the tags that a ManageImage.TagManager stores
     */
    private static LinkedList<String> tags = new LinkedList<>();

    /**
     * Reading data of all tags created by the user
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

    // cite: http://www.avajava.com/tutorials/lessons/how-do-i-write-an-object-to-a-file-and-read-it-back.html

    /**
     * Writing data of all tags created by the user in a session.
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
