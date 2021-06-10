package br.com.sicredi.usdentitysync.gestent;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.io.IOException;
import java.net.URI;

public class Gestent {



    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    public String getEntityList(){

        String respose = "";
        

        URI gestentEndPoint = URI.create("https://gestent-conector-api.prd.sicredi.cloud/gestend/v2/entidade-sicredi");

        //creating the request
        HttpRequest gestentRequest  = HttpRequest.newBuilder()
            .uri(gestentEndPoint)
            .GET()
            .build();

        try {
            HttpResponse<String> httpResponse = httpClient.send(gestentRequest, BodyHandlers.ofString());
            respose = httpResponse.body();
                            
        } catch (IOException  | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return respose;
    }
    
}
