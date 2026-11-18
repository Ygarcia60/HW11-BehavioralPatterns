package Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import MementoandMediator.Message;
import MementoandMediator.User;
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

}
