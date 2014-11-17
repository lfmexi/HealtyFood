package edu.tesis.healthyfood.sqlite;

import java.util.Date;

public class Sesion {
	private int id;
	private String user;
	private String sex;
	private String birth;
	private Date fecha_inicio;
	private Date fecha_fin;
	
	public Sesion(String u,Date fi,Date ff){
		fecha_inicio=fi;
		fecha_fin=ff;
		user=u;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public Date getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}
	
	
}
