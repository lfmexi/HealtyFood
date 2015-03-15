package edu.tesis.healthyfood;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by luis on 15/03/15.
 */
public class PasswordSetting extends DialogPreference {

    public PasswordSetting(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPositiveButtonText("");
        setNegativeButtonText("");
    }

    public PasswordSetting(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText("");
        setNegativeButtonText("");
    }

    @Override
    protected View onCreateDialogView(){
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_password_layout,null);
        return v;
    }

    @Override
    protected void onBindDialogView(View v){

    }
}
