package Test;

import MementoandMediator.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MementoandMediatorTest {
    @Test
    void userStoreName() {
        User user = new User("Alice");

        assertEquals("Alice", user.getName());
    }
}
