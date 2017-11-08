import java.io.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 * All images that a user can access.
 * POSSIBLE ENTRY POINT FOR A USER
 * TODO ImageManager shouldn't make images since already made images cannot recreate their history. Images should be
 * added from calling classes.
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

        ImageManager.images.add(newImage);

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
     * Return an array of Image files in a directory
     *
     * @param directory the path of the folder to search in.
     * @return File[] array of all images in a directory.
     */
    private ArrayList<File> findImages(String directory){

        File folder = new File(directory);
        File[] collect;

        collect = folder.listFiles(new FilenameFilter() {  // return a list of all image files.
            public boolean accept(File dir, String name) {
                return (name.endsWith(".png") | name.endsWith(".jpg") | name.endsWith(".jpeg)") |   // Considering file of the appropriate type
                        name.endsWith(".tiff") | name.endsWith(".ppm") | name.endsWith(".jfif"));
            }
        });

        return new ArrayList<File>(Arrays.asList(collect));  // TODO: null condition
    }


    /**
     * Get the names of all sub-directories in a directory.
     *
     * @param directory the path of the folder to search in.
     * @return gatherSubDirectories Array list of names of sub-directories.
     */
    private ArrayList<String> checkForSubDirectory(String directory){

        File folder = new File(directory);
        ArrayList<String> gatherSubDirectories = new ArrayList<String>();

        for(File f: folder.listFiles()){   //TODO: empty directory check
            if(f.isDirectory()){
                gatherSubDirectories.add(f.getName());
            }
        }

        return gatherSubDirectories;
    }


    /**
     * Check for sub-directories in directory, while adding images.
     *
     * @param directory the path of the folder to search in.
     * @return ArrayList<File> arrayList of all images(including those found in sub-directories).
     */
    private ArrayList<File> checkSubDirectories(String directory){

        // if no sub-directory, return findImages
        if(this.checkForSubDirectory(directory).isEmpty()){
            return findImages(directory);
        }

        // else return findImages + findImages(NEW_PATH)
        else {
            // create temporary array
            ArrayList<File> allImages = new ArrayList<>();

            // loop through the names-list generated
            for(String dirName: this.checkForSubDirectory(directory)){

                // keep recurring through names list - and adding to the temp array
                allImages.addAll(checkSubDirectories( directory + "/" + dirName ));
            }

            ArrayList<File> gather = this.findImages(directory);
            gather.addAll(allImages);
            return gather;

        }
    }


    /**
     * Convert an array of File objects into an array of Image objects.
     *
     * @param possibleImages List of images in a directory that will be converted in to Image objects.
     */
    private void convertToImageObjects(ArrayList<File> possibleImages){

        //ArrayList<Image> images = new ArrayList<>();
        for( File f: possibleImages ){
            try {
               this.addNewImage(new Image(f));

            } catch (IOException e) {
                System.out.println("Image file incorrectly read!");
                e.printStackTrace();
            }
        }

    }


    /**
     * Add all images from a specified directory into the list of images that a user has seen.
     *
     * @param directory The path of the folder the images are to be found
     */
    public void getImagesFrom(String directory){

        // get the relevant File objects from directory & sub-directory
        ArrayList<File> files = this.checkSubDirectories(directory);

        // convert the files into an array-List of Image objects.
        this.convertToImageObjects(files);

    }


    /**
     * Testing the image reader...
     * TODO: Remove towards the end of the project. (Currently great for testing)
     */
    public static void main(String[] args) {

        String directory = "/Users/akshatkumarnigam/Desktop/sample";  // the path to a sample file
        ImageManager im = new ImageManager();

        im.getImagesFrom(directory);

        for(Image img : images){
            System.out.println(img.getName());
        }
    }

}
