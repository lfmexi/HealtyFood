package edu.tesis.healthyfood.sqlite;

import java.util.Date;

public class Medicion {
	private double peso,altura,imc;
	private Date fecha;
	
	public Medicion(){}
	
	public Medicion(double peso, double altura, double imc, Date fecha) {
		super();
		this.peso = peso;
		this.altura = altura;
		this.imc = imc;
		this.fecha = fecha;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public double getAltura() {
		return altura;
	}

	public void setAltura(double altura) {
		this.altura = altura;
	}

	public double getImc() {
		return imc;
	}

	public void setImc(double imc) {
		this.imc = imc;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
}
