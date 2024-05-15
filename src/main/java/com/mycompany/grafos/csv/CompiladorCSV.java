/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.grafos.csv;

import com.mycompany.grafos.grafo.Nodo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Aarón Ortiz
 */
public class CompiladorCSV {
    
    private static ArrayList<String []> leerCSV(String nombreArchivo) throws FileNotFoundException, IOException{
        BufferedReader buffer = null;
        ArrayList<String []> listaCSV = new ArrayList<>();
        try {
            buffer = new BufferedReader(new FileReader(nombreArchivo));
            String line = buffer.readLine();
            while(line!=null){
                String [] fields = line.split(",");
                listaCSV.add(fields);
                line = buffer.readLine();
            }
        } catch (Exception e) {
            System.out.println("ERROR> " + e.getMessage());
        } finally {
            if(buffer!=null){
                buffer.close();
            }
        }
        return listaCSV;
    }

    public static HashMap<String, Nodo> cargarDatos(String pathArchivo) throws IOException{
        HashMap<String, Nodo> mapaNodos = new HashMap<>();
        ArrayList<String []> listaAristas = CompiladorCSV.leerCSV(pathArchivo);
        if (listaAristas.isEmpty()){
            return null;
        }

        for (String [] arista : listaAristas) {
            if (arista.length == 2){
                mapaNodos.put(arista[0], new Nodo(arista[0], Integer.valueOf(arista[1])));
            }else{
                Nodo padre = mapaNodos.get(arista[0]);
                Nodo hijo = mapaNodos.get(arista[1]);
                if(padre == null){
                    System.out.println("Nodo " + arista[0] + " no existe.");
                    continue;
                }
                if(hijo == null){
                    System.out.println("Nodo " + arista[1] + " no existe.");
                    continue;
                }
                padre.addArista(hijo, Integer.valueOf(arista[2]));
            }
        }
        //Imprimir los nodos
//        mapaNodos.values().stream().forEach(n -> System.out.println(n.toString()));
        return mapaNodos;
    }
    
}
