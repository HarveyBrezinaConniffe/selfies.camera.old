package com.conniffe.brezina.harvey;

import java.util.*;
import java.io.*;
import java.sql.*;
import net.sf.json.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.*;
import javax.naming.*;





public class GetList extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void destroy() {}

    public String getServletInfo() {
        return "Gets cam details";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONObject jres = new JSONObject();

        try {
	    // Use Tomcat connection pooling
	    Context initContext = new InitialContext();
	    DataSource ds = (DataSource)initContext.lookup("java:/comp/env/jdbc/selfiesdb");
	    Connection con = ds.getConnection();

            String q = "select DISTINCT SnapShotURL, HashTag from SelfieDetails;";
            PreparedStatement pst = con.prepareStatement(q);

            ResultSet rs = pst.executeQuery();
	    rs.next();
	    JSONArray cameras = new JSONArray();
	    while(rs.next()) {
		JSONObject camera = new JSONObject();
		camera.put("surl", rs.getString(1));
		camera.put("ht", rs.getString(2));
		cameras.add(camera);
	    }
	    jres.put("cameras", cameras);
            jres.put("iserr", "NO");
	    con.close();
	}
	catch(Exception e) {
	    e.printStackTrace();
	    jres.put("iserr", "YES");
	    jres.put("errmsg", e.getMessage());
	}
	response.setContentType("application/json; charset=UTF-8");
	PrintWriter out = response.getWriter();
	out.println(jres);



    }
}
