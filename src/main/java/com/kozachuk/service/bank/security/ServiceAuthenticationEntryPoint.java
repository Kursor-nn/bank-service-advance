package com.kozachuk.service.bank.security;

import com.kozachuk.service.bank.dto.ErrorResponse;
import com.kozachuk.service.bank.exceptions.AuthException;
import com.kozachuk.service.bank.exceptions.Errors;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component
public class ServiceAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        ErrorResponse errorResponse = new ErrorResponse(Errors.AUTH.getCode(), authEx.getMessage());
        writer.println(errorResponse);
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("Bank-Service");
        super.afterPropertiesSet();
    }
}
