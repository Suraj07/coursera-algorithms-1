import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] s;
    private int numOfElements = 0;
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
    }

    // is the queue empty?
    public boolean isEmpty() {
        return (numOfElements == 0);
    }

    // return the number of items on the queue
    public int size() {
        return numOfElements;
    }
    
    // resize
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < numOfElements; i++) {
            copy[i] = s[i];
        }
        s = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException("Cannot add null item to queue");
        }
        if (numOfElements == s.length) {
            resize(2 * s.length);
        }
        s[numOfElements++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("queue is empty");
        }
        int i = StdRandom.uniform(numOfElements);
        Item item = s[i];
        s[i] = s[numOfElements - 1];
        s[numOfElements - 1] = null;
        if ((numOfElements > 0) && (numOfElements == (s.length / 4))) {
            resize(s.length / 2);
        }
        numOfElements--;
        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("queue is empty");
        }
        int i = StdRandom.uniform(numOfElements);
        return s[i];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        
        private int n = numOfElements;
        private Item[] queue;
        
        public QueueIterator() {
            queue = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                queue[i] = s[i];
            }
        }
        
        public boolean hasNext() {
            return (n > 0);
        }

        public void remove() {
            throw new UnsupportedOperationException("not supported");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No elements in queue");
            }
            int i = StdRandom.uniform(n);
            Item item = queue[i];
            queue[i] = queue[n - 1];
            queue[n - 1] = null;
            n--;
            return item;
        }
    }
    
    // unit testing (optional)
    public static void main(String[] args) {
        
    }
}
