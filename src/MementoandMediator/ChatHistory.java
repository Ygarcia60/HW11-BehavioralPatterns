package MementoandMediator;

import java.util.ArrayList;
import java.util.List;

public class ChatHistory {
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
    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }
}