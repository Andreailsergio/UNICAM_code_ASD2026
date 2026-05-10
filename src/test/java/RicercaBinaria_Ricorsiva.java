/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 * Output:
 * Elemento 17 trovato in posizione 5
 * Si calcola l'indice di mezzo mid, si confronta con x, e si richiama ricorsivamente solo sulla metà sinistra (low..mid-1) o destra (mid+1..high). 
 * La condizione low > high è il caso base che gestisce la ricerca fallita.
 * 
 * La ricorrenza è T(n) = T(n/2) + 1, che per il Teorema Master (caso 2 con a=1, b=2, f(n)=1) dà T(n) = Θ(log n) 
 *  — drasticamente migliore della ricerca sequenziale O(n), ma richiede che l'array sia ordinato.
 * 
 */
public class RicercaBinaria_Ricorsiva {

    /**
     * Ricerca binaria ricorsiva su array ordinato.
     * Restituisce l'indice di x se trovato, -1 altrimenti.
     *
     * T(n) = T(n/2) + 1  →  T(n) = Θ(log n)
     */
    public static int ricercaBinaria_Ricorsiva(int[] A, int x, int low, int high) {
        if (low > high) {
            return -1; // non trovato
        }
        int mid = (low + high) / 2;
        if (A[mid] == x) {
            return mid;
        } else if (A[mid] > x) {
            return ricercaBinaria_Ricorsiva(A, x, low, mid - 1);
        } else {
            return ricercaBinaria_Ricorsiva(A, x, mid + 1, high);
        }
    }

    public static void main(String[] args) {
        int[] A = {1, 3, 5, 8, 12, 17, 25, 30, 44, 55};
        int x = 17;

        int risultato = ricercaBinaria_Ricorsiva(A, x, 0, A.length - 1);

        if (risultato != -1) {
            System.out.println("Elemento " + x + " trovato in posizione " + risultato);
        } else {
            System.out.println("Elemento " + x + " non trovato");
        }
    }
}