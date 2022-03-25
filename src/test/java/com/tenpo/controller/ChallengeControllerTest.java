package com.tenpo.controller;

import com.tenpo.dto.response.HistoriesResponseDTO;
import com.tenpo.dto.response.OperationsResponseDTO;
import com.tenpo.dto.response.UserResponseDTO;
import com.tenpo.error.catalog.TenpoErrorCode;
import com.tenpo.error.exception.BusinessException;
import com.tenpo.service.SessionService;
import com.tenpo.service.UserService;
import com.tenpo.service.impl.ChallengeServiceImpl;
import com.tenpo.utils.ConstantsTest;
import com.tenpo.utils.UtilsTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@MockitoSettings(strictness = Strictness.LENIENT)
public class ChallengeControllerTest {

    @InjectMocks
    ChallengeController challengeController;

    @Mock
    ChallengeServiceImpl challengeService;

    @Mock
    UserService userService;

    @Mock
    SessionService sessionService;

    @Test
    void testLogin() {

        doReturn(UtilsTest.buildUserLogin()).when(userService).loadUserByUsername(any(String.class), any(String.class), any(HttpServletRequest.class));

        ResponseEntity<UserResponseDTO> response =
        challengeController.login(ConstantsTest.USER_NAME, ConstantsTest.PW, ConstantsTest.REQUEST);

        UserResponseDTO user = response.getBody();

        assertEquals(ConstantsTest.USER_NAME, user.getUserName());
        assertEquals(ConstantsTest.NAME, user.getName());
        assertEquals(ConstantsTest.LAST_NAME, user.getLastName());
        assertEquals(ConstantsTest.USER_EMAIL, user.getEmail());
    }

    @Test
    void testLoginUserNotFound() {

        doThrow(new BusinessException(TenpoErrorCode.NOT_FOUND_LOGIN))
                .when(userService).loadUserByUsername(any(String.class),
                        any(String.class), any(HttpServletRequest.class));

        BusinessException be =
                assertThrows(
                        BusinessException.class,
                        () -> {
                            challengeController.login(ConstantsTest.USER_NAME, ConstantsTest.PW, ConstantsTest.REQUEST);
                        });

        assertEquals(HttpStatus.NOT_FOUND, be.getHttpStatus());
        assertEquals("tenpo.validation.parameters.invalid.login", be.getMessage());

    }

    @Test
    void testSum() {

        doReturn(ConstantsTest.RESULT).when(challengeService).getSum(any(String.class), any(String.class), any(HttpServletRequest.class));

        ResponseEntity<OperationsResponseDTO> response =
                challengeController.getSum(ConstantsTest.ONE, ConstantsTest.TEN, ConstantsTest.REQUEST);

        OperationsResponseDTO result = response.getBody();

        assertEquals(ConstantsTest.RESULT, result.getResult());
    }

    @Test
    void testSumNotAutorhized() {

        doThrow(new BusinessException(TenpoErrorCode.FORBIDDEN))
                .when(challengeService).getSum(any(String.class),
                        any(String.class), any(HttpServletRequest.class));

        BusinessException be =
                assertThrows(
                        BusinessException.class,
                        () -> {
                            challengeController.getSum(ConstantsTest.ONE, ConstantsTest.TEN, ConstantsTest.REQUEST);
                        });

        assertEquals(HttpStatus.FORBIDDEN, be.getHttpStatus());
        assertEquals("tenpo.forbidden", be.getMessage());

    }

    @Test
    void testGetHistory() {

        doReturn(UtilsTest.getHistories()).when(challengeService).getHistories(any(String.class), any(String.class), any(HttpServletRequest.class));

        ResponseEntity<HistoriesResponseDTO> response =
                challengeController.getHistories(ConstantsTest.ONE, ConstantsTest.TEN, ConstantsTest.REQUEST);

        HistoriesResponseDTO histories = response.getBody();

        assertEquals("login", histories.getHistory().get(0).getEndpoint());
        assertEquals("OK", histories.getHistory().get(0).getMessage());
        assertEquals("sum", histories.getHistory().get(1).getEndpoint());
        assertEquals("error", histories.getHistory().get(1).getMessage());

    }


}
