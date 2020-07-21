package net.intelie.challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventIteratorArray implements EventIterator {

    private Event[] eventList;
    final private int currentSize;
    private int currentIndex = -1;
    private boolean hasNext = true;

    public EventIteratorArray(Event[] newEvent) {
        this.eventList = newEvent;
        this.currentSize = this.eventList.length;
    }

    @Override
    public boolean moveNext() {
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
        return this.eventList[this.currentIndex];
    }

    @Override
    public void remove() {
        // moveNext() was called or has next element?
        if (this.currentIndex == -1 || !this.hasNext) {
            throw new IllegalStateException();
        }
        // Because we're using an array to implement the iterator, we need an auxiliary structure to remove
        // the current element
        final List<Event> tempList = new ArrayList<>(Arrays.asList(this.eventList));
        tempList.remove(this.currentIndex);
        this.eventList = tempList.toArray(new Event[0]);
    }

    @Override
    public void close() {

    }
}
