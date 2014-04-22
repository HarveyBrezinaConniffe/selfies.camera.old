package com.conniffe.brezina.harvey;

import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.conf.*;
import java.awt.Image;
import java.awt.image.*;
import java.net.URL;
import java.sql.*;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.io.*;
import java.util.*;
import java.text.*;
import java.awt.*;

public final class main {

    public static final String MYSQL_URL = "jdbc:mysql://localhost:3306/DATABASENAME";
    public static final String MYSQL_USER = "DATABASEUSER";
    public static final String MYSQL_PASSWORD = "DATABASEPASSWORD";

    public static final String RCURL = "http://89.101.225.158:8105/onvif/snapshot";
    public static final DateFormat DF = new SimpleDateFormat("EEE MMM d h:mm a yyyy");
    public static final String DATETIMEFONT = "./harveyfont.ttf";
    public static final String CREDITFONT = "./harveyfont2.ttf";
    public static final String TWEETFONT = "./harveyfont2.ttf";

	
    private String[] hashtags;
    private FilterQuery fq;
    private TwitterStream twitterStream;
    private StatusListener listener;

    public main() {
    }

    private File getpicture(String surl, String text, String tweettext, String cword) throws Exception {

	    Font hfont = loadFont();
	    Font cfont = loadcreditsFont();
	    Font tfont = loadTweetFont();

	    BufferedImage img = null;

	    URL url = new URL(surl);
	    img = ImageIO.read(url);
	    
	    BufferedImage logo = ImageIO.read(new File("./logo.png"));
	  
	    BufferedImage si = ImageIO.read(new File("./shamrock.png"));
	    
	    BufferedImage combinedImage = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
	    
	    Graphics g = combinedImage.getGraphics();



	    /*
	    if(cword.toLowerCase().equals("#paddyscam") || cword.toLowerCase().equals("#remembrancecam") || cword.toLowerCase().equals("#paddycam")) {
		BufferedImage spdt = ImageIO.read(new File("./spt.png"));
		g.drawImage(img, 0, 0, null);
		g.drawImage(spdt, (img.getWidth()/2)-(spdt.getWidth()/2), 10, null);
		
		g.setColor(Color.GREEN);
		
		g.setFont(hfont);
		g.drawString(DF.format(new java.util.Date()),10,90);
		g.setFont(cfont);
		g.drawString("Made using http://selfies.ever.camera writen by Harvey Brezina Conniffe",10,170);
		
		g.drawImage(si,510,80,null);

		g.setFont(hfont);
		g.drawString(text,10,130);

		g.setFont(tfont);
		g.setColor(Color.RED);
		g.drawString(tweettext, 10, img.getHeight()-50);
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.GREEN);
		float thickness = 5;
		Stroke oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(thickness));
		g2.drawRect(0, 0, img.getWidth(), img.getHeight());
		g2.setStroke(oldStroke);
		g = (Graphics) g2;
	    }
	    */




	    //else {
	    g.drawImage(img, 0, 0, null);
	    g.drawImage(logo, 10, 10, null);
	    //g.setColor(Color.BLACK);
	    g.setColor(Color.CYAN);
	    //g.setFont(new Font("Jazz LET", Font.PLAIN, 40)); 
	    g.setFont(hfont);
	    g.drawString(DF.format(new java.util.Date()),10,90);
	    g.setFont(cfont);
	    g.drawString("Made using http://selfies.ever.camera writen by Harvey Brezina Conniffe",10,170);
		
	    g.setFont(hfont);
	    g.drawString(text,10,130);
		
	    g.setFont(tfont);
	    g.setColor(Color.RED);
	    g.drawString(tweettext, 10, img.getHeight()-50);
	    //}
	    String currentuuid = UUID.randomUUID().toString();
	    
	    File outputfile = new File("/tmp/"+currentuuid+".png");
	    
	    ImageIO.write(combinedImage, "png", outputfile);

	    return outputfile;
    }


    private Font loadFont() throws Exception {
	URL fontUrl = new URL("http://www.webpagepublicity.com/" + "free-fonts/a/Airacobra%20Condensed.ttf");
	File f = new File(DATETIMEFONT);
        Font font = Font.createFont(Font.TRUETYPE_FONT, f);
	    //g.setFont(new Font("Jazz LET", Font.PLAIN, 40)); 
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	//font.setStyle(Font.PLAIN);
        ge.registerFont(font);
	font = font.deriveFont(30f);
	return font;
    }

    private Font loadcreditsFont() throws Exception {
        URL fontUrl = new URL("http://www.webpagepublicity.com/" + "free-fonts/a/Airacobra%20Condensed.ttf");
        File f = new File(CREDITFONT);
        Font font = Font.createFont(Font.TRUETYPE_FONT, f);
	//g.setFont(new Font("Jazz LET", Font.PLAIN, 34));                                                                                                   
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //font.setStyle(Font.PLAIN);                                                                                                                             
        ge.registerFont(font);
        font = font.deriveFont(16f);
        return font;
    }

    private Font loadTweetFont() throws Exception {
        URL fontUrl = new URL("http://www.webpagepublicity.com/" + "free-fonts/a/Airacobra%20Condensed.ttf");
        File f = new File(TWEETFONT);
        Font font = Font.createFont(Font.TRUETYPE_FONT, f);
	//g.setFont(new Font("Jazz LET", Font.PLAIN, 40));                                                                                                   
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //font.setStyle(Font.PLAIN);                                                                                                                             
        ge.registerFont(font);
        font = font.deriveFont(18f);
        return font;
    }

    private void load() throws Exception {
	System.out.println("Loading/Reloading now");
	// First thing - Get our database connection                                                                                                                                 
        Driver driver = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
        DriverManager.registerDriver(driver);
        Connection con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
	
        // Get a count of the amount of hashtags                                                                                                                                     
        String q = "select count(DISTINCT HashTag) from SelfieDetails";
        PreparedStatement pst = con.prepareStatement(q);
        ResultSet rs = pst.executeQuery();
        rs.next();
        int numHashtags = rs.getInt(1);
        rs.close();
        pst.close();
	
        // Our array for our hashtags                                                                                                                                                
        hashtags = new String[numHashtags];
	
        q = "select DISTINCT HashTag from SelfieDetails";
        pst = con.prepareStatement(q);
        rs = pst.executeQuery();
        int poscounter = 0;
        while (rs.next()) {
            //String hashtag = "#"+rs.getString(1);                                                                                                                                  
            String hashtag = rs.getString(1);
            hashtags[poscounter] = hashtag;
            poscounter++;
            System.out.println("Listening for Hashtag "+hashtag);
	    
        }
        rs.close();
        pst.close();
    }


    private void startTwitterFilter() {
        fq = new FilterQuery();
        twitterStream = new TwitterStreamFactory().getInstance();
	fq.track(hashtags);
        twitterStream.addListener(listener);
        twitterStream.filter(fq);
	}

	//Twitter twitter = TwitterFactory.getSingleton();
	//twitterStream.addListener(listener);

    /**
     * Main entry of this application.
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {

	main m = new main();
	m.load(); // Loads hashtags from Database
	m.initListener(); // Make the Twitter Listener
	m.startTwitterFilter(); // Start the Filter

    }
	

     private void initListener() {


	//loader l = new loader();
	//l.start();

	listener = new StatusListener() {
		@Override
		public void onStatus(Status status) {
		    try {
			for(int i = 0;i<hashtags.length;i++) {
			    String cword = hashtags[i];
			    if(status.getText().indexOf(cword) != -1) {
				Driver driver2 = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
				DriverManager.registerDriver(driver2);
        			Connection con2 = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
				
				String q2 = "select SnapShotURL,PhotoText,TwitterId from SelfieDetails where Hashtag = ?";
				PreparedStatement pst2 = con2.prepareStatement(q2);
				pst2.setString(1, cword);
				ResultSet rs2 = pst2.executeQuery();
				rs2.next();
				String curl = rs2.getString(1);
				String text = rs2.getString(2); 
				int id = rs2.getInt(3);
				rs2.close();
				pst2.close();

				String tweettext = "@"+status.getUser().getScreenName()+": "+status.getText();

				/**
				if (cword.toLowerCase().equals("#irelandsxsw")) {
					tweettext = "Hi @"+status.getUser().getScreenName()+" here's your live picture from Evercam @ SXSW 2014";
					}
				**/
				
				File pic = getpicture(curl,text,tweettext, cword);
				
				q2 = "select AccessToken,AccessTokenSecret from TwitterDetails where Id = "+id;
				pst2 = con2.prepareStatement(q2);
				rs2 = pst2.executeQuery();
				rs2.next();
				String at = rs2.getString(1);
				String ats = rs2.getString(2);
				rs2.close();
				pst2.close();
				String[] tvals = new String[2];
				tvals[0] = at;
				tvals[1] = ats;
				
				post(status.getUser().getScreenName(), cword, pic, tvals);
			    }
			}
		    }
		    catch(Exception e) {
			e.printStackTrace();
		    }

		}

		@Override
		    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		    //       System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
		}

            @Override
		public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                //    System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
		public void onScrubGeo(long userId, long upToStatusId) {
                //    System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }
		
	    @Override
		public void onStallWarning(StallWarning warning) {
                //    System.out.println("Got stall warning:" + warning);
            }

            @Override
		public void onException(Exception ex) {
                //    ex.printStackTrace();
            }


	    };
	

    }



	private void post(String twitterposter, String hashtag, File image, String[] twittervalues) throws Exception {
	    Twitter twitter2 = TwitterFactory.getSingleton();

	    AccessToken ac = new AccessToken(twittervalues[0], twittervalues[1]);
	    twitter2.setOAuthAccessToken(ac);

	    String tweettext = "Hey @"+twitterposter+", Your photo instantly generated with @evrcm http://selfies.ever.camera";

 	    if (hashtag.toLowerCase().equals("#irelandsxsw")) {
	    	tweettext = "Hi @"+twitterposter+" here's your live picture from Evercam @ SXSW 2014";
	    }

	    //if (hashtag.toLowerCase().equals("#paddyscam") || hashtag.toLowerCase().equals("#remembrancecam")) {
	    //	tweettext = "Hi @"+twitterposter+". Here is your St. Patrick's day photo!!";
	    //}

	    StatusUpdate message = new StatusUpdate(tweettext);

	    message.media(image);

	    twitter2.updateStatus(message);
		
		
		
		};



}
