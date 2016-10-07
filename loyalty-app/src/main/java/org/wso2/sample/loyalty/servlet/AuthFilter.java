package org.wso2.sample.loyalty.servlet;

import com.nimbusds.jose.JWSObject;
import org.wso2.sample.loyalty.APIClient;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;

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

                String encodedJWT = httpRequest.getHeader("X-JWT-Assertion".toLowerCase());

                try {
                    JWSObject jwt = JWSObject.parse(encodedJWT);
                    user = (String) jwt.getPayload().toJSONObject().get("sub");

                    if(user.contains("@carbon.super")){
                        user = user.replace("@carbon.super", "");
                    }

                    httpRequest.getSession().setAttribute("user", user);

                    APIClient apiClient = new APIClient();
                    apiClient.getAccessToken(encodedJWT);

                    httpRequest.getSession().setAttribute("apiClient", apiClient);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {

    }
}
