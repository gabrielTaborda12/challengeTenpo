package com.tenpo.service;

import com.tenpo.dto.request.CreatUserRequestDTO;
import com.tenpo.dto.response.UserResponseDTO;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    void signUp(CreatUserRequestDTO newUser);
    UserResponseDTO loadUserByUsername(String userName,String pass, HttpServletRequest request);

}
