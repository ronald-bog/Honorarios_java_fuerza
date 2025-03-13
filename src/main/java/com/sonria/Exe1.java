package com.sonria;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.logging.Level;

public class Exe1 {
    private static final Logger logger = Logger.getLogger(Exe1.class.getName());
    private static final String UNIFIED = "unified.csv";
    private static final String RESULT = "result.csv";
    private static final String[] TITULOS = {"ID CONTRATO PROFESIONAL IS",
            "HONORARIO",
            "FECHA HONORARIO",
            "ID PACIENTE",
            "PLAN TARIFARIO",
            "PROCEDIMIENTO",
            "NOMBRE PROCEDIMIENTO",
            "SUBPROCEDIMIENTO",
            "NOMBRE SUBPROCEDIMIENTO",
            "ZONA",
            "UBICACIÓN",
            "VALOR EVOLUCION",
            "ESPECIALIDAD PROCEDIMIENTO",
            "FORMA PAGO",
            "ID PROFESIONAL",
            "CENTRO DE COSTO",
            "NIT",
            "CONCEPTO",
            "% HONORARIO",
            "VALOR CONTRATADO",
            "id honorario",
            "numero  contrato",
            "fecha  factura evolucion",
            "TIPO HONORARIO",
            "CLINICA",
            "ESPECIALIDAD PROFESIONAL",
            "NOMBRE PROFESIONAL",
            "CLASE NOMINA",
            "CONCEPTO HOMOLOGADO",
            "FECHA DEV"};

    private static void processFiles() {
        Object[][] archivos = {
                //archivos Nomina
                {"GARD_Nuevos.csv", "GARANTIAS NEGATIVAS", true},
                {"HNE_Nuevos.csv", "HONORARIOS NEGATIVOS", true},
                {"HON_Nuevos.csv", "HONORARIOS POSITIVOS", false},
                {"GARP_Nuevos.csv", "GARANTIAS POSITIVAS", false}
        };

        List<String> allData = new ArrayList<>();
        for (Object[] archivo : archivos) {
            try {
                List<String> lines = Files.readAllLines(Paths.get((String) archivo[0]));
                for (String line : lines) {
                    List<String> valueList = new ArrayList<>(Arrays.asList(line.split(";")));
                    if ((boolean) archivo[2]) {
                        String value18 = valueList.size() > 18 ? valueList.remove(18) : "N/A";
                        valueList.add((String) archivo[1]);
                        valueList.add(value18);
                        allData.add(String.join(";", valueList));
                    } else {
                        allData.add(line + ";" + archivo[1] + ";_");
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
            }
        }
        String content = String.join("\r\n", allData);
        try {
            Files.writeString(Paths.get(UNIFIED), content);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ocurrió un error: ", e);
        }
    }

    private static void insertColumns() {
        List<String> allData = new ArrayList<>();
        try {
            List<String> csvData = Files.readAllLines(Paths.get(UNIFIED));
            for (String rows : csvData) {
                List<String> valueList2 = new ArrayList<>(Arrays.asList(rows.split(";")));
                valueList2.add(6, "");
                valueList2.add(8, "");
                IntStream.rangeClosed(24, 28).forEach(i -> valueList2.add(i, "a" + (i - 23)));
                allData.add(String.join(";", valueList2));
            }
            allData.addFirst(String.join(";", TITULOS));
            String content = String.join("\r\n", allData);
            Files.writeString(Paths.get(RESULT), content);

            // Eliminar el archivo UNIFIED después de escribir RESULT
            Files.deleteIfExists(Paths.get(UNIFIED));

        } catch (IOException e) {
            System.err.println("Error al procesar el archivo: " + e.getMessage());
        }
    }

    protected static void exe1(){
        processFiles();
        insertColumns();
    }
}