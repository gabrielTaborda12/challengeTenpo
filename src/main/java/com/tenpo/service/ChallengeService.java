package com.tenpo.service;

import com.tenpo.dto.response.HistoriesResponseDTO;
import com.tenpo.error.catalog.TenpoErrorCode;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface ChallengeService {

    BigDecimal getSum(String numberOne, String numberTwo,  HttpServletRequest request);

    HistoriesResponseDTO getHistories(String offset, String pageSize,HttpServletRequest request);

    void saveHistory(String endpoint, TenpoErrorCode error, String user);


}
