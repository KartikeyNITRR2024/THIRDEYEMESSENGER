package com.thirdeye.thirdeyemessenger.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.thirdeye.thirdeyemessenger.annotation.AdminRequired;
import com.thirdeye.thirdeyemessenger.externalcontrollers.ThirdEye_Purse_Connection;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);
    
    @Autowired
    ThirdEye_Purse_Connection thirdEye_Purse_Connection;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tokenString = request.getHeader("token");

        if (tokenString == null || tokenString.isEmpty()) {
            throw new Exception("Token Not Found");
        }
        
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethodAnnotation(AdminRequired.class) != null) {
            	logger.info("Going to authorize for admin user");
            	try {
            	   thirdEye_Purse_Connection.authAdminUser(tokenString);
            	} catch (Exception ex)
            	{
            		throw new Exception(ex.getMessage());
            	}
            }
            else
            {
            	logger.info("Going to authorize for nonadmin user");
            	try {
            	   thirdEye_Purse_Connection.authNonAdminUser(tokenString);
            	} catch (Exception ex)
            	{
            		throw new Exception(ex.getMessage());
            	}
            }
        }
        
        return true;
    }

}
