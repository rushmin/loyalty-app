package org.wso2.sample.loyalty.servlet;

import com.nimbusds.jose.JWSObject;

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
            String encodedJWT = httpRequest.getHeader("X-JWT-Assertion".toLowerCase());

            try {
                JWSObject jwt = JWSObject.parse(encodedJWT);
                String user = (String) jwt.getPayload().toJSONObject().get("sub");
                httpRequest.getSession().setAttribute("user", user);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {

    }
}
