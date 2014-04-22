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
import javax.sql.*;
import javax.naming.*;

public class Authorise extends HttpServlet {

    private static final String CONSUMER_KEY = "TWITTER-CONSUMER-KEY";
    private static final String CONSUMER_SECRET = "TWITTER-CONSUMER-SECRET";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void destroy() {}

    public String getServletInfo() {
        return "Authorises application";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	JSONObject jres = new JSONObject();

        	
	try {
	    String token = request.getParameter("token");
	    String verifier = request.getParameter("verifier");
	    String tokensecret = request.getParameter("ts");

            Twitter twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	   
	    RequestToken requestToken = new RequestToken(token, tokensecret);

	    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

	    //
	    // Great we are authorised !!!!
	    //
	    System.out.println("GREAT! Looks Good!!");
	    System.out.println("Access Token: "+accessToken.getToken());
	    System.out.println("Access token Secret: "+accessToken.getTokenSecret());

	    //AccessToken Atoken = twitter.getOAuthAccessToken(verifier);
	    //RequestToken requestToken = twitter.getOAuthRequestToken();

	    //twitter.setOAuthAccessToken(Atoken);


	    // Use Tomcat connection pooling                 
            Context initContext = new InitialContext();
            DataSource ds = (DataSource)initContext.lookup("java:/comp/env/jdbc/selfiesdb");
            Connection con = ds.getConnection();


	    String q = "insert into TwitterDetails (AccessToken, AccessTokenSecret) values (?, ?)";
	    PreparedStatement pst = con.prepareStatement(q);
	    pst.setString(1, accessToken.getToken());
	    pst.setString(2, accessToken.getTokenSecret());
	    pst.execute();

	    q = "select LAST_INSERT_ID()";
	    pst = con.prepareStatement(q);
	    ResultSet rs = pst.executeQuery();
	    rs.next();
	    int twitterrecordid = rs.getInt(1);
	    con.close();
	    jres.put("twitterid", new Integer(twitterrecordid));
	    jres.put("iserr", "NO");
	
        }
        catch(Exception e) {
            e.printStackTrace();
	    jres.put("iserr", "YES");
	    jres.put("errortext", e.getMessage());
	}

	response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(jres);
	
        
    }

}
