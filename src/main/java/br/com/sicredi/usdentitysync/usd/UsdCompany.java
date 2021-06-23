package br.com.sicredi.usdentitysync.usd;

public class UsdCompany {
    String COMMON_NAME;

    String sym;
    UsdDeleteFlag delete_flag;
    UsdParentCompany parent_company_uuid;
    UsdCompanyType company_type;
    String z_str_cidade;
    String z_str_cidade2;
    String z_str_estado;
    String z_str_estado2;
    String z_str_status_entidade;
    String z_str_status_entidade2;
    String z_str_cod_agencia;
    String z_str_cod_agencia2;
    String z_str_cod_entidade;
    String z_str_cod_entidade2;
    String z_str_cod_ua;
    String z_str_cod_ua2;

    public UsdCompany(String sym, Integer delete_flag, String company_type_sym){
        this.sym = sym.toUpperCase();
        this.delete_flag = new UsdDeleteFlag();
        this.company_type = new UsdCompanyType(company_type_sym);
        this.z_str_status_entidade = "ATIVA";
        this.z_str_status_entidade2 = "ATIVA";
    }

    public String getSym(){
        return this.sym;
    }

    public String getCodAgencia(){
        return this.z_str_cod_agencia;
    }

    public void setParentCompany(UsdParentCompany parentCompany){
        this.parent_company_uuid = parentCompany;
    }

    public void setCidade(String nomeCidade){
        if(nomeCidade != null) {
            this.z_str_cidade = nomeCidade.toUpperCase();
            this.z_str_cidade2 = nomeCidade.toUpperCase();
        }
    }

    public void setEstado(String siglaEstado){
        if(siglaEstado != null) {
            this.z_str_estado = siglaEstado.toUpperCase();
            this.z_str_estado2 = siglaEstado.toUpperCase();
        }
    }

    public void setCodAgencia(String codAgencia){
        this.z_str_cod_agencia = codAgencia;
        this.z_str_cod_agencia2 = codAgencia;
    }
    public void setCodEntidade(String codEntidade){
        this.z_str_cod_entidade = codEntidade;
        this.z_str_cod_entidade2 = codEntidade;
    }
    public void setCodUa(String codUa){
        this.z_str_cod_ua = codUa;
        this.z_str_cod_ua2 = codUa;
    }

}
