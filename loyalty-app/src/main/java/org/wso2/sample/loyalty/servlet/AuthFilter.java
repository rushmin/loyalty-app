package org.wso2.sample.loyalty.servlet;

import com.nimbusds.jose.JWSObject;
import net.minidev.json.JSONObject;
import org.wso2.sample.loyalty.APIClient;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Authentication / Authorization filter
 */
public class AuthFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if(servletRequest instanceof HttpServletRequest){

            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            String user = (String) httpRequest.getSession().getAttribute("user");

            if(user == null){

                String encodedJWTHeaderValue = httpRequest.getHeader("x-jwt-assertion");

                System.out.println("encodedJWTHeaderValue  " + encodedJWTHeaderValue );

                if(encodedJWTHeaderValue != null){

                    try {
                        JWSObject jwt = JWSObject.parse(encodedJWTHeaderValue);
                        JSONObject parsedPayload = jwt.getPayload().toJSONObject();

                        System.out.println("parsedPayload " + parsedPayload.toJSONString());

                        user = (String) parsedPayload.get("Subject");
                        if(user == null){
                            handleUnauthenticatedRequest(servletResponse);
                        }else{
                            user = user.replace("IS-WSO2.COM/", "");
                            if(user.contains("@carbon.super")){
                                user = user.replace("@carbon.super", "");
                            }
                            httpRequest.getSession().setAttribute("user", user);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        handleUnauthenticatedRequest(servletResponse);
                    }
                }else {
                    handleUnauthenticatedRequest(servletResponse);
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void handleUnauthenticatedRequest(ServletResponse servletResponse){
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        try {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Failed to authenticate the user");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void destroy() {

    }
}
