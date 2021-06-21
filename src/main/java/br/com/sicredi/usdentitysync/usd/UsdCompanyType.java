package br.com.sicredi.usdentitysync.usd;

public class UsdCompanyType {
    String sym;
    Integer delete_flag;

    public UsdCompanyType(String sym,Integer delete_flag){
        this.sym = sym;
        this.delete_flag = delete_flag;
    }

    public String getSym(){
        return this.sym;
    }

    public Integer getDeleteFlag(){
        return this.delete_flag;
    }
}
