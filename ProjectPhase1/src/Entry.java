import java.sql.Timestamp;

/**
 * The state of an Image.
 *
 * @version 0.1
 * @author Amarnath Parthiban
 */
public class Entry {

    /**
     * The name the Image had at this time.
     */
    private String entryName;

    /**
     * The time the Entry was created.
     */
    private Timestamp timeStamp;

    /**
     * Construct a new Entry.
     *
     * @param entryName String
     */
    public Entry(String entryName) {

        this.entryName = entryName;
        this.timeStamp = new Timestamp(System.currentTimeMillis());

    }

    public String getTimeStamp() {
        return timeStamp.toString();
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }
}
