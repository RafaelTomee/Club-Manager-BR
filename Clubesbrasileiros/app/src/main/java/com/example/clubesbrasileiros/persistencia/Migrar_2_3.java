package com.example.clubesbrasileiros.persistencia;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migrar_2_3 extends Migration {

    public Migrar_2_3(){
        super(2, 3);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database){

        database.execSQL("ALTER TABLE Clube ADD COLUMN dataFundacao INTEGER");

    }
}
