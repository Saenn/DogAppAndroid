package main.dogappandroid.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import main.dogappandroid.Dog;
import main.dogappandroid.DogImage;
import main.dogappandroid.DogInformation;
import main.dogappandroid.DogVaccine;

public class NetworkUtils {
    private static final String REGISTER_URL = "http://3.1.206.5:9000/register";
    private static final String CHECK_USERNAME = "http://3.1.206.5:9000/checkUsername";
    private static final String UPDATE_USER_URL = "http://3.1.206.5:9000/user/update";
    private static final String LOGIN_URL = "http://3.1.206.5:9000/login";
    private static final String FORGOT_URL = "http://3.1.206.5:9000/forgotForce";
    private static final String ADD_DOG_URL = "http://3.1.206.5:9000/dog/add";
    private static final String UPDATE_DOG_URL = "http://3.1.206.5:9000/dog/update";
    private static final String REPORT_PROVINCE = "http://3.1.206.5:9000/report/";
    private static final String REPORT_REGION = "http://3.1.206.5:9000/reportregion";
    private static final String REPORT_CSV = "http://3.1.206.5:9000/reportcsv";
    private static final String ADD_DOG_INFORMATION_URL = "http://3.1.206.5:9000/dog/information/add";
    private static final String ADD_DOG_VACCINE_URL = "http://3.1.206.5:9000/dog/vaccine/add";
    private static final String ADD_DOG_IMAGE_URL = "http://3.1.206.5:9000/dog/image/add";
    private static final String RETRIEVE_DOG_DATA_URL = "http://3.1.206.5:9000/dog/retrieve";

    public static String retrieveDogData(String ownerID, String username, String token) {
        String urlParams = "username=" + username + "&";
        urlParams += "ownerID=" + ownerID + "&";
        byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;
        String responseFromRequest = null;

        try {
            URL requestURL = new URL(RETRIEVE_DOG_DATA_URL);
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

    public static String addDogImage(Context context, DogImage dogImage, int rdsDogID, String username, String token) {
        String response = null;
        try {
            MultipartUtility multipart = new MultipartUtility(ADD_DOG_IMAGE_URL, "UTF-8", token);
            multipart.addFormField("username", username);
            multipart.addFormField("dogID", String.valueOf(rdsDogID));
            multipart.addFormField("side", String.valueOf(dogImage.getType()));

            Bitmap imageBitmap = BitmapFactory.decodeFile(dogImage.getImagePath());
            ByteArrayOutputStream imageByteArrayData = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageByteArrayData);
            byte[] imageData = imageByteArrayData.toByteArray();
            File image = new File(context.getCacheDir(), "dog_image_" + dogImage.getType());
            OutputStream os = new FileOutputStream(image);
            os.write(imageData);
            os.close();
            multipart.addFilePart("dogImage", image);
            response = multipart.finish();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String addDogVaccine(DogVaccine dogVaccine, int rdsDogID, String token, String username) {
        String urlParams = "username=" + username + "&";
        urlParams += "dogID=" + rdsDogID + "&";
        urlParams += "vaccineName=" + dogVaccine.getName() + "&";
        urlParams += "injectedDate=" + dogVaccine.getDate();
        byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;
        String responseFromRequest = null;

        try {
            URL requestURL = new URL(ADD_DOG_VACCINE_URL);
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

    public static String addDogInformation(DogInformation dogInformation, int rdsDogID, String token, String username) {
        String urlParams = "username=" + username + "&";
        urlParams += "dogID=" + rdsDogID + "&";
        urlParams += "dogStatus=" + dogInformation.getDogStatus() + "&";
        urlParams += "ageRange=" + dogInformation.getAgeRange() + "&";
        if (dogInformation.getAge() != -1) urlParams += "age=" + dogInformation.getAge() + "&";
        if (dogInformation.getPregnant() != -1)
            urlParams += "pregnant=" + dogInformation.getPregnant() + "&";
        if (dogInformation.getChildNumber() != -1)
            urlParams += "childNumber=" + dogInformation.getChildNumber() + "&";
        if (!dogInformation.getMissingDate().equals(""))
            urlParams += "missingDate=" + dogInformation.getMissingDate() + "&";
        if (dogInformation.getSterilized() != -1)
            urlParams += "sterilized=" + dogInformation.getSterilized() + "&";
        if (dogInformation.getSterilizedDate() != null && !dogInformation.getSterilizedDate().equals("")) {
            urlParams += "sterilizedDate=" + dogInformation.getSterilizedDate() + "&";
        }
        byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;
        String responseFromRequest = null;

        try {
            URL requestURL = new URL(ADD_DOG_INFORMATION_URL);
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

    public static String getReportProvince(String province, String token, String username) {

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

    public static String updateUser(Map<String, String> queryParams) {
        String response = null;
        try {
            MultipartUtility multipart = new MultipartUtility(UPDATE_USER_URL, "UTF-8", queryParams.get("token"));

            multipart.addFormField("username", queryParams.get("username"));
            multipart.addFormField("password", queryParams.get("password"));
            multipart.addFormField("firstName", queryParams.get("firstName"));
            multipart.addFormField("lastName", queryParams.get("lastName"));

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
            if (!queryParams.get("profilePicturePath").equals(""))
                multipart.addFilePart("profilePicture", new File(queryParams.get("profilePicturePath")));
            response = multipart.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String checkUsername(String username) {
        String urlParams = "username=" + username;
        byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;
        String responseFromRequest = null;

        try {
            URL requestURL = new URL(CHECK_USERNAME);
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
                String line;
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
                if(httpConnection.getErrorStream() != null){
                    reader = new BufferedReader(new InputStreamReader(
                            httpConnection.getErrorStream()));
                    String line;
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
                }else{
                    return "Your internet is disconnected, please try again";
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
        urlParams += "isDelete=" + dog.getIsDelete() + "&";
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
        urlParams += "isDelete=" + dog.getIsDelete() + "&";
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
