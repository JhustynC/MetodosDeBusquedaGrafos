/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.grafos.grafo;

import java.util.ArrayList;

/**
 *
 * @author Aarón Ortiz
 */
public class Nodo implements Comparable<Nodo> {
    private String nombre;
    private Integer peso;
    private ArrayList<Arista> listaAristas;

    public Nodo(String nombre, Integer peso) {
        this.nombre = nombre;
        this.peso = peso;
        this.listaAristas = new ArrayList<>();
    }
    
    public void addArista(Nodo Hijo, Integer Peso){
        Arista nuevaArista = new Arista(Peso, this, Hijo);
        this.listaAristas.add(nuevaArista);
        Hijo.getListaAristas().add(nuevaArista);
    }
    
    public ArrayList<Nodo> getPadres(){
        ArrayList<Nodo> listaPadres = new ArrayList<>();
        for (Arista next : this.listaAristas) {
            if (!next.getNodoPadre().equals(this)){
                listaPadres.add(next.getNodoHijo());
            }
        }
        return listaPadres;
    }
    
    public ArrayList<Nodo> getHijos(){
        ArrayList<Nodo> listaHijos = new ArrayList<>();
        for (Arista next : this.listaAristas) {
            if (next.getNodoPadre().equals(this)){
                listaHijos.add(next.getNodoHijo());
            }
        }
        return listaHijos;
    }

    public ArrayList<Arista> getHijosAristas(){
        ArrayList<Arista> listaHijos = new ArrayList<>();
        for (Arista next : this.listaAristas) {
            if (next.getNodoPadre().equals(this)){
                listaHijos.add(next);
            }
        }
        return listaHijos;
    }
    
    public ArrayList<Arista> getAristasHijos(){
        ArrayList<Arista> listaAristas = new ArrayList<>();
        for (Arista next : this.listaAristas) {
            if (next.getNodoPadre().equals(this)){
                listaAristas.add(next);
            }
        }
        return listaAristas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public ArrayList<Arista> getListaAristas() {
        return listaAristas;
    }

    @Override
    public String toString() {
        String listaString = new String();
        for(Arista arista: this.listaAristas){
            listaString += "    * " + arista.toString() + "\n";
        }
        return "Nodo " + nombre + ":\n Peso = " + peso + "\n listaAristas: \n" + listaString + '}';
    }
    
    @Override
    public int compareTo(Nodo n){
        return (n.getPeso()<this.peso)? 1:-1;
    }
    
}
