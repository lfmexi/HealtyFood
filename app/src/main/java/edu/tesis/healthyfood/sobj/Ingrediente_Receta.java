package edu.tesis.healthyfood.sobj;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingrediente_Receta implements Parcelable{

	private String nombre_ingrediente;
	private int unidades;
	private double gramos;
	private double litros;
	private String tipoMedida;
	private double cal_100g;
	
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

	public double getCal_100g() {
		return cal_100g;
	}

	public void setCal_100g(double cal_100g) {
		this.cal_100g = cal_100g;
	}


    public Ingrediente_Receta(Parcel in){
        setNombre_ingrediente(in.readString());
        setUnidades(in.readInt());
        setGramos(in.readDouble());
        setTipoMedida(in.readString());
        setLitros(in.readDouble());
        setCal_100g(in.readDouble());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getNombre_ingrediente());
        parcel.writeInt(getUnidades());
        parcel.writeDouble(getGramos());
        parcel.writeString(getTipoMedida());
        parcel.writeDouble(getLitros());
        parcel.writeDouble(getCal_100g());
    }

    public static final Creator<Ingrediente_Receta> CREATOR=new Creator<Ingrediente_Receta>() {
        @Override
        public Ingrediente_Receta createFromParcel(Parcel parcel) {
            return new Ingrediente_Receta(parcel);
        }

        @Override
        public Ingrediente_Receta[] newArray(int i) {
            return new Ingrediente_Receta[i];
        }
    };
}
