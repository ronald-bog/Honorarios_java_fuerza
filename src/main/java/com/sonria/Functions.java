package com.sonria;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Functions {

    public static void functions(int colFileA, int newColumFileA, String fileB, int colFindB,int colResultB) {
        String fileA = "result.csv"; // Archivo principal (se modificará directamente)

        // Mapa para almacenar los valores de búsqueda y copia del fileB
        Map<String, String> mapaBusqueda = new ConcurrentHashMap<>();

        // Leer el fileB y almacenar los valores en un mapa
        try (BufferedReader br = new BufferedReader(new FileReader(fileB))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(";");
                if (valores.length > colResultB ) {
                    String clave = valores[colFindB].trim(); // Valor de búsqueda
                    String valor = valores[colResultB ].trim();   // Valor a copiar
                    mapaBusqueda.put(clave, valor);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Crear un ExecutorService con un número de hilos igual al número de núcleos disponibles
        int numHilos = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numHilos);

        // Leer el fileA y almacenar las líneas en una lista
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Arreglo para almacenar las líneas procesadas en el orden original
        String[] lineasProcesadas = new String[lineas.size()];

        // Dividir las líneas en bloques para procesamiento concurrente
        int numLineas = lineas.size();
        int tamanoBloque = (numLineas + numHilos - 1) / numHilos; // Tamaño de cada bloque

        List<Callable<Void>> tareas = new ArrayList<>();
        for (int i = 0; i < numHilos; i++) {
            int inicio = i * tamanoBloque;
            int fin = Math.min(inicio + tamanoBloque, numLineas);

            // Verificar que el índice de inicio no sea mayor que el número de líneas
            if (inicio >= numLineas) {
                break; // Salir del bucle si no hay más líneas para procesar
            }

            List<String> subLista = lineas.subList(inicio, fin);

            // Crear una tarea para procesar el bloque de líneas
            Callable<Void> tarea = () -> {
                for (int j = 0; j < subLista.size(); j++) {
                    String lin = subLista.get(j);
                    String[] valores = lin.split(";");
                    if (valores.length > newColumFileA) {
                        String clave = valores[colFileA].trim(); // Valor de búsqueda en fileA
                        // Si hay coincidencia, copiar el valor al fileA
                        valores[newColumFileA] = mapaBusqueda.getOrDefault(clave, "error");
                    }
                    // Reconstruir la línea actualizada y guardarla en la posición correcta
                    lineasProcesadas[inicio + j] = String.join(";", valores);
                }
                return null;
            };
            tareas.add(tarea);
        }

        // Ejecutar todas las tareas en paralelo
        try {
            executor.invokeAll(tareas);
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Sobrescribir el fileA con el contenido actualizado
        try (FileWriter fw = new FileWriter(fileA)) {
            for (String linea : lineasProcesadas) {
                fw.write(linea + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Apagar el ExecutorService
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}