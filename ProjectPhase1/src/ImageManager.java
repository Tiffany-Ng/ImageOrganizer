import java.io.Serializable;
import java.util.ArrayList;

/**
 * All images that a user can access.
 * POSSIBLE ENTRY POINT FOR A USER
 */
public class ImageManager implements Serializable{

    /** The list of all images accessible by a user */
    private static ArrayList<Image> images = new ArrayList<>();


    /**
     * Add a new image
     *
     * @param newImage the new image which will be added
     */
    public void addNewImage(Image newImage){

        images.add(newImage);

    }


    /**
     * Delete an image from all directories // TODO: change if we make a directories class
     *
     * @param imgDelete the image that will be removed
     */
    public void deleteImage(Image imgDelete){

        images.remove(imgDelete);

    }


    /**
     * Check if an image of a particular name exists.
     *
     * @param imageName check if an image of this name exists.
     * @return boolean
     */
    public boolean searchByName(String imageName){

        for (Image i: images){
            if (i.getName().equals(imageName)){
                return true;
            }
        }
        return false;
    }


    /**  // TODO: implement after cleaning up image
     * Remove a particular tag from an Image
     *
     * @param tagName Tag to be removed from an image
     */
    public void removeTag(String tagName){

    }


    /**   // TODO
     * Reset the old DELETED tags for an image
     */
    public void resetOldTags(){

    }


    /** // TODO
     * Move an image to a new directory
     *
     * @param newPath the new directory path for the image.
     */
    public void move(String newPath){

    }


}
