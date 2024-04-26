package org.example.controller;

import org.example.funciones.FuncionesExtras;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class CheckArgumentos{
    private List<String> configuraciones;

    public CheckArgumentos(){
        Scanner scanner = new Scanner(System.in);
        CheckNum checkNum = new CheckNum();
        CheckNombres checkNombres = new CheckNombres();
        FuncionesExtras.delayPrint("Bienvenidos al Monopoly! Para jugar necesitamos que ingresen los siguientes datos:", 10);
        List<String> argumentos = new ArrayList<>();
        argumentos.add("Nombres (2 a 4 jugadores y separados por espacios): ");
        argumentos.add("Cantidad de casilleros (mínimo 12): ");
        argumentos.add("Número de caras del dado: ");
        argumentos.add("Monto de dinero inicial (mínimo 100): ");
        argumentos.add("Monto de dinero por vuelta: ");
        argumentos.add("Cantidad de turnos preso: ");
        argumentos.add("Monto de multa: ");
        argumentos.add("Monto de fianza: ");
        List<ConfiguracionCheckArgumentos> configuraciones = Arrays.asList(ConfiguracionCheckArgumentos.values());
        List<String> inputs = new ArrayList<>();
        for (int contador = 0; contador < argumentos.size(); contador++) {
            System.out.print(argumentos.get(contador));
            String input = scanner.nextLine();
            if (contador == 0) {
                boolean isValid = checkNombres.checkNombres(input);
                while (!isValid) {
                    System.out.print(argumentos.get(contador));
                    input = scanner.nextLine();
                    isValid = checkNombres.checkNombres(input);
                }
                inputs.add(input);
            } else {
                boolean isValid = checkNum.checkNumeros(input, configuraciones.get(contador));
                while (!isValid) {
                    System.out.print(argumentos.get(contador));
                    input = scanner.nextLine();
                    isValid = checkNum.checkNumeros(input, configuraciones.get(contador));
                }
                inputs.add(input);
            }
        }
        this.configuraciones = inputs;
    }

    public List<String> getConfiguraciones() {
        return this.configuraciones;
    }


}
