package org.wso2.sample.loyalty.servlet;

import com.nimbusds.jose.JWSObject;
import net.minidev.json.JSONObject;
import org.wso2.sample.loyalty.APIClient;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
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

                String encodedJWT = httpRequest.getHeader("X-JWT-Assertion".toLowerCase());

                try {
                    JWSObject jwt = JWSObject.parse(encodedJWT);
                    JSONObject parsedPayload = jwt.getPayload().toJSONObject();

                    user = (String) parsedPayload.get("sub");
                    if(user.contains("@carbon.super")){
                        user = user.replace("@carbon.super", "");
                    }
                    httpRequest.getSession().setAttribute("user", user);

                    String impersonatedBy = (String) parsedPayload.get("http://wso2.org/claims/impersonating_user");
                    if(impersonatedBy != null && impersonatedBy.contains("@carbon.super")){
                        impersonatedBy = impersonatedBy.replace("@carbon.super", "");
                    }
                    httpRequest.getSession().setAttribute("impersonatedBy", impersonatedBy);

                    String userDisplayName = user;
                    if(impersonatedBy != null) {
                        userDisplayName = String.format("%s (Impersonated By - %s)", userDisplayName, impersonatedBy);
                    }
                    httpRequest.getSession().setAttribute("userDisplayName", userDisplayName);


                    Map<String, String> configs = (Map<String, String>) servletRequest.getServletContext().getAttribute("loyalty.configs");
                    APIClient apiClient = new APIClient(configs);
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
