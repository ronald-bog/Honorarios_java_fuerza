package com.sonria;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        long inicioTiempo = System.nanoTime();

        // Definir las tareas a ejecutar
        Runnable[] tareas = {
                Exe1::exe1, Exe2::exe2, Exe3::exe3, Exe4::exe4,
                Exe5::exe5, Exe6::exe6, Exe7::exe7, Exe8::exe8
        };

        int totalTareas = tareas.length;
        System.out.println("""
                 _   _       _              __ _  \s
                | \\ | | __ _| |_ ___  ___  / _| |_\s
                |  \\| |/ _` | __/ __|/ _ \\| |_| __|
                | |\\  | (_| | |_\\__ \\ (_) |  _| |_\s
                |_| \\_|\\__,_|\\__|___/\\___/|_|  \\__|
                                                  \s
                Developed by Ronald Espitia ©
                """);
        System.out.println("■ Iniciando el proceso...");
        System.out.println();
        System.out.println("■ ARCHIVOS FUERZA DE VENTAS");
        System.out.println();
        for (int i = 0; i < totalTareas; i++) {
            tareas[i].run();
            mostrarBarraProgreso(i + 1, totalTareas);
        }

        // Calcular el tiempo total de ejecución
        long finTiempo = System.nanoTime();
        long duracion = finTiempo - inicioTiempo;
        double duracionSegundos = duracion / 1_000_000_000.0; // Convertir a segundos

        System.out.println("\n■ Proceso completado. Archivo \"result.csv\" actualizado.");
        System.out.println();
        System.out.printf("■ Tiempo total de ejecución: %.4f segundos%n", duracionSegundos);
        System.out.println();
        // Opción para salir
        Scanner scanner = new Scanner(System.in);
        System.out.println("» Presione ENTER para salir...");
        scanner.nextLine();
        scanner.close();
    }

    private static void mostrarBarraProgreso(int progreso, int total) {
        int anchoBarra = 30; // Tamaño de la barra de progreso
        int completado = (int) ((double) progreso / total * anchoBarra);

        StringBuilder barra = new StringBuilder("[" + "█".repeat(completado) + " ".repeat(anchoBarra - completado) + "]");
        System.out.print("\r" + barra + " " + (progreso * 100 / total) + "%");
    }
}

