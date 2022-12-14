package br.com.santander.colaborador.dto.filter;

import java.util.List;

public class DefaultFilters {

	private String userEmail;
	private DefaultFilterCollectionEnum collection;
	private List<DefaultFilter> filters;

	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public DefaultFilterCollectionEnum getCollection() {
		return collection;
	}

	public void setCollection(DefaultFilterCollectionEnum collection) {
		this.collection = collection;
	}

	public List<DefaultFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<DefaultFilter> filters) {
		this.filters = filters;
	}
}
