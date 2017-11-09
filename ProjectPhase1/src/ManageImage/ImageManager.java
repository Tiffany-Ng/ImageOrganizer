package ManageImage;

import java.io.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 * All imageFiles that a user can access.
 * POSSIBLE ENTRY POINT FOR A USER
 * TODO ManageImage.ImageManager shouldn't make imageFiles since already made imageFiles cannot recreate their history. Images should be
 * added from calling classes.
 */
public class ImageManager implements Serializable{

    /** The list of all imageFiles accessible by a user */
    private static ArrayList<ImageFile> imageFiles = new ArrayList<>();


    /**
     * Returns imageFiles
     *
     * @return ArrayList<ImageFile> imageFiles
     */
    public static ArrayList<ImageFile> getImageFiles(){

        return imageFiles;

    }

    /**
     * Get images stored in directory
     * <p>
     * Assumes stored images and directory all use absolute paths
     * </p>
     * @param directory the directory that contains returned images
     * @return the images stored in directory
     */
    public ArrayList<ImageFile> getImageFilesByDirectory(File directory){

        ArrayList<ImageFile> filteredImages = new ArrayList<>();
        for(ImageFile imageFile : imageFiles){
            File imageDirectory = imageFile.getDirectory();
            if (imageDirectory.toString().startsWith(directory.toString())){
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
    public void addImage(ImageFile imageInsert){

        boolean match = false;
        for(ImageFile imageFile:imageFiles){
            if(imageFile.equals(imageInsert)){
                match = true;
                break;
            }
        }
        if(!match){
            ImageManager.imageFiles.add(imageInsert);
        }
    }


    /**
     * Delete an image from all directories
     *
     * @param imgDelete the image that will be removed
     */
    public void deleteImage(ImageFile imgDelete){

        imageFiles.remove(imgDelete);

    }


    /**
     * Check if an image of a particular name exists.
     *
     * @param imageName check if an image of this name exists.
     * @return boolean
     */
    public boolean searchByName(String imageName){

        for (ImageFile i: imageFiles){
            if (i.getName().equals(imageName)){
                return true;
            }
        }
        return false;
    }


    /**
     * Return an array of ManageImage.ImageFile files in a directory
     *
     * @param directory the path of the folder to search in.
     * @return File[] array of all imageFiles in a directory.
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
     * Check for sub-directories in directory, while adding imageFiles.
     *
     * @param directory the path of the folder to search in.
     * @return ArrayList<File> arrayList of all imageFiles(including those found in sub-directories).
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
     * Convert an array of File objects into an array of ManageImage.ImageFile objects.
     *
     * @param possibleImages List of imageFiles in a directory that will be converted in to ManageImage.ImageFile objects.
     */
    private void convertToImageObjects(ArrayList<File> possibleImages){

        //ArrayList<ManageImage.ImageFile> imageFiles = new ArrayList<>();
        for( File f: possibleImages ){
            try {
               this.addImage(new ImageFile(f));

            } catch (IOException e) {
                System.out.println("ManageImage.ImageFile file incorrectly read!");
                e.printStackTrace();
            }
        }

    }


    /**
     * Add all imageFiles from a specified directory into the list of imageFiles that a user has seen.
     *
     * @param directory The path of the folder the imageFiles are to be found
     */
    public void createImagesFromDirectory(String directory){

        // get the relevant File objects from directory & sub-directory
        ArrayList<File> files = this.checkSubDirectories(directory);

        // convert the files into an array-List of ManageImage.ImageFile objects.
        this.convertToImageObjects(files);

    }


    /**
     * Testing the image reader...
     * TODO: Remove towards the end of the project. (Currently great for testing)
     */
    public static void main(String[] args) {

        String directory = "/Users/akshatkumarnigam/Desktop/sample";  // the path to a sample file
        ImageManager im = new ImageManager();

        im.createImagesFromDirectory(directory);

        for(ImageFile img : imageFiles){
            System.out.println(img.getName());
        }
    }

}
