package com.example.clubesbrasileiros.modelo;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Comparator;


@Entity(foreignKeys = {@ForeignKey(entity = Clube.class,
        parentColumns = "id",
        childColumns = "idClube",
        onDelete = CASCADE)})
public class Competicoes {

    public static Comparator<Competicoes> ordenacaoDecrescente = new Comparator<Competicoes>() {

        @Override
        public int compare(Competicoes competicoes1, Competicoes competicoes2) {
            return -1 * competicoes1.getDiaHoraCriacao().compareTo(competicoes2.getDiaHoraCriacao());
        }
    };


    @PrimaryKey(autoGenerate = true)
    private long id;

    private long idClube;

    @NonNull
    @ColumnInfo(index = true)
    private LocalDateTime diaHoraCriacao;

    private String text;



    public Competicoes(long idClube, @NonNull LocalDateTime diaHoraCriacao, String text) {
        this.idClube = idClube;
        this.diaHoraCriacao = diaHoraCriacao;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdClube() {
        return idClube;
    }

    public void setIdClube(long idClube) {
        this.idClube = idClube;
    }

    @NonNull
    public LocalDateTime getDiaHoraCriacao() {
        return diaHoraCriacao;
    }

    public void setDiaHoraCriacao(@NonNull LocalDateTime diaHoraCriacao) {
        this.diaHoraCriacao = diaHoraCriacao;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
