package com.example.clubesbrasileiros;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences shared = getSharedPreferences(ClubesActivity.ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        boolean darkModeAtivo = shared.getBoolean(ClubeActivity.KEY_DARK_MODE, false);
        aplicarTema(darkModeAtivo);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        setTitle(R.string.sobre);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sobre_opcoes, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemDark = menu.findItem(R.id.menuItemDarkMode);
        SharedPreferences shared = getSharedPreferences(ClubesActivity.ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        boolean darkModeAtivo = shared.getBoolean(ClubeActivity.KEY_DARK_MODE, false);
        itemDark.setChecked(darkModeAtivo);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuItemDarkMode) {
            boolean novoValor = !item.isChecked();
            SharedPreferences shared = getSharedPreferences(ClubesActivity.ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
            shared.edit().putBoolean(ClubeActivity.KEY_DARK_MODE, novoValor).apply();
            item.setChecked(novoValor);
            aplicarTema(novoValor);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void aplicarTema(boolean darkMode) {
        if (darkMode) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void abrirLinkedin(View view){
        abrirSite("https://www.linkedin.com/in/rafael-tom%C3%A9-da-silva-66096a2a6/");
    }

    private void abrirSite(String endereco){
        Intent intentAbertura = new Intent(Intent.ACTION_VIEW);

        intentAbertura.setData(Uri.parse(endereco));

        if(intentAbertura.resolveActivity(getPackageManager()) != null){
            startActivity(intentAbertura);
        } else{
            Toast.makeText(this,
                           R.string.nenhum_aplicativo_para_abrir_pagina_web,
                                  Toast.LENGTH_LONG).show();
        }
    }

    public void enviarEmailAutor(View view){
        enviarEmail(new String[]{"rafaeltomesilva@alunos.utfpr.edu.br"},getString(R.string.contato_pelo_aplicativo));
    }

    private void enviarEmail(String[] enderecos, String assunto){

    Intent intentAbertura = new Intent(Intent.ACTION_SENDTO);

    intentAbertura.setData(Uri.parse("mailto:"));
    intentAbertura.putExtra(Intent.EXTRA_EMAIL, enderecos);
    intentAbertura.putExtra(Intent.EXTRA_SUBJECT, assunto);

    if(intentAbertura.resolveActivity(getPackageManager()) != null){
        startActivity(intentAbertura);
    } else{
        Toast.makeText(this,
                R.string.nenhum_aplicativo_para_enviar_um_e_mail,
                Toast.LENGTH_LONG).show();
    }
    }

   // @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int idMenuItem = Item.getItemId();
//
//        if(idMenuItem == android.R.id.home){
//            finish();
//            return true;
//        }else{
//            return super.onOptionsItemSelected(item);
//        }
//
//    }
}