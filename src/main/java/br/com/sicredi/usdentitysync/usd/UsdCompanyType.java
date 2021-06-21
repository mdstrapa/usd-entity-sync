package br.com.sicredi.usdentitysync.usd;

public class UsdCompanyType {
    String COMMON_NAME;

    public UsdCompanyType(String sym){
        this.COMMON_NAME = sym;
    }

    public String getSym(){
        return this.COMMON_NAME;
    }
}
