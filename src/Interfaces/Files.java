/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template Files, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.io.File; 
import java.io.FileNotFoundException; 
import java.util.Scanner; 
/**
 *
 * @author KonnerEL
 */ 
public class Files {
    File file;
    public Scanner sc;
    public Files(String source) throws FileNotFoundException {
        file = new File(source); 
        sc = new Scanner(file);
        sc.useDelimiter("\\Z"); 
    }
    
    public String readNextLine() {
        return sc.nextLine();
    }
}
