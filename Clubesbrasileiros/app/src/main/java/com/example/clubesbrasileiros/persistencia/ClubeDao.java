package com.example.clubesbrasileiros.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.clubesbrasileiros.modelo.Clube;

import java.util.List;

@Dao
public interface ClubeDao {

    @Insert
    long insert(Clube clube);

    @Delete
    int delete(Clube clube);

    @Update
    int update(Clube clube);

    @Query("SELECT * FROM clube WHERE id=:id")
    Clube queryForId(long id);

    @Query("SELECT * FROM clube ORDER BY nome ASC")
    List<Clube> queryAllAscending();

    @Query("SELECT * FROM clube ORDER BY nome ASC")
    List<Clube> queryAllDownward();
}
