package edu.tesis.healthyfood;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import edu.tesis.healthyfood.sqlite.Medicion;
import edu.tesis.healthyfood.sqlite.SQLite;

public class GridFragment extends Fragment{
	
	private String user;

    public GridFragment(){}

    public static GridFragment newInstance(String usr){
        GridFragment fragment=new GridFragment();
        Bundle args = new Bundle();
        args.putString("user",usr);
        fragment.setArguments(args);
        return fragment;
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.frag_grid, container,false);
		GridView gv=(GridView)v.findViewById(R.id.gridView1);
        GridView gv_encabezado = (GridView)v.findViewById(R.id.gridView2);

        this.user = getArguments().getString("user");

        ArrayList<String>listado=new ArrayList<String>();

		SQLite sql=new SQLite(this.getActivity());
		sql.abrir();
		ArrayList<Medicion> mediciones = sql.getMediciones(user);
		sql.cerrar();

		for(int i =0;i<mediciones.size();i++){
			listado.add(new SimpleDateFormat("dd-MM/yy").format(mediciones.get(i).getFecha()));
			listado.add(mediciones.get(i).getAltura()+"");
			listado.add(mediciones.get(i).getPeso()+"");
			listado.add(Math.round(mediciones.get(i).getImc()*100.0)/100.0+"");
		}
		
		ArrayAdapter<String> aa=new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_list_item_1,listado);
		gv.setAdapter(aa);

        
        ArrayAdapter<String>enc = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.encabezado_progreso_array));
        gv_encabezado.setAdapter(enc);
		return v;
	}

}
