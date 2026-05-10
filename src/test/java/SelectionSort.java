/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 * 
 * OUTPUT
 * Numeri
 * Prima:  64 25 12 22 11
 * Dopo:   11 12 22 25 64
 * 
 * Parole
 * Prima: banana albicocca ciliegia dattero
 * Dopo: albicocca banana ciliegia dattero
 * 
 * Il ciclo esterno va da 0 a n-2 perché dopo n-1 iterazioni l'ultimo elemento è già in posizione. 
 * Il ciclo interno trova il minimo del sottarray non ancora ordinato, e lo scambia con A[i] solo se minIndex != i, evitando uno swap inutile. 
 * La versione generica usa Comparable<T> per funzionare con qualunque tipo ordinabile (Integer, String, ecc.).
 * Riguardo alla complessità: il numero di confronti è sempre esattamente (n-1) + (n-2) + … + 1 = n(n-1)/2 = Θ(n²), indipendentemente dall'input
 * — a differenza di insertion sort che può terminare prima su array quasi ordinati. 
 * Il numero di scambi è al più n-1, che è ottimale tra gli algoritmi quadratici.
 * 
 */
public class SelectionSort {

    /**
     * Ordina l'array in-place con selection sort (ordine crescente).
     * Invariante: al termine dell'i-esima iterazione, A[0..i] contiene
     * gli i+1 elementi più piccoli, in ordine.
     * Complessità: Θ(n²) in ogni caso (non migliora su array già ordinati).
     */
    public static void selectionSort(int[] A) {
        int n = A.length;
        for (int i = 0; i < n - 1; i++) {
            // Trova l'indice del minimo nel sottarray A[i..n-1]
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (A[j] < A[minIndex]) {
                    minIndex = j;
                }
            }
            // Scambia A[i] con il minimo trovato (solo se necessario)
            if (minIndex != i) {
                int temp = A[i];
                A[i] = A[minIndex];
                A[minIndex] = temp;
            }
        }
    }

    // --- Versione generica con Comparable ---

    public static <T extends Comparable<T>> void selectionSort(T[] A) {
        int n = A.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (A[j].compareTo(A[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                T temp = A[i];
                A[i] = A[minIndex];
                A[minIndex] = temp;
            }
        }
    }

    // --- Main di test ---

    public static void main(String[] args) {
        int[] arr = {64, 25, 12, 22, 11};
        System.out.print("Numeri: \n");
        System.out.print("Prima:  ");
        stampa(arr);
        selectionSort(arr);
        System.out.print("Dopo:   ");
        stampa(arr);

        // Test con tipi generici
        System.out.print("Parole: \n");
        String[] parole = {"banana", "albicocca", "ciliegia", "dattero"};
        System.out.print("Prima: ");
        for (String s : parole) System.out.print(s + " ");
        System.out.println();
        selectionSort(parole);
        System.out.print("Dopo:  ");
        for (String s : parole) System.out.print(s + " ");
        System.out.println();
    }

    private static void stampa(int[] A) {
        for (int x : A) System.out.print(x + " ");
        System.out.println();
    }
}