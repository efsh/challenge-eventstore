package net.intelie.challenges;

import java.util.*;

/**
 * This is a LinkedList implementation of EventIterator
 * <p>
 *     Uses a synchronizedList (thread-safe) to walking through the Events and provide a reliable
 *     way to multiple threads access an EventIterator instance concurrently.
 *
 *     Using this data structure we have O(1) time complexity for remove(), as we provide the position of the element.
 *     Because is implemented as a double linked list, its performance on remove() is better than Arraylist,
 *     but worse on get() method. Searching for an element takes O(n) time
 *
 *     Vector is similar with ArrayList, but it is synchronized. Vector and ArrayList require more space as
 *     more elements are added.
 *
 *     We use LinkedList (not safe-thread) instead of Vector (synchronized), to gain a better performance, because we
 *     can synchronize the LinkedList explicitly by ourselves.
 * </p>
 *
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
            // Every time needs to update the list size, because previous action could be a remove()
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
