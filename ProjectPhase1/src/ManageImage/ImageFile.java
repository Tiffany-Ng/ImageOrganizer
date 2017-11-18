package ManageImage;

import GUI.Main;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class ImageFile implements Serializable{

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

    private List<String> priorNames;

    /**
     * Construct an image representing File imageFile
     * <p>
     * Precondition: imageFile is a valid image
     * </p>
     *
     * @param imageFile The File object of the image
     * @throws InvalidFileException imageFile is an invalid image
     */
    public ImageFile(File imageFile) throws IOException {
        if (!imageFile.isFile()) {
            Main.logger.log(Level.SEVERE, "Invalid file path", new InvalidFileException("Invalid File"));
        }
        if (ImageIO.read(imageFile) == null) {
            Main.logger.log(Level.SEVERE, "Directory not being read", new InvalidFileException("Invalid File"));
        }

        this.imageFile = imageFile;
        tags = new ArrayList<>();
        priorNames = new ArrayList<>();
        log = new Log();
        directory = imageFile.getParentFile();

        List<String> priorTags = splitTags(imageFile.getName());

        String name = getName() + getExtension();
        log.addEntry(new Entry("Set initial name", name));

        if (priorTags.size() > 0) {
            tags.addAll(priorTags);
        }

        priorNames.add(nameWithTags());

    }

    /**
     * Take the name and remove all tags.
     *
     * @param rawName String
     * @return List of tags
     */
    public List<String> splitTags(String rawName) {

        int indexExtension = rawName.lastIndexOf(".");
        extension = rawName.substring(indexExtension);
        rawName = rawName.substring(0, indexExtension);
        String[] nameParts = rawName.split(" @");

        name = nameParts[0];

        return Arrays.asList(nameParts).subList(1, nameParts.length);

    }

    /**
     * Indicates if 'tag' is contained in this ManageImage.ImageFile
     *
     * @param tag The tag used as a search reference
     * @return True if this ManageImage.ImageFile contains 'tag'
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
     * Get the log of this image
     * <p>
     * The returned ManageImage.Log is mutable and will affect ManageImage.ImageFile.
     * </p>
     *
     * @return The ManageImage.Log of this image
     */
    public Log getLog() {
        return log;
    }

    /**
     * Get the extension of this image
     *
     * @return The extension of this image
     */
    public String getExtension() {
        return extension;
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
     * Get the directory of this image
     *
     * <p>
     * The returned directory is a cloned File.
     * </p>
     *
     * @return the directory of this image
     */
    public File getDirectory() {
        return new File(directory.toString());
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
     * Renames this ManageImage.ImageFile and the File this ManageImage.ImageFile represents
     * <p>
     * Precondition: newName does not contain " @"
     * </p>
     *
     * @param newName The ManageImage.ImageFile's new name
     * @throws InvalidNameException newName contains " @"
     */
    public boolean rename(String newName) {
        if (newName.contains("@")) {
            Main.logger.log(Level.SEVERE, "Name inappropriately contains \" @\"", new InvalidNameException("Name contains \\\" @\\"));
            return false;
        }
        String oldName = this.name;
        this.name = newName;

        updateFile("ManageImage.ImageFile \"" + oldName + "\" was renamed to \"" + newName + "\"");
        return true;
    }

    /**
     * Puts the name back to a previous name.
     *
     * @param entryNumber
     */
    public void revertName(int entryNumber) {

        List<String> priorTags = splitTags(priorNames.get(entryNumber));

        priorNames.add(priorNames.remove(entryNumber));

        tags.clear();
        tags.addAll(priorTags);

        log.addEntry(new Entry("Reverted " + getName() + " to prior tags: " + nameWithTags(), nameWithTags()));

    }

    public ArrayList<String> getPriorNames() {

        return new ArrayList<>(priorNames);

    }

    /**
     * Moves this ManageImage.ImageFile to newDirectory
     * <p>
     * Precondition: newDirectory is a valid directory
     * </p>
     *
     * @param newDirectory the directory that will store the image
     * @throws InvalidFileException newDirectory is an invalid directory
     */
    public void move(File newDirectory) throws IOException {   // TODO: implement this
        if (!newDirectory.isDirectory()) {
            Main.logger.log(Level.SEVERE, "Proper file path need to be selected", new InvalidFileException("Invalid directory"));


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
            Main.logger.log(Level.SEVERE, "Invalid tag name", new InvalidNameException("Tag contains \" @\""));
        }
        if (!tags.contains(tag)) {
            tags.add(tag);
            TagManager.add(tag);
            updateFile("Added tag \"" + tag + "\" to image \"" + name + "\"");
            priorNames.add(nameWithTags());
        }
    }

    /**
     * Adds tags to image
     * <p>
     * Precondition: tag in tags does not contain " @"
     * </p>
     *
     * @param tags list of tags to add
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
            //TagManager.remove(tag);
            updateFile("Removed tag \"" + tag + "\" from image \"" + name + "\"");
        }
    }

    /**
     * Update image file using current image properties and record log with logMessage
     *
     * @throws UnsuccessfulRenameException Unsuccessful rename. ManageImage.ImageFile is no longer synced to proper imageFile. ManageImage.ImageFile is
     *                                     not valid, is held by a process (ie antivirus), or an image exists in the renaming location
     */
    private void updateFile(String logMessage) {
        String oldName = imageFile.getName();
        File newImageFile = createLocation();
        boolean success = imageFile.renameTo(newImageFile);
        String newName = newImageFile.getName();
        if (success) {
            imageFile = newImageFile;
            log.addEntry(new Entry(logMessage + ":" + System.lineSeparator() + oldName + " -> " + newName + System.lineSeparator(), nameWithTags()));
        } else {
            Main.logger.log(Level.SEVERE, "File unsuccessfully renamed", new UnsuccessfulRenameException());
        }
    }

    /**
     * Create image file using current image properties
     *
     * @return File representation of this image
     */
    private File createLocation() {

        return new File(directory, nameWithTags());
    }

    public String nameWithTags () {

        StringBuilder fileName = new StringBuilder();
        fileName.append(name);
        for (String tag : tags) {
            fileName.append(" @");
            fileName.append(tag);
        }
        fileName.append(extension);

        return fileName.toString();

    }

    /**
     * this ImageFile and object are equal they represent the same image file
     * @param object the object to comapare to
     * @return indicate if this ImageObject is equal to object
     */
    @Override
    public boolean equals(Object object){
        return object instanceof ImageFile && ((ImageFile)object).getFile().equals(imageFile) && ((ImageFile)object).getDirectory().equals(this.getDirectory());
    }
}

/**
 * ManageImage.InvalidFileException represents the exception where the File type does not correspond with the expected File type
 */
class InvalidFileException extends RuntimeException {

    InvalidFileException(String message) {
        super(message);
    }
}

/**
 * ManageImage.InvalidNameException represents the exception where a tag or the image is given an invalid name
 */
class InvalidNameException extends RuntimeException {
    InvalidNameException(String message) {
        super(message);
    }
}

/**
 * ManageImage.UnsuccessfulRenameException represents the exception where the image is renamed unsuccessfully
 */
class UnsuccessfulRenameException extends RuntimeException {
    UnsuccessfulRenameException() {
        super();
    }
}