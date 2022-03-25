package com.tenpo.utils;

import com.tenpo.dto.request.CreatUserRequestDTO;
import com.tenpo.error.catalog.TenpoErrorCode;
import com.tenpo.error.exception.BusinessException;
import com.tenpo.error.util.MessageHelper;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDateTime;

public class Utils {

    public static List<TenpoErrorCode> validateBadRequestUserUp(CreatUserRequestDTO user){
        List<TenpoErrorCode> listMessage = new ArrayList<>();
        if(Objects.isNull(user)){
            listMessage.add(TenpoErrorCode.BAD_REQUEST_USER);
        }
        if(validateBadRequestLogin(user.getUserName(), user.getPassword())){
            listMessage.add(TenpoErrorCode.NOT_FOUND_LOGIN);
        }
        if(validateDefaultParam(user.getName())){
            listMessage.add(TenpoErrorCode.BAD_REQUEST_NAME);
        }
        if(validateDefaultParam(user.getLastName())){
            listMessage.add(TenpoErrorCode.BAD_REQUEST_LAST_NAME);
        }
        if(validateDefaultParam(user.getEmail())){
            listMessage.add(TenpoErrorCode.BAD_REQUEST_EMAIL);
        }
        return listMessage;
    }

    public static Boolean validateBadRequestLogin(String userName, String pass){
        return validateDefaultParam(userName) || validateDefaultParam(pass);

    }

    public static Boolean validateBadRequestSum(String numberOne, String numberTwo){
        return validateDefaultParam(numberOne) || validateIsNotNumber(numberOne) ||
                validateDefaultParam(numberTwo) || validateIsNotNumber(numberTwo);
    }

    public static String[] getListError(
            BusinessException e, List<TenpoErrorCode> errorCodes, MessageHelper messageHelper) {
        if (errorCodes == null || errorCodes.isEmpty()) {
            return e.getMessageArgs();
        } else {
            AtomicInteger index = new AtomicInteger();
            String[] messages = new String[errorCodes.size()];
            errorCodes.stream()
                    .forEach(
                            errorCode ->
                                    messages[index.getAndIncrement()] = getLtsString(errorCode, messageHelper));
            return messages;
        }
    }

    public static Date getDateNow(){
        LocalDateTime now = LocalDateTime.now();
        return Date.from(now.atZone(ZoneId.systemDefault())
                .toInstant());
    }

    private static String getLtsString(TenpoErrorCode errorCode, MessageHelper messageHelper){
        return messageHelper.resolve(errorCode.getProperties().getMessageId());
    }

    private static Boolean validateIsNotNumber(String number){
        return !number.matches("[0-9]+");
    }

    private static Boolean validateDefaultParam(String param){
        return Objects.isNull(param) || param.isEmpty();
    }

}
