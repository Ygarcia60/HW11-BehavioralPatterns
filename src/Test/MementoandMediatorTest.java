package Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import MementoandMediator.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MementoandMediatorTest {
    @Test
    void messageStoresItsDetails() {
        User alice = new User("Alice");
        User bob = new User("Bob");

        Message message = new Message(alice, List.of(bob), "Hello");

        assertSame(alice, message.getSender());
        assertEquals(List.of(bob), message.getRecipients());
        assertEquals("Hello", message.getContent());
        assertNotNull(message.getTimestamp());
    }

    @Test
    void changingOriginalRecipientsDoesNotChangeMessage() {
        User alice = new User("Alice");
        User bob = new User("Bob");

        List<User> recipients = new ArrayList<>();
        recipients.add(bob);

        Message message = new Message(alice, recipients, "Hello");
        recipients.clear();

        assertEquals(List.of(bob), message.getRecipients());
    }

    @Test
    void changingReturnedRecipientsDoesNotChangeMessage() {
        User alice = new User("Alice");
        User bob = new User("Bob");

        Message message = new Message(alice, List.of(bob), "Hello");
        message.getRecipients().clear();

        assertEquals(List.of(bob), message.getRecipients());
    }
    @Test
    void addMessageStoresMessageInHistory() {
        User alice = new User("Alice");
        User bob = new User("Bob");
        Message message = new Message(alice, List.of(bob), "Hello");

        ChatHistory history = new ChatHistory();
        history.addMessage(message);

        assertEquals(List.of(message), history.getMessages());
    }

    @Test
    void getLastMessageSentSkipsIncomingMessages() {
        User alice = new User("Alice");
        User bob = new User("Bob");

        Message first = new Message(alice, List.of(bob), "Hello");
        Message second = new Message(alice, List.of(bob), "How are you?");
        Message incoming = new Message(bob, List.of(alice), "Doing well");

        ChatHistory history = new ChatHistory();
        history.addMessage(first);
        history.addMessage(second);
        history.addMessage(incoming);

        assertSame(second, history.getLastMessageSent(alice));
    }

    @Test
    void getLastMessageSentReturnsNullWhenHistoryIsEmpty() {
        ChatHistory history = new ChatHistory();
        User alice = new User("Alice");

        assertNull(history.getLastMessageSent(alice));
    }

    @Test
    void getMessagesProtectsStoredHistory() {
        User alice = new User("Alice");
        User bob = new User("Bob");
        Message message = new Message(alice, List.of(bob), "Hello");

        ChatHistory history = new ChatHistory();
        history.addMessage(message);

        history.getMessages().clear();

        assertEquals(List.of(message), history.getMessages());
    }
    @Test
    void registerUserStoresUser() {
        ChatServer server = new ChatServer();
        User alice = new User("Alice");

        server.registerUser(alice);

        assertSame(alice, server.getUser("Alice"));
    }

    @Test
    void getUserReturnsNullForUnknownName() {
        ChatServer server = new ChatServer();

        assertNull(server.getUser("Unknown"));
    }

    @Test
    void unregisterUserRemovesUser() {
        ChatServer server = new ChatServer();
        User alice = new User("Alice");
        server.registerUser(alice);

        server.unregisterUser(alice);

        assertNull(server.getUser("Alice"));
    }
    @Test
    void sendMessageDeliversThroughServer() {
        ChatServer server = new ChatServer();
        User alice = new User("Alice");
        User bob = new User("Bob");
        server.registerUser(alice);
        server.registerUser(bob);

        alice.sendMessage(List.of(bob), "Hello");

        assertEquals("Hello",
                bob.getHistory().getMessages().get(0).getContent());
        assertEquals(1, alice.getHistory().getMessages().size());
    }

    @Test
    void receiveMessageStoresMessage() {
        User alice = new User("Alice");
        User bob = new User("Bob");
        Message message = new Message(alice, List.of(bob), "Hello");

        bob.receiveMessage(message);

        assertSame(message, bob.getHistory().getMessages().get(0));
    }

    @Test
    void unregisteredUserCannotSend() {
        User alice = new User("Alice");

        assertThrows(IllegalStateException.class,
                () -> alice.sendMessage(List.of(), "Hello"));
    }
    @Test
    void blockingStopsOnlyBlockedDelivery() {
        ChatServer server = new ChatServer();
        User alice = new User("Alice");
        User bob = new User("Bob");
        User carol = new User("Carol");
        server.registerUser(alice);
        server.registerUser(bob);
        server.registerUser(carol);

        server.blockUser(bob, alice);
        alice.sendMessage(List.of(bob, carol), "Hello");

        assertTrue(bob.getHistory().getMessages().isEmpty());
        assertEquals(1, carol.getHistory().getMessages().size());
    }
    @Test
    void saveCapturesMessageState() {
        User alice = new User("Alice");
        User bob = new User("Bob");
        Message message = new Message(alice, List.of(bob), "Hello");

        MessageMemento snapshot = message.save();

        assertEquals("Hello", snapshot.getContent());
        assertEquals(message.getTimestamp(), snapshot.getTimestamp());
    }
    @Test
    void restoreUsesSavedState() {
        User alice = new User("Alice");
        User bob = new User("Bob");
        Message message = new Message(alice, List.of(bob), "Updated message");

        MessageMemento snapshot = new MessageMemento(
                "First message", message.getTimestamp().minusMinutes(1));

        message.restore(snapshot);

        assertEquals("First message", message.getContent());
        assertEquals(snapshot.getTimestamp(), message.getTimestamp());
    }
    @Test
    void undoReplacesDeliveredContent() {
        ChatServer server = new ChatServer();
        User alice = new User("Alice");
        User bob = new User("Bob");
        server.registerUser(alice);
        server.registerUser(bob);

        alice.sendMessage(List.of(bob), "Hello");
        alice.undoLastMessage();

        assertEquals("[Message undone]",
                bob.getHistory().getMessages().get(0).getContent());
        assertEquals("[Message undone]",
                alice.getHistory().getMessages().get(0).getContent());
    }

    @Test
    void undoWithoutMessagesDoesNothing() {
        ChatServer server = new ChatServer();
        User alice = new User("Alice");
        server.registerUser(alice);

        assertDoesNotThrow(() -> alice.undoLastMessage());
    }

}
