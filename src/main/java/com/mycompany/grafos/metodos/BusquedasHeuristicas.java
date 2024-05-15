package com.mycompany.grafos.metodos;

import java.util.*;

import com.mycompany.grafos.grafo.Arista;
import com.mycompany.grafos.grafo.Nodo;


public class BusquedasHeuristicas {
    public static ArrayList<Nodo> busquedaDelGradiente(Nodo nodoInicial, boolean imprimirTabla){
        ArrayList<Nodo> recorrido = new ArrayList<>();
        recorrido.add(nodoInicial);
        if(imprimirTabla){
            System.out.println("Extraido | Cola\n----------------");
        System.out.printf("%-9s|%-25s\n", " ",String.format("(%s, %d)", nodoInicial.getNombre(), nodoInicial.getPeso()));
        }

        do{
            
            Nodo nodoPesoMinimo = recorrido.getLast().getHijos().stream().min((n1, n2) -> n1.getPeso().compareTo(n2.getPeso())).orElse(null);
            
            String extraido = String.format("(%s, %d)", recorrido.getLast().getNombre(), recorrido.getLast().getPeso());

            if(nodoPesoMinimo != null){
                recorrido.add(nodoPesoMinimo);
            }
            
            if(imprimirTabla){
//                Node graphNode = graph.getNode(recorrido.getLast().getNombre());
//                graphNode.setAttribute("ui.style", "fill-color: red;");
//                try {
//                    Thread.sleep(1000); // Pausa por 1 segundo
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                String colaString = String.format("(%s, %d)", recorrido.getLast().getNombre(), recorrido.getLast().getPeso());
                System.out.print(String.format("%-9s|%-25s\n", extraido, colaString));
            }

            if(recorrido.getLast().getPeso() == 0){
                if(imprimirTabla){
//                    Node graphNode = graph.getNode(recorrido.getLast().getNombre());
//                    graphNode.setAttribute("ui.style", "fill-color: red;");
//                    try {
//                        Thread.sleep(1000); // Pausa por 1 segundo
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    String colaString = String.format("(%s, %d)", recorrido.getLast().getNombre(), recorrido.getLast().getPeso());
                    System.out.print(String.format("%-9s|%-25s\n", colaString, ""));
                }
                break;
            }
            
        }while(recorrido.getLast().getHijos() != null);
        return recorrido;
    }
    
    public static ArrayList<Nodo> busquedaPrimeroElMejor(Nodo nodoInicial, ArrayList<Nodo> nodosFinal, Integer sizeGrafo,boolean imprimirTabla){
        PriorityQueue<Nodo> pilaPrioridad = new PriorityQueue<>();
        ArrayList<Nodo> encontrados = new ArrayList<>();
        ArrayList<Nodo> visitados = new ArrayList<>();
        pilaPrioridad.add(nodoInicial);
        visitados.add(nodoInicial);
        if(imprimirTabla){
            System.out.println("Extraido | Cola\n----------------");
            System.out.printf("%-9s|%-25s\n", " ",String.format("(%s, %d)", nodoInicial.getNombre(), nodoInicial.getPeso()));
        }

        while(!pilaPrioridad.isEmpty() && !nodosFinal.isEmpty()){          
            Nodo nodoPesoMinimo = pilaPrioridad.poll();
            if(!visitados.contains(nodoPesoMinimo)){
                visitados.add(nodoPesoMinimo);
            }

            if(nodosFinal.contains(nodoPesoMinimo)){
                nodosFinal.remove(nodoPesoMinimo);
                encontrados.add(nodoPesoMinimo);
            }
            
            if (!nodoPesoMinimo.getHijos().isEmpty()) {
                List<Nodo> listanovisitados = nodoPesoMinimo.getHijos();
                pilaPrioridad.addAll(listanovisitados);
            }
            if(imprimirTabla){
//                Node graphNode = graph.getNode(nodoPesoMinimo.getNombre());
//                graphNode.setAttribute("ui.style", "fill-color: red;");
//                try {
//                    Thread.sleep(1000); // Pausa por 1 segundo
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                String colaString = pilaPrioridad.stream().map(n -> String.format("(%s, %d)", n.getNombre(), n.getPeso())).reduce("", (a, b) -> a + b + " ");
                System.out.print(String.format("%-9s|%-25s\n", String.format("(%s,%d)", nodoPesoMinimo.getNombre(), nodoPesoMinimo.getPeso()), colaString));
            }
            
            if (visitados.size() == sizeGrafo) {
                break;
            }
            
        }
        return encontrados;
    }
    
    public static ArrayList<Arista> busquedaAEstrella(Nodo nodoInicial, ArrayList<Nodo> nodosFinal, Integer sizeGrafo, boolean imprimirTabla){
        PriorityQueue<Arista> pilaPrioridad = new PriorityQueue<>();
        ArrayList<Arista> encontrados = new ArrayList<>();
        ArrayList<Nodo> visitados = new ArrayList<>();
        pilaPrioridad.add(new Arista(0, nodoInicial, nodoInicial));
        if(imprimirTabla){
            System.out.println("Extraido            | Cola\n-----------------------------------------");
            System.out.printf("%-20s|%-25s\n"," ",String.format("(%s,%d)", nodoInicial.getNombre(), nodoInicial.getPeso()));
        }

        while(!pilaPrioridad.isEmpty() && !nodosFinal.isEmpty()){
            Arista nodoPesoMinimo = pilaPrioridad.poll();
            
            if(nodosFinal.contains(nodoPesoMinimo.getNodoHijo())){
                nodosFinal.remove(nodoPesoMinimo.getNodoHijo());
                encontrados.add(nodoPesoMinimo);
            }
            
            if (!nodoPesoMinimo.getNodoHijo().getHijosAristas().isEmpty()) {
                List<Arista> listanovisitados = nodoPesoMinimo.getNodoHijo().getHijosAristas();
                listanovisitados.forEach(t -> {
                    t.setPeso(t.getPeso() + nodoPesoMinimo.getPeso());
                    if (!visitados.contains(t.getNodoHijo())) {
                        visitados.add(t.getNodoHijo());
                    }
                });
                pilaPrioridad.addAll(listanovisitados);
            }
            
            if (imprimirTabla) {
//                Node graphNode = graph.getNode(nodoPesoMinimo.getNodoHijo().getNombre());
//                graphNode.setAttribute("ui.style", "fill-color: blue;");
//                try {
//                    Thread.sleep(100); // Pausa por 1 segundo
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                graphNode.setAttribute("ui.style", "fill-color: red;");
                String colaString = pilaPrioridad.stream().map(n -> String.format("(%s, %d)", n.getNodoHijo().getNombre(), n.getPeso()+n.getNodoHijo().getPeso())).reduce("", (a, b) -> a + b + " ");
                String extraido = String.format("(%s,%d) -> (%s,%d)", nodoPesoMinimo.getNodoHijo().getNombre(), nodoPesoMinimo.getPeso()+nodoPesoMinimo.getNodoHijo().getPeso(),nodoPesoMinimo.getNodoHijo().getNombre(), nodoPesoMinimo.getPeso());
                System.out.print(String.format("%-20s|%-25s\n", extraido, colaString));
//                try {
////                    Thread.sleep(1000); // Pausa por 1 segundo
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }

            if (visitados.size() == sizeGrafo) {
                break;
            }
            
        }
        return encontrados;
    }

    
}
