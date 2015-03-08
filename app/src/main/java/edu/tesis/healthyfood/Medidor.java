package edu.tesis.healthyfood;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import edu.tesis.healthyfood.sqlite.SQLite;

public class Medidor extends Fragment {
	private String user;
	private String sex;
	private String birth;
	private String ambito="";

    public Medidor(){}

    public static Medidor newInstance(String usuario,String sex,String birth, String ambit){
        Medidor fragment=new Medidor();
        Bundle args = new Bundle();
        args.putString("user",usuario);
        args.putString("sex",sex);
        args.putString("birth",birth);
        args.putString("ambito",ambit);
        fragment.setArguments(args);
        return fragment;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.activity_medidor, container, false);

        user = getArguments().getString("user");
        sex = getArguments().getString("sex");
        birth = getArguments().getString("birth");
        ambito = getArguments().getString("ambito");

        botonRegistrar=(Button)view.findViewById(R.id.botonRegistrarMedidas);
		campoPeso = (EditText)view.findViewById(R.id.campoPeso);
		campoAltura = (EditText)view.findViewById(R.id.campoAltura);
				selectObjetivo = (Spinner)view.findViewById(R.id.selectObjetivo);
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(),
		        R.array.objective_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectObjetivo.setAdapter(adapter);
		botonRegistrar.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonOnClick();
			}
		});
		return view;
	}

	private void botonOnClick(){
		if(!campoPeso.getText().toString().equals("") && !campoAltura.getText().toString().equals("")){
			
			double peso = Double.parseDouble(campoPeso.getText().toString());
			double altura = Double.parseDouble(campoAltura.getText().toString());
			double imc = peso/Math.pow(altura, 2);
			
			if(altura>=1 && altura<=3){
				SQLite sql = new SQLite(getActivity());
				sql.abrir();
				sql.addMedicion(user, peso, altura, imc);
				sql.cerrar();
				
				sql.abrir();
				sql.addObjetivo(user, this.selectObjetivo.getSelectedItem().toString());
				sql.cerrar();
				
				String [] fecha = birth.split("-");
				int year = Integer.parseInt(fecha[0]);
				
				Calendar c = Calendar.getInstance();
				
				int year_today=c.get(Calendar.YEAR);
				
				int diferencia = year_today-year;
				double tmb_val=(10*peso)+(6.25*altura*100)-(5*diferencia);
				if(sex.equals("Hombre")){
					tmb_val+=5;
				}else{
					tmb_val-=161;
				}
				sql.abrir();
				if(sql.addTMB(user, tmb_val))Toast.makeText(getActivity(), "Usted necesita "+(tmb_val*1.2)+" cal para mantener su peso", Toast.LENGTH_LONG).show();
				sql.cerrar();

				if(ambito.equals("registro")){
					Intent i = new Intent(getActivity(),MenuPrincipal.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("infoUser", user);
					this.startActivity(i);
				}else{
					getActivity().getSupportFragmentManager().popBackStack();
				}
			}else{
				AlertDialog.Builder alert=new AlertDialog.Builder(this.getActivity());
				alert.setTitle("Altura incorrecta");
				alert.setMessage("El valor de la altura debe encontrase entre 1 y 3 metros");
				alert.show();
			}
		}
	}


	private Button botonRegistrar;
	private EditText campoPeso;
	private EditText campoAltura;
	private Spinner selectObjetivo;
}
