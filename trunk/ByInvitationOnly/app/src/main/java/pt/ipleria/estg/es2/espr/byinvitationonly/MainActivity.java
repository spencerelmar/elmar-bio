package pt.ipleria.estg.es2.espr.byinvitationonly;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import pt.ipleria.estg.es2.espr.byinvitationonly.modelo.Contacto;


public class MainActivity extends Activity {
    private boolean isChecked = false;
    private Contacto contacto;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        obterContactoUtilizador();
    }

    private void obterContactoUtilizador() {
        contacto = new Contacto("Elmar", "spencerelmar@gmail.com");
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

    private void invertCheckin(final MenuItem item) {

        if (isChecked == false) {
            if (this.contacto != null && !contacto.getEmail().isEmpty() && !contacto.getNome().isEmpty()) {
                AlertDialog.Builder construtor = new AlertDialog.Builder(this);

                construtor.setTitle("Confirmação").setMessage("Tem a certeza que pretende partilhar os dados:\n" + "Nome: " +
                        contacto.getNome() + "E-Mail: " + contacto.getEmail()).setNegativeButton("Não", null)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (temRede()) {
                                    isChecked = true;
                                    item.setIcon(R.drawable.ic_action_group);

                                    Intent intent = new Intent(getApplicationContext(), whoishereActivity.class);
                                    intent.putExtra(whoishereActivity.is_Checked_IN, isChecked);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Não ha rede, tente novamente", Toast
                                            .LENGTH_LONG);
                                }
                            }
                        });
                construtor.create().show();

            } else {
                Toast.makeText(this, "Dados incompletos!", Toast.LENGTH_SHORT).show();

            }

        } else {
            isChecked = false;
            item.setIcon(R.drawable.ic_action_alone);
        }
    }

    private boolean temRede() {

        //conectivity management pesquisar
        return true;
    }
}
