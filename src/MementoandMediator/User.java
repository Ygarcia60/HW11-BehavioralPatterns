package MementoandMediator;

import java.util.List;

public class User {
    private final String name;
    private ChatServer server;
    private final ChatHistory history = new ChatHistory();
    private Message lastSentMessage;
    private MessageMemento lastSentSnapshot;

    public User(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setServer(ChatServer server) {
        this.server = server;
    }

    public ChatHistory getHistory() {
        return history;
    }

    public void sendMessage(List<User> recipients, String content) {
        if (server == null) {
            throw new IllegalStateException("User is not registered.");
        }

        // Save the state that undo will restore.
        Message message = new Message(
                this, recipients, "[Message undone]");
        MessageMemento snapshot = message.save();

        // Assign the actual text and deliver through the mediator.
        message.setContent(content);
        server.sendMessage(message);
        history.addMessage(message);

        lastSentMessage = message;
        lastSentSnapshot = snapshot;
    }
    public void undoLastMessage() {
        if (server == null) {
            throw new IllegalStateException("User is not registered.");
        }

        if (lastSentMessage == null) {
            return;
        }

        server.undoMessage(this, lastSentMessage, lastSentSnapshot);

        // Prevent undoing the same send twice.
        lastSentMessage = null;
        lastSentSnapshot = null;
    }

    public void receiveMessage(Message message) {
        history.addMessage(message);
    }
}
