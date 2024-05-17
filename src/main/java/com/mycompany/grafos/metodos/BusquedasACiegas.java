/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.grafos.metodos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import com.mycompany.grafos.grafo.Nodo;
import javafx.util.Pair;

public class BusquedasACiegas {
    private static final ArrayList<Nodo> visitadosComun = new ArrayList<>();
    private static boolean finishBusquedaBi = false;
    
    private static class hiloBusquedaAmplitud extends Thread{
        private Queue<Nodo> cola;
        private ArrayList<Nodo> visitados;
        private Nodo nodoInicial;
        private Nodo nodoExtraido;
        private String nombre;

        private Boolean imprimir;

        public hiloBusquedaAmplitud(ThreadGroup grupo, Nodo nodoInicial, Nodo nodoFinal, String nombre, Boolean imprimir) {
            super(grupo, nombre);
            this.cola = new LinkedList<>();
            this.visitados = new ArrayList<>();
            this.nodoInicial = nodoInicial;
            this.nombre = nombre;
            this.imprimir = imprimir;
        }
        

        @Override
        public void run() {
            synchronized (visitadosComun) {
                cola.add(nodoInicial);
                visitados.add(nodoInicial);
                do {
                    String colaString = "Cola Thread " + nombre + ":";
                    for (Nodo nodoCola : cola) {
                        if(nodoCola != null){
                            colaString += nodoCola.getNombre() + ",";
                        }
                    }
                    if(imprimir) System.out.println(colaString);
                    if(cola.isEmpty()){
                        continue;
                    }
                    nodoExtraido = cola.remove();
                    if(imprimir) System.out.println("------------------------------\nExtraccion: " + nodoExtraido.getNombre());
                    if(visitadosComun.contains(nodoExtraido)){
                        finishBusquedaBi = true;
                        visitadosComun.notifyAll();
                        return;
                    }else{
                        visitadosComun.add(nodoExtraido);
                    }
                    for (Nodo hijo : nodoExtraido.getHijos()) {
                        if (nodoExtraido == null){
                            continue;
                        }
                        if(!visitados.contains(hijo)){
                            visitados.add(hijo);
                            cola.add(hijo);
                        }
                    }
                } while (!cola.isEmpty());
            }
        }
        
        public static void detenerTodos() {
            synchronized (visitadosComun) {
                visitadosComun.notifyAll();
            }
        }
        
        public Nodo getNodoInterseccion(){
            return nodoExtraido;
        }
    }
    
    public static ArrayList<Nodo> busquedaAmplitud(Nodo nodoInicial, ArrayList<Nodo> nodosFinal,boolean imprimirTabla){
        Queue<Nodo> cola = new LinkedList<>();
        ArrayList<Nodo> visitados = new ArrayList<>();
        ArrayList<Nodo> encontrados = new ArrayList<>();
        cola.add(nodoInicial);
        visitados.add(nodoInicial);
        if(imprimirTabla) System.out.println("Extraido | Cola\n----------------\n");
        do {
            String colaString = "";
            for (Nodo nodoCola : cola) {
                if(nodoCola != null){
                    colaString += nodoCola.getNombre() + ",";   
                }
            }

            if(cola.isEmpty()){
                continue;
            }

            Nodo nodoExtraido = cola.remove();
            if (imprimirTabla) {
                System.out.print(String.format("%-9s|%-25s\n", nodoExtraido.getNombre(), colaString));
            }

            if(nodosFinal.contains(nodoExtraido)){
                encontrados.add(nodoExtraido);
                nodosFinal.remove(nodoExtraido);
                if(nodosFinal.isEmpty()){
                    return encontrados;
                }
            }
            for (Nodo hijo : nodoExtraido.getHijos()) {
                if (nodoExtraido == null){
                    continue;
                }
                if(!visitados.contains(hijo)){
                    visitados.add(hijo);
                    cola.add(hijo);
                }
            }
        } while (!cola.isEmpty());
        return encontrados;
    }
    
    public static ArrayList<Nodo> busquedaProfundidad(Nodo nodoInicial, ArrayList<Nodo> nodosFinal,boolean imprimirTabla){
        Stack<Nodo> pila = new Stack<>();
        ArrayList<Nodo> visitados = new ArrayList<>();
        ArrayList<Nodo> encontrados = new ArrayList<>();
        pila.push(nodoInicial);
        visitados.add(nodoInicial);
        if(imprimirTabla) System.out.println("Extraido | Cola\n----------------");
        do {
            String colaString = "";
            for (Nodo nodoCola : pila.reversed()) {
                if(nodoCola != null){
                    colaString += nodoCola.getNombre() + ",";
                }
            }
            if(pila.isEmpty()){
                continue;
            }
            Nodo nodoExtraido = pila.pop();
            if (imprimirTabla) {
                System.out.print(String.format("%-9s|%-25s\n", nodoExtraido.getNombre(), colaString));
            }
            if(nodosFinal.contains(nodoExtraido)){
                encontrados.add(nodoExtraido);
                nodosFinal.remove(nodoExtraido);
                if(nodosFinal.isEmpty()){
                    return encontrados;
                }
            }
            for (Nodo hijo : nodoExtraido.getHijos().reversed()) {
                if(nodoExtraido == null){
                    continue;
                }
                if(!visitados.contains(hijo)){
                    visitados.add(hijo);
                    pila.push(hijo);
                }
            }
        } while (!pila.isEmpty());
        return encontrados;
    }
    
    
    public static ArrayList<Nodo> busquedaBidireccional(Nodo nodoInicial, ArrayList<Nodo> nodosFinal, Boolean imprimir){
        ArrayList<Nodo> interseccionEncontrada = new ArrayList<>();
        
        for (Nodo buscaNodo : nodosFinal) {

            visitadosComun.clear();
            finishBusquedaBi = false;
            
            ThreadGroup grupo = new ThreadGroup("grupo");
            hiloBusquedaAmplitud threadInicio = new hiloBusquedaAmplitud(grupo, nodoInicial, buscaNodo, "Inicial " + nodoInicial.getNombre(), imprimir);
            hiloBusquedaAmplitud threadFinal = new hiloBusquedaAmplitud(grupo, buscaNodo, nodoInicial, "Final " + buscaNodo.getNombre(), imprimir);

            threadInicio.setPriority(Thread.NORM_PRIORITY);
            threadFinal.setPriority(Thread.NORM_PRIORITY);

            threadInicio.start();
            threadFinal.start();

            synchronized (visitadosComun) {
                while(!finishBusquedaBi){
                    try {
                        visitadosComun.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(threadFinal.getState() == Thread.State.TERMINATED){
                interseccionEncontrada.add(threadFinal.nodoExtraido);
                grupo.interrupt();

            }else if(threadInicio.getState() == Thread.State.TERMINATED){
                interseccionEncontrada.add(threadInicio.nodoExtraido);
                grupo.interrupt();
            }

            try {
                threadInicio.join();
                threadFinal.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return interseccionEncontrada;
    }
    
    public static ArrayList<Nodo> busquedaProfundidadIterativa(Nodo nodoInicial, ArrayList<Nodo> nodosFinal, Integer numNodos, boolean imprimirTabla){
        ArrayList<Nodo> encontrados = new ArrayList<>();
        ArrayList<Nodo> visitados = new ArrayList<>();
        int nivel = 0;

        while(visitados.size() != numNodos){
            if(imprimirTabla) System.out.printf("----------Nivel %d----------\n", nivel++);
            Stack<Nodo> pila = new Stack<>();
            ArrayList<Nodo> visitadosIteracion = new ArrayList<>();
            pila.push(nodoInicial);
            if(imprimirTabla) System.out.println("Extraido | Cola\n----------------");
            do {
                String colaString = "";
                for (Nodo nodoCola : pila.reversed()) {
                    if(nodoCola != null){
                        colaString += nodoCola.getNombre() + ",";
                    }
                }
                if(pila.isEmpty()){
                    continue;
                }
                Nodo nodoExtraido = pila.pop();
                if (imprimirTabla) {
                    System.out.print(String.format("%-9s|%-25s\n", nodoExtraido.getNombre(), colaString));
                }
                if(nodosFinal.contains(nodoExtraido)){
                    encontrados.add(nodoExtraido);
                    nodosFinal.remove(nodoExtraido);
                    if(nodosFinal.isEmpty()){
                        return encontrados;
                    }
                }
                if(visitados.contains(nodoExtraido)){
                    for (Nodo hijo : nodoExtraido.getHijos().reversed()) {
                        if(nodoExtraido == null){
                            continue;
                        }
                        if(!visitadosIteracion.contains(hijo)){
                            visitadosIteracion.add(hijo);
                            pila.push(hijo);
                        }
                    }
                }else{
                    visitados.add(nodoExtraido);
                }
                
            } while (!pila.isEmpty());
        }
        return encontrados;
    }
    
    public static ArrayList<Pair<Nodo, Integer>> busquedaCostoUniforme(Nodo nodoInicial, ArrayList<Nodo> nodosFinal, Integer sizeGrafo, boolean imprimirTabla){
//        PriorityQueue<Pair<Nodo, Integer>> listaRecorrido = new PriorityQueue<>(Comparator.comparing((t) -> t.getRight()));
        PriorityQueue<Pair<Nodo, Integer>> listaRecorrido = new PriorityQueue<>(
                (pair1, pair2) -> {
                    int pesoCompare = pair1.getRight().compareTo(pair2.getRight());
                    if (pesoCompare != 0) {
                        return pesoCompare;
                    } else {
                        return pair1.getLeft().getNombre().compareTo(pair2.getLeft().getNombre());
                    }
                }
        );

        ArrayList<Nodo> visitados = new ArrayList<>();
        ArrayList<Pair<Nodo, Integer>> encontrados = new ArrayList<>();
        listaRecorrido.add(new Pair<>(nodoInicial,0));
        visitados.add(listaRecorrido.peek().getLeft());
        if(imprimirTabla) System.out.println("Extraido | Cola\n----------------");

        do{
            if(nodosFinal.size() == encontrados.size()){
                return encontrados;
            }

            Pair<Nodo, Integer> pairExtraido = listaRecorrido.poll();
            String extraido = String.format("%s(%d)", pairExtraido.getLeft().getNombre(), pairExtraido.getRight());
            
            if(nodosFinal.contains(pairExtraido.getLeft())){
                
                if (encontrados.stream().map(n -> n.getLeft()).toList().contains(pairExtraido.getLeft())){
                    
                    if(encontrados.removeIf(n -> (n.getRight() > pairExtraido.getRight()) && (n.getLeft() == pairExtraido.getLeft()))){
                       encontrados.add(pairExtraido); 
                    }
                    
                }else{
                    encontrados.add(pairExtraido);
                }
            }
            
            if(pairExtraido.getLeft().getAristasHijos().isEmpty()){
                String recorrido = "";
                for (Pair<Nodo, Integer> n : listaRecorrido) {
                    String aux = String.format("%s(%d), ", n.getLeft().getNombre(), n.getRight());
                    recorrido += aux;
                }

                if (imprimirTabla) {
                    System.out.print(String.format("%-9s|%-60s\n", extraido, recorrido));
                }
                continue;
            }

            String recorrido = "";

//            pairExtraido.getLeft().getAristasHijos().forEach(arista -> listaRecorrido.add(new Pair<>(arista.getNodoHijo(),arista.getPeso() + pairExtraido.getRight())));
            pairExtraido.getLeft().getAristasHijos().forEach(arista -> {
                Pair<Nodo, Integer> newPair = new Pair<>(arista.getNodoHijo(), arista.getPeso() + pairExtraido.getRight());
                if (!listaRecorrido.contains(newPair)) {
                    listaRecorrido.add(newPair);
                }
            });

            for (Pair<Nodo, Integer> n : listaRecorrido) {
                String aux = String.format("%s(%d), ", n.getLeft().getNombre(), n.getRight());
                recorrido += aux;
            }
            if (imprimirTabla) {
                System.out.print(String.format("%-9s|%-60s\n", extraido, recorrido));
            }

            if(visitados.size() == sizeGrafo){
                break;
            }
        }while(!listaRecorrido.isEmpty());
        return encontrados;
    }
}
