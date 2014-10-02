package edu.tesis.healthyfood.sobj;

public class Ingrediente_Receta {

	private String nombre_ingrediente;
	private int unidades;
	private double gramos;
	private double litros;
	private String tipoMedida;
	
	public Ingrediente_Receta(String n,String t){
		nombre_ingrediente=n;
		tipoMedida= t;
	}
	
	public Ingrediente_Receta(String n, int u,double g){
		nombre_ingrediente=n; unidades=u; gramos=g;
	}

	public String getNombre_ingrediente() {
		return nombre_ingrediente;
	}

	public void setNombre_ingrediente(String nombre_ingrediente) {
		this.nombre_ingrediente = nombre_ingrediente;
	}

	public int getUnidades() {
		return unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}

	public double getGramos() {
		return gramos;
	}

	public void setGramos(double gramos) {
		this.gramos = gramos;
	}

	public String getTipoMedida() {
		return tipoMedida;
	}

	public void setTipoMedida(String tipoMedida) {
		this.tipoMedida = tipoMedida;
	}

	public double getLitros() {
		return litros;
	}

	public void setLitros(double litros) {
		this.litros = litros;
	}
	
	
}
