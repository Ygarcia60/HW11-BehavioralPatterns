package MementoandMediator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


public class searchMessageByUser implements Iterator<Message> {
    private final List<Message> messages;
    private final User userToSearchWith;
    private int index = 0;

    public searchMessageByUser(List<Message> messages, User userToSearchWith) {
        this.messages = messages;
        this.userToSearchWith = userToSearchWith;
    }

    private boolean matchesUser(Message message) {
        String name = userToSearchWith.getName();

        if (message.getSender().getName().equals(name)) {
            return true;
        }
        for (User recipient : message.getRecipients()) {
            if (recipient.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasNext() {
        while (index < messages.size()) {
            if (matchesUser(messages.get(index))) {
                return true;
            }
            index++;
        }
        return false;
    }
    @Override
    public Message next() {
        if (!hasNext()) {
            throw new NoSuchElementException(
                    "No more messages involving this user");
        }
        return messages.get(index++);
    }
}
