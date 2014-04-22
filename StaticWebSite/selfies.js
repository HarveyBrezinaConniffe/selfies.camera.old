var SnapShotURL;
var HashTag;
var TwitterId;
var PhotoText;
var oauth_completed;
$(document).ready(function() {
        // PAGE HAS LOADED
        $("#submitbutton").bind("click", submitButtonClicked);
        var oauth_token = getCGIParameter("oauth_token");
	var oauth_verifier = getCGIParameter("oauth_verifier");
	oauth_completed = getCGIParameter("oauth_completed");

	//
	// These variables will be in the URL **IF** the 
	// person is returning from Twitter oauth
	//
	if (oauth_token != "" && oauth_verifier != "") {
	    // Just arrived back from Twitter
	    // Now we need to validate the Access details (another AJAX Servlet call)

	   
	    // Restore the data entry from the cookies 
	    $("#cameraaddress").val(getCookie("cameraaddress"));
	    $("#hashtag").val(getCookie("hashtag"));
	    $("#photoheading").val(getCookie("photoheading"));

	    // Show the Spinning loader
	    showSpinningLoader();
	    

	    var stufftosend = new Object();
	    stufftosend.token = oauth_token;
	    stufftosend.verifier = oauth_verifier;
	    var ts = getCookie("ts");
	    stufftosend.ts = ts;
	    
	    $.ajax({
                type: 'GET',
                url: '/Authorise',
                context: $('body'),
                dataType: 'json',
                data: stufftosend,
                success: function(json) {
		    if (json.iserr == "Y") {
			alert("Error!  "+json.errortext);
			}
		    else {
			// All is OK!
                    	//alert("authorised - hopefully.  Twitter Id is "+json.twitterid);
			//Reload our page
                        setCookie("TwitterId", json.twitterid);
                        
			location.href = "./index.html?oauth_completed=YES";
			
		    }
                },
                error: function(jqxhr, textstatus, errorthrown) {
                    alert("Oh oh - something went wrong!.  The error is: "+errorthrown);
                }
            });
	}
	else if (oauth_completed != "") {
		// We are being called back by ourselves and we [think] oauth
		// has been successful

	    	// Restore the data-entry from the cookies
	        $("#cameraaddress").val(getCookie("cameraaddress"));
	        $("#hashtag").val(getCookie("hashtag"));
	        $("#photoheading").val(getCookie("photoheading"));

		// change text to "connected to twitter"
	        $("#connectedtotwittertext").html("<span style=\"color: #97fd9f;\">CONNECTED TO TWITTER ACCOUNT</span>");

	        // change Twitter to green image
	        $("#twitterimg").attr("src", "./twitter_connect-green.png");
	
		
		
		}
        else {
		//
		// We are **NOT** being called back from any other pages
		//

		// Make the Twitter oauth image clickable
		$("#twitterimg").bind("click", twitterImageClicked);
	}
   
	// Allow the About button to be click 
	$("#aboutbutton").bind("click", aboutButtonClicked);
});

function showSpinningLoader() {
	// Show Spinning Loading Thing here
	var opts = {
	  lines: 13, // The number of lines to draw
	  length: 80, // The length of each line
	  width: 40, // The line thickness
	  radius: 80, // The radius of the inner circle
	  corners: 1, // Corner roundness (0..1)
	  rotate: 0, // The rotation offset
	  direction: 1, // 1: clockwise, -1: counterclockwise
	  color: '#000', // #rgb or #rrggbb or array of colors
	  speed: 2, // Rounds per second
	  trail: 60, // Afterglow percentage
	  shadow: true, // Whether to render a shadow
	  hwaccel: false, // Whether to use hardware acceleration
	  className: 'spinner', // The CSS class to assign to the spinner
	  zIndex: 2e9, // The z-index (defaults to 2000000000)
	  top: 'auto', // Top position relative to parent in px
	  left: 'auto' // Left position relative to parent in px
	};
	$("#loadingdiv").show();
	var target = document.getElementById('loadingdiv');
	var spinner = new Spinner(opts).spin(target);
}
	

function submitButtonClicked() {
    // Validation - check they entered everything and that they are connected to twitter
    showSpinningLoader();
    //alert("click");
    if(oauth_completed != "YES") {
	alert("Please connect to twitter");
    }
    else {
	PhotoText = $('#photoheading').val();
        //alert(PhotoText);
        HashTag = $('#hashtag').val();
        SnapShotURL = $('#caddress').val();

	var stufftosend = new Object();
        stufftosend.PhotoText = PhotoText;
	stufftosend.HashTag = HashTag;
	stufftosend.TwitterId = getCookie("TwitterId");
	stufftosend.SnapShotURL = SnapShotURL;
	//alert(JSON.stringify(stufftosend));

        $.ajax({
	    type: 'GET',
            url: '/SaveSelfieDetails',
            context: $('body'),
            dataType: 'json',
            data: stufftosend,
            success: function(json) {
		alert("success - we have added your details to our database - nice page here really soon");
	    }
	});
    }
    
};

function twitterImageClicked() {

	// Show the spinning loader
	showSpinningLoader();

	// validation here

	// Store their camera address, hashtag & photo heading text
	setCookie("cameraaddress", $("#cameraaddress").val());
	setCookie("hashtag", $("#hashtag").val());
	setCookie("photoheading", $("#photoheading").val());

	var stufftosend = new Object();
        $.ajax({
                type: 'GET',
                url: '/GetURL',
                context: $('body'),
                dataType: 'json',
                data: stufftosend,
                success: function(json) {
                    if (json.iserr == 'YES') {
                        alert("Sorry, there has been an error: "+json.errortext);
                    }
                    else {

                        var url = json.URL;
			var ts = json.TS;
			setCookie("ts", ts);
			location.href = url;
                    }
                },
		error: function(jqxhr, textstatus, errorthrown) {
                    alert("Oh oh - something went wrong!.  The error is: "+errorthrown);
                }
	});
        
        
}

function aboutButtonClicked() {
	alert("Show About Popup Page Here");
	}




function getCGIParameter(name) {
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regexS = "[\\?&]" + name + "=([^&#]*)";
        var regex = new RegExp(regexS);
        var results = regex.exec(window.location.href);
        if (results == null) {
                return "";
                }
        else {
                return decodeURIComponent(results[1].replace(/\+/g, " "));
                }
        }

function getCookie(c_name) {
        var i,x,y,ARRcookies=document.cookie.split(";");
        for (i=0;i<ARRcookies.length;i++) {
                x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
                y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
                x=x.replace(/^\s+|\s+$/g,"");
                if (x==c_name) {
                        return unescape(y);
                        }
                }
        }

function setCookie(cname, cvalue) {
        var exdays = 3650;
        var exdate=new Date();
        exdate.setDate(exdate.getDate() + exdays);
        var cvalueWithExpiry = escape(cvalue) + "; expires="+exdate.toUTCString();
        document.cookie= cname + "=" + cvalueWithExpiry;
        }

	
