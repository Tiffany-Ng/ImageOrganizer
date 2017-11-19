package ManageImage;

import java.io.Serializable;
import java.util.Date;

/**
 * The state of an ImageFile at a given time.
 */
public class Entry implements Serializable {

    /**
     * The name the ManageImage.ImageFile had at this time.
     */
    private String entryName;

    /**
     * The time the ManageImage.Entry was created.
     */
    private Date currDate;

    /**
     * Construct a new ManageImage.Entry.
     *
     * @param entryName String
     */
    Entry(String entryName) {

        this.entryName = entryName;
        this.currDate = new Date();

    }

    private Date getDate() {
        return this.currDate;
    }

    private String getEntryName() {
        return entryName;
    }

    /**
     * Return the entry in an easy to read format. date acquired - entry information.
     *
     * ex.  Mon Nov 13 23:08:15 EST 2017 - Added new tag: @beach
     */
    @Override
    public String toString() {

        return getDate().toString() + " - " + getEntryName();

    }
}
