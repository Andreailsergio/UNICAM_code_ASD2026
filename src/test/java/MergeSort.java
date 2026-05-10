/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 */
public class MergeSort {

    /**
     * MERGE-SORT(A, p, r) - Cormen cap. 2
     * Ordina il sottoarray A[p..r] in ordine crescente.
     * 
     * Nel caso peggiore ha complessità Theta(n*log n) + aggravio di occupazione spazio Omega(n)
     */
    public static void mergeSort(int[] A, int p, int r) 
    {
        // controlla che non siamo nel caso base "c'è un solo elemento"
        if (p < r) {
            int q = (p + r) / 2;          // Divide -> trova l'indice dell'elemento di mezzo (arrontondando all'intero)
            mergeSort(A, p, q);            // Impera (sinistra) ricorsivamente
            mergeSort(A, q + 1, r);        // Impera (destra) ricorsivamente
            merge(A, p, q, r);             // Combina
        }
    }

    /**
     * MERGE(A, p, q, r) - Cormen cap. 2
     * Fonde i sottoarray ordinati A[p..q] e A[q+1..r].
     */
    public static void merge(int[] A, int p, int q, int r) {
        int nL = q - p + 1;   // dimensione sottoarray sinistro
        int nR = r - q;        // dimensione sottoarray destro

        // Crea i due sottoarray temporanei
        // la creazione di strutture dati temporanee ulteriori richiede un aggravio computazionale (> tempo di esecuzione proporzionale al numero di elementi n)
        int[] L = new int[nL];
        int[] R = new int[nR];

        // Copia i dati
        for (int i = 0; i < nL; i++) L[i] = A[p + i];
        for (int j = 0; j < nR; j++) R[j] = A[q + 1 + j];

        // Fondi L e R in A[p..r]
        int i = 0, j = 0, k = p;
        // finché ci sono elementi in entrambi i vettori di appoggio destro e sinistro -> inserisco nell'array A il più piccolo/uguale dopo un confronto
        while (i < nL && j < nR) {
            if (L[i] <= R[j]) {
                A[k] = L[i];
                i++;
            } else {
                A[k] = R[j];
                j++;
            }
            k++;
        }

        // Copia gli elementi rimanenti di destra o di sinistra (se ce ne sono)
        // a questo punto già ordinati quindi semplicemente li inserisco in sequenza con lo stesso ordine
        while (i < nL) { A[k] = L[i]; i++; k++; }
        while (j < nR) { A[k] = R[j]; j++; k++; }
    }

    public static void main(String[] args) {
        int[] A = {5, 2, 4, 6, 1, 3};

        System.out.print("Prima:  ");
        for (int x : A) System.out.print(x + " ");

        // passo alla classe il vettore e gli indici del primo e dell'ultimo elemento
        mergeSort(A, 0, A.length - 1);

        System.out.print("\nDopo:   ");
        for (int x : A) System.out.print(x + " ");
    }
}
