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

    // TODO : Add directory for an image (create Log object)

    /**
     * Construct an image representing File imageFile
     * @param imageFile The File object of the image
     */
    public Image(File imageFile){
        // Precondition: imageFile is valid
        this.imageFile = imageFile;
        tags = new ArrayList<>();

        String rawName = imageFile.getName();
        // Todo Check how extension is displayed
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
     * Renames this Image and the File this Image represents
     * @param newName The Image's new name
     */
    public void rename(String newName){
        this.name = newName;

        StringBuilder fileName = new StringBuilder();
        fileName.append(newName);
        for(String tag:tags){
            fileName.append(" @");
            fileName.append(tag);
        }
        // Todo Check how extension is added
        File newLocation = new File(imageFile.getParentFile(), fileName.toString());
        // Todo Check what if name already exists? What if newLocation = imageFile?
        boolean success = imageFile.renameTo(newLocation);

        if(!success){
            // Todo Set more specific exception or make exception
            throw new RuntimeException();
        }
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
}
