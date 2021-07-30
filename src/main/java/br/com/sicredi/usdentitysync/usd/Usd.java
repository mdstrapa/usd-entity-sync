package br.com.sicredi.usdentitysync.usd;

import br.com.sicredi.usdentitysync.Configuration;
import br.com.sicredi.usdentitysync.gestent.Entity;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Usd {

    private UsdJsonFormatter usdJsonFormatter = new UsdJsonFormatter();

    private Configuration config = new Configuration();
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
                .setHeader("X-AccessKey","1929623256")
                .build();
                
        }else if (method == "POST") {
            usdRequest = HttpRequest.newBuilder()
                .uri(usdEndPoint)
                .method(method, BodyPublishers.ofString(requestBody))
                .setHeader("Accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .setHeader("X-AccessKey","1929623256")
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
                if (log.isDebugEnabled()) log.info(httpResponse.body());
                responseBody = usdJsonFormatter.formatObjectResponse(httpResponse.body(),"rest_access");
                System.out.println(responseBody);
                usdRestAccess = new Gson().fromJson(responseBody, UsdRestAccess.class);

                usdRestAccess.registerNewAccessKey(String.valueOf(usdRestAccess.access_key) , String.valueOf(usdRestAccess.expiration_date));
    
            } catch (IOException | InterruptedException e) {
                log.error(this.getClass().getSimpleName() + " ::: " +e.getMessage());
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

            if (log.isDebugEnabled()) log.info(httpResponse.body());

            if (httpResponse.statusCode()==200){

                responseBody = usdJsonFormatter.formatListResponse(httpResponse.body());

                Type listOfMyClassObject = new TypeToken<ArrayList<UsdCompany>>() {}.getType();

                usdCompanies = new Gson().fromJson(responseBody, listOfMyClassObject);
            }

        } catch (IOException | InterruptedException e) {
            log.error(this.getClass().getSimpleName() + " ::: " +e.getMessage());
            e.printStackTrace();
        }
        return usdCompanies;
    }


    public Boolean checkIfCompanyExists(Entity entity){
        Boolean result = true;

        Integer totalCount = 0;
 
        String companyCode = entity.getCodigoCooperativa() + entity.getCodigoAgencia();

        UsdRestAccess restAccess = getAccessKey();

        HttpRequest request = buildUsdRequest("GET","ca_cmpny?WC=z_str_cod_agencia2%3D%27" + companyCode + "%27", "", restAccess.access_key);

        try {            
            
            HttpResponse<String> httpResponse = httpClient.send(request, BodyHandlers.ofString());

            if (log.isDebugEnabled()) log.info(httpResponse.body());

            if (httpResponse.statusCode()==200){

                totalCount = usdJsonFormatter.getTotalCountFromQuery(httpResponse.body());
                //System.out.println(httpResponse.body());
                if (totalCount > 0) result = true;
            }

        } catch (IOException | InterruptedException e) {
            log.error(this.getClass().getSimpleName() + " ::: " +e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public Boolean createCompany(UsdCompany usdCompany){
        Boolean result = false;

        UsdRestAccess restAccess = getAccessKey();
        
        UsdJsonFormatter usdJsonFormatter = new UsdJsonFormatter();
        
        String requestBody = usdJsonFormatter.formatRequestBodyForCreate(usdCompany, "ca_cmpny");;
        
        HttpRequest request = buildUsdRequest("POST","ca_cmpny/", requestBody, restAccess.access_key);

        if (log.isDebugEnabled()) log.debug(requestBody);


        try {            
            
            HttpResponse<String> httpResponse = httpClient.send(request, BodyHandlers.ofString());

            if (log.isDebugEnabled()) log.info(httpResponse.body());

            if (httpResponse.statusCode()==201) result = true;



        } catch (IOException | InterruptedException e) {
            log.info(this.getClass().getSimpleName() + " ::: " +e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    public String getParentCompany(UsdCompany childCompany){
        String parentRellAttr = "";

        UsdRestAccess restAccess = getAccessKey();

        String whereClause = "";

        if (childCompany.company_type.COMMON_NAME == "UA"){
            whereClause = "sym%20like%20%27" + childCompany.getCodAgencia().substring(0,4) + "%25%27%20and%20company_type%20%3D%201000047";
        }

        HttpRequest request = buildUsdRequest("GET","ca_cmpny?WC=" + whereClause, "", restAccess.access_key);

        try {            
            
            HttpResponse<String> httpResponse = httpClient.send(request, BodyHandlers.ofString());

            if (log.isDebugEnabled()) log.info(httpResponse.body());

            if (httpResponse.statusCode()==200){

                parentRellAttr = usdJsonFormatter.getRellAttrFromResponse(httpResponse.body());
                if (log.isDebugEnabled()) log.debug("The Rell ATTR is " + parentRellAttr);

            }

        } catch (IOException | InterruptedException e) {
            log.error(this.getClass().getSimpleName() + " ::: " +e.getMessage());
            e.printStackTrace();
        }

        return parentRellAttr;
    }
    
}
