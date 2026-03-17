package com.example.clubesbrasileiros.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.clubesbrasileiros.modelo.Clube;
import com.example.clubesbrasileiros.modelo.Competicoes;

@Database(entities = {Clube.class, Competicoes.class}, version = 4)
@TypeConverters({ConverterDivisao.class, ConverterLocalDate.class, ConverterLocalDateTime.class})
public abstract class ClubesDatabase extends RoomDatabase {

    public abstract ClubeDao getClubeDao();
    public abstract CompeticoesDao getCompeticoesDao();

    private static ClubesDatabase INSTANCE;

    public static ClubesDatabase getInstance(final Context context){
        if(INSTANCE == null) {
            synchronized (ClubesDatabase.class){
                if(INSTANCE == null){

//                    INSTANCE = Room.databaseBuilder(context,
//                                                    ClubesDatabase.class,
//                                                    "clubes.db").allowMainThreadQueries().build();

                    Builder builder = Room.databaseBuilder(context, ClubesDatabase.class,"clubes.db");

                    builder.allowMainThreadQueries();
                    builder.addMigrations(new Migrar_1_2());
                    builder.addMigrations(new Migrar_2_3());
                    builder.addMigrations(new Migrar_3_4());

//                    builder.fallbackToDestructiveMigration();

                    INSTANCE = (ClubesDatabase) builder.build();

                }
            }
        }
        return INSTANCE;
    }


}
