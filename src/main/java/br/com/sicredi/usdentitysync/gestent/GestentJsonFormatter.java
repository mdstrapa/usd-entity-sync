package br.com.sicredi.usdentitysync.gestent;

public class GestentJsonFormatter {

    public String getEntityListFromResponse(String responseJson){
        String response = responseJson.substring(responseJson.indexOf("["));
        return response.substring(0, response.lastIndexOf("]") + 1);
    }
    
}
