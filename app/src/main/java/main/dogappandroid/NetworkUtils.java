package main.dogappandroid;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
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
        String response = null;
        try {
            MultipartUtility multipart = new MultipartUtility(REGISTER_URL, "UTF-8");
//            mandatory field
            multipart.addFormField("email", queryParams.get("email"));
            multipart.addFormField("password", queryParams.get("password"));
            multipart.addFormField("firstName", queryParams.get("firstName"));
            multipart.addFormField("lastName", queryParams.get("lastName"));
//            optional field
            if (!(queryParams.get("address") == ""))
                multipart.addFormField("address", queryParams.get("address"));
            if (!(queryParams.get("subdistrict") == ""))
                multipart.addFormField("subdistrict", queryParams.get("subdistrict"));
            if (!(queryParams.get("district") == ""))
                multipart.addFormField("district", queryParams.get("district"));
            if (!(queryParams.get("province") == ""))
                multipart.addFormField("province", queryParams.get("province"));
            if (!(queryParams.get("phone") == ""))
                multipart.addFormField("phone", queryParams.get("phone"));
            if (!(queryParams.get("forgotQuestion") == ""))
                multipart.addFormField("forgotQuestion", queryParams.get("forgotQuestion"));
            if (!(queryParams.get("forgotAnswer") == ""))
                multipart.addFormField("forgotAnswer", queryParams.get("forgotAnswer"));
            if ((queryParams.get("profilePicturePath") != null))
                multipart.addFilePart("profilePicture", new File(queryParams.get("profilePicturePath")));

            response = multipart.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
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

            reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            String line;
            StringBuilder contentBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
                contentBuilder.append("\n");
            }
            if (contentBuilder.length() == 0) {
                return "";
            }
            responseFromRequest = contentBuilder.toString();
        } catch (IOException e) {
            try {
                reader = new BufferedReader(new InputStreamReader(
                        httpConnection.getErrorStream()));
                String line = null;
                StringBuilder contentBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
//                response.add(line);
                    contentBuilder.append(line);
                    contentBuilder.append("\n");
                }
                reader.close();
                if (contentBuilder.length() == 0) {
                    return "";
                }
                responseFromRequest = contentBuilder.toString();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
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
