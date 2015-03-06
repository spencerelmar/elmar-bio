package pt.ipleria.estg.es2.espr.byinvitationonly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            invertCheckin(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void invertCheckin(MenuItem item) {
        isChecked = !isChecked;
        item.setIcon(isChecked ? R.drawable.ic_action_group : R.drawable.ic_action_alone);

        if (isChecked) {
            Intent intent = new Intent(this, whoishereActivity.class);
            intent.putExtra(whoishereActivity.is_Checked_IN, isChecked);
            startActivity(intent);

        }
    }
}
