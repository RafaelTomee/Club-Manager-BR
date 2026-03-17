package com.example.clubesbrasileiros.persistencia;

import androidx.room.TypeConverter;

import com.example.clubesbrasileiros.modelo.Divisao;

public class ConverterDivisao {

    public static Divisao[] divisoes = Divisao.values();

    @TypeConverter
    public static int fromEnumToInt(Divisao divisao){
        if(divisao == null){
            return -1;
        }
        return divisao.ordinal();
    }

    @TypeConverter
    public static Divisao fromIntToEnum(int ordinal){
        if(ordinal < 0){
            return null;
        }

        return divisoes[ordinal];
    }

}
