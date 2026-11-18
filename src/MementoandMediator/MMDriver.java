package MementoandMediator;

import java.util.Iterator;
import java.util.List;
public class MMDriver {
    public static void main(String[] args) {
        ChatServer server = new ChatServer();

        User alice = new User("Alice");
        User bob = new User("Bob");
        User carol = new User("Carol");

        server.registerUser(alice);
        server.registerUser(bob);
        server.registerUser(carol);

        // Demonstrate delivery to multiple recipients.
        System.out.println("=== Alice sends to Bob and Carol ===");
        alice.sendMessage(List.of(bob, carol), "Hello everyone!");

        printHistory(bob);
        printHistory(carol);

        // Demonstrate recipient-specific blocking.
        System.out.println("\n=== Bob blocks Alice ===");
        server.blockUser(bob, alice);
        alice.sendMessage(List.of(bob, carol), "Second message");

        printHistory(bob);
        printHistory(carol);

        // Demonstrate undo of Alice's latest send.
        System.out.println("\n=== Alice undoes her second message ===");
        alice.undoLastMessage();

        printHistory(alice);
        printHistory(carol);

        System.out.println(
                "\nIterator part:=== Alice's messages involving Carol ===");

        Iterator<Message> iterator = alice.iterator(carol);

        while (iterator.hasNext()) {
            Message message = iterator.next();

            System.out.println(
                    message.getSender().getName()
                            + ": " + message.getContent());
        }
    }

    public static void printHistory(User user) {
        System.out.println(user.getName() + "'s history:");

        for (Message message : user.getHistory().getMessages()) {
            System.out.println(
                    message.getTimestamp()
                            + " | " + message.getSender().getName()
                            + ": " + message.getContent());
        }
    }
}
