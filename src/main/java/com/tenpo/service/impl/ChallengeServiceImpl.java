package com.tenpo.service.impl;

import com.tenpo.dto.response.HistoriesResponseDTO;
import com.tenpo.dto.response.HistoryResponseDTO;
import com.tenpo.entity.History;
import com.tenpo.error.catalog.TenpoErrorCode;
import com.tenpo.error.exception.BusinessException;
import com.tenpo.repository.HistoryRepository;
import com.tenpo.service.ChallengeService;
import com.tenpo.service.SessionService;
import com.tenpo.utils.Constants;
import com.tenpo.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChallengeServiceImpl.class);

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    SessionService sessionService;

    @Override
    public BigDecimal getSum(String numberOne, String numberTwo, HttpServletRequest request) {

        try{
           this.checkForbidden(request);

            if(Utils.validateBadRequestSum(numberOne, numberTwo)){
                throw getException(Constants.ENDPOINT_SUM,
                        TenpoErrorCode.PARAM_INVALID, sessionService.getUserSession(request));
            }

            BigDecimal firstNumber = new BigDecimal(numberOne);
            BigDecimal secondNumber = new BigDecimal(numberTwo);
            BigDecimal calculation = firstNumber.add(secondNumber);
            this.saveHistory(Constants.ENDPOINT_SUM, null, sessionService.getUserSession(request));
            return calculation;

        }catch(BusinessException be){
            LOGGER.error("Exception message: ({})",be.getMessage());
            throw be;
        }
        catch(Exception e){
            LOGGER.error("Exception message: ({})",e.getMessage());
            throw new BusinessException(TenpoErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public HistoriesResponseDTO getHistories(String offset, String pageSize, HttpServletRequest request) {
        try {
            //validar number
            Page<History> history = historyRepository.findAll(PageRequest.of(Integer.valueOf(offset), Integer.valueOf(pageSize)));
            List<HistoryResponseDTO> histories = history.stream().map(h -> getHistory(h)).collect(Collectors.toList());
            this.saveHistory(Constants.ENDPOINT_GET_HISTORIES, null, sessionService.getUserSession(request));
            return HistoriesResponseDTO.builder().history(histories).build();
        }
        catch(Exception e){
            LOGGER.error("Exception message: ({})",e.getMessage());
            throw new BusinessException(TenpoErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void saveHistory(String endpoint, TenpoErrorCode error, String user) {
        History history = History.builder()
                .endpoint(endpoint)
                .dateConsumer(Utils.getDateNow())
                .https(getHttps(error))
                .message(getMessageHttps(error))
                .consumer(getConsumer(user))
                .build();
        historyRepository.save(history);
    }

    private void checkForbidden(HttpServletRequest request){
        if(sessionService.validateSession(request)){
            throw getException(Constants.ENDPOINT_SUM, TenpoErrorCode.FORBIDDEN, null);
        }
    }

    private BusinessException getException(String endpoint, TenpoErrorCode errorCode, String user){
        BusinessException exception = new BusinessException(errorCode);
        this.saveHistory(endpoint, errorCode, null);
        LOGGER.error("Exception message: ({})",exception.getMessage());
        return exception;
    }

    private String getHttps(TenpoErrorCode errorCode){
        return Objects.isNull(errorCode) ? Constants.RESPONSE_OK_NO_CONTENT :
                String.valueOf(errorCode.getProperties().getHttpCode().unwrapType().value());
    }

    private String getMessageHttps(TenpoErrorCode errorCode){
        return Objects.isNull(errorCode) ? Constants.RESPONSE_OK :
                errorCode.getProperties().getMessageId();
    }

    private String getConsumer(String user){
        return Objects.isNull(user) ? Constants.USER_NOT_LOGGED : user;
    }

    private HistoryResponseDTO getHistory(History history){
        return HistoryResponseDTO.builder()
                .id_record(history.getId().intValue())
                .date(history.getDateConsumer())
                .endpoint(history.getEndpoint())
                .https(history.getHttps())
                .message(history.getMessage())
                .build();
    }

}
