package com.conniffe.brezina.harvey;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import net.sf.json.*;
import java.net.*;
import twitter4j.*;
import twitter4j.auth.*;


public class GetURL extends HttpServlet {

    private static final String CONSUMER_KEY = "TWITTER-CONSUMER-KEY";
    private static final String CONSUMER_SECRET = "TWITTER-CONSUMER-SECRET";


    public void init(ServletConfig config) throws ServletException {
	super.init(config);
    }
    
    public void destroy() {}
    
    public String getServletInfo() {
	return "Gets authentication url";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	JSONObject jres = new JSONObject();

	try {
      	    Twitter twitter = new TwitterFactory().getInstance();
	    twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	    RequestToken requestToken = twitter.getOAuthRequestToken();
	    String tokensecret = requestToken.getTokenSecret();
	    jres.put("TS", tokensecret);
	    jres.put("URL", requestToken.getAuthorizationURL());
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
