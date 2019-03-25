/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import DataStructures.Producción;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author KonnerEL
 */
public class Util { 
    public ArrayList<String> getSímbolosGramaticales(String Cuerpo) {
        ArrayList<String> SímbolosGramaticales = new ArrayList<>();
        for (int i = 0; i < Cuerpo.length(); i++) {
            if (i < Cuerpo.length() - 1 && Cuerpo.substring(i + 1, i + 2).equals("'")) {
                SímbolosGramaticales.add(Cuerpo.substring(i, i + 2));
                if (i == Cuerpo.length() - 2) { break; } 
                else i++;
            } else if (i < Cuerpo.length() - 1 && Cuerpo.substring(i, i + 1).equals("i") && Cuerpo.substring(i + 1, i + 2).equals("d")) {
                SímbolosGramaticales.add("id");
                if (i == Cuerpo.length() - 2) { break; } 
                else i++;
            } else SímbolosGramaticales.add(Cuerpo.substring(i, i + 1));
        }
        return SímbolosGramaticales;
    }
    
    public int getPositionFromSet(String string, Set<String>set) {
        ArrayList<String>list = new ArrayList<>(set);
        int i = 0;
        if (string.equals("$")) i = set.size();
        else i = list.indexOf(string);
        return i;
    }
    
    public String[] splitProducción(String Cuerpo) {
        String[] producciones;
        if (Cuerpo.contains("|")) producciones = Cuerpo.split("\\|");
        else {
            producciones = new String[1];
            producciones[0] = Cuerpo;
        }
        return producciones;
    }
    
    public String SettoString(Set<String>S) {
        String string = "{";
        Iterator it = S.iterator();
        while (it.hasNext()) {
            String next = (String) it.next();
            if (it.hasNext()) string = string + next + ",";
            else string = string + next;
        }
        string = string + "}";
        return string;
    }
    
    public String ArrayListtoString(ArrayList<String>AL, int i) {
        String string = "";
        for (int j = i; j < AL.size(); j++) 
            string = string + AL.get(j);
        return string;
    }
    
    public String StacktoString(Stack<String>S) {
        String string = "";
        Iterator it = S.iterator();
        while (it.hasNext()) 
            string = string + it.next();
        return string;
    }
    
    public String GramáticatoString(ArrayList<Producción>AL) {
        String string = "";
        for (Producción P: AL) 
            string = string + P.getEncabezado() + "➝" + P.getCuerpo() + "\n";
        return string;
    }
    
    public String[][] TablaMtoString(Producción[][] M) {
        String[][] TablaM = new String[M.length][M[0].length];
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                if (M[i][j] != null) TablaM[i][j] = M[i][j].getEncabezado() + "->" + M[i][j].getCuerpo();
                else TablaM[i][j] = "";
            }
        }
        return TablaM;
    }
}
