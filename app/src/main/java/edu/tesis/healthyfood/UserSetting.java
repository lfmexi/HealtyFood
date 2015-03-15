package edu.tesis.healthyfood;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.tesis.healthyfood.settingsTasks.UserAsyncTask;
import edu.tesis.healthyfood.sqlite.SQLite;
import edu.tesis.healthyfood.sqlite.Sesion;

/**
 * Created by luis on 15/03/15.
 */
public class UserSetting extends DialogPreference {

    private EditText textUsername;
    private EditText textUser;
    private EditText textEmail;

    public UserSetting(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText(context.getResources().getString(R.string.editar_button));
        setNegativeButtonText(context.getResources().getString(R.string.cancelar_button));
    }

    public UserSetting(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPositiveButtonText(context.getResources().getString(R.string.editar_button));
        setNegativeButtonText(context.getResources().getString(R.string.cancelar_button));
    }

    @Override
    protected View onCreateDialogView(){
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_profile_layout,null);
        return v;
    }

    @Override
    protected void onBindDialogView(View v){
        textUsername = (EditText)v.findViewById(R.id.editUserProf);
        textEmail = (EditText)v.findViewById(R.id.editEmail);
        textUser=(EditText)v.findViewById(R.id.editNombre);

        SQLite sql = new SQLite(this.getContext());
        sql.abrir();
        Sesion s = sql.getLastSesion();
        sql.cerrar();

        if(s!=null){
            UserAsyncTask userAsyncTask=new UserAsyncTask(this,0);
            userAsyncTask.execute(s.getUser());
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult){
        if(positiveResult){

            if(textUsername!=null){

                UserAsyncTask userAsyncTask=new UserAsyncTask(this,1);
                userAsyncTask.execute(textUsername.getText().toString(),
                        textUser.getText().toString(),
                        textEmail.getText().toString());
            }
        }
    }

    public void setTextUsername(String text){
        textUsername.setText(text);
    }

    public void setTextEmail(String text){
        textEmail.setText(text);
    }

    public void setTextUser(String text){
        textUser.setText(text);
    }


}
