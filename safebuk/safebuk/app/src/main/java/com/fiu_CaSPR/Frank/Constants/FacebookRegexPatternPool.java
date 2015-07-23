package com.fiu_CaSPR.Frank.Constants;

import android.util.Log;

import com.fiu_CaSPR.Frank.DataStructures.FriendRequest;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fhu004 on 6/15/2015.
 */
public class FacebookRegexPatternPool {

    public static String accessToken = "";
    public static String cookie = "";
    public static String userName = "";
    public static String div = "";
    public static boolean processed = false;

    public static Elements elements = null;

    // Mobile HTML source
    public static String mobileHTML = "";

    // Find the style of the page
    public static Pattern style = Pattern.compile("<html[^*]*<body[^>]*[>]");

    // find id pattern in the friend div
    public static Pattern findIdPattern = Pattern.compile("hovercard=\"[^*].+?</a></div>");

    // Find user name, use on friend div
    public static Pattern findFriendRequestName = Pattern.compile("user.php.+?</a>");

    // find the location pattern in Overview
    public static Pattern findUniversityPattern = Pattern.compile("<div class=\"_c[^*].+?</a>");

    // In overview Pattern find lives in
    public static Pattern livesInPattern = Pattern.compile(("Lives in[^*].+?</a>"));

    // Finds the friend request Divs
    public static  Pattern r = Pattern.compile("<h2 class=\"_34e\">Respond to Your *[^*].+?</div></div></div></div></div></div>");

    // Finds friend Requests
    public static  Pattern friendRequestPattern = Pattern.compile("<div class=\"clearfix ruUserBox _3-z\".+?</div></div></div></div>");

    // Finds profile Picture Div:
    public static Pattern profilePicDivs = Pattern.compile("<div><a class=\"a[^*].+?Pr");

    // Finds Number of profile pics in the div stream
    public static Pattern numOfProfilePicsPat = Pattern.compile("<div class=\"_46-h _53f2\"");

    // Finds the lifeEvent Div
    public static Pattern lifeEventDiv = Pattern.compile("<ul class=\"uiList[^*]*Born");

    // Finds the number of LifeEvents in the lifeeventDiv
    public static Pattern lifeEvents = Pattern.compile("class=\"_3fo8");


    public static ArrayList<String> friendNames = new ArrayList<>();
    public static HashMap<String,String> friendsHashmap = new HashMap<>();

    public static ArrayList<FriendRequest> friendRequestList = new ArrayList<>();
    public static FriendRequest currentFriendRequest = new FriendRequest();
    public static String styleDiv = "";

    public static void findNames(String friendRequestsHtmlSource){

        final Matcher findName = findFriendRequestName.matcher(friendRequestsHtmlSource);
        final Matcher findIdMatcher = FacebookRegexPatternPool.findIdPattern.matcher(friendRequestsHtmlSource);
        StringBuilder builder = new StringBuilder();
        while(findName.find()){
            String[] friendSourceSplit = findName.group().split(">");
            for(int i =0;i < friendSourceSplit[1].length(); ++i){
                if(friendSourceSplit[1].toCharArray()[i] == '<'){
                    break;
                }
                builder.append(friendSourceSplit[1].toCharArray()[i]);
            }
           // Log.i("Friend Name : ", friendName);
            if(!friendNames.contains(builder.toString())) {
                friendNames.add(builder.toString());
                currentFriendRequest.setName(builder.toString());
            }else break;
            builder = new StringBuilder();
            if(findIdMatcher.find()){
                char[] sCut = findIdMatcher.group().substring(39,findIdMatcher.group().length()-7).toCharArray();
                for(int i =0;i< sCut.length; ++i){
                    if(sCut[i] == '\"'){
                        break;
                    }
                    builder.append(sCut[i]);
                }
                //friendsHashmap.put(friendName,id);
                currentFriendRequest.setId(builder.toString());
            }
        }
    }


    // Finds university and current location
    public static void processCurrentFriendRequestOverview(String cookie){
        String url = "https://www.facebook.com/" +currentFriendRequest.getId() ;
        try {
            Connection.Response response = Jsoup.connect(url).cookie(url, cookie).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            url = response.url() +"/about";
            response = Jsoup.connect(url).cookie(url, cookie).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            final Matcher match1 = findUniversityPattern.matcher(response.body());
            final Matcher match2 = livesInPattern.matcher(response.body());
            // l stands for location
            if(match1.find()) {
                String[] l = match1.group().split(">");
                l[l.length - 1].substring(0, l[l.length - 1].length() - 3);
                Log.i("regexPattern: " , " livesIn" + l[l.length - 1].substring(0, l[l.length - 1].length() - 3));
            }else currentFriendRequest.hasInfo(false);

            if(match2.find()){
                String[] l = match1.group().split(">");
                l[l.length - 1].substring(0, l[l.length - 1].length() - 3);
                Log.i("regexPattern: " , " findUniversity" + l[l.length - 1].substring(0, l[l.length - 1].length() - 3));
            }else currentFriendRequest.hasInfo(false);

            Log.i("After overview process", currentFriendRequest.getDangerScore());
        }catch(Exception e){

        }
    }


    public static void processCurrentFriendRequestPhotos(String cookie){
        String url = "https://www.facebook.com/" +currentFriendRequest.getId();
        try {
            Connection.Response response = Jsoup.connect(url).cookie(url, cookie).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            url = response.url()  +"/photos_albums";
            response = Jsoup.connect(url).cookie(url, cookie).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            final Matcher match = profilePicDivs.matcher(response.body());
            int numOfPics = 0;
            if(match.find()){
                final Matcher profilePicMatcher = numOfProfilePicsPat.matcher(match.group());
                while(profilePicMatcher.find())
                    numOfPics += 1;
            }
            currentFriendRequest.setPhotoNumber(numOfPics);
            Log.i("After profile pics", currentFriendRequest.getDangerScore());
        }catch(Exception e){
            Log.e("Error: ", e.toString());
        }
    }

    public static void processCurrentFriendRequestLifeEvents(String cookie){
        String url = "https://www.facebook.com/" +currentFriendRequest.getId() ;
        try{
            Connection.Response response = Jsoup.connect(url).cookie(url, cookie).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            url = response.url()+"/about?section=year-overviews&pnref=about";
            response = Jsoup.connect(url).cookie(url, cookie).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            int numOfLifeEvents = 0;
            final Matcher match = lifeEventDiv.matcher(response.body());
            if(match.find()){
                final Matcher match2 = lifeEvents.matcher(match.group());
                while(match2.find()){
                    numOfLifeEvents+=1;
                }
            }
            currentFriendRequest.numberOfLifeEvents(numOfLifeEvents);
            Log.i("After life events", currentFriendRequest.getDangerScore());
        }catch(Exception e){
            Log.e("Error: ", e.toString());
        }
    }

}
