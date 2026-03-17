package com.example.clubesbrasileiros.persistencia;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


public class Migrar_1_2  extends Migration {

    public Migrar_1_2(){
        super(1,2);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database){
        database.execSQL("CREATE TABLE IF NOT EXISTS `Clube_provisorio` (`" +
                "id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `" +
                "nome` TEXT NOT NULL, `" +
                "mundial` INTEGER NOT NULL, `" +
                "estado` INTEGER NOT NULL, `" +
                "divisao` INTEGER)");

        database.execSQL("INSERT INTO Clube_provisorio(id, nome, mundial, estado, divisao)"+
                            "SELECT id, nome, mundial, estado, "+
                            "CASE " +
                            "WHEN divisao = 'Serie_A' THEN 0 "+
                            "WHEN divisao = 'Serie_B' THEN 1 "+
                            "WHEN divisao = 'Serie_C' THEN 2 "+
                            "WHEN divisao = 'Serie_D' THEN 3 "+
                            "ELSE -1 " +
                            "END " +
                            "FROM Clube");

        database.execSQL("DROP TABLE Clube");
        database.execSQL("ALTER TABLE Clube_provisorio RENAME TO Clube");

        database.execSQL("CREATE INDEX IF NOT EXISTS `index_Clube_nome` ON `Clube` (`nome`)");


    }


}
