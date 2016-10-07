package org.wso2.sample.loyalty;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Client for the loyalty API
 */
public class APIClient {

    private static final String API_ENDPOINT = "http://52.55.56.170:8282/loyality/1.0.0/reward/";

    private String accessToken;

    public void getAccessToken(String jwt){

        try {

            String tokenEndpoint = "http://52.55.56.170:8282/token";
            String consumerKey = "4mo5hg2bqGHuIFf7FdJjWw_40coa";
            String consumerSecret = "0FdK3CTXTkcfJ0rTu4hSeuPLHEka";

            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpPost tokenRequest = new HttpPost(tokenEndpoint);

            List<NameValuePair> formParameters = new ArrayList<NameValuePair>();
            formParameters.add(new BasicNameValuePair("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer"));
            formParameters.add(new BasicNameValuePair("assertion", jwt));
            tokenRequest.setEntity(new UrlEncodedFormEntity(formParameters));

            tokenRequest.addHeader("Authorization", "Basic " + Base64.encodeBase64String((consumerKey + ":" + consumerSecret).getBytes("UTF-8")));

            CloseableHttpResponse response = httpclient.execute(tokenRequest);

            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){
                JSONParser jsonParser = new JSONParser(JSONParser.MODE_PERMISSIVE);
                JSONObject parsedResponse = (JSONObject) jsonParser.parse(response.getEntity().getContent());

                accessToken = (String) parsedResponse.get("access_token");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getPoints(String username){

        if(accessToken == null){
            return -1;
        }

        String requestURL = null;
        try {
            requestURL = API_ENDPOINT + URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet pointReadRequest = new HttpGet(requestURL);

        pointReadRequest.addHeader("Authorization", "Bearer " + accessToken);

        try {
            CloseableHttpResponse response = httpclient.execute(pointReadRequest);

            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){

                JSONParser jsonParser = new JSONParser(JSONParser.MODE_PERMISSIVE);
                JSONObject parsedResponse = (JSONObject) jsonParser.parse(response.getEntity().getContent());

                return (Integer) parsedResponse.get("points");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void updatePoints(String username, int points){

        if(accessToken == null){
            return;
        }

        String requestURL = API_ENDPOINT;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost pointUpdateRequest = new HttpPost(requestURL);

        JSONObject payload = new JSONObject();
        payload.put("user", username);
        payload.put("points", points);

        StringEntity entity = new StringEntity(payload.toJSONString(), ContentType.create("application/json", "UTF-8"));
        pointUpdateRequest.setEntity(entity);

        pointUpdateRequest.addHeader("Authorization", "Bearer " + accessToken);

        try {
            CloseableHttpResponse response = httpclient.execute(pointUpdateRequest);

            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){

                JSONParser jsonParser = new JSONParser(JSONParser.MODE_PERMISSIVE);
                JSONObject parsedResponse = (JSONObject) jsonParser.parse(response.getEntity().getContent());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return;
    }
}
