/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import Interfaces.Files;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.*;
import Algorithms.Util;

/**
 *
 * @author KonnerEL
 */
public class Gramática {
    Files f;
    public String SímboloInicio;
    public Set<String>Terminales;
    public Set<String>NoTerminales;
    public ArrayList<Producción> Producciones;
    public Gramática GramáticaEquivalente;
    public Producción[][] M;
    
    public Gramática(String Source) throws FileNotFoundException {
        Terminales = new LinkedHashSet<>();
        NoTerminales = new LinkedHashSet<>();
        Producciones = new ArrayList();
        f = new Files(Source);
    }
    
    public Gramática() {
        Terminales = new LinkedHashSet<>();
        NoTerminales = new LinkedHashSet<>();
        Producciones = new ArrayList();
    }
    
    public void setSímboloInicio(String producción) {
        Producción P = new Producción(producción);
        SímboloInicio = P.Encabezado;
    }
    
    public void setSímboloInicio(Producción P) {
        SímboloInicio = P.Encabezado;
    }

    public void setNoTerminales() {
        for (Producción P: Producciones) 
           NoTerminales.add(P.Encabezado);
    }
    
    public void setTerminales() {
        for (Producción P: Producciones) {
            String[] Characters;
            String auxString = P.Cuerpo;
            /*if (P.Cuerpo.contains("|")) {
                auxString = P.Cuerpo.replace("|", "");
                System.out.println(auxString);
            }*/
            Characters = auxString.split("");
            for (int i = 0; i < Characters.length; i++) {
                if (!Characters[i].equals("|") && !Characters[i].equals("'") && !Characters[i].equals("&")) {
                    if (isIdentifier(Characters, i)) {
                        Terminales.add("id");
                        i++;
                        if (i == Characters.length-2) { break; }
                    } else {
                        if (!NoTerminales.contains(Characters[i])) Terminales.add(Characters[i]);  
                    } 
                }
            }          
        }   
    }
    
    public void addProducción(String producción) {
        Producción P = new Producción(producción);
        Producciones.add(P);
    }
    
    public void show() {
        System.out.println("-------Gramática de Entrada-------");
        Producciones.forEach((P) -> {
            System.out.println(P.Encabezado + "->" + P.Cuerpo);
        });
        System.out.println("-------Símbolo de Inicio-------");
        System.out.println(SímboloInicio);
        //Iterator itr = NoTerminales.iterator();
        System.out.println("-------Simbolos No Terminales-------");
        System.out.println(NoTerminales);
        /*while(itr.hasNext()) {
            System.out.println(itr.next());
        }
        itr = Terminales.iterator();*/
        System.out.println("-------Simbolos Terminales-------");
        /*while(itr.hasNext()) {
            System.out.println(itr.next());
        }*/
        //
        System.out.println(Terminales);
        System.out.println("-------Gramática Equivalente-------");
        GramáticaEquivalente.Producciones.forEach((P) -> {
            System.out.println(P.Encabezado + "->" + P.Cuerpo);
        });
        System.out.println("-------Símbolo de Inicio-------");
        System.out.println(SímboloInicio);
        //itr = GramáticaEquivalente.NoTerminales.iterator();
        System.out.println("-------Simbolos No Terminales-------");
        /*while(itr.hasNext()) {
            System.out.println(itr.next());
        }
        itr = GramáticaEquivalente.Terminales.iterator();*/
        System.out.println(GramáticaEquivalente.NoTerminales);
        System.out.println("-------Simbolos Terminales-------");
        /*while(itr.hasNext()) {
            System.out.println(itr.next());
        }*/
        System.out.println(GramáticaEquivalente.Terminales);
        System.out.println("-------Conjunto de Primeros-------");
        for (Producción P: GramáticaEquivalente.Producciones) {
            Set<String>Primero = P.Primero;
            System.out.println("P(" + P.getEncabezado() + "): " + P.Primero);
        }
        System.out.println("-------Conjunto de Siguientes-------");
        for (Producción P: GramáticaEquivalente.Producciones) {
            Set<String>Siguiente = P.Siguiente;
            System.out.println("S(" + P.getEncabezado() + "): " + P.Siguiente);
        }
        System.out.println("-----------------------------------------------Tabla M-----------------------------------------------");
        Iterator it = GramáticaEquivalente.Terminales.iterator();
        System.out.print(String.format("%-16s", " "));
        while (it.hasNext()) 
            System.out.print(String.format("%-16s", it.next()));
        System.out.print(String.format("%-16s", "$"));
        System.out.println("");
        it = GramáticaEquivalente.NoTerminales.iterator();
        for (int i = 0; i < M.length; i++) {
            System.out.print(String.format("%-16s", it.next()));
            for (int j = 0; j < M[0].length; j++) {
                if (M[i][j] != null) 
                    System.out.print(String.format("%-16s", M[i][j].getEncabezado()+"->"+M[i][j].getCuerpo()));
                else System.out.print(String.format("%-16s", " "));
            }
            System.out.println("");
        }
    }
    
    public void buildGramática() {
        String FirstLine = f.readNextLine();
        setSímboloInicio(FirstLine);
        String EncabezadoActual = FirstLine.substring(0,1);
        String ProducciónActual = FirstLine;
        while (f.sc.hasNextLine()) {
            String CurrentLine = f.readNextLine();
            if (EncabezadoActual.equals(CurrentLine.substring(0,1))) 
                ProducciónActual = ProducciónActual + "|" + CurrentLine.substring(3);
            else {
                addProducción(ProducciónActual);
                EncabezadoActual = CurrentLine.substring(0, 1);
                ProducciónActual = CurrentLine;
            }
        }
        addProducción(ProducciónActual);
        setNoTerminales();
        setTerminales();
    }
    
    public boolean isToken(String Token) {
        switch(Token) {
            case "id":
                return true;
        }
        return false;
    }
    
    public boolean isIdentifier(String[] Token, int i) {
        try {
           if (Token[i].equals("i") && Token[i+1].equals("d")) { return true; }
        } catch(Exception e) {}
        
        return false;
    } 
    
    public boolean isIdentifier(String Token) {
        try {
           if (Token.substring(0,2).equals("id")) { return true; }
        } catch(Exception e) {}
        
        return false;
    }
    
    public boolean esProducciónconRecursividadEliminable(Producción P) {
        String[] splitProducciones = P.Cuerpo.split("\\|");
        Boolean Flag = false;
        int i = 0;
        String A = P.Encabezado;  
        while (i < splitProducciones.length) {
            String Producción = splitProducciones[i];
            if (Producción.substring(0, 1).equals(A)) { Flag = true; } else { break; }
            i++;
        }
        //if (i == splitProducciones.length) Flag = false; //Si no hay Betha, entonces no se elimina Recursividad, sino que se Factoriza
        return Flag;
    }
    
    public boolean esProducciónFactorizable(Producción P) {
        String[] splitProducciones = P.Cuerpo.split("\\|");
        Boolean Flag = false;
        int i = 0;
        while (i < splitProducciones.length - 1 && !Flag) {
            String Producción1 = splitProducciones[i];
            String Producción2 = splitProducciones[i+1];
            if (Producción1.substring(0, 1).equals(Producción2.substring(0, 1))) {
                int LengthProducción1 = splitProducciones[i].length();
                int LengthProducción2 = splitProducciones[i+1].length();
                int MinLength = LengthProducción1 < LengthProducción2 ? LengthProducción1 : LengthProducción2;
                for (int j = 1; j <= MinLength; j++) {
                    if (Producción1.substring(0, j).equals(Producción2.substring(0, j))) Flag = true;
                    else { break;}
                }
            } 
            i++;
        }
        return Flag;
    }
    
    public void buildGramáticaEquivalente() {
        GramáticaEquivalente = new Gramática();
        GramáticaEquivalente.setSímboloInicio(this.Producciones.get(0));
        for (Producción P : this.Producciones) {
            if (esProducciónconRecursividadEliminable(P)) {
                String[] splitProducciones = P.Cuerpo.split("\\|");
                int i = 0;
                String A = P.Encabezado;
                ArrayList<String> α = new ArrayList();
                ArrayList<String> β = new ArrayList();
                while (i < splitProducciones.length) {
                    String Producción = splitProducciones[i];
                    if (Producción.substring(0, 1).equals(A)) 
                        α.add(Producción.substring(1, Producción.length())); 
                    else β.add(Producción);
                    i++;
                }
//                System.out.println("------Alpha------");
//                System.out.println(α);
//                System.out.println("------Betha------");
//                System.out.println(β);
                String stringProduccionA = P.Encabezado + "->" /*+ β.get(0) + P.Encabezado + "'"*/; 
                if (!β.isEmpty()) 
                    if (!β.get(0).equals("&")) stringProduccionA = stringProduccionA + β.get(0);
                stringProduccionA = stringProduccionA + P.Encabezado + "'";
                for (int j = 1; j < β.size(); j++) {
                    if (!β.get(j).equals("&")) 
                        stringProduccionA = stringProduccionA + "|" + β.get(j) + P.Encabezado + "'";
                    else stringProduccionA = stringProduccionA + "|" + P.Encabezado + "'";
                }
                GramáticaEquivalente.Producciones.add(new Producción(stringProduccionA));
                String stringProduccionAPrima = P.Encabezado + "'" + "->" + α.get(0) + P.Encabezado + "'";
                for (int j = 1; j < α.size(); j++) 
                    stringProduccionAPrima = stringProduccionAPrima + "|" + α.get(j) + P.Encabezado + "'";
                if (!β.isEmpty()) stringProduccionAPrima = stringProduccionAPrima + "|&";
                GramáticaEquivalente.Producciones.add(new Producción(stringProduccionAPrima));
            } else if (esProducciónFactorizable(P)) {
                String[] splitProducciones = P.Cuerpo.split("\\|");
                int i = 0;
                String α = "";
                ArrayList<String> β = new ArrayList();
                ArrayList<String> δ = new ArrayList();
                while (i < splitProducciones.length - 1 /*&& Flag*/) {
                    String Producción1 = splitProducciones[i];
                    String Producción2 = splitProducciones[i + 1];
                    if (Producción1.substring(0, 1).equals(Producción2.substring(0, 1))) {
                        int LengthProducción1 = splitProducciones[i].length();
                        int LengthProducción2 = splitProducciones[i + 1].length();
                        int MinLength = LengthProducción1 < LengthProducción2 ? LengthProducción1 : LengthProducción2;
                        for (int j = 1; j <= MinLength; j++) {
                            if (Producción1.substring(0, j).equals(Producción2.substring(0, j))) 
                                α = Producción1.substring(0, j);
                            else { break; }
                        }
                    } else { break; }
                    i++;
                }
                for (int j = 0; j <= i; j++) {
                    String ProducciónActual = splitProducciones[j].replace(α, "");
                    if (ProducciónActual.equals("")) β.add("&");
                    else β.add(ProducciónActual);
                }
                for (int j = i + 1; j < splitProducciones.length; j++) 
                    δ.add(splitProducciones[j]);               
//                System.out.println("------Alpha------");
//                    System.out.println(α);
//                    System.out.println("------Betha------");
//                    System.out.println(β);
//                    System.out.println("------Delta------");
//                    System.out.println(δ);
                String stringProduccionA = P.Encabezado + "->" + α + P.Encabezado + "'";
                if (!δ.isEmpty()) 
                    stringProduccionA = stringProduccionA + "|" + δ.get(0);
                for (int j = 1; j < δ.size(); j++) 
                    stringProduccionA = stringProduccionA + "|" + δ.get(j);
                GramáticaEquivalente.Producciones.add(new Producción(stringProduccionA));
                String stringProduccionAPrima = P.Encabezado + "'" + "->" + β.get(0);
                for (int j = 1; j < β.size(); j++) 
                    stringProduccionAPrima = stringProduccionAPrima + "|" + β.get(j);
                GramáticaEquivalente.Producciones.add(new Producción(stringProduccionAPrima));
            } else GramáticaEquivalente.Producciones.add(P);
        }
        GramáticaEquivalente.setNoTerminales();
        GramáticaEquivalente.setTerminales();
        for (Producción P:GramáticaEquivalente.Producciones) {
            Set<String>Primero = new LinkedHashSet<>();
            P.Primero = calculatePrimero(P.Cuerpo, Primero);
        }
        for (String NoTerminal: GramáticaEquivalente.NoTerminales) 
            calculateSiguiente(NoTerminal);
    }
    
    public Set<String> calculatePrimero(String α, Set<String>Primero) {
        if (α.contains("|")) {
            String[] Cuerpo = α.split("\\|");
            for (String Producción: Cuerpo) 
                calculatePrimero(Producción,Primero);
        } else {
            if (isIdentifier(α)) Primero.add("id");
            else {
                if (α.equals("&")) Primero.add("&");
                if (GramáticaEquivalente.Terminales.contains(α.substring(0, 1))) Primero.add(α.substring(0,1));
                else {
                    String Alpha = α.substring(0, 1);
                    String AfterAlpha = "";
                    if (α.length() > 1) {
                        if (α.substring(1, 2).equals("'")) {
                            Alpha = Alpha + "'";
                            if (!α.substring(2).isEmpty()) AfterAlpha = α.substring(2);
                        } else AfterAlpha = α.substring(1);  
                    }
                    if (GramáticaEquivalente.NoTerminales.contains(Alpha)) {
                        calculatePrimero(getProducción(Alpha).Cuerpo, Primero); 
                        if (canProduceEmpty(Alpha)) 
                            if (!AfterAlpha.isEmpty()) calculatePrimero(AfterAlpha, Primero);  
                    } 
                }
                
            }
        }    
        return Primero;
    }
    
    public void calculateSiguiente(String NoTerminal) {
        Util u = new Util();
        if (NoTerminal.equals(GramáticaEquivalente.SímboloInicio)) getProducción(NoTerminal).Siguiente.add("$");
        for (Producción P: GramáticaEquivalente.Producciones) {
            ArrayList<String> SímbolosGramaticales;
            String[] producciones = u.splitProducción(P.Cuerpo);
            for (String Cuerpo : producciones) {
                SímbolosGramaticales = u.getSímbolosGramaticales(Cuerpo);
                if (SímbolosGramaticales.contains(NoTerminal)) {
                    int indexNoTerminal = SímbolosGramaticales.indexOf(NoTerminal);
                    if (indexNoTerminal != SímbolosGramaticales.size() - 1) {
                        String SímboloGramatical = u.ArrayListtoString(SímbolosGramaticales, indexNoTerminal + 1);
                        Set<String>Primero = new LinkedHashSet<>();
                        if (SímboloGramatical.length() > 1) {
                            Primero = calculatePrimero(SímboloGramatical, Primero);
                            Primero.remove("&");
                        } else {
                            if (GramáticaEquivalente.Terminales.contains(SímboloGramatical) || GramáticaEquivalente.NoTerminales.contains(SímboloGramatical)) {                            
                                if (GramáticaEquivalente.NoTerminales.contains(SímboloGramatical)) {
                                    for (String Terminal: getProducción(SímboloGramatical).Primero) 
                                        if (!Terminal.equals("&")) Primero.add(Terminal);
                                } else Primero = calculatePrimero(SímboloGramatical, Primero);
                                Primero.remove("&");
                            }
                        }   
                        getProducción(NoTerminal).Siguiente.addAll(Primero);
                        if (GramáticaEquivalente.NoTerminales.contains(SímboloGramatical)) {
                            boolean containsEmpty = getProducción(SímboloGramatical).Cuerpo.contains("&");
                            if (containsEmpty) {
                                if (!P.Encabezado.equals(NoTerminal)) {
                                    if (getProducción(P.Encabezado).Siguiente.isEmpty()) 
                                        calculateSiguiente(P.Encabezado);
                                    getProducción(NoTerminal).Siguiente.addAll(getProducción(P.Encabezado).Siguiente);
                                    //getProducción(NoTerminal).Siguiente.add("S(" + P.Encabezado + ")");
                                }
                            }
                        }
                    } else {
                        if (!P.Encabezado.equals(NoTerminal))  {
                            if (getProducción(P.Encabezado).Siguiente.isEmpty()) calculateSiguiente(P.Encabezado);
                            getProducción(NoTerminal).Siguiente.addAll(getProducción(P.Encabezado).Siguiente);
                            //getProducción(NoTerminal).Siguiente.add("S(" + P.Encabezado + ")");
                        }
                    }
                }
            }
        }
    }
    
    public void buildTablaM() {
        Util u = new Util();
        M = new Producción[GramáticaEquivalente.NoTerminales.size()][GramáticaEquivalente.Terminales.size() + 1];
        //System.out.println("["+M.length+"]["+M[0].length+"]");
        for (Producción P: GramáticaEquivalente.Producciones) {
            String[] producciones = u.splitProducción(P.Cuerpo);
            for (String producción: producciones) {
                if (!producción.substring(0, 1).equals("&")) {
                    Set<String> Primero = new LinkedHashSet<>();
                    Primero = calculatePrimero(producción, Primero);
                    Primero.remove("&");
                    for (String Terminal : Primero) {
                        int indexNoTerminales = u.getPositionFromSet(P.Encabezado, GramáticaEquivalente.NoTerminales);
                        int indexTerminales = u.getPositionFromSet(Terminal, GramáticaEquivalente.Terminales);
                        Producción pr = new Producción(P.Encabezado + "->" + producción);
                        M[indexNoTerminales][indexTerminales] = pr;
                        //System.out.println("M["+indexNoTerminales+"]["+indexTerminales+"] = " + pr.Encabezado + "->" + pr.Cuerpo);
                    }
                }       
                if (canProduceEmpty(producción)) {
                    Set<String>Siguiente = P.Siguiente;
                    for (String Terminal: Siguiente) {
                        int indexNoTerminales = u.getPositionFromSet(P.Encabezado, GramáticaEquivalente.NoTerminales);
                        int indexTerminales = u.getPositionFromSet(Terminal, GramáticaEquivalente.Terminales);
                        Producción pr = new Producción(P.Encabezado + "->" + producción);                  
                        M[indexNoTerminales][indexTerminales] = pr; 
                        //System.out.println("M["+indexNoTerminales+"]["+indexTerminales+"] = " + pr.Encabezado + "->" + pr.Cuerpo);
                    }
                } 
            }
        }
    }
    
    public boolean canProduceEmpty(String Cuerpo) {
        Queue<String> Cola = new LinkedList<>();
        Cola.add(Cuerpo);
        boolean Flag = false;
        while (!Cola.isEmpty()) {
            String Producción = Cola.poll();
            if (Producción.contains("|")) {
                String[] Productions = Producción.split("\\|");
                for (int i = 0; i < Productions.length; i++) 
                    Cola.add(Productions[i]);   
            } else {
                String Alpha = Producción.substring(0, 1);
                if (Producción.length() > 1) 
                    if (Producción.substring(1, 2).equals("'")) Alpha = Alpha + "'";
                if (Alpha.length() == 1) {
                    if (Alpha.equals("&")) { Flag = true; break; }
                    if (GramáticaEquivalente.NoTerminales.contains(Alpha)) Cola.add(getProducción(Alpha).Cuerpo);
                } else if (GramáticaEquivalente.NoTerminales.contains(Alpha)) Cola.add(getProducción(Alpha).Cuerpo);
            }
        }
        return Flag;
    }
    
    public Producción getProducción(String Encabezado) {
        for (Producción P: GramáticaEquivalente.Producciones) 
            if (P.Encabezado.equals(Encabezado)) return P;   
        return null;
    }       
}

