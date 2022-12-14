package br.com.santander.colaborador.service;

import java.util.List;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpStatus;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.eclipse.microprofile.opentracing.Traced;

import br.com.santander.colaborador.dto.ParameterBean;
import br.com.santander.colaborador.entity.Colaborador;
import br.com.santander.colaborador.repository.ColaboradorRepository;
import io.smallrye.mutiny.Uni;

@Traced
@ApplicationScoped
public class ColaboradorService {
	
	@Inject
	ColaboradorRepository collaboratorRepository;
	
	public Uni<List<Colaborador>> findByParam(ParameterBean param) {
		Document document = new Document();
		
		if (param.name != null) {
			document.put("name", new BsonRegularExpression(param.name, "i"));
		}
		
		if (param.city != null) {
			document.put("city", new BsonRegularExpression(param.city, "i"));
		}	
		
		if (param.email != null) {
			document.put("email", new BsonRegularExpression(param.email, "i"));
		}
		
		if (param.code != null) {
			document.put("code", new BsonRegularExpression(param.code, "i"));
		}
		
		return collaboratorRepository.list(document);
	}
	
	
	public Uni<Void> create(Colaborador person) {
		return collaboratorRepository.existsWithName(person.nome)
			.onItem()
			.transformToUni(existsWithName -> {
				if (existsWithName) {
					throw new WebApplicationException(Response.ok("Já existe um colaborador com o nome " + person.nome)
							.status(HttpStatus.SC_BAD_REQUEST).build());
				}
				
				return collaboratorRepository.existsWithCode(person.codigo)
					.onItem()
					.transformToUni(existsWithCode -> {
						if (existsWithCode) {
							throw new WebApplicationException(Response.ok("Já existe um colaborador com o código " + person.codigo)
									.status(HttpStatus.SC_BAD_REQUEST).build());
						}
						
						return collaboratorRepository.persist(person).onItem().ignore().andContinueWithNull();
					});
			});
	}
	
	public Uni<Void> updateSkills(String id, List<String> skills) {
		return collaboratorRepository.findById(id)
				.chain(c -> {
					try {
						c.skills = skills;
						return collaboratorRepository.update(c);
					} catch (Exception e) {
						throw new WebApplicationException(Response.ok("Erro na atualização do colaborador!")
								.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build());
					}
				}).onFailure().transform(e -> new WebApplicationException(
						Response.ok("Colaborador não encontrado")
										.status(HttpStatus.SC_NOT_FOUND).build()))
				;
	}
	
	public Uni<List<Colaborador>> findByCodes(Set<String> codes) {
		Document query = new Document();
		
		if (CollectionUtils.isNotEmpty(codes)) {
			query.put("code", new Document().append("$in", codes));
		}
		
		return collaboratorRepository.list(query);
	}

	public Uni<Long> delete(String id) {
		return collaboratorRepository.delete(id);
	}

	public Uni<Void> update(Colaborador coll) {
		return collaboratorRepository.update(coll);
	}
}
