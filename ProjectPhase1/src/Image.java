import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Image implements Serializable{

    /**
     * The File object of the image
     */
    private File imageFile;
    /**
     * The list of image tags
     */
    private ArrayList<String> tags;
    /**
     * The name without tags
     */
    private String name;

    /**
     * The history of the image
     */
    private Log log;

    /**
     * Extension of image
     */
    private String extension;

    /**
     * Directory of image
     */
    private File directory;

    /**
     * Construct an image representing File imageFile
     * <p>
     * Precondition: imageFile is a valid image
     * </p>
     *
     * @param imageFile The File object of the image
     * @throws InvalidFileException imageFile is an invalid image
     */
    public Image(File imageFile) throws IOException {
        if (!imageFile.isFile()) {
            throw new InvalidFileException("Invalid file");
        }
        if (ImageIO.read(imageFile) == null) {
            throw new InvalidFileException("Invalid image");
        }

        this.imageFile = imageFile;
        tags = new ArrayList<>();
        log = new Log();
        directory = imageFile.getParentFile();

        String rawName = imageFile.getName();
        int indexExtension = rawName.lastIndexOf(".");
        extension = rawName.substring(indexExtension);
        rawName = rawName.substring(0, indexExtension);
        String[] nameParts = rawName.split(" @");

        name = nameParts[0];

        if (nameParts.length > 1) {
            tags.addAll(Arrays.asList(nameParts).subList(1, nameParts.length));
        }
    }

    /**
     * Indicates if 'tag' is contained in this Image
     *
     * @param tag The tag used as a search reference
     * @return True if this Image contains 'tag'
     */
    public boolean containsTag(String tag) {
        return tags.contains(tag);
    }

    /**
     * Get the tags of this image
     * <p>
     * The returned list is a cloned copy. Changes to list will not mutate this image
     * </p>
     *
     * @return The tags of this image
     */
    public ArrayList<String> getTags() {
        return new ArrayList<>(tags);
    }

    /**
     * Get the name, without tag information, of this image
     *
     * @return the name of this image
     */
    public String getName() {
        return name;
    }

    /**
     * Get the file this image represents
     * <p>
     * The returned file is a cloned copy. Changes, especially changes that mutate the actual file, may affect if
     * this image's file is valid.
     * </p>
     *
     * @return the file this image represents
     */
    public File getFile() {
        return new File(imageFile.toString());
    }

    /**
     * Renames this Image and the File this Image represents
     * <p>
     * Precondition: newName does not contain " @"
     * </p>
     *
     * @param newName The Image's new name
     * @throws InvalidNameException newName contains " @"
     */
    public void rename(String newName) {
        if (newName.contains(" @")) {
            throw new InvalidNameException("Name contains \" @\"");
        }
        String oldName = this.name;
        this.name = newName;

        updateFile("Image \"" + oldName + "\" was renamed to \"" + newName + "\"");
    }

    /**
     * Moves this Image to newDirectory
     * <p>
     * Precondition: newDirectory is a valid directory
     * </p>
     *
     * @param newDirectory the directory that will store the image
     * @throws InvalidFileException newDirectory is an invalid directory
     */
    public void move(File newDirectory) {
        if (!newDirectory.isDirectory()) {
            throw new InvalidFileException("Invalid directory");
        }

        directory = newDirectory;
        updateFile("Moved image \"" + name + "\" to \"" + newDirectory.toString() + "\"");
    }

    /**
     * Adds tag to image
     * <p>
     * Precondition: tag does not contain " @"
     * </p>
     *
     * @param tag to add
     * @throws InvalidNameException tag contains " @"
     */
    public void addTag(String tag) {
        if (tag.contains(" @")) {
            throw new InvalidNameException("Tag contains \" @\"");
        }
        if (!tags.contains(tag)) {
            tags.add(tag);
            updateFile("Added tag \"" + tag + "\" to image \"" + name + "\"");
        }
    }

    /**
     * Adds tags to image
     * <p>
     * Precondition: tag in tags does not contain " @"
     * </p>
     *
     * @param tags to add
     * @throws InvalidNameException tag in tags contains " @"
     */
    public void addTag(String[] tags) {
        for(String tag : tags){
            addTag(tag);
        }
    }

    /**
     * Removes tag from image
     *
     * @param tag tag to remove
     */
    public void removeTag(String tag) {
        if (tag.contains(tag)) {
            tags.remove(tag);
            updateFile("Removed tag \"" + tag + "\" from image \"" + name + "\"");
        }
    }

    /**
     * Update image file using current image properties and record log with logMessage
     *
     * @throws UnsuccessfulRenameException Unsuccessful rename. Image is no longer synced to proper imageFile. Image is
     *                                     not valid, is held by a process (ie antivirus), or an image exists in the renaming location
     */
    private void updateFile(String logMessage) {
        File newImageFile = createLocation();
        boolean success = imageFile.renameTo(newImageFile);
        if (success) {
            imageFile = newImageFile;
            log.addEntry(new Entry(logMessage));
        } else {
            throw new UnsuccessfulRenameException();
        }
    }

    /**
     * Create image file using current image properties
     *
     * @return File representation of this image
     */
    private File createLocation() {
        StringBuilder fileName = new StringBuilder();
        fileName.append(name);
        for (String tag : tags) {
            fileName.append(" @");
            fileName.append(tag);
        }
        fileName.append(extension);
        return new File(directory, fileName.toString());
    }
}

/**
 * InvalidFileException represents the exception where the File type does not correspond with the expected File type
 */
class InvalidFileException extends RuntimeException {
    InvalidFileException(String message) {
        super(message);
    }
}

/**
 * InvalidNameException represents the exception where a tag or the image is given an invalid name
 */
class InvalidNameException extends RuntimeException {
    InvalidNameException(String message) {
        super(message);
    }
}

/**
 * UnsuccessfulRenameException represents the exception where the image is renamed unsuccessfully
 */
class UnsuccessfulRenameException extends RuntimeException {
    UnsuccessfulRenameException() {
        super();
    }
}