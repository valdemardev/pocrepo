package br.com.santander.colaborador.dto;

import javax.ws.rs.QueryParam;

public class ParameterBean {
	
	@QueryParam("name") 
    public String name;

    @QueryParam("code") 
    public String code;

    @QueryParam("email") 
    public String email;
    
    @QueryParam("city") 
    public String city;
}