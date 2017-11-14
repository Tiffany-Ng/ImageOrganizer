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

    private String imageName;

    /**
     * Construct a new ManageImage.Entry.
     *
     * @param entryName String
     */
    public Entry(String entryName, String imageName) {

        this.entryName = entryName;
        this.imageName = imageName;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {

        return getDate().toString() + " - " + getEntryName();

    }
}
