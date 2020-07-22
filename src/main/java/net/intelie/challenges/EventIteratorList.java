package net.intelie.challenges;

import java.util.*;

/**
 * This is a LinkedList implementation of EventIterator
 * <p>
 *     Uses a synchronizedList (thread-safe) to walking through the Events and provide a reliable
 *     way to multiple threads access an EventIterator instance concurrently
 * </p>
 */
public class EventIteratorList implements EventIterator {

    private int currentSize = 0;
    private int currentIndex = -1;
    private boolean hasNext = true;

    // to prevent accidental unsynchronized access to the list, we should "wrapped" using
    // the Collections.synchronizedList() method
    private List<Event> eventList = Collections.synchronizedList(new LinkedList<>());

    public EventIteratorList(List<Event> events) {
        this.eventList = events;
    }

    @Override
    public boolean moveNext() {

        try {
            this.currentSize = this.eventList.size();
        } catch (final NullPointerException e) {
            // Prevents null eventList
        }

        // has next element?
        if (++this.currentIndex < this.currentSize) {
            return true;
        }
        this.hasNext = false;
        return false;

    }

    @Override
    public Event current() {
        // moveNext() was called or has next element?
        if (this.currentIndex == -1 || !this.hasNext) {
            throw new IllegalStateException();
        }
        return this.eventList.get(this.currentIndex);
    }

    @Override
    public void remove() {
        // moveNext() was called or has next element?
        if (this.currentIndex == -1 || !this.hasNext) {
            throw new IllegalStateException();
        }

        // this operation generates a structural modification in the list, so we must be synchronize it.
        synchronized (this.eventList) {
            this.eventList.remove(this.currentIndex);
        }

    }

    @Override
    public void close() {

    }
}
