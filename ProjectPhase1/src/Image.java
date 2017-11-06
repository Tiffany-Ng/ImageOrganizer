import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Image {

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
     * @param imageFile The File object of the image
     */
    public Image(File imageFile){
        // Precondition: imageFile is valid
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

        if(nameParts.length > 1){
            tags.addAll(Arrays.asList(nameParts).subList(1, nameParts.length));
        }
    }

    /**
     * Indicates if 'tag' is contained in this Image
     * @param tag The tag used as a search reference
     * @return True if this Image contains 'tag'
     */
    public boolean containsTag(String tag){
        return tags.contains(tag);
    }

    /**
     * Get the tags of this image
     * <p>
     * The returned list is a cloned copy. Changes to list will not mutate this image
     * </p>
     * @return The tags of this image
     */
    public ArrayList<String> getTags(){
        return new ArrayList<>(tags);
    }

    /**
     * Get the name, without tag information, of this image
     * @return the name of this image
     */
    public String getName(){
        return name;
    }

    /**
     * Get the file this image represents
     * <p>
     * The returned file is a cloned copy. Changes, especially changes that mutate the actual file, may affect if
     * this image's file is valid.
     * </p>
     * @return the file this image represents
     */
    public File getFile(){
        return new File(imageFile.toString());
    }

    /**
     * Renames this Image and the File this Image represents
     * @param newName The Image's new name
     */
    public void rename(String newName){
        String oldName = this.name;
        this.name = newName;

        updateFile("Image \"" + oldName + "\" was renamed to \"" + newName + "\"");
    }

    /**
     * Moves this Image to newDirectory
     * @param newDirectory the directory that will store the image
     */
    public void move(File newDirectory){
        // Precondition: Assume directory is valid
        directory = newDirectory;
        updateFile("Moved image \"" + name + "\" to \"" + newDirectory.toString() + "\"");
    }

    /**
     * Adds tag to image
     * @param tag to add
     */
    public void addTag(String tag){
        if(!tags.contains(tag)){
            tags.add(tag);
            updateFile("Added tag \"" + tag + "\" to image \"" + name + "\"");
        }
    }

    /**
     * Removes tag from image
     * @param tag tag to remove
     */
    public void removeTag(String tag){
        if(tag.contains(tag)){
            tags.remove(tag);
            updateFile("Removed tag \"" + tag + "\" from image \"" + name + "\"");
        }
    }

    /**
     * Update image file using current image properties and record log with logMessage
     */
    private void updateFile(String logMessage){
        boolean success = imageFile.renameTo(createLocation());
        if(success){
            log.addEntry(new Entry(logMessage));
        }
    }

    /**
     * Create image file using current image properties
     * @return File representation of this image
     */
    private File createLocation(){
        StringBuilder fileName = new StringBuilder();
        fileName.append(name);
        for(String tag:tags){
            fileName.append(" @");
            fileName.append(tag);
        }
        fileName.append(extension);
        File location = new File(directory, fileName.toString());
        // Todo Check what if name already exists? What if newLocation = imageFile?
        System.out.println(location.toString());
        return location;
    }

    public static void main(String[] args) {
        //TODO used to debug Image. Delete in final submission
        File imageFile = new File("C:\\Users\\allan\\Downloads\\csc207 test\\Tester.png");

        Image image = new Image(imageFile);
        image.rename("Test");
        image.rename("Tester");
        /**
        image.addTag("test");
        image.addTag("budapest");
        image.addTag("budapest");
        **/

        System.out.println(image.getName());
        System.out.println(image.getTags().toString());
        System.out.println(image.extension);

        for(Entry e : image.log){
            System.out.println(e.getEntryName());
        }

    }
}
