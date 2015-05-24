package edu.tesis.healthyfood.sobj;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luis on 24/05/15.
 */
public class Instruccion implements Parcelable {

    private int idInstruccion;
    private String detalle;

    public Instruccion(int id,String detail){
        idInstruccion=id;
        detalle=detail;
    }

    public int getIdInstruccion() {
        return idInstruccion;
    }

    public void setIdInstruccion(int idInstruccion) {
        this.idInstruccion = idInstruccion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Instruccion(Parcel in){
        setIdInstruccion(in.readInt());
        setDetalle(in.readString());
    }

    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getIdInstruccion());
        parcel.writeString(getDetalle());
    }

    public static final Creator<Instruccion> CREATOR= new Creator<Instruccion>() {
        @Override
        public Instruccion createFromParcel(Parcel parcel) {
            return new Instruccion(parcel);
        }

        @Override
        public Instruccion[] newArray(int i) {
            return new Instruccion[i];
        }
    };
}
