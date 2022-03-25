package com.tenpo.sevice;

import com.tenpo.dto.response.UserResponseDTO;
import com.tenpo.entity.User;
import com.tenpo.error.catalog.TenpoErrorCode;
import com.tenpo.error.exception.BusinessException;
import com.tenpo.repository.UserRepository;
import com.tenpo.service.ChallengeService;
import com.tenpo.service.SessionService;
import com.tenpo.service.impl.UserServiceImpl;
import com.tenpo.utils.Constants;
import com.tenpo.utils.ConstantsTest;
import com.tenpo.utils.UtilsTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    ChallengeService challengeService;

    @Mock
    UserRepository userRepository;

    @Mock
    SessionService sessionService;

    @Test
    void testSignUpSuccess() {

        doReturn(new User()).when(userRepository).save(any(User.class));

        doNothing().when(challengeService).saveHistory(Constants.ENDPOINT_SING_UP, null, null);

        userService.signUp(UtilsTest.buildNewUser());

        assertTrue(true);

    }

    @Test
    void testSignUpNotSuccess() {

        doAnswer(
                invocation -> {
                    throw new Exception();
                })
                .when(userRepository)
                .save(any(User.class));

        doNothing().when(challengeService).saveHistory(Constants.ENDPOINT_SING_UP, TenpoErrorCode.INTERNAL_SERVER_ERROR, null);

        BusinessException e =
                assertThrows(
                        BusinessException.class,
                        () -> {
                            userService.signUp(UtilsTest.buildNewUser());
                        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getHttpStatus());
        assertEquals("tenpo.server.error", e.getMessage());

    }

    @Test
    void testLoginSuccess() {

        doReturn(UtilsTest.buildUser()).when(userRepository).findUserByUserNameAndPassword(anyString(), anyString());

        doNothing().when(sessionService).upSession(UtilsTest.buildUser().getUserName(), ConstantsTest.REQUEST);

        doNothing().when(challengeService).saveHistory(Constants.ENDPOINT_LOGIN, null, UtilsTest.buildUser().getUserName());

        UserResponseDTO response =userService.loadUserByUsername(ConstantsTest.USER_NAME, ConstantsTest.PW, ConstantsTest.REQUEST);

        assertEquals(ConstantsTest.USER_NAME, response.getUserName());
        assertEquals(ConstantsTest.LAST_NAME, response.getLastName());
        assertEquals(ConstantsTest.NAME, response.getName());
        assertEquals(ConstantsTest.USER_EMAIL, response.getEmail());
    }

    @Test
    void testLoginUserNotFound() {

        doReturn(null).when(userRepository).findUserByUserNameAndPassword(anyString(), anyString());


        doNothing().when(challengeService).saveHistory(Constants.ENDPOINT_LOGIN, null, null);

        BusinessException e =
                assertThrows(
                        BusinessException.class,
                        () -> {
                            userService.loadUserByUsername(ConstantsTest.USER_NAME, ConstantsTest.PW, ConstantsTest.REQUEST);
                        });

        assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
        assertEquals("tenpo.validation.parameters.invalid.login", e.getMessage());
    }

    @Test
    void testLoginException() {

        doAnswer(
                invocation -> {
                    throw new Exception();
                })
                .when(userRepository).
                findUserByUserNameAndPassword(anyString(), anyString());

        BusinessException e =
                assertThrows(
                        BusinessException.class,
                        () -> {
                            userService.loadUserByUsername(ConstantsTest.USER_NAME, ConstantsTest.PW, ConstantsTest.REQUEST);
                        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getHttpStatus());
        assertEquals("tenpo.server.error", e.getMessage());

    }
}
