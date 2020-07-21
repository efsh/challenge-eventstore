package net.intelie.challenges;

import org.junit.Assert;
import org.junit.Test;

public class EventIteratorTest {

    @Test(expected = IllegalStateException.class)
    public void currentFailTest() {
        final Event foo1 = new Event("foo", 10L);
        final Event[] eventArray = {foo1};
        final EventIterator eventIterator = new EventIteratorArray(eventArray);
        eventIterator.current();
    }

    @Test(expected = IllegalStateException.class)
    public void removeFailTest() {
        final Event foo = new Event("foo", 10L);
        final Event[] eventArray = {foo};
        final EventIterator eventIterator = new EventIteratorArray(eventArray);
        eventIterator.remove();
    }

    @Test
    public void checkAllEventsTest() {
        final Event foo1 = new Event("foo", 10L);
        final Event foo2 = new Event("foo", 20L);
        final Event bar = new Event("bar", 15L);
        final Event[] eventArray = {foo1, foo2, bar};
        final EventIterator eventIterator = new EventIteratorArray(eventArray);
        eventIterator.moveNext();
        Assert.assertEquals(foo1, eventIterator.current());
        eventIterator.moveNext();
        Assert.assertEquals(foo2, eventIterator.current());
        eventIterator.moveNext();
        Assert.assertEquals(bar, eventIterator.current());
        Assert.assertFalse(eventIterator.moveNext());
    }
}
