package br.com.santander.colaborador.entity;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.javers.core.metamodel.annotation.Id;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;

@MongoEntity(collection = "collaborator")
public class Colaborador extends ReactivePanacheMongoEntityBase {
	
	@JsonIgnore
	@Id
	public ObjectId id;
	
	@NotBlank(message = "Nome é obrigatório!")
	public String nome;
	
	@NotBlank(message = "Código é obrigatório!")
	public String codigo;
	
	@NotBlank(message = "E-mail é obrigatório!")
	@Email(message = "E-mail inválido!")
	public String email;
	
	@NotBlank(message = "CEP é obrigatório!")
	public String cep;
	
	@NotNull(message = "Número é obrigatório!")
	public Integer numero;
	
	@NotBlank(message = "Nome da rua é obrigatório!")
	public String rua;
	
	@NotBlank(message = "Bairro é obrigatório!")
	public String bairro;
	
	@NotBlank(message = "UF é obrigatória!")
	public String uf;
	
	@NotBlank(message = "Cidade é obrigatória!")
	public String cidade;
	
	public List<String> skills;
}
