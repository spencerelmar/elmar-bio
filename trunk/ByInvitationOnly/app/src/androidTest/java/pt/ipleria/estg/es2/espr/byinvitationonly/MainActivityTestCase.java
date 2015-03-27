package pt.ipleria.estg.es2.espr.byinvitationonly;

import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.robotium.solo.Solo;

import pt.ipleria.estg.es2.espr.byinvitationonly.modelo.Contacto;

/**
 * Created by ESPR on 06/03/2015.
 */
public class MainActivityTestCase extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public MainActivityTestCase() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp(); //ocorre sempre antes dos testes
        solo = new Solo(getInstrumentation(), getActivity()); //garantir que esta na atividade
        solo.unlockScreen();
    }

    @Override
    protected void tearDown() throws Exception {
        //uso do robotico
        solo.finishOpenedActivities();
        super.tearDown(); //ocorre depois dos testes
    }

    /*Dado que estou na aplicação, Quando pressiono o botão "I'm here" e as
    informações do meu contacto estão preenchidas (pelo menos nome e email),
    Então deve ser-me exibida uma mensagem de confirmação da activação desta
    funcionalidade.*/
    public void testJanelaConfirmacao() {
        adicionarContactoUtilizador(new Contacto("Nome exemplo", "email@exemplo.com.pt"));
        solo.clickOnActionBarItem(R.id.action_i_m_here);
        boolean apareceu = solo.waitForText(getActivity().getString(R.string.confirmacao), 1, 5000);

        assertTrue("A mensagem de confirmação não apareceu", apareceu);
    }

    /*Dado que estou na mensagem de confirmação da funcionalidade "I'm here",
    Quando pressiono no botão "Sim" e tenho rede, Então os meus dados devem
    ser submetidos e partilhados com os restantes participantes que activaram a
    funcionalidade "I'm here" e o botão deve mudar para o estado activo*/

    public void testConfirmarSubmissao() {
        adicionarContactoUtilizador(new Contacto("Nome exemplo", "email@exemplo.com.pt"));
        solo.clickOnActionBarItem(R.id.action_i_m_here);
        solo.clickOnButton(getActivity().getString(R.string.sim));

        //garantir que tinha rede
        solo.setWiFiData(true);
        // solo.setMobileData(true); nao existi no nosso ambiente

        boolean mudouAtividade = solo.waitForActivity(whoishereActivity.class, 5000);
        assertTrue("Não mudou de Atividade", mudouAtividade);

        assertTrue("Icon nao apareceu", solo.waitForView(R.id.action_i_m_here));

        assertIcon("Icon não mudou", R.id.action_i_m_here, R.drawable.ic_action_group);


    }

    private void assertIcon(String msg, int item, int iconEsperado) {
        //garantir que o botao mudou (imagem)
        TextView tv = (TextView)
                solo.getCurrentActivity().findViewById(item);
        BitmapDrawable icon = (BitmapDrawable) tv.getCompoundDrawables()[0];
        BitmapDrawable esperado = (BitmapDrawable) solo.getCurrentActivity().getDrawable(iconEsperado);

        boolean iguais = esperado.getBitmap().sameAs(icon.getBitmap());
        assertTrue(msg, iguais);
    }


    public void testContactNaoPreenchido() {
        adicionarContactoUtilizador(new Contacto("", ""));

        solo.clickOnActionBarItem(R.id.action_i_m_here);
        assertTrue("Não apareceu caixa de dialogo", solo.waitForDialogToOpen());
        assertTrue("Caixa de dialogo errada", solo.waitForText(solo.getString(R.string.dados_incompletos)));
        assertTrue("Não tem botão aceitar", solo.searchText(solo.getString(R.string.sim)));
        assertTrue("Não tem botão cancelar", solo.searchText(solo.getString(R.string.sim)));
    }


    private void adicionarContactoUtilizador(Contacto contacto) {
        //contacto = new Contacto("Elmar", "spencerelmar@gmail.com"); isto é criar um contacto
        //agora vamos criar um contacto no ficheiro preferenc
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(solo.getCurrentActivity());
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(solo.getString(R.string.pref_key_name), contacto.getNome());
        editor.putString(solo.getString(R.string.pref_key_email), contacto.getEmail());
        editor.apply();
    }

    /*Dado que estou na mensagem de confirmação da funcionalidade "I'm here",
    Quando pressiono no botão "Sim" e tenho rede, Então os meus dados devem
    ser submetidos e partilhados com os restantes participantes que activaram a
    funcionalidade "I'm here" e o botão deve mudar para o estado activo*/

    private void abrirJanelaDeConfirmacao() {
        solo.clickOnActionBarItem(R.id.action_i_m_here);
    }

    public void testNaoAceitaPartilha() {
        adicionarContactoUtilizador(new Contacto("Nome exemplo", "Email@exeplo.com"));
        abrirJanelaDeConfirmacao();
        solo.clickOnButton(solo.getString(R.string.nao));
        solo.waitForDialogToClose();
        solo.assertCurrentActivity("Não fiquei na MainAtivity", MainActivity.class);
        assertIcon("Icon mudou", R.id.action_i_m_here, R.drawable.ic_action_alone);

    }
    /*Dado que estou a visualizar a mensagem perguntando se pretendo preencher as
    informações do meu contacto, Quando selecciono "Sim", Então devo ser
    reenchaminhado para o ecrã de preenchimento de detalhes do meu contacto*/

    public void testMostrarPreenchimentoContactos() {
        adicionarContactoUtilizador(new Contacto("", ""));
        solo.clickOnButton(R.id.action_i_m_here);
        solo.waitForDialogToOpen();
        solo.clickOnButton(solo.getString(R.string.sim));
        boolean abriu = solo.waitForActivity(ContactSettingsActivity.class, 5000);
        assertTrue("Não mudou para a atividade de preenchimento de detalhes", abriu);
    }

    /*Dado que estou a visualizar a mensagem perguntando se pretendo preencher as
    informações do meu contacto, Quando selecciono "Não", Então deve ser
    exibido o ecrã inicial mantendo-se a funcionalidade "I'm here" desactivada*/

    public void testNaoPreencherDadosContactos() {
        adicionarContactoUtilizador(new Contacto("", ""));
        solo.clickOnButton(R.id.action_i_m_here);
        solo.waitForDialogToOpen();
        solo.clickOnButton(solo.getString(R.string.nao));
        solo.waitForDialogToClose();
        solo.assertCurrentActivity("Mudou de atividade", MainActivity.class);
        assertIcon("Icon mudou", R.id.action_i_m_here, R.drawable.ic_action_alone);
    }

    /*Dado que estou no ecrã de preenchimento de detalhes do meu contacto,
    Quando termino o preenchimento dos dados, pressiono o botão de back, e
    tenho as informações do meu contacto preenchidas (pelo menos nome e email),
    Então deve-me ser exibida uma mensagem de confirmação da activação desta
    funcionalidade*/

    public void testGuardarDadosContactos() {
        testMostrarPreenchimentoContactos();
        solo.clickOnText(solo.getString(R.string.nome));
        solo.waitForDialogToOpen();
        solo.enterText(0, "Nome Exemplo");
        solo.clickOnButton(1);
        solo.waitForDialogToClose();

        solo.clickOnText(solo.getString(R.string.email));
        solo.waitForDialogToOpen();
        solo.enterText(0, "email@example.com");
        solo.clickOnButton(1);
        solo.waitForDialogToClose();

        solo.goBack();

        assertTrue("Não voltou a MainActivity", solo.waitForActivity(MainActivity.class));
        solo.waitForDialogToOpen();

        assertTrue(
                "Não apareceu a janela de confirmaçao", solo.searchText(solo.getString(R.string.confirmacao))
        );

    }
    /*Dado que estou no ecrã de preenchimento de detalhes do meu contacto,
    Quando termino o preenchimento dos dados, pressiono o botão de back, e não
    tenho as informações do meu contacto preenchidas (pelo menos nome e email),
    Então deve ser exibido o ecrã inicial mantendo-se a funcionalidade "I'm here"
    desactivada*/

    public void testNaoPreechimentoDeDados() {
        testMostrarPreenchimentoContactos();
        solo.goBack();

        assertTrue("Não voltou a MainActivity", solo.waitForActivity(MainActivity.class));

        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(solo.getCurrentActivity());
        String nome = prefs.getString(solo.getString(R.string.pref_key_name), "");
        String email = prefs.getString(solo.getString(R.string.pref_key_email), "");
        assertTrue(nome.isEmpty() || email.isEmpty());
    }


}