package br.com.santander.colaborador.repository;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.bson.Document;
import br.com.santander.colaborador.entity.Colaborador;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public interface ColaboradorRepository {

	Uni<List<Colaborador>> list(Document query);

	Uni<Void> persist(Colaborador person);

	Uni<Boolean> existsWithName(String name);

	Uni<Boolean> existsWithCode(String code);

	Uni<Colaborador> findById(String id);

	Uni<Void> update(Colaborador collaborator);

	Uni<Long> delete(String id);
	
}
