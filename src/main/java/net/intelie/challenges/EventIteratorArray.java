package net.intelie.challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventIteratorArray implements EventIterator {

    private Event[] eventList;
    private int currentSize;
    private int currentIndex = -1;
    private boolean hasNext = true;

    public EventIteratorArray(Event[] newEvent) {
        this.eventList = newEvent;
        this.currentSize = this.eventList.length;
    }

    @Override
    public boolean moveNext() {
        if (this.currentIndex < this.currentSize && this.eventList[++this.currentIndex] != null) {
            this.currentIndex++;
            return true;
        }
        this.hasNext = false;
        return false;
    }

    @Override
    public Event current() {
        if (this.currentIndex == -1 || this.hasNext) {
            throw new IllegalStateException();
        }
        return this.eventList[this.currentIndex];
    }

    @Override
    public void remove() {
        if (this.currentIndex == -1 || this.hasNext) {
            throw new IllegalStateException();
        }
        List<Event> tempList = new ArrayList<>(Arrays.asList(this.eventList));
        tempList.remove(this.currentIndex);
        this.eventList = tempList.toArray(new Event[0]);
    }

    @Override
    public void close() {
        this.eventList = null;
        this.currentIndex = -1;
        this.hasNext = true;
    }
}
