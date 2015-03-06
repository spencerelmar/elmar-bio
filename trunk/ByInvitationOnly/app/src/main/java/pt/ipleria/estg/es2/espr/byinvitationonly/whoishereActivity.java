package pt.ipleria.estg.es2.espr.byinvitationonly;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class whoishereActivity extends Activity {

    public static final String is_Checked_IN = "pt.ipleiria.estg.est2.espr.is_Checked_IN";
    private boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whoishere);

        getIntent().getBooleanExtra(is_Checked_IN, false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_whoishere, menu);


        menu.findItem(R.id.action_i_m_here).setIcon(isChecked ? R.drawable.ic_action_group : R.drawable.ic_action_alone);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_i_m_here) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
