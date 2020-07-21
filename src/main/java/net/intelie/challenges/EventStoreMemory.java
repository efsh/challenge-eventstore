package net.intelie.challenges;

public class EventStoreMemory implements EventStore{
    @Override
    public void insert(Event event) {

    }

    @Override
    public void removeAll(String type) {

    }

    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        return null;
    }
}
