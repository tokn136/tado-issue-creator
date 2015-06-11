package demo;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Thomas on 6/11/2015.
 */
public class HttpConnector {

    private final static String USER_AGENT = "Mozilla/5.0";

    public static String sendGet(String url) throws Exception {
        return sendGet(url, "");
    }

    public static int sendGetResponseCodeOnly(String url, String authenticationString) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        if(!authenticationString.isEmpty()) {
            con.setRequestProperty("Authorization", "Basic " + authenticationString);
        }
        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        return con.getResponseCode();
    }

    public static String sendGet(String url, String authenticationString) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");
        if(!authenticationString.isEmpty()) {
            con.setRequestProperty("Authorization", "Basic " + authenticationString);
        }

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        return response.toString();

    }

    public static String sendPost(String url, String postPayload) throws Exception {
        return sendPost(url, postPayload, "");
    }

    public static String sendPost(String url, String postPayload, String authenticationString) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        if(!authenticationString.isEmpty()) {
            con.setRequestProperty("Authorization", "Basic " + authenticationString);
        }

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postPayload);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post payload : " + postPayload);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        return response.toString();
    }

    public static String sendPatch(String url, String postPayload, String authenticationString) throws Exception {
        // TODO JDK doesn't support PATCH, change to Apache

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("PATCH");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        if(!authenticationString.isEmpty()) {
            con.setRequestProperty("Authorization", "Basic " + authenticationString);
        }

        // Send patch request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postPayload);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'PATCH' request to URL : " + url);
        System.out.println("Patch payload : " + postPayload);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        return response.toString();
    }

    public static String getBasicAuthenticationString(String name, String password){
        String authString = name + ":" + password;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
        return authStringEnc;
    }

}
