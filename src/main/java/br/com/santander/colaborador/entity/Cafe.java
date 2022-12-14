package br.com.santander.colaborador.entity;

public class Cafe {

	public Integer id;
    public String name;
    public String countryOfOrigin;
    public Integer price;

    public Cafe() {
    }

    public Cafe(Integer id, String name, String countryOfOrigin, Integer price) {
        this.id = id;
        this.name = name;
        this.countryOfOrigin = countryOfOrigin;
        this.price = price;
    }
}
