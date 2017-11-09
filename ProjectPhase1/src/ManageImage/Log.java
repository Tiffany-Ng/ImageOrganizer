package ManageImage;

import ManageImage.Entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A ManageImage.Log of all changes to an ManageImage.ImageFile.
 * A ManageImage.Log is able to iterate through every ManageImage.Entry it has recorded.
 *
 * @version 0.1
 * @author Amarnath Parthiban
 */
public class Log implements Iterable<Entry>, Serializable {

    /**
     * List of all changes, each stored as an ManageImage.Entry.
     */
    private List<Entry> history;

    /**
     * Construct a new ManageImage.Log.
     */
    public Log() {

        history = new ArrayList<>();

    }

    /**
     * Get the most recent entry in the history.
     *
     * @return ManageImage.Entry
     */
    public Entry recentEntry() {

        return history.get(history.size() -1 );  //TODO: Will need to convert into suitable redable format.

    }

    /**
     * Records a new ManageImage.Entry in the history.
     *
     * @param e ManageImage.Entry
     */
    public void addEntry(Entry e) {

        history.add(e);

    }

    /**
     * The Iterator of ManageImage.Log, used to go through each ManageImage.Entry.
     *
     * @return Iterator
     */
    @Override
    public Iterator<Entry> iterator() {

        return new LogIterator();

    }

    /**
     * The Iterator ManageImage.Log uses to go through each ManageImage.Entry found in history.
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
         * The next ManageImage.Entry.
         *
         * @return ManageImage.Entry
         */
        @Override
        public Entry next() {

            return history.get(index++);

        }

    }

}
