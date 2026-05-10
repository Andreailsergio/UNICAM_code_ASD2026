/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 */
public class RicercaBinaria_Iterativa {

    /**
     * Ricerca binaria iterativa su array ordinato.
     * Restituisce l'indice di x se trovato, -1 altrimenti.
     *
     * Pseudocodice Cormen (ITERATIVE-BINARY-SEARCH):
     *   while low <= high
     *     mid = floor((low + high) / 2)
     *     if x == A[mid]  return mid
     *     elseif x > A[mid]  low = mid + 1
     *     else  high = mid - 1
     *   return NIL
     *
     * T(n) = Θ(log n)
     * 
     * OUTPUT:
     * Elemento 17 trovato in posizione 5
     * La logica è identica alla versione ricorsiva: low e high delimitano la porzione di array ancora da esaminare, 
     * e ad ogni iterazione il while dimezza l'intervallo aggiornando uno dei due estremi. 
     * Il ciclo termina quando low > high (elemento assente) o quando A[mid] == x (elemento trovato).
     * La complessità rimane Θ(log n) — la ricorrenza T(n) = T(n/2) + Θ(1) ha la stessa soluzione 
     * — ma la versione iterativa è preferibile, in pratica, perché evita l'overhead delle chiamate ricorsive sullo stack.
     */
    public static int ricercaBinaria_Iterativa(int[] A, int x) {
        int low = 0;
        int high = A.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (x == A[mid]) {
                return mid;        // trovato
            } else if (x > A[mid]) {
                low = mid + 1;     // cerca nella metà destra
            } else {
                high = mid - 1;    // cerca nella metà sinistra
            }
        }
        return -1; // non trovato
    }

    public static void main(String[] args) {
        int[] A = {1, 3, 5, 8, 12, 17, 25, 30, 44, 55};
        int x = 17;

        int risultato = ricercaBinaria_Iterativa(A, x);

        if (risultato != -1) {
            System.out.println("Elemento " + x + " trovato in posizione " + risultato);
        } else {
            System.out.println("Elemento " + x + " non trovato");
        }
    }
}
