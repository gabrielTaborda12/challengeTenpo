package com.tenpo.utils;

import com.tenpo.dto.request.CreatUserRequestDTO;
import com.tenpo.dto.response.HistoriesResponseDTO;
import com.tenpo.dto.response.HistoryResponseDTO;
import com.tenpo.dto.response.UserResponseDTO;
import com.tenpo.entity.History;
import com.tenpo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

public class UtilsTest {

    public static UserResponseDTO buildUserLogin(){
        return UserResponseDTO.builder()
                .name(ConstantsTest.NAME)
                .lastName(ConstantsTest.LAST_NAME)
                .userName(ConstantsTest.USER_NAME)
                .email(ConstantsTest.USER_EMAIL)
                .build();
    }

    public static HistoriesResponseDTO getHistories(){
        List<HistoryResponseDTO> histories = new ArrayList<>();
        histories.add(HistoryResponseDTO.builder().id_record(1)
                .message("OK").endpoint("login").build());
        histories.add(HistoryResponseDTO.builder().id_record(1)
                .message("error").endpoint("sum").build());
        return HistoriesResponseDTO.builder()
                .history(histories).build();
    }

    public static Page<History> getHistoriesPage(){
        List<History> listHistories = new ArrayList<>();
        listHistories.add(History.builder().
                endpoint("login")
                .message("OK")
                .id(1L).build());
        listHistories.add(History.builder().
                endpoint("sum")
                .message("error")
                .id(1L).build());
        Page<History> historiesPage = new PageImpl<>(listHistories);
        return historiesPage;
    }

    public static CreatUserRequestDTO buildNewUser(){
        return CreatUserRequestDTO.builder()
                .name(ConstantsTest.NAME)
                .lastName(ConstantsTest.LAST_NAME)
                .userName(ConstantsTest.USER_NAME)
                .email(ConstantsTest.USER_EMAIL)
                .password(ConstantsTest.PW)
                .build();
    }

    public static User buildUser(){
        return User.builder()
                .name(ConstantsTest.NAME)
                .lastName(ConstantsTest.LAST_NAME)
                .userName(ConstantsTest.USER_NAME)
                .email(ConstantsTest.USER_EMAIL)
                .build();
    }

}
