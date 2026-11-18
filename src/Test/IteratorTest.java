package Test;

import MementoandMediator.Message;
import MementoandMediator.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class IteratorTest {
    private User alice;
    private User bob;
    private User carol;

    private Message unrelated;
    private Message outgoing;
    private Message incoming;

    @BeforeEach
    void setUp() {
        alice = new User("Alice");
        bob = new User("Bob");
        carol = new User("Carol");

        unrelated = new Message(
                alice, List.of(carol), "Hello Carol");

        outgoing = new Message(
                alice, List.of(bob), "Hello Bob");

        incoming = new Message(
                bob, List.of(alice), "Hello Alice");

        alice.getHistory().addMessage(unrelated);
        alice.getHistory().addMessage(outgoing);
        alice.getHistory().addMessage(incoming);
    }

    @Test
    void historyIteratorFindsSentAndReceivedMessages() {
        Iterator<Message> iterator =
                alice.getHistory().iterator(bob);

        assertSame(outgoing, iterator.next());
        assertSame(incoming, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void userIteratorDelegatesToHistory() {
        Iterator<Message> iterator = alice.iterator(bob);

        assertSame(outgoing, iterator.next());
    }

    @Test
    void repeatedHasNextDoesNotSkipMatch() {
        Iterator<Message> iterator = alice.iterator(bob);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertSame(outgoing, iterator.next());
    }

    @Test
    void noMatchesReturnsFalse() {
        Iterator<Message> iterator =
                alice.iterator(new User("Unknown"));

        assertFalse(iterator.hasNext());
    }

    @Test
    void nextWithoutMatchThrowsException() {
        Iterator<Message> iterator =
                alice.iterator(new User("Unknown"));

        assertThrows(NoSuchElementException.class,
                () -> iterator.next());
    }

    @Test
    void filteringMatchesUsername() {
        Iterator<Message> iterator =
                alice.iterator(new User("Bob"));

        assertSame(outgoing, iterator.next());
    }
}
