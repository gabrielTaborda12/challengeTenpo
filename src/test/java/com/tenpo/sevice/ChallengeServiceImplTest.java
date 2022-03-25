package com.tenpo.sevice;

import com.tenpo.dto.response.HistoriesResponseDTO;
import com.tenpo.entity.History;
import com.tenpo.error.exception.BusinessException;
import com.tenpo.repository.HistoryRepository;
import com.tenpo.service.SessionService;
import com.tenpo.service.impl.ChallengeServiceImpl;
import com.tenpo.utils.ConstantsTest;
import com.tenpo.utils.UtilsTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@MockitoSettings(strictness = Strictness.LENIENT)
public class ChallengeServiceImplTest {

    @InjectMocks
    ChallengeServiceImpl challengeService;

    @Mock
    SessionService sessionService;

    @Mock
    HistoryRepository historyRepository;

    @Test
    void testSumSuccess() {

        doReturn(Boolean.FALSE).when(sessionService).validateSession(any(HttpServletRequest.class));

        doReturn(new History()).when(historyRepository).save(any(History.class));

        BigDecimal result = challengeService.getSum(ConstantsTest.ONE, ConstantsTest.TEN, ConstantsTest.REQUEST);

        assertEquals(ConstantsTest.RESULT, result);

    }

    @Test
    void testSumNotAccess() {

        doReturn(Boolean.TRUE).when(sessionService).validateSession(any(HttpServletRequest.class));

        doReturn(new History()).when(historyRepository).save(any(History.class));

        BusinessException e =
                assertThrows(
                        BusinessException.class,
                        () -> {
                            challengeService.getSum(ConstantsTest.ONE, ConstantsTest.TEN, ConstantsTest.REQUEST);
                        });

        assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
        assertEquals("tenpo.forbidden", e.getMessage());

    }

    @Test
    void testSumError() {

        doReturn(Boolean.TRUE).when(sessionService).validateSession(any(HttpServletRequest.class));

        doAnswer(
                invocation -> {
                    throw new Exception();
                })
                .when(historyRepository)
                .save(any(History.class));

        BusinessException e =
                assertThrows(
                        BusinessException.class,
                        () -> {
                            challengeService.getSum(ConstantsTest.ONE, ConstantsTest.TEN, ConstantsTest.REQUEST);
                        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getHttpStatus());
        assertEquals("tenpo.server.error", e.getMessage());

    }


    @Test
    void testGtHistoriesSuccess() {

        doReturn(UtilsTest.getHistoriesPage()).when(historyRepository)
                .findAll(PageRequest.of(Integer.valueOf(ConstantsTest.ONE),
                        Integer.valueOf(ConstantsTest.TEN)));

        doReturn(new History()).when(historyRepository).save(any(History.class));

        HistoriesResponseDTO result = challengeService.getHistories(ConstantsTest.ONE, ConstantsTest.TEN, ConstantsTest.REQUEST);

        assertEquals("OK", result.getHistory().get(0).getMessage());
        assertEquals("login", result.getHistory().get(0).getEndpoint());
        assertEquals("error", result.getHistory().get(1).getMessage());
        assertEquals("sum", result.getHistory().get(1).getEndpoint());

    }

}
