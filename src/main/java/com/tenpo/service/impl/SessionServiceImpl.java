package com.tenpo.service.impl;

import com.tenpo.error.catalog.TenpoErrorCode;
import com.tenpo.error.exception.BusinessException;
import com.tenpo.service.SessionService;
import com.tenpo.utils.Constants;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Service
public class SessionServiceImpl implements SessionService {

    @Override
    public void upSession(String user ,HttpServletRequest request) {
        this.getSession(request).setAttribute(Constants.USER_NAME,user);
    }

    @Override
    public String getUserSession(HttpServletRequest request) {
        if(Objects.isNull(this.getSession(request).getAttribute(Constants.USER_NAME))){
            return null;
        }
        return this.getSession(request).getAttribute(Constants.USER_NAME).toString();
    }

    @Override
    public Boolean validateSession(HttpServletRequest request) {
        HttpSession mySession = this.getSession(request);
        return Objects.isNull(mySession.getAttribute(Constants.USER_NAME));
    }

    @Override
    public void downSession(HttpServletRequest request) {
        try{
            HttpSession mySession= this.getSession(request);
            mySession.invalidate();
        }catch (Exception e){
            throw new BusinessException(TenpoErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpSession getSession(HttpServletRequest request) {
        return request.getSession(true);
    }
}
