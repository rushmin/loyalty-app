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
public class PurchasingServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int amount = Integer.parseInt(req.getParameter("amount"));

        int points = (amount / 100) * 10;

        String username = (String) req.getSession().getAttribute("user");

        APIClient apiClient = (APIClient) req.getSession().getAttribute("apiClient");
        apiClient.updatePoints(username, points);

        req.getRequestDispatcher("points.jsp").forward(req, resp);

    }
}
