package com.example.clubesbrasileiros;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.clubesbrasileiros.modelo.Clube;
import com.example.clubesbrasileiros.modelo.Divisao;
import com.example.clubesbrasileiros.persistencia.ClubesDatabase;
import com.example.clubesbrasileiros.utils.UtilsLocalDate;
import com.example.clubesbrasileiros.utils.UtilsAlert;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;

public class ClubeActivity extends AppCompatActivity {

    public static final String KEY_ID = "ID";
    public static final String KEY_MODO = "MODO";
    public static final String KEY_DARK_MODE = "DARK_MODE";



    public static final int MODO_NOVO = 0;
    public static final int MODO_EDITAR = 1;


    private EditText editTextClube, editTextDataFundacao;
    private CheckBox checkBoxMundial;
    private RadioGroup radioGroupDivisao;
    private Spinner spinnerEstado;
    private RadioButton radioButtonA, radioButtonB, radioButtonC, radioButtonD;

    private int modo;

    private Clube clubeOriginal;

    private LocalDate dataFundacao;
    private int anosParaTras;

    private Button buttonCompeticoes;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clube);

        SharedPreferences shared = getSharedPreferences(ClubesActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        boolean darkModeAtivo = shared.getBoolean(KEY_DARK_MODE, false);
        aplicarTema(darkModeAtivo);

        editTextClube = findViewById(R.id.editTextClube);
        editTextDataFundacao = findViewById(R.id.editTextDataFundacao);
        checkBoxMundial = findViewById(R.id.checkBoxMundial);
        radioGroupDivisao = findViewById(R.id.radioGroupDivisao);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        radioButtonA = findViewById((R.id.radioButtonSerieA));
        radioButtonB = findViewById((R.id.radioButtonSerieB));
        radioButtonC = findViewById((R.id.radioButtonSerieC));
        radioButtonD = findViewById((R.id.radioButtonSerieD));
        buttonCompeticoes = findViewById((R.id.buttonCompeticao));

        editTextDataFundacao.setFocusable(false);
        editTextDataFundacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDatePickerDialog();
            }
        });

        anosParaTras = getResources().getInteger(R.integer.anos_para_tras);


        dataFundacao = LocalDate.now().minusYears(anosParaTras);
        Intent intentAbertura = getIntent();

        Bundle bundle = intentAbertura.getExtras();

        popularSpinner();

        if(bundle != null){
            modo =bundle.getInt(KEY_MODO);

            if (modo == MODO_NOVO){
                setTitle(getString(R.string.novo_clube));

                buttonCompeticoes.setVisibility(View.INVISIBLE);
            }else {
                setTitle(getString(R.string.editar_clube));

                long id = bundle.getLong(KEY_ID);

                ClubesDatabase database = ClubesDatabase.getInstance(this);
                clubeOriginal = database.getClubeDao().queryForId(id);


                editTextClube.setText(clubeOriginal.getNome());

                dataFundacao = clubeOriginal.getDataFundacao();

                if(clubeOriginal.getDataFundacao() != null){
                    dataFundacao = clubeOriginal.getDataFundacao();
                }

                editTextDataFundacao.setText(UtilsLocalDate.formatLocalDate(dataFundacao));


                checkBoxMundial.setChecked(clubeOriginal.isMundial());
                spinnerEstado.setSelection(clubeOriginal.getEstado());

                Divisao divisao = clubeOriginal.getDivisao();

                if(divisao == Divisao.Serie_A){
                    radioButtonA.setChecked(true);
                } else if (divisao == Divisao.Serie_B) {
                    radioButtonB.setChecked(true);
                } else if (divisao == Divisao.Serie_C) {
                    radioButtonC.setChecked(true);
                } else if (divisao == Divisao.Serie_D) {
                    radioButtonD.setChecked(true);
                }

                editTextClube.requestFocus();
                editTextClube.setSelection(editTextClube.getText().length());

                int totalCompeticoes = database.getCompeticoesDao().totalIdClube(clubeOriginal.getId());

                buttonCompeticoes.setText(getString(R.string.competicoes, totalCompeticoes));
            }
        }

    }

    private void mostrarDatePickerDialog(){
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                dataFundacao = LocalDate.of(year, month + 1, dayOfMonth);

                editTextDataFundacao.setText(UtilsLocalDate.formatLocalDate(dataFundacao));
            }
        };


        DatePickerDialog picker = new DatePickerDialog(this,
                                                                    R.style.SpinnerDatePickerDialogTheme,
                                                                    listener,
                                                                    dataFundacao.getYear(),
                                                                    dataFundacao.getMonthValue() - 1,
                                                                    dataFundacao.getDayOfMonth());
        long dataMaximaMillis = UtilsLocalDate.toMillissegundos(LocalDate.now());

        picker.getDatePicker().setMaxDate(dataMaximaMillis);
        picker.show();
    }


    private void popularSpinner(){
        ArrayList<String> lista = new ArrayList<>();

        lista.add(getString(R.string.acre));
        lista.add(getString(R.string.alagoas));
        lista.add(getString(R.string.amap));
        lista.add(getString(R.string.amazonas));
        lista.add(getString(R.string.bahia));
        lista.add(getString(R.string.cear));
        lista.add(getString(R.string.esp_rito_santo));
        lista.add(getString(R.string.goi_s));
        lista.add(getString(R.string.maranh_o));
        lista.add(getString(R.string.mato_grosso));
        lista.add(getString(R.string.mato_grosso_do_sul));
        lista.add(getString(R.string.minas_gerais));
        lista.add(getString(R.string.par));
        lista.add(getString(R.string.para_ba));
        lista.add(getString(R.string.paran));
        lista.add(getString(R.string.pernambuco));
        lista.add(getString(R.string.piau));
        lista.add(getString(R.string.rio_de_janeiro));
        lista.add(getString(R.string.rio_grande_do_norte));
        lista.add(getString(R.string.rio_grande_do_sul));
        lista.add(getString(R.string.rond_nia));
        lista.add(getString(R.string.roraima));
        lista.add(getString(R.string.santa_catarina));
        lista.add(getString(R.string.s_o_paulo));
        lista.add(getString(R.string.sergipe));
        lista.add(getString(R.string.tocantins));
        lista.add(getString(R.string.distrito_federal));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,lista);

        spinnerEstado.setAdapter(adapter);
    }

    public void limparCampos(){

        final String nome = editTextClube.getText().toString();

        final LocalDate dataFundacaoAnterior = dataFundacao;

        final boolean mundial = checkBoxMundial.isChecked();
        final int radioButtonId = radioGroupDivisao.getCheckedRadioButtonId();
        final int estado = spinnerEstado.getSelectedItemPosition();

        final ScrollView scrollView = findViewById(R.id.main);
        final View viewComFoco = scrollView.findFocus();

        editTextClube.setText(null);

        editTextDataFundacao.setText(null);
        dataFundacao = LocalDate.now().minusYears(anosParaTras);

        checkBoxMundial.setChecked(false);
        radioGroupDivisao.clearCheck();
        spinnerEstado.setSelection(0);

        editTextClube.requestFocus();

        Snackbar snackbar = Snackbar.make(scrollView,
                                          R.string.as_entradas_foram_apagadas,
                                          Snackbar.LENGTH_LONG);

        snackbar.setAction(getString(R.string.desfazer), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextClube.setText(nome);
                checkBoxMundial.setChecked(mundial);

                dataFundacao = dataFundacaoAnterior;
                editTextDataFundacao.setText(UtilsLocalDate.formatLocalDate(dataFundacao));

                if(radioButtonId == R.id.radioButtonSerieA){
                    radioButtonA.setChecked(true);
                } else if (radioButtonId == R.id.radioButtonSerieB) {
                    radioButtonB.setChecked(true);
                } else if (radioButtonId == R.id.radioButtonSerieC) {
                    radioButtonC.setChecked(true);
                } else if (radioButtonId == R.id.radioButtonSerieD) {
                    radioButtonD.setChecked(true);
                }


                spinnerEstado.setSelection(estado);

                if(viewComFoco != null){
                    viewComFoco.requestFocus();
                }
            }
        });

        snackbar.show();
    }

    public void salvarValores(){
        String clube = editTextClube.getText().toString();

        if(clube == null || clube.trim().isEmpty()){
            UtilsAlert.mostrarAviso(this,R.string.faltou_entrar_com_o_nome_do_clube);
            editTextClube.requestFocus();
            return;
        }

        clube = clube.trim();

        String dataFundacaoString = editTextDataFundacao.getText().toString();

        if(dataFundacaoString == null || dataFundacaoString.trim().isEmpty()){
            UtilsAlert.mostrarAviso(this, R.string.faltou_entrar_com_data_fundacao);
            return;
        }

        int idade = UtilsLocalDate.diferencaEmAnosParaHoje(dataFundacao);

        if(idade < 1 ){
            UtilsAlert.mostrarAviso(this, R.string.idade_invalida);
            return;
        }

        int radioButtonId = radioGroupDivisao.getCheckedRadioButtonId();

        Divisao divisao;

        if(radioButtonId == R.id.radioButtonSerieA){
            divisao = Divisao.Serie_A;
        }else if(radioButtonId == R.id.radioButtonSerieB) {
            divisao = Divisao.Serie_B;;
        }else if(radioButtonId == R.id.radioButtonSerieC) {
            divisao = Divisao.Serie_C;;
        }else if(radioButtonId == R.id.radioButtonSerieD) {
            divisao = Divisao.Serie_D;;
        } else{
            UtilsAlert.mostrarAviso(this, R.string.faltou_preencher_a_divisao);
            return;
        }

        int estado = spinnerEstado.getSelectedItemPosition();

        if(estado == AdapterView.INVALID_POSITION){
            Toast.makeText(this, R.string.faltou_exibir_tipos_no_spinner, Toast.LENGTH_LONG).show();
            return;
        }

        boolean mundial = checkBoxMundial.isChecked();

        Clube clubes = new Clube(clube, mundial, estado, divisao, dataFundacao);



        if(clube.equals(clubeOriginal)){
            setResult(ClubeActivity.RESULT_CANCELED);
            finish();
            return;
        }

        Intent intentResposta = new Intent();

        ClubesDatabase database =  ClubesDatabase.getInstance(this);

        if(modo == MODO_NOVO){
            long novoId = database.getClubeDao().insert(clubes);

            if(novoId <=0){
                UtilsAlert.mostrarAviso(this, R.string.erro_ao_tentar_inserir);
                return;
            }

            clubes.setId(novoId);



        } else{

            clubes.setId(clubeOriginal.getId());

            int quantidadeAlterada = database.getClubeDao().update(clubes);

            if(quantidadeAlterada != 1){
                UtilsAlert.mostrarAviso(this, R.string.erro_ao_tentar_o_update);
                return;
            }
        }

        intentResposta.putExtra(KEY_ID, clubes.getId());






        setResult(ClubeActivity.RESULT_OK, intentResposta);

        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.clube_opcoes, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        SharedPreferences shared = getSharedPreferences(ClubesActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        boolean darkModeAtivo = shared.getBoolean(KEY_DARK_MODE, false);

        MenuItem itemDark = menu.findItem(R.id.menuItemDarkMode);
        itemDark.setChecked(darkModeAtivo);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if(idMenuItem == R.id.menuItemSalvar){
            salvarValores();
            return true;
        } else if(idMenuItem == R.id.menuItemLimpar){
            limparCampos();
            return true;
        }else if (idMenuItem == R.id.menuItemDarkMode) {
            boolean novoValor = !item.isChecked();
            salvarDarkMode(novoValor);
            item.setChecked(novoValor);
            aplicarTema(novoValor);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
            }
    }

    private void aplicarTema(boolean darkMode){
        if (darkMode) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void salvarDarkMode(boolean ativo){
        SharedPreferences shared = getSharedPreferences(ClubesActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(KEY_DARK_MODE, ativo);
        editor.apply();
    }

    ActivityResultLauncher<Intent> launcherCompeticoes = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult o) {

                    ClubesDatabase database = ClubesDatabase.getInstance(ClubeActivity.this);

                    int totalCompeticoes = database.getCompeticoesDao().totalIdClube(clubeOriginal.getId());

                    buttonCompeticoes.setText(getString(R.string.competicoes, totalCompeticoes));
                }
            });

    public void abrirAnotacoes(View view){

        Intent intentAbertura = new Intent(this, CompeticoesActivity.class);

        intentAbertura.putExtra(CompeticoesActivity.KEY_ID_CLUBE, clubeOriginal.getId());

        launcherCompeticoes.launch(intentAbertura);
    }



}