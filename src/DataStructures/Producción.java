/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author KonnerEL
 */
public final class Producción {
    String Encabezado;
    String Cuerpo;
    public Set<String>Primero;
    public Set<String>Siguiente;
    
    public Producción(String producción) {
        String[] splitProducción = producción.split("->");
        setEncabezado(splitProducción[0]);
        setCuerpo(splitProducción[1]);
        Primero = new LinkedHashSet<>();
        Siguiente = new LinkedHashSet<>();
    }
    
    public void setEncabezado(String encabezado) {
        Encabezado = encabezado;
    }
    
    public void setCuerpo(String cuerpo) {
        Cuerpo = cuerpo;
    }

    public String getEncabezado() {
        return Encabezado;
    }

    public String getCuerpo() {
        return Cuerpo;
    }
}
