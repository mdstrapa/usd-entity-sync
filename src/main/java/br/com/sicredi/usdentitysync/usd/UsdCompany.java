package br.com.sicredi.usdentitysync.usd;

public class UsdCompany {
    String COMMON_NAME;

    String sym;
    Integer delete_flag;
    String parent_company_uuid;
    String z_str_cidade;
    String z_str_estado;
    String z_str_status_entidade;
    String z_str_cod_agencia;
    String z_str_cod_entidade;
    String z_str_cod_ua;

    public UsdCompany(String sym, Integer delete_flag){
        this.sym = sym;
        this.delete_flag = delete_flag;
    }

    public String getSym(){
        return this.sym;
    }

    public void setParentCompany(String parentCompanyUuid){
        this.parent_company_uuid = parentCompanyUuid;
    }

    public void setCidade(String nomeCidade){
        this.z_str_cidade = nomeCidade;
    }

    public void setEstado(String siglaEstado){
        this.z_str_estado = siglaEstado;
    }
    public void setStatusEntidade(String statusEntidade){
        this.z_str_status_entidade = statusEntidade;
    }
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
