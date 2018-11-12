/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.partida;

import java.io.Serializable;

/**
 * Concentra la cantidad de veces que fueron seleccionadas cada una de las
 * respuestas a una pregunta
 *
 * @version 1.0 29 Oct 2018
 * @author Carlos Onorio
 */
public class GraficaRespuestas implements Serializable {

    private int contadorA;
    private int contadorB;
    private int contadorC;
    private int contadorD;

    /**
     * Inicializa los contadores de las respuestas en 0
     */
    public GraficaRespuestas() {
        this.contadorA = 0;
        this.contadorB = 0;
        this.contadorC = 0;
        this.contadorD = 0;
    }

    /**
     * Aumenta el contador de la respuesta (A,B,C,D) que llega como parametro
     *
     * @param respuesta Respuesta elegida por un jugador
     */
    public void actualizarGrafica(char respuesta) {
        switch (respuesta) {
            case 'A':
                contadorA++;
                break;
            case 'B':
                contadorB++;
                break;
            case 'C':
                contadorC++;
                break;
            case 'D':
                contadorD++;
                break;
        }
    }
    
    public int getContadorA() {
        return contadorA;
    }

    public int getContadorB() {
        return contadorB;
    }

    public int getContadorC() {
        return contadorC;
    }

    public int getContadorD() {
        return contadorD;
    }

}
