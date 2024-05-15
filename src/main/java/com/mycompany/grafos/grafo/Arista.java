/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.grafos.grafo;

/**
 *
 * @author Aarón Ortiz
 */
public class Arista implements Comparable<Arista>{
    private Integer peso;
    private Nodo nodoPadre;
    private Nodo nodoHijo;

    public Arista(Integer peso, Nodo nodoPadre, Nodo nodoHijo) {
        this.peso = peso;
        this.nodoPadre = nodoPadre;
        this.nodoHijo = nodoHijo;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Nodo getNodoHijo() {
        return nodoHijo;
    }

    public void setNodoHijo(Nodo nodoHijo) {
        this.nodoHijo = nodoHijo;
    }

    public Nodo getNodoPadre() {
        return nodoPadre;
    }

    @Override
    public int compareTo(Arista arista) {
        if(arista.getPeso()==this.peso){
            return (arista.getNodoHijo().getNombre().compareTo(this.getNodoHijo().getNombre()) > 0)? 1:-1;
        }

        return (arista.getPeso()+arista.getNodoHijo().getPeso()<this.peso + this.getNodoHijo().getPeso())? 1:-1;
    }
    
    
}
