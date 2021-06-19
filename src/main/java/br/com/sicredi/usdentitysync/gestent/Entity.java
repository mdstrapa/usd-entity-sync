package br.com.sicredi.usdentitysync.gestent;


public class Entity {
    int idEntidadeSicredi;
    String codigoTipoEntidade;
    String codigoCooperativa;
    String codigoAgencia;
    String codigoEntidade;
    String nomeFantasia;
    String nomeRazaoSocial;
    String dataAtualizacao;
    String nomeCidade;
    String siglaEstado;
    String action;


    public Entity(int idEntidadeSicredi,String codigoTipoEntidade, String codigoCooperativa, String codigoAgencia, String nomeFantasia, String nomeRazaoSocial, String dataAtualizacao, String nomeCidade, String siglaEstado){
        this.idEntidadeSicredi = idEntidadeSicredi;
        this.codigoTipoEntidade = codigoTipoEntidade;
        this.codigoCooperativa = codigoCooperativa;
        this.codigoAgencia = codigoAgencia;
        this.nomeFantasia = nomeFantasia;
        this.nomeRazaoSocial = nomeRazaoSocial;
        this.dataAtualizacao = dataAtualizacao;
        this.nomeCidade = nomeCidade;
        this.siglaEstado = siglaEstado;
        this.action = "";
    }

    public String getCodigoTipoEntidade(){
        return this.codigoTipoEntidade;
    }

    public String getCodigoCooperativa(){
        return this.codigoCooperativa;
    }
    
    public String getCodigoAgencia(){
        return this.codigoAgencia;
    }

    public String getNomeFantasia(){
        return this.nomeFantasia;
    }

    public String getNomeRazaoSocial(){
        return this.nomeRazaoSocial;
    }

    public String getDataAtualizacao(){
        return this.dataAtualizacao;
    }

    public String getNomeCidade(){
        return this.nomeCidade;
    }

    public String getSiglaEstado(){
        return this.siglaEstado;
    }

    public void setAction(String action){
        this.action = action;
    }

    public String getAction(){
        return this.action;
    }
}
