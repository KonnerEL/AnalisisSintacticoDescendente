/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import DataStructures.Producción;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author KonnerEL
 */
public class AnálisisSintácticoDescedentePredictivoNoRecursivo {
    Producción[][] TablaM;
    Stack<String>Pila;
    String w;
    public ArrayList<String>PilaArrayList;
    public ArrayList<String>EntradaArrayList;
    public ArrayList<String>SalidaArrayList;
    Set<String>NoTerminales;
    Set<String>Terminales;
    String SímboloInicio;
    
    public AnálisisSintácticoDescedentePredictivoNoRecursivo() {
        Pila = new Stack<>();
        Pila.push("$");
        PilaArrayList = new ArrayList<>();
        EntradaArrayList = new ArrayList<>();
        SalidaArrayList = new ArrayList<>();        
    }
    
    public void algorithm(String Entrada) {
        w = Entrada + "$";
        Util u = new Util();
        boolean Flag = true;
        ArrayList<String>SímbolosGramaticales = u.getSímbolosGramaticales(w);
        int i = 0;
        String a = SímbolosGramaticales.get(0);
        Pila.push(SímboloInicio);
        String X = "";
        String Salida = "";
        do {
            //System.out.println("Pila: " + Pila);
            PilaArrayList.add(u.StacktoString(Pila));
            EntradaArrayList.add(u.ArrayListtoString(SímbolosGramaticales, i));
            //System.out.println("Entrada: " + u.ArrayListtoString(SímbolosGramaticales,i));
            Salida = "";
            //System.out.println("Entrada: " + a);
            X = Pila.peek();
            if (X.equals("$") || Terminales.contains(X)) {
                if (X.equals(a)) { X = Pila.pop(); i++; a = SímbolosGramaticales.get(i); SalidaArrayList.add(""); }
                else { Flag = false; break; }
            } else {
                int index1 = u.getPositionFromSet(X, NoTerminales), index2;
                if (!Terminales.contains(a) && !a.equals("$")) { Flag = false; break; }
                else index2 = u.getPositionFromSet(a, Terminales);
                //System.out.println(X);
                //System.out.println(a);
                //System.out.println(TablaM[index1][index2].getCuerpo());
                if (TablaM[index1][index2] != null && index2 != -1) {
                    X = Pila.pop();
                    ArrayList<String>Producción = new ArrayList<>();
                    Producción = u.getSímbolosGramaticales(TablaM[index1][index2].getCuerpo());
                    Collections.reverse(Producción);
                    for (String str:Producción) {
                        if (!str.equals("&")) Pila.push(str);
                    }
                    Salida = TablaM[index1][index2].getEncabezado() + "->" + TablaM[index1][index2].getCuerpo();
                    //System.out.println("Pila: " + Pila);
                    //System.out.println("Entrada: " + u.ArrayListtoString(SímbolosGramaticales,i));
                    //System.out.println("Salida: " + Salida);
                    SalidaArrayList.add(Salida);
                } 
                else { Flag = false; break; }
            }
        } while (!Pila.peek().equals("$"));
        if (Flag) { PilaArrayList.add("$"); EntradaArrayList.add("$"); SalidaArrayList.add("Aceptar"); }
        else SalidaArrayList.add("Rechazar");
    }
    
    public void show() {
        System.out.println("---------------Algoritmo de Reconocimiento de Cadena---------------");
        System.out.println("Pila                        Entrada                        Salida");
        for (int i = 0; i < PilaArrayList.size(); i++) 
            System.out.println(String.format("%-28s", PilaArrayList.get(i)) +  String.format("%-31s", EntradaArrayList.get(i)) + SalidaArrayList.get(i));
    }

    public void setSímboloInicio(String SímboloInicio) {
        this.SímboloInicio = SímboloInicio;
    }

    public void setTablaM(Producción[][] TablaM) {
        this.TablaM = TablaM;
    }

    public void setNoTerminales(Set<String> NoTerminales) {
        this.NoTerminales = NoTerminales;
    }

    public void setTerminales(Set<String> Terminales) {
        this.Terminales = Terminales;
    }
}
