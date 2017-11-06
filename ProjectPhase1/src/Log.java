import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A Log of all changes to an Image.
 * A Log is able to iterate through every Entry it has recorded.
 *
 * @version 0.1
 * @author Amarnath Parthiban
 */
public class Log implements Iterable<Entry>, Serializable {

    /**
     * List of all changes, each stored as an Entry.
     */
    private List<Entry> history;

    /**
     * Construct a new Log.
     */
    public Log() {

        history = new ArrayList<>();

    }

    /**
     * Get the most recent entry in the history.
     *
     * @return Entry
     */
    public Entry recentEntry() {

        return history.get(history.size() -1 );  //TODO: Will need to convert into suitable redable format.

    }

    /**
     * Records a new Entry in the history.
     *
     * @param e Entry
     */
    public void addEntry(Entry e) {

        history.add(e);

    }

    /**
     * The Iterator of Log, used to go through each Entry.
     *
     * @return Iterator
     */
    @Override
    public Iterator<Entry> iterator() {

        return new LogIterator();

    }

    /**
     * The Iterator Log uses to go through each Entry found in history.
     */
    private class LogIterator implements Iterator<Entry> {

        private int index = 0;

        /**
         * Checks if there are more elements needed to be iterated through.
         *
         * @return boolean
         */
        @Override
        public boolean hasNext() {

            return index < history.size();

        }

        /**
         * The next Entry.
         *
         * @return Entry
         */
        @Override
        public Entry next() {

            return history.get(index++);

        }

    }

}
