package ManageImage;

import GUI.Main;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class ImageFile implements Serializable {

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
     * <p>Precondition: imageFile is a valid image</p>
     *
     * @param imageFile The File object of the image
     * @throws InvalidFileException imageFile is an invalid image
     */
    ImageFile(File imageFile) throws IOException {
        if (!imageFile.isFile()) {
            Main.logger.log(Level.SEVERE, "Invalid file path", new InvalidFileException("Invalid File"));
        } else if (ImageIO.read(imageFile) == null) {
            Main.logger.log(
                    Level.SEVERE, "Directory not being read", new InvalidFileException("Invalid File"));
        } else {
            this.imageFile = imageFile;
            tags = new ArrayList<>();
            priorNames = new ArrayList<>();
            log = new Log();

            setUpFromImageFile();
            log.addEntry(new Entry("Set initial name"));
            priorNames.add(nameWithTags());
        }


    }

    /**
     * Sets up instance variables from imageFile
     */
    private void setUpFromImageFile() {
        List<String> tags = splitTags(imageFile.getName());
        this.tags = new ArrayList<>();
        this.tags.addAll(tags);
        directory = imageFile.getParentFile();
    }

    /**
     * Take the name and remove all tags.
     *
     * @param rawName String
     * @return List of tags
     */
    private List<String> splitTags(String rawName) {

        int indexExtension = rawName.lastIndexOf(".");
        extension = rawName.substring(indexExtension);
        rawName = rawName.substring(0, indexExtension);
        String[] nameParts = rawName.split(" @");

        name = nameParts[0];

        return Arrays.asList(nameParts).subList(1, nameParts.length);
    }

    /**
     * Get the tags of this image
     * <p>The returned list is a cloned copy. Changes to list will not mutate this image</p>
     *
     * @return The tags of this image
     */
    public ArrayList<String> getTags() {
        return new ArrayList<>(tags);
    }

    /**
     * Get the log of this image
     * <p>The returned ManageImage.Log is mutable and will affect ManageImage.ImageFile.
     *
     * @return The ManageImage.Log of this image
     */
    public Log getLog() {
        return log;
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
     * <p>
     * <p>The returned directory is a cloned File.
     *
     * @return the directory of this image
     */
    public File getDirectory() {
        return new File(directory.toString());
    }

    /**
     * Get the file this image represents
     * <p>The returned file is a cloned copy. Changes, especially changes that mutate the actual file,
     * may affect if this image's file is valid.
     *
     * @return the file this image represents
     */
    public File getFile() {
        return new File(imageFile.toString());
    }

    /**
     * Renames this ManageImage.ImageFile and the File this ManageImage.ImageFile represents
     * <p>
     * <p>Precondition: newName does not contain " @"
     *
     * @param newName The ManageImage.ImageFile's new name
     * @return indicates a successful rename
     * @throws InvalidNameException newName contains " @"
     */
    public boolean rename(String newName) {
        if (newName.contains("@")) {
            Main.logger.log(
                    Level.SEVERE,
                    "Name inappropriately contains \" @\"",
                    new InvalidNameException("Name contains \\\" @\\"));
            return false;
        } else if (newName.equals(this.name)) {
            return false;
        } else {
            String oldName = this.name;
            this.name = newName;

            boolean success = updateFile("Image \"" + oldName + "\" was renamed to \"" + newName + "\"");
            if (success) {
                priorNames.remove(nameWithTags());
                priorNames.add(nameWithTags());
            }
            return success;
        }
    }

    /**
     * Puts the name back to a previous name.
     *
     * @param entryNumber int: position of entry to revert to
     * @return indicate if the revert was successful
     */
    public boolean revertName(int entryNumber) {

        List<String> priorTags = splitTags(priorNames.get(entryNumber));

        tags.clear();
        tags.addAll(priorTags);

        boolean success = updateFile("Reverted " + getName() + " to prior tags: " + nameWithTags());
        if (success) {
            priorNames.add(priorNames.remove(entryNumber));
        }
        return success;
    }

    public ArrayList<String> getPriorNames() {

        return new ArrayList<>(priorNames);
    }

    /**
     * Moves this ManageImage.ImageFile to newDirectory
     * <p>Precondition: newDirectory is a valid directory
     *
     * @param newDirectory the directory that will store the image
     * @throws InvalidFileException newDirectory is an invalid directory
     */
    public boolean move(File newDirectory) throws IOException { // TODO: implement this
        boolean success;
        if (!newDirectory.isDirectory()) {
            success = false;
            Main.logger.log(
                    Level.SEVERE,
                    "Proper file path need to be selected",
                    new InvalidFileException("Invalid directory"));
        } else {
            directory = newDirectory;
            success = updateFile("Moved image \"" + name + "\" to \"" + newDirectory.toString() + "\"");
        }
        return success;
    }

    /**
     * Adds tag to image
     * <p>Precondition: tag does not contain " @"
     *
     * @param tag to add
     * @return Indicates if the tag insertion was successful
     * @throws InvalidNameException tag contains " @"
     */
    public boolean addTag(String tag) {
        boolean success = false;
        if (tag.contains(" @")) {
            Main.logger.log(
                    Level.SEVERE, "Invalid tag name", new InvalidNameException("Tag contains \" @\""));
        } else if (!tags.contains(tag)) {
            tags.add(tag);
            success = updateFile("Added tag \"" + tag + "\" to image \"" + name + "\"");
            if (success) priorNames.add(nameWithTags());
            if (!TagManager.getTags().contains(tag)) TagManager.add(tag);
        }
        return success;
    }

    /**
     * Removes tag from image
     *
     * @param tag tag to remove
     * @return Indicates if the tag removal was successful
     */
    public boolean removeTag(String tag) {
        boolean success = false;
        if (tag.contains(tag)) {
            tags.remove(tag);
            // TagManager.remove(tag);
            success = updateFile("Removed tag \"" + tag + "\" from image \"" + name + "\"");
            if (success) priorNames.add(nameWithTags());
        }
        return success;
    }

    /**
     * Update image file using current image properties and record log with logMessage
     *
     * @return Indicates if the update was successful
     * @throws UnsuccessfulRenameException Unsuccessful rename. ManageImage.ImageFile is no longer
     *                                     synced to proper imageFile. ManageImage.ImageFile is not valid, is held by a process (ie
     *                                     antivirus), or an image exists in the renaming location
     */
    private boolean updateFile(String logMessage) {
        String oldName = imageFile.getName();
        File newImageFile = createLocation();
        boolean success = imageFile.renameTo(newImageFile);
        String newName = newImageFile.getName();
        if (success) {
            imageFile = newImageFile;
            log.addEntry(
                    new Entry(
                            logMessage
                                    + ":"
                                    + System.lineSeparator()
                                    + oldName
                                    + " -> "
                                    + newName
                                    + System.lineSeparator()));
        } else {
            setUpFromImageFile();
            Main.logger.log(
                    Level.SEVERE, "File unsuccessfully renamed", new UnsuccessfulRenameException());
        }
        return success;
    }

    /**
     * Create image file using current image properties
     *
     * @return File representation of this image
     */
    private File createLocation() {

        return new File(directory, nameWithTags());
    }

    public String nameWithTags() {

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
     *
     * @param object the object to compare to
     * @return indicate if this ImageObject is equal to object
     */
    @Override
    public boolean equals(Object object) {
        return object instanceof ImageFile
                && ((ImageFile) object).getFile().equals(imageFile)
                && ((ImageFile) object).getDirectory().equals(this.getDirectory());
    }
}

/**
 * ManageImage.InvalidFileException represents the exception where the File type does not correspond
 * with the expected File type
 */
class InvalidFileException extends RuntimeException {

    InvalidFileException(String message) {
        super(message);
    }
}

/**
 * ManageImage.InvalidNameException represents the exception where a tag or the image is given an
 * invalid name
 */
class InvalidNameException extends RuntimeException {
    InvalidNameException(String message) {
        super(message);
    }
}

/**
 * ManageImage.UnsuccessfulRenameException represents the exception where the image is renamed
 * unsuccessfully
 */
class UnsuccessfulRenameException extends RuntimeException {
    UnsuccessfulRenameException() {
        super();
    }
}
