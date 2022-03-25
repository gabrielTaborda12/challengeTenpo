package com.tenpo.controller;

import com.tenpo.dto.request.CreatUserRequestDTO;
import com.tenpo.dto.response.HistoriesResponseDTO;
import com.tenpo.dto.response.OperationsResponseDTO;
import com.tenpo.dto.response.UserResponseDTO;
import com.tenpo.service.SessionService;
import com.tenpo.service.UserService;
import com.tenpo.service.impl.ChallengeServiceImpl;
import com.tenpo.service.impl.UserServiceImpl;
import com.tenpo.utils.Constants;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;


@RestController
@Api("Contract Devices operations")
@Validated
public class ChallengeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChallengeController.class);

    @Autowired
    ChallengeServiceImpl challengeService;

    @Autowired
    UserService userService;

    @Autowired
    SessionService sessionService;

    @PostMapping(
            value = "/challenge/create_user",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signUp(
            @Valid @RequestBody CreatUserRequestDTO request) {

        LOGGER.info("singUp");

        userService.signUp(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping(
            value = "/challenge/login/{user_name}/{password}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> login(
            @PathVariable("user_name") String userName, @PathVariable("password") String pass, HttpServletRequest request) {

        LOGGER.info("login user: ({})", userName);

        UserResponseDTO user = userService.loadUserByUsername(userName,pass,request);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/challenge/logonOut")
    public  ResponseEntity<Void> signOff(HttpServletRequest request) {
        LOGGER.info("logonOut user: ({})", sessionService.getUserSession(request));
        sessionService.downSession(request);
        challengeService.saveHistory(Constants.ENDPOINT_SIGN_OFF, null, sessionService.getUserSession(request));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(
            value = "/challenge/sum",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OperationsResponseDTO> getSum(
            @RequestParam("number_one") String numberOne, @RequestParam("number_two") String numberTwo,
            HttpServletRequest request) {

        LOGGER.info("sum with : ({}) and ({})", numberOne, numberTwo);

        BigDecimal result = challengeService.getSum(numberOne, numberTwo, request);

        return ResponseEntity.ok(OperationsResponseDTO.builder()
                .result(result).build());
    }

    @GetMapping(
            value = "/challenge/history/{offset}/{pageSize}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HistoriesResponseDTO> getHistories(
            @PathVariable("offset") String offset,
            @PathVariable("pageSize") String pageSize ,HttpServletRequest request) {
        HistoriesResponseDTO response = challengeService.getHistories(offset, pageSize, request);

        return ResponseEntity.ok(response);
    }

}
