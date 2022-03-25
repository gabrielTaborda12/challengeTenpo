package com.tenpo.service.impl;

import com.tenpo.dto.request.CreatUserRequestDTO;
import com.tenpo.dto.response.UserResponseDTO;
import com.tenpo.entity.User;
import com.tenpo.error.catalog.TenpoErrorCode;
import com.tenpo.error.exception.BusinessException;
import com.tenpo.error.util.MessageHelper;
import com.tenpo.repository.UserRepository;
import com.tenpo.service.ChallengeService;
import com.tenpo.service.SessionService;
import com.tenpo.service.UserService;
import com.tenpo.utils.Constants;
import com.tenpo.utils.Utils;
import com.tenpo.utils.UtilsCrypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChallengeService challengeService;

    @Autowired
    SessionService sessionService;

    @Autowired
    MessageHelper messageHelper;

    @Override
    public void signUp(CreatUserRequestDTO newUser) {

        try{
            List listBadRequest = Utils.validateBadRequestUserUp(newUser);

            if(Objects.isNull(listBadRequest) || listBadRequest.isEmpty()) {
                this.createUser(newUser);
            }
            else{
                BusinessException exception = this.getBadRequest(listBadRequest);
                challengeService.saveHistory(Constants.ENDPOINT_SING_UP, TenpoErrorCode.PARAM_INVALID,null);
                LOGGER.error("Exception message: ({})",exception.getMessage());
                throw exception;
            }

        }catch (BusinessException be){
            LOGGER.error("Exception message: ({})",be.getMessage());
            throw be;
        }catch(Exception e){
            LOGGER.error("Exception message: ({})",e.getMessage());
            challengeService.saveHistory(Constants.ENDPOINT_SING_UP, TenpoErrorCode.INTERNAL_SERVER_ERROR, null);
            BusinessException be = new BusinessException(TenpoErrorCode.INTERNAL_SERVER_ERROR);
            throw be;
        }
    }

    @Override
    public UserResponseDTO loadUserByUsername(String userName,String pass, HttpServletRequest request) {
        try{
            Boolean isBadRequest = Utils.validateBadRequestLogin(userName, pass);
            if(!isBadRequest){
                User user = userRepository.findUserByUserNameAndPassword(userName, UtilsCrypto.encrypt(pass));
                this.validUser(user);
                sessionService.upSession(user.getUserName(), request);
                return buildResponse(user);
            }else{
                BusinessException exception = new BusinessException(TenpoErrorCode.PARAM_INVALID);
                challengeService.saveHistory(Constants.ENDPOINT_LOGIN, TenpoErrorCode.PARAM_INVALID, null);
                LOGGER.error("Exception message: ({})",exception.getMessage());
                throw exception;
            }

        }catch (BusinessException be){
            LOGGER.error("Exception message: ({})",be.getMessage());
            throw be;
        }
        catch (Exception e){
            LOGGER.error("Exception message: ({})",e.getMessage());
            challengeService.saveHistory(Constants.ENDPOINT_LOGIN, TenpoErrorCode.INTERNAL_SERVER_ERROR, null);
            BusinessException be = new BusinessException(TenpoErrorCode.INTERNAL_SERVER_ERROR);
            throw be;
        }
    }

    private void validUser(User user){
        if(Objects.isNull(user)){
            challengeService.saveHistory(Constants.ENDPOINT_LOGIN, TenpoErrorCode.NOT_FOUND_LOGIN, null);
            BusinessException e = new BusinessException(TenpoErrorCode.NOT_FOUND_LOGIN);
            throw e;
        }
    }

    private UserResponseDTO buildResponse(User user){
        try{
            UserResponseDTO response =  UserResponseDTO.builder()
                    .userName(user.getUserName())
                    .name(user.getName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .build();

            challengeService.saveHistory(Constants.ENDPOINT_LOGIN, null, user.getUserName());
            return response;
        }
        catch (Exception e){
           LOGGER.error("Exception message: ({})",e.getMessage());
            throw new BusinessException(TenpoErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void createUser(CreatUserRequestDTO newUser){
        try{
            User user = User.builder()
                    .userName(newUser.getUserName())
                    .name(newUser.getName())
                    .lastName(newUser.getLastName())
                    .email(newUser.getEmail())
                    .password(UtilsCrypto.encrypt(newUser.getPassword()))
                    .status(Constants.STATUS_ACTIVE)
                    .build();
            userRepository.save(user);
            challengeService.saveHistory(Constants.ENDPOINT_SING_UP, null,null);
        }
        catch (Exception e){
            challengeService.saveHistory(Constants.ENDPOINT_SING_UP, TenpoErrorCode.INTERNAL_SERVER_ERROR,null);
            throw new BusinessException(TenpoErrorCode.INTERNAL_SERVER_ERROR);
        }

    }

    private BusinessException getBadRequest(List<TenpoErrorCode> errorCodes) {
        BusinessException businessException =
                new BusinessException(TenpoErrorCode.PARAM_INVALID);
        String[] list = Utils.getListError(businessException, errorCodes, messageHelper);
        BusinessException badRequestException =
                new BusinessException(TenpoErrorCode.PARAM_INVALID, list, list);
        return badRequestException;
    }

}
