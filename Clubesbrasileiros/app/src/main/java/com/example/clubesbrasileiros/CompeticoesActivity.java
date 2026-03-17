package com.example.clubesbrasileiros;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;

import com.example.clubesbrasileiros.modelo.Clube;
import com.example.clubesbrasileiros.modelo.Competicoes;
import com.example.clubesbrasileiros.persistencia.ClubesDatabase;
import com.example.clubesbrasileiros.utils.UtilsAlert;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class CompeticoesActivity extends AppCompatActivity {

    public static final String KEY_ID_CLUBE = "KEY_ID_CLUBE";
    private ListView ListViewCompeticoes;
    private List<Competicoes> listaCompeticoes;

    private CompeticoesAdapter competicoesAdapter;

    private int posicaoSelecionada = -1;

    public static final String ARQUIVO_PREFERENCIAS = "com.example.clubesbrasileiros.PREFERENCIAS";

    public static final String KEY_ORDENACAO_ASCENDENTE = "ODERNACAO_ASCENDENTE";

    private boolean ordenacaoAscendente = true;

    private MenuItem menuItemOrdenacao;

    private ActionMode actionMode;

    private View viewSelecionada;
    private Drawable backgroundDrawable;

    private Clube clube;

    private ActionMode.Callback actionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {


            int idmenuItem = item.getItemId();
            if (idmenuItem == R.id.menuitemEditar) {
                editarCompeticoes();
                return true;
            } else if (idmenuItem == R.id.menuItemExcluir) {
                excluirCompeticoes();

                return true;
            } else {
                return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (viewSelecionada != null) {
                viewSelecionada.setBackground(backgroundDrawable);
            }

            actionMode = null;
            viewSelecionada = null;
            backgroundDrawable = null;

            ListViewCompeticoes.setEnabled(true);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competicoes);

        Intent intentAbertura = getIntent();

        Bundle bundle = intentAbertura.getExtras();

        if (bundle != null) {

            long id = bundle.getLong(KEY_ID_CLUBE);

            ClubesDatabase database = ClubesDatabase.getInstance(this);

            clube = database.getClubeDao().queryForId(id);

            setTitle(getString(R.string.competicoes_de_clube, clube.getNome()));
        } else {
            // Faltou passar o parâmetro na abertura da Activity
        }

        ListViewCompeticoes = findViewById(R.id.ListViewCompeticoes);


        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        boolean darkModeAtivo = shared.getBoolean(ClubeActivity.KEY_DARK_MODE, false);
        aplicarTema(darkModeAtivo);


        ListViewCompeticoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Clube clube = (Clube) ListViewCompeticoes.getItemAtPosition(i);

                Toast.makeText(getApplicationContext(),
                        getString(R.string.clube_de_nome) + " " + clube.getNome() + getString(R.string.foi_clicado),
                        Toast.LENGTH_LONG).show();
            }
        });

        ListViewCompeticoes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (actionMode != null) {
                    return false;
                }

                posicaoSelecionada = i;

                viewSelecionada = view;
                backgroundDrawable = view.getBackground();

                view.setBackgroundColor(getColor(R.color.corSelecionada));


                ListViewCompeticoes.setEnabled(false);//////////////////////////////////////////////////////////////////

                actionMode = startSupportActionMode(actionCallback);

                return true;


            }
        });


        popularListaCompeticoes();
        registerForContextMenu(ListViewCompeticoes);

    }

    private void popularListaCompeticoes() {

        ClubesDatabase database = ClubesDatabase.getInstance(this);

        listaCompeticoes = database.getCompeticoesDao().queryForIdClube(clube.getId());


        competicoesAdapter = new CompeticoesAdapter(this, listaCompeticoes);

        ListViewCompeticoes.setAdapter(competicoesAdapter);
    }

    public void abrirSobre() {
        Intent intentAbertura = new Intent(this, SobreActivity.class);

        startActivity(intentAbertura);
    }


//public void novaCompeticoes(){
//    Intent intentAbertura = new Intent(this, ClubeActivity.class);
//
//    intentAbertura.putExtra(ClubeActivity.KEY_MODO, ClubeActivity.MODO_NOVO);
//
//    launcherNovoClube.launch(intentAbertura);
//}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.competicoes_opcoes, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menuItemAdicionar) {
            novaCompeticoes();
            return true;
        } else if (id == R.id.menuItemDarkMode) {
            boolean novoValor = !item.isChecked();

            SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
            shared.edit().putBoolean(ClubeActivity.KEY_DARK_MODE, novoValor).apply();

            aplicarTema(novoValor);

            item.setChecked(novoValor);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void excluirCompeticoes() {

        final Competicoes competicoes = listaCompeticoes.get(posicaoSelecionada);

        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ClubesDatabase database = ClubesDatabase.getInstance(CompeticoesActivity.this);

                int quantidadeAlterada = database.getCompeticoesDao().delete(competicoes);

                if (quantidadeAlterada != 1) {
                    UtilsAlert.mostrarAviso(CompeticoesActivity.this, R.string.erro_ao_tentar_excluir);
                    return;
                }

                listaCompeticoes.remove(posicaoSelecionada);
                competicoesAdapter.notifyDataSetChanged();

                posicaoSelecionada = -1;
                actionMode.finish();
            }
        };
        UtilsAlert.confirmarAcao(this, getString(R.string.deseja_apagar_competicoes), listenerSim, null);


    }

    private void novaCompeticoes() {
        UtilsAlert.OnTextEnteredListener listener = new UtilsAlert.OnTextEnteredListener() {

            @Override
            public void onTextEntered(String texto) {

                if (texto == null || texto.trim().isEmpty()) {
                    UtilsAlert.mostrarAviso(CompeticoesActivity.this, R.string.o_texto_nao_pode_ser_vazio);
                    return;
                }

                Competicoes competicoes = new Competicoes(clube.getId(), LocalDateTime.now(), texto);

                ClubesDatabase database = ClubesDatabase.getInstance(CompeticoesActivity.this);

                long novoId = database.getCompeticoesDao().insert(competicoes);

                if (novoId <= 0) {
                    UtilsAlert.mostrarAviso(CompeticoesActivity.this, R.string.erro_ao_tentar_inserir);
                    return;
                }

                competicoes.setId(novoId);

                listaCompeticoes.add(competicoes);

                Collections.sort(listaCompeticoes, Competicoes.ordenacaoDecrescente);
//            CompeticoesAdapter.notifyDataSetChanged();
            }
        };
        UtilsAlert.lerTexto(this, R.string.nova_competicao, R.layout.entrada_competicoes, R.id.editTextCompeticao, null, listener);
    }


    private void editarCompeticoes() {
        final Competicoes competicoes = listaCompeticoes.get(posicaoSelecionada);

        UtilsAlert.OnTextEnteredListener listener = new UtilsAlert.OnTextEnteredListener() {

            @Override
            public void onTextEntered(String texto) {

                if (texto == null || texto.trim().isEmpty()) {
                    UtilsAlert.mostrarAviso(CompeticoesActivity.this, R.string.o_texto_nao_pode_ser_vazio);
                    return;
                }

                if (texto.equalsIgnoreCase(competicoes.getText())){
                    return;
                }

                competicoes.setText(texto);


                ClubesDatabase database = ClubesDatabase.getInstance(CompeticoesActivity.this);

                long novoId = database.getCompeticoesDao().update(competicoes);

                if (novoId <= 0) {
                    UtilsAlert.mostrarAviso(CompeticoesActivity.this, R.string.erro_ao_tentar_o_update);
                    return;
                }

                competicoes.setId(novoId);
                posicaoSelecionada = -1;

                Collections.sort(listaCompeticoes, Competicoes.ordenacaoDecrescente);
//            CompeticoesAdapter.notifyDataSetChanged();

                if(actionMode != null){
                    actionMode.finish();
                }
            }
        };

        UtilsAlert.lerTexto(this, R.string.edite_texto_competicao, R.layout.entrada_competicoes, R.id.editTextCompeticao, competicoes.getText(), listener);
    }





    private void aplicarTema(boolean darkMode) {
        if (darkMode) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        boolean darkModeAtivo = shared.getBoolean(ClubeActivity.KEY_DARK_MODE, false);
        aplicarTema(darkModeAtivo);
    }

}






