package MementoandMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class ChatHistory implements IterableByUser {
    private final List<Message> messages = new ArrayList<>();

    public void addMessage(Message message) {
        messages.add(message);
    }
    public Message getLastMessageSent(User sender) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            Message message = messages.get(i);

            if (message.getSender() == sender) {
                return message;
            }
        }
        return null;
    }
    @Override
    public Iterator<Message> iterator(User userToSearchWith) {
        return new searchMessageByUser(getMessages(), userToSearchWith);
    }
    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }
}