package com.example.clubesbrasileiros.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.clubesbrasileiros.modelo.Competicoes;

import java.util.List;


@Dao
public interface CompeticoesDao {
    @Insert
    long insert(Competicoes competicoes);

    @Delete
    int delete  (Competicoes competicoes);

    @Update
    int update(Competicoes competicoes);

    @Query("SELECT * FROM Competicoes WHERE id=:id")
    Competicoes queryForId(long id);

    @Query("SELECT * FROM Competicoes WHERE idClube=:idClube ORDER BY diaHoraCriacao DESC")
    List<Competicoes> queryForIdClube(long idClube);

    @Query("SELECT count(*) FROM Competicoes WHERE idClube =:idClube")
    int totalIdClube(long idClube);

}
