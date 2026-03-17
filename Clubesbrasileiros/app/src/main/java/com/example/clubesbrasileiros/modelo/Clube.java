package com.example.clubesbrasileiros.modelo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

@Entity
public class Clube implements Cloneable {

    public static Comparator<Clube> ordenacaoCrescente = new Comparator<Clube>(){
        @Override
        public int compare(Clube clube1, Clube clube2){
            return clube1.getNome().compareToIgnoreCase(clube2.getNome());
        }
    };

    public static Comparator<Clube> ordenacaoDecrescente = new Comparator<Clube>(){
        @Override
        public int compare(Clube clube1, Clube clube2){
            return -1 * clube1.getNome().compareToIgnoreCase(clube2.getNome());
        }
    };
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(index = true)
    private String nome;

    private  boolean mundial;

    private int estado;

    private Divisao divisao;

    private LocalDate dataFundacao;

    public Clube(String nome, boolean mundial, int estado, Divisao divisao, LocalDate dataFundacao) {
        this.nome = nome;
        this.mundial = mundial;
        this.estado = estado;
        this.divisao = divisao;
        this.dataFundacao = dataFundacao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isMundial() {
        return mundial;
    }

    public void setMundial(boolean mundial) {
        this.mundial = mundial;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Divisao getDivisao() {
        return divisao;
    }

    public void setDivisao(Divisao divisao) {
        this.divisao = divisao;
    }

    public LocalDate getDataFundacao() {
        return dataFundacao;
    }

    public void setDataFundacao(LocalDate dataFundacao) {
        this.dataFundacao = dataFundacao;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {

        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Clube clube = (Clube) o;

        if(dataFundacao == null && clube.dataFundacao != null){
            return false;
        }

        if(dataFundacao != null && dataFundacao.equals(clube.dataFundacao) == false){
            return false;
        }

        return mundial == clube.mundial &&
                estado == clube.estado &&
                nome.equals(clube.nome) &&
                divisao == clube.divisao;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, mundial, estado, divisao, dataFundacao);
    }

    @Override
    public String toString() {
        return nome + "\n" +
                mundial + "\n" +
                estado + "\n" +
                divisao + "\n" +
                dataFundacao
                ;
    }
}
