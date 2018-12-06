/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.utileria;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Carlos Onorio
 */
public class UtileriaCadenaTest {
    
    public UtileriaCadenaTest() {
    }

    @Test
    public void probarCadenaValida() {
        boolean valorEsperado = true;
        boolean valorObtenido = UtileriaCadena.validarCadena("cadena", 1, 6);
        assertEquals("Cadena válida", valorEsperado, valorObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void probarCadenaLongitudInvalida() {
        boolean valorEsperado = false;
        boolean valorObtenido = UtileriaCadena.validarCadena("cadena", 1, 3);
        assertEquals("Cadena inválida", valorEsperado, valorObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void probarCadenaVacia() {
        boolean valorEsperado = false;
        boolean valorObtenido = UtileriaCadena.validarCadena("", 1, 3);
        assertEquals("Cadena inválida", valorEsperado, valorObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void probarCadenaNula() {
        boolean valorEsperado = false;
        boolean valorObtenido = UtileriaCadena.validarCadena(null, 1, 3);
        assertEquals("Cadena inválida", valorEsperado, valorObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void probarCadenaEmpiezaEspacio() {
        boolean valorEsperado = false;
        boolean valorObtenido = UtileriaCadena.validarCadena(" hola", 1, 3);
        assertEquals("Cadena inválida", valorEsperado, valorObtenido);
    }
    
}
