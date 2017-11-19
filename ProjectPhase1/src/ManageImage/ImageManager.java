package ManageImage;

import GUI.Main;
import GUI.PicGrid;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;

/**
 * Provides entry-point for the data a user can access. Data includes all image files in a
 * directory.
 */
public class ImageManager implements Serializable {

    /**
     * The list of all imageFiles accessible by a user
     */
    private static ArrayList<ImageFile> imageFiles = new ArrayList<>();

    /**
     * Loading data from serialized file out.ser
     * <p>
     * <p>cite:
     *Adapted from: http://www.avajava.com/tutorials/lessons/how-do-i-write-an-object-to-a-file-and-read-it-back.html Date: Nov 9, 2017
     */
    @SuppressWarnings("unchecked")
    public static void load() {
        try {

            FileInputStream inputStream = new FileInputStream("out.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            imageFiles = (ArrayList<ManageImage.ImageFile>) objectInputStream.readObject();
            Set<ImageFile> removeDuplicates = new HashSet<>();

            // remove duplicate Images from ser file
            removeDuplicates.addAll(imageFiles);
            imageFiles.clear();
            imageFiles.addAll(removeDuplicates);
            objectInputStream.close();
            imageFiles.removeIf(imageFile -> !imageFile.getFile().exists());

        } catch (IOException e) {
            Main.logger.log(Level.SEVERE, "ImageManager serialized file incorrectly read", e);
        } catch (ClassNotFoundException e) {
            Main.logger.log(Level.SEVERE, "ImageManager class path not realized", e);
        }
    }

    /**
     * Writing data onto the serialized file out.ser.
     * <p>
     * <p>// cite:
     * Adapted from: http://www.avajava.com/tutorials/lessons/how-do-i-write-an-object-to-a-file-and-read-it-back.html Date: Nov 9, 2017
     */
    public static void save() {
        try {

            FileOutputStream fileOutputStream = new FileOutputStream("out.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(imageFiles);
            objectOutputStream.close();

            imageFiles.clear();

        } catch (FileNotFoundException e) {
            Main.logger.log(Level.SEVERE, "ImageManager serialized file not found for writing", e);
        } catch (IOException e) {
            Main.logger.log(Level.SEVERE, "ImageManager serialized file incorrectly read", e);
        }
    }

    /**
     * Get images stored in directory
     * <p>
     * <p>Assumes stored images and directory all use absolute paths
     *
     * @param directory the directory that contains returned images
     * @return the images stored in directory
     */
    public static ArrayList<ImageFile> getImageFilesByDirectory(File directory) {

        // cite:
        // https://stackoverflow.com/questions/4746671/how-to-check-if-a-given-path-is-possible-child-of-another-path
        ArrayList<ImageFile> filteredImages = new ArrayList<>();
        for (ImageFile imageFile : imageFiles) {
            File imageDirectory = imageFile.getDirectory();
            Path child = Paths.get(imageDirectory.toString()).toAbsolutePath();
            Path parent = Paths.get(directory.toString()).toAbsolutePath();
            if (child.startsWith(parent)) {
                filteredImages.add(imageFile);
            }
        }
        return filteredImages;
    }

    /**
     * Add a new image
     *
     * @param imageInsert the image which will be added
     */
    private static void addImage(ImageFile imageInsert) {

        boolean match = false;
        for (ImageFile imageFile : imageFiles) {
            if (imageFile.equals(imageInsert)) {
                match = true;
                break;
            }
        }
        if (!match) {
            ImageManager.imageFiles.add(imageInsert);
            for (String tag : imageInsert.getTags())
                if (!TagManager.getTags().contains(tag)) {
                    TagManager.getTags().add(tag);
                }
        }
    }

    /**
     * Return an array of ManageImage.ImageFile files in a directory
     *
     * @param directory the path of the folder to search in.
     * @return File[] array of all imageFiles in a directory.
     */
    private static ArrayList<File> findImages(String directory) {

        File folder = new File(directory);
        File[] collect;

        // return a list of all image files.
        collect =
                folder.listFiles(
                        (dir, name) -> (name.endsWith(".png")
                                | name.endsWith(".jpg")
                                | name.endsWith(".jpeg")
                                | // Considering file of the appropriate type
                                name.endsWith(".tiff")
                                | name.endsWith(".ppm")
                                | name.endsWith(".jfif")));

        if (collect == null) { // no files of the right format were found
            return new ArrayList<>();
        }

        return new ArrayList<>(Arrays.asList(collect));
    }

    /**
     * Get the names of all sub-directories in a directory.
     *
     * @param directory the path of the folder to search in.
     * @return gatherSubDirectories Array list of names of sub-directories.
     */
    private static ArrayList<String> checkForSubDirectory(String directory) {

        File folder = new File(directory);
        ArrayList<String> gatherSubDirectories = new ArrayList<>();

        File[] allFiles = folder.listFiles();

        if (allFiles == null) {
            return gatherSubDirectories;
        } else {
            for (File f : allFiles) {
                if (f.isDirectory()) {
                    gatherSubDirectories.add(f.getName());
                }
            }
        }

        return gatherSubDirectories;
    }

    /**
     * Check for sub-directories in directory, while adding imageFiles.
     *
     * @param directory the path of the folder to search in.
     * @return ArrayList<File> arrayList of all imageFiles(including those found in sub-directories).
     */
    private static ArrayList<File> checkSubDirectories(String directory) {

        // if no sub-directory, return findImages
        if (checkForSubDirectory(directory).isEmpty()) {
            return findImages(directory);
        }

        // else return findImages + findImages(NEW_PATH)
        else {
            ArrayList<File> allImages = new ArrayList<>();

            // loop through the names-list generated
            for (String dirName : checkForSubDirectory(directory)) {

                // keep recurring through names list - and adding to the temp array
                allImages.addAll(checkSubDirectories(directory + "/" + dirName));
            }

            ArrayList<File> gather = findImages(directory);
            gather.addAll(allImages);
            return gather;
        }
    }

    /**
     * Convert an array of File objects into an array of ManageImage.ImageFile objects.
     *
     * @param possibleImages List of imageFiles in a directory that will be converted in to
     *                       ManageImage.ImageFile objects.
     */
    private static void convertToImageObjects(ArrayList<File> possibleImages) {

        // ArrayList<ManageImage.ImageFile> imageFiles = new ArrayList<>();
        for (File f : possibleImages) {
            try {
                addImage(new ImageFile(f));

            } catch (IOException e) {
                Main.logger.log(Level.SEVERE, "ImageManager serialized file incorrectly read", e);
            }
        }
    }

    /**
     * Add all imageFiles from a specified directory into the list of imageFiles that a user has seen.
     *
     * @param directory The path of the folder the imageFiles are to be found
     */
    public static void createImagesFromDirectory(String directory) {

        // get the relevant File objects from directory & sub-directory
        ArrayList<File> files = checkSubDirectories(directory);

        // convert the files into an array-List of ManageImage.ImageFile objects.
        convertToImageObjects(files);
    }

    /**
     * Delete a tag from TagManager.tags and all ImageFiles containing that tag.
     *
     * @param tag The tag to delete from TagManager.tags and ImageFiles all containing that tag.
     */
    public static void deleteGlobalTag(String tag) {

        ArrayList<ImageFile> list = PicGrid.getDisplayedFiles();
        for (ImageFile file : list) {
            file.removeTag(tag);
        }
        if (TagManager.getTags().contains(tag))
            TagManager.getTags().remove(tag);
    }

    /**
     * Add a tag from TagManager.tags and all ImageFiles containing that tag.
     *
     * @param tag The tag to add to TagManager.tags and ImageFiles
     */
    public static void addGlobalTag(String tag) {
        ArrayList<ImageFile> list = PicGrid.getDisplayedFiles();

        for (ImageFile file : list) file.addTag(tag);
    }


}
