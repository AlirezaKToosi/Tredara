package com.novare.tredara.services;

import com.novare.tredara.models.EActionType;
import com.novare.tredara.models.Item;
import com.novare.tredara.models.Log;
import com.novare.tredara.models.User;
import com.novare.tredara.repositories.ItemRepo;
import com.novare.tredara.repositories.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemRepo itemRepo;

    private LogService logService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        logService = new LogService(logRepository, userService, itemRepo);
    }

    @Test
    public void testSaveLog() {
        Log log = new Log();
        // Set log properties
        logService.saveLog(log);
        Mockito.verify(logRepository).save(log);
    }

    @Test
    public void testLogSuccessfulLogin() {
        String userName = "Test";

        User user = new User();
        Mockito.when(userService.findByEmail(userName)).thenReturn(Optional.of(user));

        final Log[] capturedLog = {new Log()};
        Mockito.when(logRepository.save(Mockito.any(Log.class))).thenAnswer(invocation -> {
            capturedLog[0] = invocation.getArgument(0);
            return capturedLog[0];
        });

        logService.logSuccessfulLogin(userName);

        // Verify that the log was saved with the correct details
        assertEquals(EActionType.SUCCESSFUL_LOGIN, capturedLog[0].getActionType());
        assertEquals("User logged in successfully.", capturedLog[0].getActionDetails());
        assertEquals(user, capturedLog[0].getUser());
        assertNotNull(capturedLog[0].getTimestamp());


    }

    @Test
    public void testLogFailedLogin() {
        String userName = "Test";
        User user = new User();
        Mockito.when(userService.findByEmail(userName)).thenReturn(Optional.of(user));

        // Capture the log that is saved
        final Log[] capturedLog = {new Log()};
        Mockito.when(logRepository.save(Mockito.any(Log.class))).thenAnswer(invocation -> {
            capturedLog[0] = invocation.getArgument(0);
            return capturedLog[0];
        });

        logService.logFailedLogin(userName);

        // Verify that the log was saved with the correct details
        assertEquals(EActionType.FAILED_LOGIN, capturedLog[0].getActionType());
        assertEquals("User login failed for email: " + userName, capturedLog[0].getActionDetails());
        assertEquals(user, capturedLog[0].getUser());
        assertNotNull(capturedLog[0].getTimestamp());

    }

    @Test
    public void testLogResourceAccess() {
        EActionType action = EActionType.CREATE_ITEM;
        String userName = "Test";
        Long itemId = 123L;
        Mockito.when(userService.findByEmail(userName)).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepo.findById(itemId)).thenReturn(Optional.of(new Item()));

        // Capture the log that is saved
        final Log[] capturedLog = {new Log()};
        Mockito.when(logRepository.save(Mockito.any(Log.class))).thenAnswer(invocation -> {
            capturedLog[0] = invocation.getArgument(0);
            return capturedLog[0];
        });

        logService.logResourceAccess(action, userName, itemId);

        assertEquals(action, capturedLog[0].getActionType());
        assertNotNull(capturedLog[0].getTimestamp());
        assertNotNull(capturedLog[0].getUser());
        assertNotNull(capturedLog[0].getItem());
        assertNotNull(capturedLog[0].getActionDetails());
    }

}