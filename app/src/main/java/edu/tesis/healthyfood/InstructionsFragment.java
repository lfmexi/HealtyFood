package edu.tesis.healthyfood;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.tesis.healthyfood.sobj.Instruccion;
import edu.tesis.healthyfood.sobj.InstruccionListItem;

/**
 * Created by luis on 24/05/15.
 */
public class InstructionsFragment extends Fragment {

    private ArrayList<InstruccionListItem> items;

    public InstructionsFragment(){}

    private ListView listView;

    public static InstructionsFragment newInstance(){
        InstructionsFragment fragment=new InstructionsFragment();
        Bundle args = new Bundle();
        //agrega contenedor de instrucciones
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_ingreso_listado_instrucciones,container,false);
        listView = (ListView)rootView.findViewById(R.id.listViewInstruc);
        Button botonAgregar = (Button)rootView.findViewById(R.id.buttonAddInstruccion);

        items = new ArrayList<>();
        for(Map.Entry<Integer,Instruccion> entry:PublicaReceta.instrucciones.mapa.entrySet()){
            final int idInstr = entry.getKey();
            View.OnClickListener editar = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editarOnClick(idInstr);
                }
            };
            View.OnClickListener eliminar = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eliminarOnClick(idInstr);
                }
            };
            items.add(new InstruccionListItem(entry.getValue(),editar,eliminar));
        }
        InstructionListAdapter instructionListAdapter=new InstructionListAdapter(getActivity(),items);
        listView.setAdapter(instructionListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                seleccionar(i);
            }
        });

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregar();
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void agregar(){

    }

    private void seleccionar(int position){

    }

    private void editarOnClick(int idEdit){

    }

    private void eliminarOnClick(int idEliminar){

    }

    private static class InstructionListAdapter extends ArrayAdapter<InstruccionListItem>{
        public InstructionListAdapter(Context c,List<InstruccionListItem> objects){
            super(c,0,objects);
        }

        public View getView(int position,View convertView,ViewGroup parent){
            View view = getItem(position).getView(position,convertView,getContext());
            return view;
        }
    }
}
