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

import br.com.sicredi.usdentitysync.Configuration;
import br.com.sicredi.usdentitysync.Log;
import br.com.sicredi.usdentitysync.LogType;

public class Gestent {

    Configuration config = new Configuration();
    Log log = new Log();

    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    public List<Entity> getEntityList(int pageNumber){

        List<Entity> entityList = new ArrayList<>();
        Gson gson = new Gson();
        GestentJsonFormatter jsonFormatter = new GestentJsonFormatter();

        String gestEntEndPoint = config.getProperty("gestEntEndPoint");
        URI gestentEndPoint = URI.create(gestEntEndPoint + "entidade-sicredi/?page=" + pageNumber);

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
            log.addLogLine(LogType.ERROR, this.getClass().getSimpleName() + " ::: " +e.getMessage());
            e.printStackTrace();
        }

        return entityList;
    }
    
}
