package main.dogappandroid;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NetworkUtils {
    private static final String REGISTER_URL = "http://10.0.2.2:9000/register";
    private static final String LOGIN_URL = "http://10.0.2.2:9000/login";

    static String register(Map<String, String> queryParams) {
        String urlParams = "email=" + queryParams.get("email") + "&";
        urlParams += "password=" + queryParams.get("password") + "&";
        urlParams += "firstName=" + queryParams.get("firstName") + "&";
        urlParams += "lastName=" + queryParams.get("lastName") + "&";
        urlParams += "address=" + queryParams.get("address") + "&";
        urlParams += "subdistrict=" + queryParams.get("subdistrict") + "&";
        urlParams += "district=" + queryParams.get("district") + "&";
        urlParams += "province=" + queryParams.get("province") + "&";
        urlParams += "profilePicture=" + queryParams.get("profilePicture") + "&";
        urlParams += "phone=" + queryParams.get("phone") + "&";
        urlParams += "forgotQuestion=" + queryParams.get("forgotQuestion") + "&";
        urlParams += "forgotAnswer=" + queryParams.get("forgotAnswer");
        byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;
        String responseFromRequest = null;

        try {
            URL requestURL = new URL(REGISTER_URL);
            httpConnection = (HttpURLConnection) requestURL.openConnection();

            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            wr.write(postData);

            StringBuilder contentBuilder;
            reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            String line;
            contentBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
                contentBuilder.append("\n");
            }
            if (contentBuilder.length() == 0) {
                return null;
            }
            responseFromRequest = contentBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseFromRequest;
    }

    static String login(Map<String, String> queryParams) {
        String urlParams = "email=" + queryParams.get("email") + "&";
        urlParams += "password=" + queryParams.get("password");
        byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;
        String responseFromRequest = null;

        try {
            URL requestURL = new URL(LOGIN_URL);
            httpConnection = (HttpURLConnection) requestURL.openConnection();

            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            wr.write(postData);

            StringBuilder contentBuilder;
            reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            String line;
            contentBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
                contentBuilder.append("\n");
            }
            if (contentBuilder.length() == 0) {
                return "";
            }
            responseFromRequest = contentBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseFromRequest;
    }
}
