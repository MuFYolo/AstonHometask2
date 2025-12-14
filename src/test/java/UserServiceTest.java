import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDaoMock;

    @InjectMocks
    private UserDao userService;

    @Test
    public void testCreateAdultUserSuccess() {
        doNothing().when(userDaoMock).save(any(User.class));

        User createdUser = userService.createAndReturnUser("Alice", "alice@example.com", 25);

        assertNotNull(createdUser);
        assertEquals("Alice", createdUser.getName());

        verify(userDaoMock, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateMinorUserFails() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.createAndReturnUser("Bob", "bob@example.com", 16);
        });

        assertTrue(thrown.getMessage().contains("User must be adult"));

        verify(userDaoMock, never()).save(any(User.class));
    }
}