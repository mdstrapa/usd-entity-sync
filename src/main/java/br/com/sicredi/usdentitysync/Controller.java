package br.com.sicredi.usdentitysync;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.usdentitysync.gestent.Entity;
import br.com.sicredi.usdentitysync.gestent.Gestent;
import br.com.sicredi.usdentitysync.usd.Usd;
import br.com.sicredi.usdentitysync.usd.UsdCompany;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.*;

@RestController
public class Controller {

    @GetMapping("/")
    public String index(){

        List<Entity> entityListToProcess = getEntityToProcess();

        processEntities(entityListToProcess);

        String htmlBody = showEntities(entityListToProcess);

        //List<UsdCompany> usdCompanies = getUsdCompanies();

        //String htmlBody = showCompanies(usdCompanies);

        return htmlBody;
    }

    public String createNewEntities(@RequestParam String entityType){
        return "ok";
    }

    private Date getThrasholdDate(){
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        Date returnDate = new Date();
        try{
            returnDate = sdformat.parse("2021-05-01");
        }catch (ParseException e){
            System.out.println(e.getMessage());
        }
        return returnDate;
    }

    private Date convertToDate(String dateToConvert){
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        Date returnDate = new Date();
        try{
            returnDate = sdformat.parse(dateToConvert);
        }catch (ParseException e){
            System.out.println(e.getMessage());
        }
        return returnDate;
    }

    private boolean evaluateModifiedDate(Date dateToCompare){
        Boolean result = false;
        Date thrasholdDate = getThrasholdDate();
        if (dateToCompare.compareTo(thrasholdDate) > 0 ) result = true;
        return result;
    }

    private List<Entity> getEntityToProcess(){
        Gestent gestent = new Gestent();

        List<String> entityTypesToProcess = new ArrayList<>();

        entityTypesToProcess.add("AGENCIA");
        entityTypesToProcess.add("COOPERATIVA");
        entityTypesToProcess.add("BANCO");
        entityTypesToProcess.add("SUREG");
        entityTypesToProcess.add("CENTRALIZADORA");

        List<Entity> entityListPagged = new ArrayList<>();
        List<Entity> entityListToProcess = new ArrayList<>();

        for(int c = 0;c<40;c++){
            entityListPagged = gestent.getEntityList(c);
            for (Entity entity : entityListPagged) {
                System.out.println("Processing entity: " + entity.getNomeFantasia() + " - " + entity.getCodigoTipoEntidade());
                if (entity.getDataAtualizacao() != null && entity.getDataAtualizacao() != "null" ){
                    if (evaluateModifiedDate(convertToDate(entity.getDataAtualizacao()))){
                        if (entityTypesToProcess.contains(entity.getCodigoTipoEntidade())) entityListToProcess.add(entity);
                    }
                }
            }
        }

        return entityListToProcess;
    }

    private void processEntities(List<Entity> entitiesToProcess){
        for (Entity entity : entitiesToProcess) {
            if (!entityExists(entity)) createEntityInUsd(entity);
        }
    }
    
    
    private Boolean entityExists(Entity entity){
        Usd usd = new Usd();
        return usd.checkIfCompanyExists(entity.getCodigoCooperativa() + entity.getCodigoAgencia());
    }

    private void createEntityInUsd(Entity entity){

        UsdCompany newCompany = new UsdCompany(entity.getCodigoCooperativa() + entity.getCodigoAgencia() + " - " + entity.getNomeFantasia(),0,"UA");

        newCompany.setCodAgencia(entity.getCodigoAgencia());
        newCompany.setCodEntidade(entity.getCodigoCooperativa());
        newCompany.setCidade(entity.getNomeCidade());
        newCompany.setEstado(entity.getSiglaEstado());
        Usd usd = new Usd();

        usd.createCompany(newCompany);

        entity.setAction("WILL CREATE");
    }

    private String showEntities(List<Entity> entityListToShow){
        String htmlBody = "<table>";

        int entityCount = 1;
        for (Entity entity : entityListToShow) {
            htmlBody = htmlBody 
                + "<tr><td>"  + entityCount
                + "</td><td>" + entity.getCodigoCooperativa() + entity.getCodigoAgencia()
                + "</td><td>" + entity.getNomeFantasia()
                + "</td><td>" + entity.getCodigoTipoEntidade() 
                + "</td><td>" + entity.getDataAtualizacao() 
                + "</td><td>" + entity.getAction() 
                + "</td></tr>";
            entityCount++;
        }
        
        htmlBody = htmlBody + "</table>";
        return htmlBody;
    }

    private List<UsdCompany> getUsdCompanies(){

        List<UsdCompany> usdCompanies = new ArrayList<>();
        Usd usd = new Usd();


        usdCompanies = usd.getUsdCompanies();

        return usdCompanies;

    }

    private String showCompanies(List<UsdCompany> usdCompanies){

        String htmlBody = "<table>";

        int companyCount = 1;
        for (UsdCompany company : usdCompanies) {
            htmlBody = htmlBody 
                + "<tr><td>"  + companyCount
                + "</td><td>" + company.getSym()
                + "</td></tr>";
            companyCount++;
        }
        
        htmlBody = htmlBody + "</table>";
        return htmlBody;
    }
}
