package net.intelie.challenges;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class EventIteratorTest {

    @Test(expected = IllegalStateException.class)
    public void currentFailTest() {

        final Event foo1 = new Event("foo", 10L);
        final List<Event> eventList = new LinkedList<>();
        eventList.add(foo1);

        final EventIterator eventIterator = new EventIteratorList(eventList);
        eventIterator.current();
    }

    @Test(expected = IllegalStateException.class)
    public void removeFailTest() {

        final Event foo = new Event("foo", 10L);
        final List<Event> eventList = new LinkedList<>();
        eventList.add(foo);

        final EventIterator eventIterator = new EventIteratorList(eventList);
        eventIterator.remove();
    }

    @Test
    public void removeSuccessTest() {

        final Event foo = new Event("foo", 10L);
        final Event bar = new Event("bar", 15L);
        final List<Event> eventList = new LinkedList<>();
        eventList.add(foo);
        eventList.add(bar);

        final EventIterator eventIterator = new EventIteratorList(eventList);
        eventIterator.moveNext();
        eventIterator.remove();
        Assert.assertEquals(bar, eventIterator.current());
    }

    @Test
    public void walkingThroughEventsTest() {
        final Event foo1 = new Event("foo", 10L);
        final Event foo2 = new Event("foo", 20L);
        final Event bar = new Event("bar", 15L);
        final List<Event> eventList = new LinkedList<>();
        eventList.add(foo1);
        eventList.add(foo2);
        eventList.add(bar);

        final EventIterator eventIterator = new EventIteratorList(eventList);
        Assert.assertTrue(eventIterator.moveNext());
        Assert.assertTrue(eventIterator.moveNext());
        Assert.assertTrue(eventIterator.moveNext());
        Assert.assertFalse(eventIterator.moveNext());
    }

    @Test
    public void checkAllEventsTest() {

        final Event foo1 = new Event("foo", 10L);
        final Event foo2 = new Event("foo", 20L);
        final Event bar = new Event("bar", 15L);
        final List<Event> eventList = new LinkedList<>();
        eventList.add(foo1);
        eventList.add(foo2);
        eventList.add(bar);

        final EventIterator eventIterator = new EventIteratorList(eventList);
        eventIterator.moveNext();
        Assert.assertEquals(foo1, eventIterator.current());
        eventIterator.moveNext();
        Assert.assertEquals(foo2, eventIterator.current());
        eventIterator.moveNext();
        Assert.assertEquals(bar, eventIterator.current());
        Assert.assertFalse(eventIterator.moveNext());
    }

    @Test
    public void nullEventIteratorTest() {

        final EventIterator eventIterator = new EventIteratorList(null);
        Assert.assertFalse(eventIterator.moveNext());

    }
}
