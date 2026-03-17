package com.example.clubesbrasileiros.persistencia;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migrar_3_4 extends Migration {

    public Migrar_3_4(){
        super(4, 5);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database){

        database.execSQL("CREATE TABLE IF NOT EXISTS `Competicoes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `idClube` INTEGER NOT NULL, `diaHoraCriacao` INTEGER NOT NULL, `text` TEXT, FOREIGN KEY(`idClube`) REFERENCES `Clube`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_Competicoes_diaHoraCriacao` ON `Competicoes` (`diaHoraCriacao`)");
    }
}
