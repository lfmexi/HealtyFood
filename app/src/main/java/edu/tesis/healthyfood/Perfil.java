package edu.tesis.healthyfood;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Perfil extends Fragment {

	
	String user="";
	String sex;
	String birth;

    public Perfil(){}

    public static Perfil newInstance(String user,String sex,String birth){
        Perfil fragment=new Perfil();
        Bundle args = new Bundle();
        args.putString("user",user);
        args.putString("sex",sex);
        args.putString("birth",birth);
        fragment.setArguments(args);
        return fragment;
    }
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.activity_perfil, container, false);

        user =  getArguments().getString("user");
        sex = getArguments().getString("sex");
        birth = getArguments().getString("birth");

        botonMedir=(Button)view.findViewById(R.id.botonNuevaMedida);
		TextView txt=(TextView)view.findViewById(R.id.textUser);
		txt.setText("BIENVENIDO "+user.toUpperCase());
		
		Button boton = (Button)view.findViewById(R.id.button1);
		Button botonrec = (Button)view.findViewById(R.id.button_misrecetas);
		Button botonIngr = (Button)view.findViewById(R.id.button2);
		
		botonProgreso=(Button)view.findViewById(R.id.botonGetProgreso);
		botonMedir.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				medir();
			}
		});
		botonProgreso.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				progreso();
			}
		});

		botonrec.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				myrecipes();
			}
		});

		boton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				calorias();
			}
		});
		
		botonIngr.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ingredientes();
			}
			
		});
		return view;
	}
	
	private void ingredientes(){
		Intent i = new Intent(this.getActivity(),Ingredientes.class);
		i.putExtra("ambito", "perfil");
		this.startActivity(i);
	}
	
	private void calorias(){
		final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
        ft.replace(R.id.content_frame, ConsumoDiario.newInstance(user),getResources().getString(R.string.consumo_calorias));
        ft.addToBackStack(null);
        ft.commit();
	}
	
	private void medir(){
        final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
        ft.replace(R.id.content_frame, Medidor.newInstance(user,sex,birth, "perfil"), "Medidor");
        ft.addToBackStack(null);
        ft.commit();
	}
	
	private void myrecipes(){
        final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
        ft.replace(R.id.content_frame, MisRecetas.newInstance(user), "Mis Recetas");
        ft.addToBackStack(null);
        ft.commit();
	}

	private void progreso(){
		Intent i = new Intent(this.getActivity(),ProgresoIMC.class);
		i.putExtra("infoUser", user);
		this.startActivity(i);
	}
	

	private Button botonMedir;
	private Button botonProgreso;
}
