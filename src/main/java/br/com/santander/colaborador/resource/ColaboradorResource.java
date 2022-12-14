package br.com.santander.colaborador.resource;

import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.annotations.SseElementType;
import br.com.santander.colaborador.dto.ParameterBean;
import br.com.santander.colaborador.entity.Colaborador;
import br.com.santander.colaborador.service.ColaboradorService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("/collaborator")
public class ColaboradorResource {

	@Inject
	ColaboradorService service;

	@GET
	@Path("/stream")
	@Produces(MediaType.SERVER_SENT_EVENTS)
	@SseElementType(MediaType.APPLICATION_JSON)
	public Multi<Colaborador> streamPersons() {
		return Colaborador.streamAll();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Uni<Void> create(@Valid Colaborador col) {
		return service.create(col);
	}

	@GET
	@Path("/findByParam")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Uni<List<Colaborador>> findByParam(@BeanParam ParameterBean param) {
		return service.findByParam(param);
	}
	
	@GET
	@Path("/findByCodes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Uni<List<Colaborador>> findByCodes(@QueryParam("code") Set<String> codes) {
		return service.findByCodes(codes);
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Uni<Void> update(@Valid Colaborador coll) {
		if (coll.id == null) {
			throw new WebApplicationException(Response.ok("ID obrigatório").status(HttpStatus.SC_BAD_REQUEST).build());
		}
		return service.update(coll).onItem().ignore().andContinueWithNull();
	}
	
	@PATCH
	@Path("/{id}/skills")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Uni<Void> partialUpdateSkills(@PathParam("id") String id, List<String> skills) {
		if (id == null) {
			throw new WebApplicationException(Response.ok("ID obrigatório").status(HttpStatus.SC_BAD_REQUEST).build());
		}
		
		return service.updateSkills(id, skills);
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Uni<Response> delete(@PathParam("id") String id) {
		return service.delete(id)
				.chain(() -> Uni.createFrom().item(Response.status(HttpStatus.SC_NO_CONTENT).build()));
	}
}
