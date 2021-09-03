//package com.example.astroapp;
//
//import android.app.Activity;
//import android.os.AsyncTask;
//import android.os.Build;
//import androidx.annotation.RequiresApi;
//import org.json.JSONObject;
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Base64;
//import java.util.Base64.Encoder;
//import java.util.Collections;
//import java.net.URLEncoder;
//import java.util.Random;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class YahooWeatherService extends AsyncTask<Void, Void, String> {
//
//    private OkHttpClient client = new OkHttpClient();
//    private final String url = "https://weather-ydn-yql.media.yahoo.com/forecastrss";
//    private String authorizationLine;
//    private String location = "lodz";
//    private int isCelsius = 0;
//    private Activity mainActivity;
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private String get(String url) throws IOException {
//        String appId = "EjQajT6u";
//        Request request = new Request.Builder()
//                .url(url)
//                .header("Authorization", authorizationLine)
//                .header("X-Yahoo-App-Id", appId)
//                .header("Content-Type", "application/json")
//                .build();
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        }
//    }
//
//    public void createFile(String jsonContent, Activity activity) throws Exception {
//        JSONObject object = new JSONObject(jsonContent);
//        if (isCelsius==0)
//            object.put("unit", "c");
//        else
//            object.put("unit", "f");
//        JSONObject locationObject = object.getJSONObject("location");
//        String location_name = locationObject.get("city").toString();
//        String filename = location_name.toLowerCase()+".json";
//        String dirName = activity.getCacheDir().toString() + "/AstroWeatherApp/";
//        File dir = new File (dirName);
//        dir.mkdirs();
//        PrintWriter out = new PrintWriter(new FileWriter(dirName+filename));
//        out.write(object.toString());
//        out.close();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    protected String doInBackground(Void... voids) {
//        String response = "";
//        try {
//            if (isCelsius==0)
//                response = get(url + "?location=" + location + "&format=json&u=c");//
//            else
//                response = get(url + "?location=" + location + "&format=json");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return response;
//    }
//
//    protected void onPostExecute(String result) {
//        //if (result == null)
//        //	Toast.makeText(mainActivity, "Couldn't download city info. Data may be outdated", Toast.LENGTH_LONG).show();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public YahooWeatherService(String location, Activity mainActivity, int isCelsius) throws Exception {
//        this.isCelsius = isCelsius;
//        this.mainActivity = mainActivity;
//        this.location = location.toLowerCase().replaceAll("\\s","_");;
//
//        long timestamp = new Date().getTime() / 1000;
//        byte[] nonce = new byte[32];
//
//        Random rand = new Random();
//        rand.nextBytes(nonce);
//        String oauthNonce = "8nduRhXFXQ";//new String(nonce).replaceAll("\\W", "");
//
//        List<String> parameters = new ArrayList<>();
//        String consumerKey = "dj0yJmk9cXdSYWw4YTdBTXJnJmQ9WVdrOVJXcFJZV3BVTm5VbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PTdi";
//        parameters.add("oauth_consumer_key=" + consumerKey);
//        parameters.add("oauth_nonce=" + oauthNonce);
//        parameters.add("oauth_signature_method=HMAC-SHA1");
//        parameters.add("oauth_timestamp=" + timestamp);
//        parameters.add("oauth_version=1.0");
//        parameters.add("location=" + URLEncoder.encode(this.location, "UTF-8"));
//        parameters.add("format=json");
//        if (this.isCelsius==0)
//            parameters.add("u=c");
//        Collections.sort(parameters);
//
//        StringBuilder parametersList = new StringBuilder();
//        for (int i = 0; i < parameters.size(); i++) {
//            parametersList.append((i > 0) ? "&" : "").append(parameters.get(i));
//        }
//
//        String signatureString = "GET&" +
//                URLEncoder.encode(url, "UTF-8") + "&" +
//                URLEncoder.encode(parametersList.toString(), "UTF-8");
//
//        String signature = null;
//        try {
//            String consumerSecret = "11e957fc5e1d478f43ff0e0f4be728690c4119d3";
//            SecretKeySpec signingKey = new SecretKeySpec((consumerSecret + "&").getBytes(), "HmacSHA1");
//            Mac mac = Mac.getInstance("HmacSHA1");
//            mac.init(signingKey);
//            byte[] rawHMAC = mac.doFinal(signatureString.getBytes());
//            Encoder encoder = Base64.getEncoder();
//            signature = encoder.encodeToString(rawHMAC);
//        } catch (Exception e) {
//            System.err.println("Unable to append signature");
//            System.exit(0);
//        }
//
//        authorizationLine = "OAuth " +
//                "oauth_consumer_key=\"" + consumerKey + "\", " +
//                "oauth_nonce=\"" + oauthNonce + "\", " +
//                "oauth_timestamp=\"" + timestamp + "\", " +
//                "oauth_signature_method=\"HMAC-SHA1\", " +
//                "oauth_signature=\"" + signature + "\", " +
//                "oauth_version=\"1.0\"";
//
//    }
//}