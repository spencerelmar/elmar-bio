package pt.ipleria.estg.es2.espr.byinvitationonly;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class ContactSettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.propriedades_contacto);
    }


}
