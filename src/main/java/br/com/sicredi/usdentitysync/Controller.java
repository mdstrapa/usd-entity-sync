package br.com.sicredi.usdentitysync;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.usdentitysync.gestent.Gestent;

@RestController
public class Controller {
    


    @GetMapping("/")
    public String hello(){

        Gestent gestent = new Gestent();



        return gestent.getEntityList();
    }
}
