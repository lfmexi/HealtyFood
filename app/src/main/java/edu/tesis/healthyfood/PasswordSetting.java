package edu.tesis.healthyfood;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.tesis.healthyfood.settingsTasks.PasswordAsyncTask;
import edu.tesis.healthyfood.settingsTasks.SettingAsyncTask;
import edu.tesis.healthyfood.settingsTasks.UserAsyncTask;
import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.Sesion;

/**
 * Created by luis on 15/03/15.
 */
public class PasswordSetting extends DialogPreference {

    private EditText password;
    private EditText newpass;
    private EditText confirm;
    private String usuario;

    public PasswordSetting(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPositiveButtonText(context.getResources().getString(R.string.editar_button));
        setNegativeButtonText(context.getResources().getString(R.string.cancelar_button));
    }

    public PasswordSetting(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText(context.getResources().getString(R.string.editar_button));
        setNegativeButtonText(context.getResources().getString(R.string.cancelar_button));
    }

    @Override
    protected View onCreateDialogView(){
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_password_layout,null);
        return v;
    }

    @Override
    protected void onBindDialogView(View v){
        password=(EditText)v.findViewById(R.id.editPassword);
        newpass =(EditText)v.findViewById(R.id.editNuevoPassword);
        confirm=(EditText)v.findViewById(R.id.editConfirmNuevo);
        SQLite sql = new SQLite(this.getContext());
        sql.abrir();
        Sesion s = sql.getLastSesion();
        sql.cerrar();
        if(s!=null){
            usuario=s.getUser();
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult){
        if(positiveResult){

            if(password!=null){
                String npass=newpass.getText().toString();
                String conf = confirm.getText().toString();
                if(npass.equals(conf)){
                    PasswordAsyncTask passwordAsyncTask=new PasswordAsyncTask(this, SettingAsyncTask.UPDATE_ACTION);
                    passwordAsyncTask.execute(usuario,password.getText().toString(),npass);
                }else{
                    Toast.makeText(this.getContext(),"Las claves no coinciden",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
