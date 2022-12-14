package br.com.santander.colaborador.dto.filter;

public class DefaultFilter {

	private String field;
	private DefaultFilterTypeEnum type;
	private String value;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public DefaultFilterTypeEnum getType() {
		return type;
	}

	public void setType(DefaultFilterTypeEnum type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
