package br.com.sicredi.usdentitysync;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.usdentitysync.gestent.Entity;
import br.com.sicredi.usdentitysync.gestent.Gestent;
import br.com.sicredi.usdentitysync.usd.Usd;
import br.com.sicredi.usdentitysync.usd.UsdCompany;
import br.com.sicredi.usdentitysync.usd.UsdParentCompany;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.*;

@RestController
public class Controller {

        //entityTypesToProcess.add("AGENCIA");
        // entityTypesToProcess.add("COOPERATIVA");
        // entityTypesToProcess.add("BANCO");
        // entityTypesToProcess.add("SUREG");
        // entityTypesToProcess.add("CENTRALIZADORA");

    @GetMapping("/syncEntities")
    public String syncEntities(@RequestParam ExecutionMode executionMode, @RequestParam String entityTypeToProcess, @RequestParam String thresholdDate){
        
        System.out.println("Process Begins =======================");

        List<Entity> entityListToProcess = getEntityToProcess(entityTypeToProcess,thresholdDate);

        processEntities(entityListToProcess,executionMode);

        String htmlBody = showProcessResult(entityListToProcess,executionMode);    
        
        System.out.println("Process End ==========================");

        return htmlBody;
    }

    @GetMapping("/")
    public String index(){
        return "This is the API that syncs Entities into USD";
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

    private boolean evaluateModifiedDate(Date entityModifiedDate, Date thrasholdDate){
        Boolean result = false;
        if (entityModifiedDate.compareTo(thrasholdDate) > 0 ) result = true;
        return result;
    }

    private List<Entity> getEntityToProcess(String entityTypeToProcess, String thresholdDate){
        Gestent gestent = new Gestent();
        List<Entity> entityListPagged = new ArrayList<>();
        List<Entity> entityListToProcess = new ArrayList<>();

        for(int c = 0;c<384;c++){
            entityListPagged = gestent.getEntityList(c);
            for (Entity entity : entityListPagged) {
                //System.out.println("Processing entity: " + entity.getNomeFantasia() + " - " + entity.getCodigoTipoEntidade());
                if (entity.getDataAtualizacao() != null && entity.getDataAtualizacao() != "null" ){
                    if (evaluateModifiedDate(convertToDate(entity.getDataAtualizacao()),convertToDate(thresholdDate))){
                        if (entityTypeToProcess.equals(entity.getCodigoTipoEntidade())) entityListToProcess.add(entity);
                    }
                }
            }
        }

        return entityListToProcess;
    }

    private void processEntities(List<Entity> entitiesToProcess, ExecutionMode executionMode){
        Usd usd = new Usd();

        for (Entity entity : entitiesToProcess) {
            if (!usd.checkIfCompanyExists(entity)){
                switch (executionMode){
                    case WRITE:
                        createEntityInUsd(entity);
                    case CHECKONLY:
                        entity.setAction("Would Be Created");
                }
            }else entity.setAction("Already Exist in USD");
        }
    }

    private void createEntityInUsd(Entity entity){

        UsdCompany newCompany = new UsdCompany(entity.getCodigoCooperativa() + entity.getCodigoAgencia() + " - " + entity.getNomeFantasia(),0,"UA");

        newCompany.setCodAgencia(entity.getCodigoCooperativa() + entity.getCodigoAgencia());
        newCompany.setCodEntidade(entity.getCodigoCooperativa());
        newCompany.setCodUa(entity.getCodigoAgencia());
        newCompany.setCidade(entity.getNomeCidade());
        newCompany.setEstado(entity.getSiglaEstado());
        
        Usd usd = new Usd();

        String parentCompanyUuid = usd.getParentCompany(newCompany);
        UsdParentCompany parentCompany = new UsdParentCompany(parentCompanyUuid);
        newCompany.setParentCompany(parentCompany);

        if (usd.createCompany(newCompany)) entity.setAction("ENTITY CREATED");
        else entity.setAction("Error on entity creation");
    }

    private String showProcessResult(List<Entity> entityListToShow, ExecutionMode executionMode){
        String htmlBody = "<table>";
        htmlBody = htmlBody + "<tr><td colspan=6></td>Execution Mode: " + executionMode + "</td></tr>";
        htmlBody = htmlBody + "<tr><td></td><td>Code</td><td>Name</td><td>Type</td><td>Mod Date</td><td>Action</td></tr>";
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

}
