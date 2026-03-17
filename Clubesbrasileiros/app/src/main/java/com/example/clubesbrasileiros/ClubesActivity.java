package com.example.clubesbrasileiros;

import android.content.Context;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.clubesbrasileiros.modelo.Clube;
import com.example.clubesbrasileiros.persistencia.ClubesDatabase;
import com.example.clubesbrasileiros.utils.UtilsAlert;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

public class ClubesActivity extends AppCompatActivity {

    private ListView ListViewClubes;
    private List<Clube> listaClubes;

    private ClubeAdapter clubeAdapter;

    private int posicaoSelecionada = -1;

    public static final String ARQUIVO_PREFERENCIAS = "com.example.clubesbrasileiros.PREFERENCIAS";

    public static final String KEY_ORDENACAO_ASCENDENTE = "ODERNACAO_ASCENDENTE";

    private boolean ordenacaoAscendente = true;

    private MenuItem menuItemOrdenacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubes);

        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        boolean darkModeAtivo = shared.getBoolean(ClubeActivity.KEY_DARK_MODE, false);
        aplicarTema(darkModeAtivo);

        setTitle(getString(R.string.controle_de_clubes));

        ListViewClubes = findViewById(R.id.listViewClubes);

        ListViewClubes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Clube clube = (Clube) ListViewClubes.getItemAtPosition(i);

                Toast.makeText(getApplicationContext(),
                        getString(R.string.clube_de_nome) +" "+ clube.getNome() + getString(R.string.foi_clicado),
                        Toast.LENGTH_LONG).show();
            }
        });

        ListViewClubes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(actionMode != null){
                    return false;
                }

                posicaoSelecionada = i;

                viewSelecionada = view;
                backgroundDrawable = view.getBackground();

                view.setBackgroundColor(getColor(R.color.corSelecionada ));


                ListViewClubes.setEnabled(false);//////////////////////////////////////////////////////////////////

                actionMode = startSupportActionMode(actionCallback);

                return true;



            }
        });

        lerPreferencias();

        popularListaClubes();
        registerForContextMenu(ListViewClubes);

    }

    private void popularListaClubes(){

        ClubesDatabase database = ClubesDatabase.getInstance(this);

        if(ordenacaoAscendente){
            listaClubes = database.getClubeDao().queryAllAscending();
        } else{
            listaClubes = database.getClubeDao().queryAllDownward();
        }


        clubeAdapter = new ClubeAdapter(this, listaClubes);

        ListViewClubes.setAdapter(clubeAdapter);
    }

    public void abrirSobre(){
        Intent intentAbertura = new Intent(this, SobreActivity.class);

        startActivity(intentAbertura);
    }

    ActivityResultLauncher<Intent> launcherNovoClube = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == ClubesActivity.RESULT_OK){
                        Intent intent = result.getData();

                        Bundle bundle = intent.getExtras();

                        if(bundle != null){
                            long id = bundle.getLong(ClubeActivity.KEY_ID);

                            ClubesDatabase database = ClubesDatabase.getInstance(ClubesActivity.this);

                            Clube clube = database.getClubeDao().queryForId(id);

                            listaClubes.add(clube);

                            ordenarlista();
                        }
                    }



                }
            });

    public void abrirNovoClube(){
        Intent intentAbertura = new Intent(this, ClubeActivity.class);

        intentAbertura.putExtra(ClubeActivity.KEY_MODO, ClubeActivity.MODO_NOVO);

        launcherNovoClube.launch(intentAbertura);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clubes_opcoes, menu);

        menuItemOrdenacao = menu.findItem(R.id.menuItemOrdenacao);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menuItemAdicionar) {
            abrirNovoClube();
            return true;
        } else if (id == R.id.menuItemSobre) {
            abrirSobre();
            return true;
        } else if (id == R.id.menuItemDarkMode) {
            boolean novoValor = !item.isChecked();

            SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
            shared.edit().putBoolean(ClubeActivity.KEY_DARK_MODE, novoValor).apply();

            aplicarTema(novoValor);

            item.setChecked(novoValor);
            return true;
        } else if(id == R.id.menuItemOrdenacao) {

            salvarPreferenciaOrdenacao(!ordenacaoAscendente);
            atualizarIconeDaOrdenacao();
            ordenarlista();
            return true;
                }else{
                return super.onOptionsItemSelected(item);
            }
        }


    private void excluirClube(){

        final Clube clube = listaClubes.get(posicaoSelecionada);
        //String mensagem = getString(R.string.deseja_apagar) + clube.getNome() + "\"";

        String mensagem = getString(R.string.deseja_apagar, clube.getNome());

        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

                ClubesDatabase database = ClubesDatabase.getInstance(ClubesActivity.this);

                int quantidadeAlterada = database.getClubeDao().delete(clube);

                if(quantidadeAlterada != 1){
                    UtilsAlert.mostrarAviso(ClubesActivity.this, R.string.erro_ao_tentar_excluir);
                    return;
                }

                listaClubes.remove(posicaoSelecionada);
                clubeAdapter.notifyDataSetChanged();

                posicaoSelecionada = -1;
                actionMode.finish();
            }
        };
        UtilsAlert.confirmarAcao(this, mensagem, listenerSim, null);


    }

    ActivityResultLauncher<Intent> launcherEditarClube = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == ClubesActivity.RESULT_OK){
                        Intent intent = result.getData();

                        Bundle bundle = intent.getExtras();

                        if(bundle != null){
                            final Clube clubeOriginal = listaClubes.get(posicaoSelecionada);

                            long id = bundle.getLong(ClubeActivity.KEY_ID);

                            final ClubesDatabase database = ClubesDatabase.getInstance(ClubesActivity.this);

                            final Clube clubeEditado = database.getClubeDao().queryForId(id);

                            final Clube cloneClubeOriginal;

                            listaClubes.set(posicaoSelecionada, clubeEditado);

                            ordenarlista();

                            final ConstraintLayout constraintLayout = findViewById(R.id.main);

                            Snackbar snackbar = Snackbar.make(constraintLayout,
                                                              R.string.alteracao_realizada,
                                                              Snackbar.LENGTH_LONG);

                            snackbar.setAction(R.string.desfazer, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    int quantidadeAlterada = database.getClubeDao().update(clubeOriginal);

                                    if(quantidadeAlterada != 1){
                                        UtilsAlert.mostrarAviso(ClubesActivity.this, R.string.erro_ao_tentar_o_update);
                                        return;
                                    }

                                    listaClubes.remove(clubeEditado);
                                    listaClubes.add(clubeOriginal);

                                    ordenarlista();

                                }
                            });
                            snackbar.show();
                        }
                    }


                    posicaoSelecionada = -1;
                    if(actionMode != null){
                        actionMode.finish();
                    }
                }
            });

    private void editarClube(){

        Clube clube = listaClubes.get(posicaoSelecionada);

        Intent intentAbertura = new Intent(this, ClubeActivity.class);

        intentAbertura.putExtra(ClubeActivity.KEY_MODO, ClubeActivity.MODO_EDITAR);

        intentAbertura.putExtra(ClubeActivity.KEY_ID, clube.getId());


        launcherEditarClube.launch(intentAbertura);

    }



    private ActionMode actionMode;

    private View viewSelecionada;
    private Drawable backgroundDrawable;

    private ActionMode.Callback actionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.item_selecionado,menu);
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
                editarClube();
                return true;
            } else if (idmenuItem == R.id.menuItemExcluir) {
                excluirClube();

                return true;
            } else {
                return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if(viewSelecionada != null){
                viewSelecionada.setBackground(backgroundDrawable);
            }

            actionMode = null;
            viewSelecionada = null;
            backgroundDrawable = null;

            ListViewClubes.setEnabled(true);
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Atualiza o ícone da ordenação
        atualizarIconeDaOrdenacao();

        // Atualiza o estado do Dark Mode
        MenuItem itemDark = menu.findItem(R.id.menuItemDarkMode);
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        boolean darkModeAtivo = shared.getBoolean(ClubeActivity.KEY_DARK_MODE, false);
        itemDark.setChecked(darkModeAtivo);

        return true; // indica que o menu foi atualizado
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

    public void ordenarlista(){
        if(ordenacaoAscendente){
            Collections.sort(listaClubes, Clube.ordenacaoCrescente);
        }else{
            Collections.sort(listaClubes, Clube.ordenacaoDecrescente);
        }

        clubeAdapter.notifyDataSetChanged();
    }

    private void atualizarIconeDaOrdenacao(){
        if(ordenacaoAscendente){
            menuItemOrdenacao.setIcon(R.drawable.ic_action_az);
        }else{
            menuItemOrdenacao.setIcon(R.drawable.ic_action_za);
        }
    }


    private void lerPreferencias(){

            SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
            ordenacaoAscendente = shared.getBoolean(KEY_ORDENACAO_ASCENDENTE, true);
        }



    private void salvarPreferenciaOrdenacao(boolean novoValor){
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putBoolean(KEY_ORDENACAO_ASCENDENTE, novoValor);

        editor.commit();

        ordenacaoAscendente = novoValor;
    }



}