package MementoandMediator;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

public class Message {
    private final User sender;
    private final List<User> recipients;
    private  LocalDateTime timestamp;
    private  String content;

    public Message(User sender, List<User> recipients, String content) {
        this.sender = sender;
        this.recipients = new  ArrayList<>(recipients);
        this.timestamp = LocalDateTime.now();
        this.content = content;
    }

    public User getSender() {
        return sender;
    }
    public List<User> getRecipients() {
        return new ArrayList<>(recipients);
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String getContent() {
        return content;
    }
    public MessageMemento save() {
        return new MessageMemento(content, timestamp);
    }
    public void restore(MessageMemento snapshot) {
        this.content = snapshot.getContent();
        this.timestamp = snapshot.getTimestamp();
    }
    public void setContent(String content) {
        this.content = content;
    }
}
