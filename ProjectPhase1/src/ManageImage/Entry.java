package ManageImage;

import java.io.Serializable;
import java.util.Date;

/**
 * The state of an ManageImage.ImageFile.
 *
 * @version 0.2
 * @author Amarnath Parthiban
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
    public Entry(String entryName) {

        this.entryName = entryName;
        this.currDate = new Date();

    }

    public Date getDate() {
        return this.currDate;
    }

    public void setTimeStamp(Date newDate) {
        this.currDate = newDate;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) { this.entryName = entryName; }

    @Override
    public String toString() {

        return getDate().toString() + " - " + getEntryName();

    }
}
