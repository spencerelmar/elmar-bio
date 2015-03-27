package pt.ipleria.estg.es2.espr.byinvitationonly;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import pt.ipleria.estg.es2.espr.byinvitationonly.modelo.Contacto;


public class MainActivity extends Activity {
    public static final int PEDIDO_DE_CONTACTO = 1;
    private boolean isChecked = false;
    private Contacto contacto;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    private void obterContactoUtilizador() {
        //contacto = new Contacto("Elmar", "spencerelmar@gmail.com"); isto é criar um contacto
        //agora vamos criar um contacto no ficheiro preferenc
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nome = prefs.getString(getString(R.string.pref_key_name), "");
        String email = prefs.getString(getString(R.string.pref_key_email), "");
        contacto = new Contacto(nome, email);
    }

/*
    private void adicionarContactoUtilizador(Contacto contacto) {
        //contacto = new Contacto("Elmar", "spencerelmar@gmail.com"); isto é criar um contacto
        //agora vamos criar um contacto no ficheiro preferenc
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor= prefs.edit();

        editor.putString(getString(R.string.pref_key_name), contacto.getNome());
        editor.putString(getString(R.string.pref_key_email), contacto.getEmail());
        editor.apply();
    }
*/


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
            obterContactoUtilizador();
            if (this.contacto != null && !contacto.getEmail().isEmpty() && !contacto.getNome().isEmpty()) {
                mostrarJanelaConfirmacao(item);

            } else {
                mostrarJanelaPedidoPreenchimento();
            }

        } else {
            isChecked = false;
            item.setIcon(R.drawable.ic_action_alone);
        }
    }

    private void mostrarJanelaPedidoPreenchimento() {
        AlertDialog.Builder construtor = new AlertDialog.Builder(this);
        construtor.setTitle(getString(R.string.dados_incompletos)).setMessage(getString(R.string.messagem_completar_dados))
                .setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                abrirActivitySolicitacaoDados();
                            }
                        }
                ).setNegativeButton(getString(R.string.nao), null);
        construtor.create().show();
    }

    private void abrirActivitySolicitacaoDados() {
        Intent i = new Intent(this, ContactSettingsActivity.class);
        // startActivity(i);
        startActivityForResult(i, PEDIDO_DE_CONTACTO);
    }

    //metodo que espera do resultado da atividade
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PEDIDO_DE_CONTACTO) {
            obterContactoUtilizador();
            if (contacto != null && !contacto.getNome().isEmpty() && !contacto.getEmail().isEmpty()) {
                mostrarJanelaConfirmacao(item);
            }
        }
    }


    private void mostrarJanelaConfirmacao(final MenuItem item) {
        AlertDialog.Builder construtor = new AlertDialog.Builder(this);

        construtor.setTitle(getString(R.string.confirmacao)).setMessage("Tem a certeza que pretende partilhar os dados:\n" + getString(R.string.nome) +
                contacto.getNome() + getString(R.string.email) + contacto.getEmail()).setNegativeButton(getString(R.string.nao), null)
                .setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (temRede()) {
                            isChecked = true;
                            item.setIcon(R.drawable.ic_action_group);

                            Intent intent = new Intent(getApplicationContext(), whoishereActivity.class);
                            intent.putExtra(whoishereActivity.is_Checked_IN, isChecked);
                            startActivity(intent);
                        } else {
                            //Toast.makeText(MainActivity.this, getString(R.string.rede_desativo), Toast
                            //      .LENGTH_LONG);
                            showConnectivyError();
                        }
                    }
                });
        construtor.create().show();
    }

    private void showConnectivyError() {
        AlertDialog.Builder construtor = new AlertDialog.Builder(this);
        construtor.setTitle(getString(R.string.erro)).setMessage(getString(R.string.sem_rede))
                .setPositiveButton(getString(R.string.ok), null)
                .create().show();
    }

    private boolean temRede() {

        //conectivity management pesquisar
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        return (info != null && info.isConnectedOrConnecting());
    }
}
