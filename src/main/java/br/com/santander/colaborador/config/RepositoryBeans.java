package br.com.santander.colaborador.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import br.com.santander.colaborador.repository.ColaboradorRepository;
import br.com.santander.colaborador.repository.ColaboradorRepositoryReactivePanache;

public class RepositoryBeans {

	@Produces
	@ApplicationScoped
	public ColaboradorRepository collaboratorRepository() {
		return new ColaboradorRepositoryReactivePanache();
	}
	
}
