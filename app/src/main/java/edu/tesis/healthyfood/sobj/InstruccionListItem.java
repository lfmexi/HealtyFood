package edu.tesis.healthyfood.sobj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.tesis.healthyfood.R;

/**
 * Created by luis on 24/05/15.
 */
public class InstruccionListItem {

    private Instruccion instruccion;
    private View.OnClickListener editar;
    private View.OnClickListener eliminar;

    public InstruccionListItem(Instruccion instr,View.OnClickListener clickEdit,View.OnClickListener clickDelete){
        instruccion=instr;
        editar=clickEdit;
        eliminar=clickDelete;
    }

    public Instruccion getInstruccion() {
        return instruccion;
    }

    public void setInstruccion(Instruccion instruccion) {
        this.instruccion = instruccion;
    }

    public View.OnClickListener getEditar() {
        return editar;
    }

    public void setEditar(View.OnClickListener editar) {
        this.editar = editar;
    }

    public View.OnClickListener getEliminar() {
        return eliminar;
    }

    public void setEliminar(View.OnClickListener eliminar) {
        this.eliminar = eliminar;
    }

    public View getView(int pos,View convertView,Context c){
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView = LayoutInflater.from(c).inflate(R.layout.instruction_list_item,null);
            holder.textDescription=(TextView)convertView.findViewById(R.id.textInstruccion);
            holder.botonEditar = (Button)convertView.findViewById(R.id.botonEditar);
            holder.botonEliminar = (Button)convertView.findViewById(R.id.botonEliminar);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textDescription.setText(instruccion.getDetalle());
        holder.botonEditar.setOnClickListener(editar);
        holder.botonEliminar.setOnClickListener(eliminar);
        return convertView;
    }

    private static  class ViewHolder{
        TextView textDescription;
        Button botonEliminar;
        Button botonEditar;
    }
}
