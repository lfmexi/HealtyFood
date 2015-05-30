package edu.tesis.healthyfood.sobj;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by luis on 24/05/15.
 */
public class ContenedorInstrucciones implements Parcelable {
    public TreeMap<Integer,Instruccion> mapa;

    public ContenedorInstrucciones(){mapa = new TreeMap<Integer,Instruccion>();}

    public ContenedorInstrucciones(Parcel in){
        this();
        int n=in.readInt();
        for(int i=0;i<n;i++){
            int key = in.readInt();
            Instruccion instruccion = in.readParcelable(Instruccion.class.getClassLoader());
            mapa.put(key,instruccion);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        final int n=mapa.size();
        parcel.writeInt(n);
        if(n>0){
            for(Map.Entry<Integer,Instruccion> entry:mapa.entrySet()){
                parcel.writeInt(entry.getKey());
                parcel.writeParcelable(entry.getValue(),i);
            }
        }
    }

    public static final Creator<ContenedorInstrucciones> CREATOR=new Creator<ContenedorInstrucciones>() {
        @Override
        public ContenedorInstrucciones createFromParcel(Parcel parcel) {
            return new ContenedorInstrucciones(parcel);
        }

        @Override
        public ContenedorInstrucciones[] newArray(int i) {
            return new ContenedorInstrucciones[i];
        }
    };
}
