package com.mycompany.grafos;

import com.mycompany.grafos.grafo.Arista;
import com.mycompany.grafos.csv.CompiladorCSV;
import com.mycompany.grafos.grafo.Nodo;
import com.mycompany.grafos.metodos.BusquedasACiegas;
import com.mycompany.grafos.metodos.BusquedasHeuristicas;
import javafx.util.Pair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class main {

    public static void main(String[] args) throws IOException {

        HashMap<String, Nodo> grafo = CompiladorCSV.cargarDatos("src\\main\\java\\com\\mycompany\\grafos\\csv\\grafo.csv");
        if (grafo == null){
            System.out.println("Error al cargar el archivo.");
            return;
        }

        boolean salir = false;

        while(!salir){
            
            System.out.println("""
                   \n============MENU============= 
                   1) Busqueda en Amplitud
                   2) Busqueda en Profundida
                   3) Busqueda Bidireccional
                   4) Busqueda Profundida Iterativa
                   5) Busqueda Costo Uniforme
                   6) Busqueda del Gradiente
                   7) Busqueda Primero el Mejor
                   8) Busqueda A*
                   9) Comparacion todos los metodos
                   10) Salir
                   Opción:""");

            Scanner sc = new Scanner(System.in);
            String opcion = sc.nextLine();
            switch(opcion){
                case "1":
                    busquedaAmplitud(grafo);
                    break;
                case "2":
                    busquedaProfundidad(grafo);
                    break;
                case "3":
                    busquedaBidireccional(grafo);
                    break;
                case "4":
                    busquedaProfundidadIterativa(grafo);
                    break;
                case "5":
                    busquedaCostoUniforme(grafo);
                    break;
                case "6":
                    busquedaDelGradiente(grafo);
                    break;
                case "7":
                    busquedaPrimeroElMejor(grafo);
                    break;
                case "8":
                    busquedaAEstrella(grafo);
                    break;
                case "9":
                    calcularTiemposMetodos(grafo);
//                    System.out.println("Presione Enter para continuar...");
//                    sc.nextLine();
                    break;
                case "10":
                    System.out.println("===Gracias===");
                    salir = true;
                    break;
                default:
                    System.out.println("Ingreso Invalido.");
            }
        }

        System.exit(0);
    }

    public static void calcularTiemposMetodos(HashMap<String, Nodo> grafo){
        ArrayList<Nodo> nodosABuscar = new ArrayList<>();
        Nodo nodoInicial = grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo inicial: ").getNombre());
        int numNodoFinales = ingresarNumero("Ingrese el numero de nodos finales: ");
        for(int i = 0; i < numNodoFinales; i++){
            nodosABuscar.add(grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo final: ").getNombre()));
        }

        int profundidad = calcularProfundidad(nodoInicial);
        int hiosMaximos = contarMaximoHijos(grafo);
        int aristas = contarAristas(grafo);

        System.out.println("|  Nombre Busqueda   |Tiempo de ejecución |Compleidad Espacial |Compleidad Temporal |");
        long tiempoInicio = System.nanoTime();
        BusquedasACiegas.busquedaProfundidad(nodoInicial, new ArrayList<>(nodosABuscar),false);
        long tiempoFinal = System.nanoTime();
        System.out.printf("|%-20s|%-20d|%-20d|%-20d|\n", "Profundidad", (tiempoFinal - tiempoInicio), hiosMaximos * profundidad, (int) Math.pow(hiosMaximos, profundidad));
        //System.out.println("Profundidad       |   " + (tiempoFinal - tiempoInicio) + " ns" + "   |   " + hiosMaximos * profundidad + "   |   " + (int) Math.pow(hiosMaximos, profundidad));

        tiempoInicio = System.nanoTime();
        BusquedasACiegas.busquedaAmplitud(nodoInicial, new ArrayList<>(nodosABuscar),false);
        tiempoFinal = System.nanoTime();
        System.out.printf("|%-20s|%-20d|%-20d|%-20d|\n", "Amplitud", (tiempoFinal - tiempoInicio), (int) Math.pow(hiosMaximos, profundidad), (int) Math.pow(hiosMaximos, profundidad));
        //System.out.println("Amplitud          |   " + (tiempoFinal - tiempoInicio) + " ns" + "   |   " + (int) Math.pow(hiosMaximos, profundidad) + "   |   " + (int) Math.pow(hiosMaximos, profundidad));

        tiempoInicio = System.nanoTime();
        BusquedasACiegas.busquedaBidireccional(nodoInicial, nodosABuscar, false);
        tiempoFinal = System.nanoTime();
        System.out.printf("|%-20s|%-20d|%-20d|%-20d|\n", "Bidireccional", (tiempoFinal - tiempoInicio), (int) Math.pow(hiosMaximos, (double) profundidad / 2), (int) Math.pow(hiosMaximos, (double) profundidad / 2));
        //System.out.println("Bidireccional     |   " + (tiempoFinal - tiempoInicio) + " ns" + "   |   " + (int) Math.pow(hiosMaximos, (double) (int) profundidad / 2) + "   |   " + (int) Math.pow(hiosMaximos, (double) (int) profundidad / 2));

        tiempoInicio = System.nanoTime();
        BusquedasACiegas.busquedaProfundidadIterativa(nodoInicial, new ArrayList<>(nodosABuscar),grafo.size(), false);
        tiempoFinal = System.nanoTime();
        System.out.printf("|%-20s|%-20d|%-20d|%-20d|\n", "Profundidad Iter", (tiempoFinal - tiempoInicio), hiosMaximos * profundidad, (int) Math.pow(hiosMaximos, profundidad));
        //System.out.println("Profundidad Iter  |   " + (tiempoFinal - tiempoInicio) + " ns" + "   |   " + hiosMaximos * profundidad + "   |   " + (int) Math.pow(hiosMaximos, profundidad));

        tiempoInicio = System.nanoTime();
        ArrayList<Pair<Nodo,Integer>> nodo = BusquedasACiegas.busquedaCostoUniforme(nodoInicial, new ArrayList<>(nodosABuscar), grafo.size(),false);
        tiempoFinal = System.nanoTime();
        if (!nodo.isEmpty()){
            System.out.printf("|%-20s|%-20d|%-20d|%-20d|\n", "Costo Uniforme", (tiempoFinal - tiempoInicio), (int) Math.pow(hiosMaximos, (double) (int) nodo.get(0).getRight() / calcularCostoMinimo(grafo)), (int) Math.pow(hiosMaximos, (double) (int) nodo.get(0).getRight() / calcularCostoMinimo(grafo)));
        }else{
            System.out.printf("|%-20s|%-20d|%-20s|%-20s|\n", "Costo Uniforme", (tiempoFinal - tiempoInicio), "Nodo no encontrado", "Nodo no encontrado");
        }
        //System.out.println("Costo Uniforme    |   " + (tiempoFinal - tiempoInicio) + " ns" + "   |   " + (int) Math.pow(hiosMaximos, (double) (int) nodo.get(0).getRight() / calcularCostoMinimo(grafo)) + "   |   " + (int) Math.pow(hiosMaximos, (double) (int) nodo.get(0).getRight() / calcularCostoMinimo(grafo)));

        tiempoInicio = System.nanoTime();
        BusquedasHeuristicas.busquedaDelGradiente(nodoInicial,false);
        tiempoFinal = System.nanoTime();
        System.out.printf("|%-20s|%-20d|%-20d|%-20d|\n", "Del Gradiente", (tiempoFinal - tiempoInicio), 1, (int) Math.pow(hiosMaximos, profundidad));
        //System.out.println("Del Gradiente     |   " + (tiempoFinal - tiempoInicio + " ns" + "   |   " + aristas + "   |   " + aristas));

        tiempoInicio = System.nanoTime();
        BusquedasHeuristicas.busquedaPrimeroElMejor(nodoInicial, nodosABuscar, grafo.size(), false);
        tiempoFinal = System.nanoTime();
        double complejididadTemporal = aristas * Math.log(grafo.size());
        System.out.printf("|%-20s|%-20d|%-20d|%-20d|\n", "Primero Mejor", (tiempoFinal - tiempoInicio), aristas+grafo.size(), (int) complejididadTemporal);
        //System.out.println("Primero Mejor     |   " + (tiempoFinal - tiempoInicio));

        tiempoInicio = System.nanoTime();
        BusquedasHeuristicas.busquedaAEstrella(nodoInicial, nodosABuscar, grafo.size(), false);
        tiempoFinal = System.nanoTime();
        System.out.printf("|%-20s|%-20d|%-20d|%-20d|\n", "A*", (tiempoFinal - tiempoInicio), aristas+grafo.size(), (int) complejididadTemporal);
        //System.out.println("A*                |   " + (tiempoFinal - tiempoInicio));

    }

    //sacar el costo minimo de todas las aristas del grafo
    public static int calcularCostoMinimo(HashMap<String, Nodo> grafo){
        ArrayList<Integer> costos = new ArrayList<>();
        grafo.forEach((k, v) -> v.getHijosAristas().forEach(t -> costos.add(t.getPeso())));
        return costos.stream().min(Integer::compare).get();
    }

    //contar todas las aristas en el grafo
    public static int contarAristas(HashMap<String, Nodo> grafo){
        int aristas = 0;
        ArrayList<Integer> numeroAristas = new ArrayList<>();
        grafo.forEach((k, v) -> numeroAristas.add(v.getHijos().size()));
        for(int i : numeroAristas){
            aristas += i;
        }
        return aristas;
    }

    //contar la cantidad de hijo de todos los nodos
    public static int contarMaximoHijos(HashMap<String, Nodo> grafo){
        ArrayList<Integer> hijos = new ArrayList<>();
        grafo.forEach((k, v) -> hijos.add(v.getHijos().size()));
        //sacar el mayor
        return hijos.stream().max(Integer::compare).get();
    }


    //calcular la profundidad mas grande en el grafo
    public static int calcularProfundidad(Nodo nodoRaiz){
        int profundidad = 0;
        ArrayList<Nodo> hijos = new ArrayList<>();
        ArrayList<Nodo> padres = new ArrayList<>();
        ArrayList<Nodo> nodosVisitados = new ArrayList<>();
        hijos.add(nodoRaiz);
        while(!hijos.isEmpty()){
            padres.addAll(hijos);
            hijos.clear();
            for(Nodo nodo : padres){
                if(!nodosVisitados.contains(nodo)){
                    nodosVisitados.add(nodo);
                    hijos.addAll(nodo.getHijos());
                }
            }
            profundidad++;
        }
        return profundidad;
    }

    public static void busquedaAmplitud(HashMap<String, Nodo> grafo){
        ArrayList<Nodo> nodosABuscar = new ArrayList<>();
        Nodo nodoInicial = grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo inicial: ").getNombre());
        int numNodoFinales = ingresarNumero("Ingrese el numero de nodos finales: ");
        for(int i = 0; i < numNodoFinales; i++){
            nodosABuscar.add(grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo final: ").getNombre()));
        }
        ArrayList<Nodo> nodosEncontrado = BusquedasACiegas.busquedaAmplitud(nodoInicial, nodosABuscar,true);
        System.out.print("Nodos Encontrados: ");
        imprimirNodosEncontrados(nodosEncontrado);
    }

    public static void busquedaProfundidad(HashMap<String, Nodo> grafo){
        ArrayList<Nodo> nodosABuscar = new ArrayList<>();
        Nodo nodoInicial = grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo inicial: ").getNombre());
        int numNodoFinales = ingresarNumero("Ingrese el numero de nodos finales: ");
        for(int i = 0; i < numNodoFinales; i++){
            nodosABuscar.add(grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo final: ").getNombre()));
        }
        ArrayList<Nodo> nodosEncontrado = BusquedasACiegas.busquedaProfundidad(nodoInicial, nodosABuscar,true);
        imprimirNodosEncontrados(nodosEncontrado);
    }

    public static void busquedaBidireccional(HashMap<String, Nodo> grafo){
        ArrayList<Nodo> nodosABuscar = new ArrayList<>();
        Nodo nodoInicial = grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo inicial: ").getNombre());
        int numNodoFinales = ingresarNumero("Ingrese el numero de nodos finales: ");
        for(int i = 0; i < numNodoFinales; i++){
            nodosABuscar.add(grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo final: ").getNombre()));
        }
        ArrayList<Nodo> nodosEncontrado = BusquedasACiegas.busquedaBidireccional(nodoInicial, nodosABuscar, true);
        imprimirNodosEncontrados(nodosEncontrado);
    }

    public static void busquedaProfundidadIterativa(HashMap<String, Nodo> grafo){
        ArrayList<Nodo> nodosABuscar = new ArrayList<>();
        Nodo nodoInicial = grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo inicial: ").getNombre());
        int numNodoFinales = ingresarNumero("Ingrese el numero de nodos finales: ");
        for(int i = 0; i < numNodoFinales; i++){
            nodosABuscar.add(grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo final: ").getNombre()));
        }
        ArrayList<Nodo> nodosEncontrado = BusquedasACiegas.busquedaProfundidadIterativa(nodoInicial, nodosABuscar, grafo.size(), true);
        imprimirNodosEncontrados(nodosEncontrado);
    }

    public static void busquedaCostoUniforme(HashMap<String, Nodo> grafo){
        ArrayList<Nodo> nodosABuscar = new ArrayList<>();
        Nodo nodoInicial = grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo inicial: ").getNombre());
        int numNodoFinales = ingresarNumero("Ingrese el numero de nodos finales: ");
        for(int i = 0; i < numNodoFinales; i++){
            nodosABuscar.add(grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo final: ").getNombre()));
        }
        ArrayList<Pair<Nodo, Integer>> nodosEncontrado = BusquedasACiegas.busquedaCostoUniforme(nodoInicial, nodosABuscar, grafo.size(),true);
        imprimirNodosEncontradosPair(nodosEncontrado);
    }

    public static void busquedaDelGradiente(HashMap<String, Nodo> grafo){
        Nodo nodoInicial = grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo inicial: ").getNombre());
        ArrayList<Nodo> nodosEncontrado = BusquedasHeuristicas.busquedaDelGradiente(nodoInicial,true);
        imprimirNodosEncontrados(nodosEncontrado);
    }

    public static void busquedaPrimeroElMejor(HashMap<String, Nodo> grafo){
        ArrayList<Nodo> nodosABuscar = new ArrayList<>();
        Nodo nodoInicial = grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo inicial: ").getNombre());
        int numNodoFinales = ingresarNumero("Ingrese el numero de nodos finales: ");
        for(int i = 0; i < numNodoFinales; i++){
            nodosABuscar.add(grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo final: ").getNombre()));
        }
        ArrayList<Nodo> nodosEncontrado = BusquedasHeuristicas.busquedaPrimeroElMejor(nodoInicial,nodosABuscar, grafo.size(), true);
        imprimirNodosEncontrados(nodosEncontrado);
    }

    public static void busquedaAEstrella(HashMap<String, Nodo> grafo){
        ArrayList<Nodo> nodosABuscar = new ArrayList<>();
        Nodo nodoInicial = grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo inicial: ").getNombre());
        int numNodoFinales = ingresarNumero("Ingrese el numero de nodos finales: ");
        for(int i = 0; i < numNodoFinales; i++){
            nodosABuscar.add(grafo.get(buscarNodo(grafo, "Ingrese el nombre del nodo final: ").getNombre()));
        }
        ArrayList<Arista> nodosEncontrado = BusquedasHeuristicas.busquedaAEstrella(nodoInicial, nodosABuscar, grafo.size(), true);
        assert nodosEncontrado != null;
        //imprimirNodosEncontrados(nodosEncontrado);
    }

    //ingresa el usuario el nombre por teclado del nodo se verifica si existe en el grafo y se retorna el nodo, no sale si no encuentra el nodo
    public static Nodo buscarNodo(HashMap<String, Nodo> grafo, String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        String nombreNodo = sc.nextLine();
        Nodo nodo = grafo.get(nombreNodo);
        while(nodo == null){
            System.out.println("Nodo no encontrado, intente de nuevo.");
            System.out.println(mensaje);
            nombreNodo = sc.nextLine();
            nodo = grafo.get(nombreNodo);
        }
        return nodo;
    }

    //ingresa un numero por teclado y se verifica que sea un numero entero, no sale si no hay un numero entero
    public static int ingresarNumero(String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        while(!sc.hasNextInt()){
            System.out.println("No es un numero entero, intente de nuevo.");
            System.out.println(mensaje);
            sc.next();
        }
        return sc.nextInt();
    }

    //impirme los nodos encontrados en la busqueda
    public static void imprimirNodosEncontrados(ArrayList<Nodo> nodosEncontrados){
        if (!nodosEncontrados.isEmpty()){
            System.out.println();
            nodosEncontrados.forEach(n -> System.out.print(n.getNombre() + ", "));
        }else{
            System.out.println("Nodo no encontrado.");
        }
    }

    public static void imprimirNodosEncontradosPair(ArrayList<Pair<Nodo, Integer>> nodosEncontrados){
        if (!nodosEncontrados.isEmpty()){
            System.out.println("Nodos Encontrados: ");
            nodosEncontrados.forEach(n -> System.out.printf("(%s, %d), ", n.getLeft().getNombre(), n.getRight()));
        }else{
            System.out.println("Nodo no encontrado.");
        }
    }

}
