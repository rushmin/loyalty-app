package org.wso2.sample.loyalty.servlet;

import org.wso2.sample.loyalty.APIClient;
import org.wso2.sample.loyalty.DataStore;

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

        DataStore dataStore = DataStore.getInstance();

        int currentPoints = dataStore.getPoints(username);

        int updatedPoints = currentPoints - points;

        if(updatedPoints < 0){
            updatedPoints = 0;
        }

        dataStore.updatePoints(username, updatedPoints);

        resp.sendRedirect("points.jsp");

    }
}
