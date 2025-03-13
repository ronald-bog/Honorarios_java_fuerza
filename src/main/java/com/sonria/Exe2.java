package com.sonria;

public class Exe2 {

    protected static void exe2() {
        int colFileA = 5; // Columna en fileA para buscar coincidencias
        int newColumFileA = 6;    // Columna en fileA donde se pegará el valor
        String fileB = "PLANES_TARIFARIOS.csv"; // Archivo de búsqueda (de donde se obtendrán los valores)
        int colFindB = 5; // Columna en fileB para buscar coincidencias
        int colResultB = 6;   // Columna en fileB de donde se copiará el valor
        Functions.functions(colFileA, newColumFileA, fileB, colFindB, colResultB);
    }
}
