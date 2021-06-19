package br.com.sicredi.usdentitysync.usd;

import br.com.sicredi.usdentitysync.Configuration;
import br.com.sicredi.usdentitysync.Log;
import br.com.sicredi.usdentitysync.LogType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Usd {

    private UsdJsonFormatter usdJsonFormatter = new UsdJsonFormatter();

    private Configuration config = new Configuration();
    private Log log = new Log();
    private Gson gson = new Gson();


    private static HttpClient httpClient = HttpClient.newHttpClient();

    private HttpRequest buildUsdRequest(String method, String usdObject, String requestBody, int accessKey){

        Configuration configuration = new Configuration();

        URI usdEndPoint = URI.create(configuration.getProperty("usdEndPoint")+ usdObject);
        
        HttpRequest usdRequest = null;

        if (usdObject == "rest_access"){
            usdRequest = HttpRequest.newBuilder()
                .uri(usdEndPoint)
                .method(method, BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "application/json")
                .setHeader("Accept", "application/json")
                .setHeader("Authorization", "Basic bWFyY29zX3N0cmFwYXpvbjpFVUEyMDIxZWhub2lz")
                .build();
        }else if (method == "GET") {
            usdRequest = HttpRequest.newBuilder()
                .uri(usdEndPoint)
                .method(method, BodyPublishers.ofString(requestBody))
                .setHeader("Accept", "application/json")
                .setHeader("X-Obj-Attrs", "summary,description,category")
                .setHeader("X-AccessKey",String.valueOf(accessKey))
                .build();
        }else if (method == "POST") {
            usdRequest = HttpRequest.newBuilder()
                .uri(usdEndPoint)
                .method(method, BodyPublishers.ofString(requestBody))
                .setHeader("Accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .setHeader("X-AccessKey",String.valueOf(accessKey))
                .build();
        }
//.setHeader("X-AccessKey","1712166793")
//.setHeader("X-AccessKey", String.valueOf(accessKey))

        return usdRequest;
    }

    private UsdRestAccess getAccessKey(){
        UsdRestAccess usdRestAccess = new UsdRestAccess();

        if (usdRestAccess.isAccessKeyStillValid()) usdRestAccess.loadAccessKeyFromFile();
        else{
            String responseBody;
            String requestBody = "{ \"rest_access\" : {} }";
    
            HttpRequest request = buildUsdRequest("POST","rest_access",requestBody,0);
    
            try {
                HttpResponse<String> httpResponse = httpClient.send(request, BodyHandlers.ofString());
                if (config.isDebugMode()) log.addLogLine(LogType.INFO, httpResponse.body());
                responseBody = usdJsonFormatter.formatObjectResponse(httpResponse.body(),"rest_access");
                System.out.println(responseBody);
                usdRestAccess = new Gson().fromJson(responseBody, UsdRestAccess.class);

                usdRestAccess.registerNewAccessKey(String.valueOf(usdRestAccess.access_key) , String.valueOf(usdRestAccess.expiration_date));
    
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        return usdRestAccess;
    }

    public List<UsdCompany> getUsdCompanies(){

        String responseBody;
        List<UsdCompany> usdCompanies = new ArrayList<>();
 
        UsdRestAccess restAccess = getAccessKey();

        HttpRequest request = buildUsdRequest("GET","ca_cmpny?WC=delete_flag=0", "", restAccess.access_key);

        try {            
            
            HttpResponse<String> httpResponse = httpClient.send(request, BodyHandlers.ofString());

            if (config.isDebugMode()) log.addLogLine(LogType.INFO, httpResponse.body());

            if (httpResponse.statusCode()==200){

                responseBody = usdJsonFormatter.formatListResponse(httpResponse.body());

                //System.out.println(responseBody);

                Type listOfMyClassObject = new TypeToken<ArrayList<UsdCompany>>() {}.getType();

                usdCompanies = new Gson().fromJson(responseBody, listOfMyClassObject);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("An error has occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return usdCompanies;
    }


    public Boolean checkIfCompanyExists(String companyCode){
        Boolean result = false;

        Integer totalCount = 0;
 
        UsdRestAccess restAccess = getAccessKey();

        HttpRequest request = buildUsdRequest("GET","ca_cmpny?WC=z_str_cod_agencia%3D%27" + companyCode + "%27", "", restAccess.access_key);

        try {            
            
            HttpResponse<String> httpResponse = httpClient.send(request, BodyHandlers.ofString());

            if (config.isDebugMode()) log.addLogLine(LogType.INFO, httpResponse.body());

            if (httpResponse.statusCode()==200){

                //System.out.println(httpResponse.body());

                totalCount = usdJsonFormatter.getTotalCountFromQuery(httpResponse.body());

                //System.out.println(totalCount);

                if (totalCount > 0) result = true;
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("An error has occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public Boolean createCompany(UsdCompany usdCompany){
        Boolean result = false;

        UsdRestAccess restAccess = getAccessKey();

        String requestBody = gson.toJson(usdCompany);

        HttpRequest request = buildUsdRequest("POST","ca_cmpny/", requestBody, restAccess.access_key);

        System.out.println(requestBody);


        // try {            
            
        //     HttpResponse<String> httpResponse = httpClient.send(request, BodyHandlers.ofString());

        //     if (config.isDebugMode()) log.addLogLine(LogType.INFO, httpResponse.body());

        //     if (httpResponse.statusCode()==2021) result = true;



        // } catch (IOException | InterruptedException e) {
        //     System.out.println("An error has occurred: " + e.getMessage());
        //     e.printStackTrace();
        // }

        return result;
    }
    
}
