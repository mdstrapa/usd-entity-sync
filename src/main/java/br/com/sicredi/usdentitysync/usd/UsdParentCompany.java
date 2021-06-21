package br.com.sicredi.usdentitysync.usd;

public class UsdParentCompany {
    String REL_ATTR;

    public UsdParentCompany(String uuid){
        this.REL_ATTR = uuid;
    }

    public String getSym(){
        return this.REL_ATTR;
    }
    
}
