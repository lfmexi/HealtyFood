package edu.tesis.healthyfood.sobj;

import java.io.Serializable;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class ContenedorIngredientes implements Serializable {
	public TreeMap<String,Ingrediente_Receta> lista;
	
	public ContenedorIngredientes(){
		lista = new TreeMap<String,Ingrediente_Receta>();
	}
}
