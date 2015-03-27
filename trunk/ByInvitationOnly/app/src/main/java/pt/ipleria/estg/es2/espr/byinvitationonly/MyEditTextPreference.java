package pt.ipleria.estg.es2.espr.byinvitationonly;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.AttributeSet;

/**
 * Created by ESPR on 13/03/2015.
 */
public class MyEditTextPreference extends EditTextPreference {

    private String sumarioInicial;


    public MyEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //definirListener();    se o getsumary nao funcionar
    }

    public MyEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //definirListener();    se o getsumary nao funcionar
    }

    public MyEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        //definirListener();    se o getsumary nao funcionar
    }

    public MyEditTextPreference(Context context) {
        super(context);
        //definirListener();    se o getsumary nao funcionar
    }

    @Override
    public CharSequence getSummary() {
        if (getEditText().getText().toString().isEmpty()) {
            return super.getSummary();
        }
        return getEditText().getText().toString();
    }

    //no caso do getSumar nao funcionar
    private void definirListener() {
        sumarioInicial = getSummary().toString();
        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().isEmpty())
                    setSummary(sumarioInicial);
                else
                    setSummary(newValue.toString());
                return true;

            }
        });
    }
}
