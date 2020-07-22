package net.intelie.challenges;

import org.junit.Assert;
import org.junit.Test;

public class EventStoreTest {

    @Test
    public void removeAllTest() {
        final Event foo1 = new Event("foo", 10L);
        final Event foo2 = new Event("foo", 20L);
        final Event bar = new Event("bar", 30L);

        final EventStore store = new EventStoreInMemory();
        store.insert(foo1);
        store.insert(foo2);
        store.insert(bar);
        store.removeAll("foo");

        final EventIterator result = store.query("foo", 10L, 40L);

        Assert.assertFalse(result.moveNext());
    }

    @Test
    public void removeNonExistTest() {
        final Event foo1 = new Event("foo", 10L);
        final Event foo2 = new Event("foo", 20L);

        final EventStore store = new EventStoreInMemory();
        store.insert(foo1);
        store.insert(foo2);
        store.removeAll("bar");

        final EventIterator result = store.query("foo", 10L, 40L);
        Assert.assertTrue(result.moveNext());
        Assert.assertEquals(foo1, result.current());
        Assert.assertTrue(result.moveNext());
        Assert.assertEquals(foo2, result.current());
        Assert.assertFalse(result.moveNext());

    }

    @Test
    public void timestampOutOfRangeTest() {
        final Event foo = new Event("foo", 10L);

        final EventStore store = new EventStoreInMemory();
        store.insert(foo);

        final EventIterator result = store.query("foo", 20L, 40L);

        Assert.assertFalse(result.moveNext());
    }

    @Test
    public void typeNonExistTest() {
        final Event foo = new Event("foo", 10L);

        final EventStore store = new EventStoreInMemory();
        store.insert(foo);

        final EventIterator result = store.query("bar", 10L, 40L);

        Assert.assertFalse(result.moveNext());
    }

    @Test
    public void happySearchTest() {
        final Event foo1 = new Event("foo", 10L);
        final Event foo2 = new Event("foo", 20L);
        final Event foo3 = new Event("foo", 40L);
        final Event bar = new Event("bar", 30L);

        final EventStore store = new EventStoreInMemory();
        store.insert(foo1);
        store.insert(foo2);
        store.insert(foo3);
        store.insert(bar);

        final EventIterator result = store.query("foo", 10L, 40L);

        Assert.assertTrue(result.moveNext());
        Assert.assertEquals(foo1, result.current());
        Assert.assertTrue(result.moveNext());
        Assert.assertEquals(foo2, result.current());
        Assert.assertFalse(result.moveNext());
    }

}