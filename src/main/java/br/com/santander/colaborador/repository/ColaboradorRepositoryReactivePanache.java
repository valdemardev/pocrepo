package br.com.santander.colaborador.repository;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import br.com.santander.colaborador.entity.Colaborador;
import io.smallrye.mutiny.Uni;

public class ColaboradorRepositoryReactivePanache implements ColaboradorRepository {

	@Override
	public Uni<List<Colaborador>> list(Document query) {
		return Colaborador.list(query);
	}

	@Override
	public Uni<Void> persist(Colaborador person) {
		return Colaborador.persist(person);
	}

	@Override
	public Uni<Boolean> existsWithName(String name) {
		return Colaborador.find(String.format("{ 'name': /^%s$/i }", name)).count()
				.onItem()
				.transformToUni(quantity -> quantity > 0 ? 
						Uni.createFrom().item(Boolean.TRUE) : 
							Uni.createFrom().item(Boolean.FALSE));
	}

	@Override
	public Uni<Boolean> existsWithCode(String code) {
		return Colaborador.find(String.format("{ 'code': /^%s$/i }", code)).count()
				.onItem()
				.transformToUni(quantity -> quantity > 0 ? 
						Uni.createFrom().item(Boolean.TRUE) : 
							Uni.createFrom().item(Boolean.FALSE));
	}

	@Override
	public Uni<Colaborador> findById(String id) {
		return Colaborador.findById(new ObjectId(id));
	}

	@Override
	public Uni<Void> update(Colaborador collaborator) {
		return Colaborador.update(collaborator);
	}

	@Override
	public Uni<Long> delete(String id) {
		return Colaborador.delete(id);
	}

}
