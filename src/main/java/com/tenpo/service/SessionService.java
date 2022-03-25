package com.tenpo.service;

import javax.servlet.http.HttpServletRequest;

public interface SessionService {

    void upSession(String user, HttpServletRequest request);
    String getUserSession(HttpServletRequest request);
    Boolean validateSession(HttpServletRequest request);
    void downSession(HttpServletRequest request);
}
