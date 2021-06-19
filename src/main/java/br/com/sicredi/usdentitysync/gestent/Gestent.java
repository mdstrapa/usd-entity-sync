package br.com.sicredi.usdentitysync.gestent;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.io.IOException;

import java.net.URI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Gestent {



    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    public List<Entity> getEntityList(int pageNumber){

        List<Entity> entityList = new ArrayList<>();
        Gson gson = new Gson();
        GestentJsonFormatter jsonFormatter = new GestentJsonFormatter();

        URI gestentEndPoint = URI.create("https://gestent-conector-api.prd.sicredi.cloud/gestend/v2/entidade-sicredi/?page=" + pageNumber);

        //creating the request
        HttpRequest gestentRequest  = HttpRequest.newBuilder()
            .uri(gestentEndPoint)
            .GET()
            .build();

        try {
            HttpResponse<String> httpResponse = httpClient.send(gestentRequest, BodyHandlers.ofString());

            Type listOfMyClassObject = new TypeToken<ArrayList<Entity>>() {}.getType();

            entityList = gson.fromJson(jsonFormatter.getEntityListFromResponse(httpResponse.body()), listOfMyClassObject);
                            
        } catch (IOException  | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return entityList;
    }
    
}
