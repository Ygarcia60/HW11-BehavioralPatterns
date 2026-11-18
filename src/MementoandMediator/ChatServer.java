package MementoandMediator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class ChatServer {
    private final Map<String, User> users = new HashMap<>();
    private final Map<User, Set<User>> blockedUsers = new HashMap<>();

    public void registerUser(User user) {
        users.put(user.getName(), user);
        user.setServer(this);
    }

    public User getUser(String name) {
        return users.get(name);
    }
    public void unregisterUser(User user) {
        users.remove(user.getName());
        user.setServer(null);
    }
    public void sendMessage(Message message) {
        User sender = message.getSender();

        if (users.get(sender.getName()) != sender) {
            throw new IllegalStateException("Sender is not registered.");
        }

        for (User recipient : message.getRecipients()) {
            Set<User> blocked = blockedUsers.get(recipient);

            if (blocked != null && blocked.contains(sender)) {
                continue;
            }

            if (users.get(recipient.getName()) == recipient
                    && recipient != sender) {
                recipient.receiveMessage(message);
            }
        }
    }
    public void blockUser(User recipient, User senderToBlock) {
        blockedUsers.putIfAbsent(recipient, new HashSet<>());
        blockedUsers.get(recipient).add(senderToBlock);
    }
    public void undoMessage(User user, Message message,
                            MessageMemento snapshot) {
        if (users.get(user.getName()) != user
                || message.getSender() != user) {
            throw new IllegalStateException(
                    "Only the registered sender can undo this message.");
        }

        message.restore(snapshot);
    }
}
