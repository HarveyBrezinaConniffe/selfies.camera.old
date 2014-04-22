package com.conniffe.brezina.harvey;

import java.util.*;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import net.sf.json.*;
import java.net.*;
import twitter4j.*;
import twitter4j.auth.*;


public class SaveSelfieDetails extends HttpServlet {

    public static final String MYSQL_URL = "jdbc:mysql://localhost:3306/DATABASENAME";
    public static final String MYSQL_USER = "DATABASEUSER";
    public static final String MYSQL_PASSWORD = "DATABASEPASSWORD";
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    public void destroy() {}
    
    public String getServletInfo() {
        return "Saves selfie details";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
        JSONObject jres = new JSONObject();

	try {
	    String SnapShotURL = request.getParameter("SnapShotURL");
	    String HashTag = request.getParameter("HashTag");
	    String PhotoText = request.getParameter("PhotoText");
	    int TwitterId = Integer.parseInt(request.getParameter("TwitterId"));

	    Driver driver = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
            DriverManager.registerDriver(driver);
            Connection con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);

            String q = "insert into SelfieDetails (SnapShotURL, HashTag, PhotoText, TwitterId) values (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(q);
            pst.setString(1, SnapShotURL);
            pst.setString(2, HashTag);
	    pst.setString(3, PhotoText);
	    pst.setInt(4, TwitterId);

            pst.execute();

	    jres.put("iserr", "NO");
	}

	catch(Exception e) {
	    e.printStackTrace();
	    jres.put("iserr", "YES");
	    jres.put("errtext", e.getMessage());
	}

    response.setContentType("application/json; charset=UTF-8");
    PrintWriter out = response.getWriter();
    out.println(jres);

    }
}
