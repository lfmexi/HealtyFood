package edu.tesis.healthyfood.sobj;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;
import java.util.TreeMap;

public class ContenedorIngredientes implements Parcelable {

    public TreeMap<String,Ingrediente_Receta> lista;
	
	public ContenedorIngredientes(){
		lista = new TreeMap<String,Ingrediente_Receta>();
	}

    public ContenedorIngredientes(Parcel parcel){
        this();
        int n=parcel.readInt();
        for(int i=0;i<n;i++){
            String key=parcel.readString();
            Ingrediente_Receta value=parcel.readParcelable(Ingrediente_Receta.class.getClassLoader());
            lista.put(key,value);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        final int n=lista.size();
        parcel.writeInt(n);
        if(n>0){
            for(Map.Entry<String,Ingrediente_Receta> entry:lista.entrySet()){
                parcel.writeString(entry.getKey());
                parcel.writeParcelable(entry.getValue(),i);
            }
        }
    }

    public static final Creator<ContenedorIngredientes> CREATOR=new Creator<ContenedorIngredientes>(){

        @Override
        public ContenedorIngredientes createFromParcel(Parcel parcel) {
            return new ContenedorIngredientes(parcel);
        }

        @Override
        public ContenedorIngredientes[] newArray(int i) {
            return new ContenedorIngredientes[i];
        }
    };
}
