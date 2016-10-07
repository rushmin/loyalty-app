package org.wso2.sample.loyalty.servlet;

import org.wso2.sample.loyalty.APIClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by rushmin on 10/7/16.
 */
public class RedeemingServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int points = Integer.parseInt(req.getParameter("amount"));

        String username = (String) req.getSession().getAttribute("user");

        APIClient apiClient = (APIClient) req.getSession().getAttribute("apiClient");

        int currentPoints = apiClient.getPoints(username);

        int updatedPoints = currentPoints - points;

        if(updatedPoints < 0){
            updatedPoints = 0;
        }

        apiClient.updatePoints(username, updatedPoints);

        req.getRequestDispatcher("points.jsp").forward(req, resp);

    }
}
