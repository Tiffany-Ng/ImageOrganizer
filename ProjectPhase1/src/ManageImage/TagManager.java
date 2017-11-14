package ManageImage;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * ManageImage.TagManager represents a pool of unique tags
 */
public class TagManager implements Serializable{

    /**
     * the tags that a ManageImage.TagManager stores
     */
    private static LinkedList<String> tags = new LinkedList<>();;


    /**
     * Adds tag to tagManager if tag is not yet added
     * @param tag the tag to be added to ManageImage.TagManager
     */
    public static void add(String tag){
        if(!tags.contains(tag)){
            tags.add(tag);
        }
    }

    /**
     * Removes tag from tagManager
     * @param tag the tag to be removed
     */
    public static void remove(String tag){
        tags.remove(tag);
    }

    /**
     * Get all tags that contains tagSearch
     * @param tagSearch the filter for all stored tags
     * @return all tags that contains tagSearch, with similar first letters ordered first
     */
    public static LinkedList<String> search(String tagSearch){
        LinkedList<String> sameFirstLetter = new LinkedList<>();
        LinkedList<String> otherMatches = new LinkedList<>();
        for(String tag : tags){
            if(tag.toLowerCase().contains(tagSearch.toLowerCase())){
                if(tagSearch.length() == 0 || tag.length() == 0 || !(tag.charAt(0) == tagSearch.charAt(0))){
                    otherMatches.add(tag);
                }else{
                    sameFirstLetter.add(tag);
                }
            }
        }
        LinkedList<String> orderedResults = new LinkedList<>();
        orderedResults.addAll(sameFirstLetter);
        orderedResults.addAll(otherMatches);
        return orderedResults;
    }
}
