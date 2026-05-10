/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 */
public class RicercaSequenziale {

    /**
     * Cerca x nell'array A (non ordinato).
     * Restituisce l'indice di x se trovato, -1 altrimenti.
     *
     * T_best(n)    = 1         → x è in prima posizione
     * T_worst(n)   = n         → x è in ultima posizione o assente
     * T_average(n) = (n+1)/2   → distribuzione uniforme
     * 
     * si scorre l'array dall'inizio, e si esce appena si trova il primo match — senza continuare inutilmente. 
     * Se il ciclo termina senza trovare nulla, si restituisce -1 (equivalente al "non trovato")
     */
    public static int ricercaSequenziale(int[] A, int x) {
        for (int i = 0; i < A.length; i++) {
            if (A[i] == x) {
                return i; // trovato: restituisce la posizione
            }
        }
        return -1; // non trovato
    }

    public static void main(String[] args) {
        int[] A = {12, 1, 8, 17, 25, 3, 44, 12, 9, 30};
        // int x = 25; // average "Elemento 25 trovato in posizione 4"
        // int x = 114; // worst "Elemento 114 non trovato"
        // int x = 12;  // best -> pole position "Elemento 12 trovato in posizione 0"
        int x = 30;  // worst -> last position "Elemento 30 trovato in posizione 9"

        int risultato = ricercaSequenziale(A, x);

        if (risultato != -1) {
            System.out.println("Elemento " + x + " trovato in posizione " + risultato);
        } else {
            System.out.println("Elemento " + x + " non trovato");
        }
    }
}