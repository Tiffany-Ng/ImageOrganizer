import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FilenameFilter;

/**
 * All images that a user can access.
 * POSSIBLE ENTRY POINT FOR A USER
 */
public class ImageManager {

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
     * Delete an image from all directories
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


    /**
     * Return a list of all relevant(png/jpeg/jpg etc.) files in a particular directory.
     *
     * @param directory the path of the folder to search in.
     * @return File[] An array list of images in the specified directory.
     */
    private File[] findFiles(String directory){

        // Get reference to the concerned folder
        File folder = new File(directory);

        return folder.listFiles(new FilenameFilter() {  // return a list of all image files.
            public boolean accept(File dir, String name) {
                return (name.endsWith(".png") | name.endsWith(".jpg") | name.endsWith(".jpeg)") |   // Considering file of the appropriate type
                        name.endsWith(".tiff") | name.endsWith(".ppm") | name.endsWith(".jfif"));
            }
        });

    }


    /**
     * Convert an array of File objects into an array of Image objects.
     *
     * @param possibleImages List of images in a directory that will be converted in to Image objects.
     * @return images An array-list of converted Image objects
     */
    private ArrayList<Image> convertToImageObjects(File[] possibleImages){

        ArrayList<Image> images = new ArrayList<>();
        for( File f: possibleImages ){
            try {
                images.add(new Image(f));
            } catch (IOException e) {
                System.out.println("Image file incorrectly read!");
                e.printStackTrace();
            }
        }

        return images;
    }


    /**
     * Return an array-List of Image objects in a specified directory.
     *
     * @param directory The path of the folder the images are to be found
     * @return ArrayList<Image> All Images found int the directory.
     */
    public ArrayList<Image> getImagesFrom(String directory){

        // get the relevant File objects from directory(png/jpeg/etc.) format
        File[] files = this.findFiles(directory);

        // convert the files into an array-List of Image objects.
        return this.convertToImageObjects(files);

    }


    /**
     * Testing the image reader...
     * TODO: Remove towards the end of the project. (Currently great for testing)
     */
    public static void main(String[] args) {

        String directory = "/Users/akshatkumarnigam/Desktop";  // the path to my desktop
        ImageManager im = new ImageManager();

        ArrayList<Image> images = im.getImagesFrom(directory);
        for(Image img : images){
            System.out.println(img.getName());
        }
    }

}
