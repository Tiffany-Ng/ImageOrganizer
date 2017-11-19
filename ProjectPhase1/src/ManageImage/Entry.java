package ManageImage;

import java.io.Serializable;
import java.util.Date;

/**
 * The state of an ManageImage.ImageFile.
 *
 * @author Amarnath Parthiban
 * @version 0.2
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

    @Override
    public String toString() {

        return getDate().toString() + " - " + getEntryName();

    }
}
