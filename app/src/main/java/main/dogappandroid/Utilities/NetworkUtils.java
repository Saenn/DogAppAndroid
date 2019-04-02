package main.dogappandroid.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import main.dogappandroid.Dog;

public class NetworkUtils {
    private static final String REGISTER_URL = "http://10.0.2.2:9000/register";
    private static final String LOGIN_URL = "http://10.0.2.2:9000/login";
    private static final String FORGOT_URL = "http://10.0.2.2:9000/forgot";
    private static final String ADD_DOG_URL = "http://10.0.2.2:9000/dog/add";
    private static final String UPDATE_DOG_URL = "http://10.0.2.2:9000/dog/update";
    private static final String REPORT_PROVINCE = "http://10.0.2.2:9000/report/";
    private static final String REPORT_REGION = "http://10.0.2.2:9000/reportregion";
    private static final String REPORT_CSV = "http://10.0.2.2:9000/reportcsv";

    public static String getReportCsv(String email, String token, String username) {

        String urlParams = "username=" + username + "&";
        urlParams += "email=" + email;
        byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;
        String responseFromRequest = null;

        try {
            URL requestURL = new URL(REPORT_CSV);
            httpConnection = (HttpURLConnection) requestURL.openConnection();

            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConnection.addRequestProperty("Authorization", token);

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

    public static String getReportRegion(String eiei, String token, String username) {

        String urlParams = "username=" + username;
        byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;
        String responseFromRequest = null;

        try {
            URL requestURL = new URL(REPORT_REGION);
            httpConnection = (HttpURLConnection) requestURL.openConnection();

            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConnection.addRequestProperty("Authorization", token);

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

    public static String getReportProvince(String province,String token, String username) {

        String urlParams = "username=" + username;
        byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;
        String responseFromRequest = null;

        try {
            URL requestURL = new URL(REPORT_PROVINCE + province);
            httpConnection = (HttpURLConnection) requestURL.openConnection();

            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConnection.addRequestProperty("Authorization", token);

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

    public static String register(Map<String, String> queryParams) {
        String response = null;
        try {
            MultipartUtility multipart = new MultipartUtility(REGISTER_URL, "UTF-8");
//            mandatory field
            multipart.addFormField("username", queryParams.get("username"));
            multipart.addFormField("password", queryParams.get("password"));
            multipart.addFormField("firstName", queryParams.get("firstName"));
            multipart.addFormField("lastName", queryParams.get("lastName"));
//            optional field
            if (!(queryParams.get("email") == ""))
                multipart.addFormField("email", queryParams.get("email"));
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

    public static String login(Map<String, String> queryParams) {
        String urlParams = "username=" + queryParams.get("username") + "&";
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

    public static String forgotPassword(Map<String, String> queryParams) {
        String response = null;
        try {
            MultipartUtility multipart = new MultipartUtility(FORGOT_URL, "UTF-8");
//            mandatory field
            multipart.addFormField("username", queryParams.get("username"));
            multipart.addFormField("password", queryParams.get("password"));
            multipart.addFormField("forgotQuestion", queryParams.get("forgotQuestion"));
            multipart.addFormField("forgotAnswer", queryParams.get("forgotAnswer"));
            response = multipart.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Bitmap getBitmapFromUrl(String urlString) {
        try {
            URL url = new java.net.URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String addDog(Dog dog, int ownerID, String token, String username) {
        String urlParams = "username=" + username + "&";
        urlParams += "dogType=" + dog.getDogType() + "&";
        urlParams += "gender=" + dog.getGender() + "&";
        urlParams += "ageRange=" + dog.getAgeRange() + "&";
        urlParams += "address=" + dog.getAddress() + "&";
        urlParams += "subdistrict=" + dog.getSubdistrict() + "&";
        urlParams += "district=" + dog.getDistrict() + "&";
        urlParams += "province=" + dog.getProvince() + "&";
        urlParams += "latitude=" + dog.getLatitude() + "&";
        urlParams += "longitude=" + dog.getLongitude() + "&";
        urlParams += "ownerID=" + ownerID;
        if (dog.getColor() != "") urlParams += "&color=" + dog.getColor();
        if (dog.getName() != "") urlParams += "&name=" + dog.getName();
        if (dog.getBreed() != "") urlParams += "&breed=" + dog.getBreed();
        if (dog.getAge() != -1) urlParams += "&age=" + dog.getAge();

        byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;
        String responseFromRequest = null;

        try {
            URL requestURL = new URL(ADD_DOG_URL);
            httpConnection = (HttpURLConnection) requestURL.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConnection.addRequestProperty("Authorization", token);
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
                if (httpConnection.getErrorStream() != null) {
                    reader = new BufferedReader(new InputStreamReader(
                            httpConnection.getErrorStream()));
                    String line = null;
                    StringBuilder contentBuilder = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        contentBuilder.append(line);
                        contentBuilder.append("\n");
                    }
                    reader.close();
                    if (contentBuilder.length() == 0) {
                        return "";
                    }
                    responseFromRequest = contentBuilder.toString();
                } else {
                    Log.d("Server Error", "Server doesn't response");
                }
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

    public static String updateDog(Dog dog, int ownerID, String token, String username) {
        String urlParams = "username=" + username + "&";
        urlParams += "dogType=" + dog.getDogType() + "&";
        urlParams += "gender=" + dog.getGender() + "&";
        urlParams += "ageRange=" + dog.getAgeRange() + "&";
        urlParams += "address=" + dog.getAddress() + "&";
        urlParams += "subdistrict=" + dog.getSubdistrict() + "&";
        urlParams += "district=" + dog.getDistrict() + "&";
        urlParams += "province=" + dog.getProvince() + "&";
        urlParams += "latitude=" + dog.getLatitude() + "&";
        urlParams += "longitude=" + dog.getLongitude() + "&";
        urlParams += "dogID=" + dog.getDogID() + "&";
        urlParams += "ownerID=" + ownerID;
        if (dog.getColor() != "") urlParams += "&color=" + dog.getColor();
        if (dog.getName() != "") urlParams += "&name=" + dog.getName();
        if (dog.getBreed() != "") urlParams += "&breed=" + dog.getBreed();
        if (dog.getAge() != -1) urlParams += "&age=" + dog.getAge();

        byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;
        String responseFromRequest = null;

        try {
            URL requestURL = new URL(UPDATE_DOG_URL);
            httpConnection = (HttpURLConnection) requestURL.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConnection.addRequestProperty("Authorization", token);
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
