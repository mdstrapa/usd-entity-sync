package br.com.sicredi.usdentitysync.usd;

public class UsdCompany {
    String COMMON_NAME;

    String sym;
    Integer delete_flag;
    UsdParentCompany parent_company;
    UsdCompanyType company_type;
    String z_str_cidade;
    String z_str_estado;
    String z_str_status_entidade;
    String z_str_cod_agencia;
    String z_str_cod_entidade;
    String z_str_cod_ua;

    public UsdCompany(String sym, Integer delete_flag, String company_type_sym){
        this.sym = sym.toUpperCase();
        this.delete_flag = delete_flag;
        this.company_type = new UsdCompanyType(company_type_sym);
        this.z_str_status_entidade = "ATIVA";
    }

    public String getSym(){
        return this.sym;
    }

    public String getCodAgencia(){
        return this.z_str_cod_agencia;
    }

    public void setParentCompany(UsdParentCompany parentCompany){
        this.parent_company = parentCompany;
    }

    public void setCidade(String nomeCidade){
        if(nomeCidade != null) this.z_str_cidade = nomeCidade.toUpperCase();
    }

    public void setEstado(String siglaEstado){
        if(siglaEstado != null) this.z_str_estado = siglaEstado.toUpperCase();
    }
    // public void setStatusEntidade(String statusEntidade){
    //     this.z_str_status_entidade = statusEntidade;
    // }
    public void setCodAgencia(String codAgencia){
        this.z_str_cod_agencia = codAgencia;
    }
    public void setCodEntidade(String codEntidade){
        this.z_str_cod_entidade = codEntidade;
    }
    public void setCodUa(String codUa){
        this.z_str_cod_ua = codUa;
    }

}
