/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 * 
 * Output:
 * Array originale: [12, 1, 8, 17, 25, 3, 44, 12, 9, 30]
 * Lomuto:          [1, 3, 8, 9, 12, 12, 17, 25, 30, 44]
 * Randomizzato:    [1, 3, 8, 9, 12, 12, 17, 25, 30, 44]
 * Hoare:           [1, 3, 8, 9, 12, 12, 17, 25, 30, 44]
 * 
 * --- Caso peggiore deterministico: array già ordinato ---
 * Prima:           [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
 * Dopo Lomuto:     [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
 * Dopo Random:     [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
 * 
 */
import java.util.Arrays;
import java.util.Random;

public class QuickSort {

    private static final Random RNG = new Random();

    // =========================================================
    //  PARTIZIONE DI LOMUTO — O(n)
    //  Sceglie A[r] come pivot.
    //  Invariante: A[p..i] <= pivot, A[i+1..j-1] > pivot
    //  Restituisce l'indice finale del pivot.
    // =========================================================

    private static int partitionLomuto(int[] A, int p, int r) {
        int pivot = A[r];   // il pivot è sempre l'ultimo elemento
        int i = p - 1;      // i = ultimo indice della zona "piccoli"

        for (int j = p; j < r; j++) {
            if (A[j] <= pivot) {
                i++;
                swap(A, i, j);  // porta A[j] nella zona "piccoli"
            }
        }
        swap(A, i + 1, r);  // mette il pivot nella posizione corretta
        return i + 1;        // restituisce l'indice del pivot
    }

    // =========================================================
    //  PARTIZIONE DI HOARE — O(n)
    //  Versione originale del Cormen: due indici che si avvicinano.
    //  Sceglie A[p] come pivot. Più efficiente di Lomuto
    //  (circa 3 volte meno scambi in media).
    //  ATTENZIONE: restituisce un indice q tale che
    //  A[p..q] <= pivot e A[q+1..r] >= pivot
    //  (il pivot NON è necessariamente in A[q])
    // =========================================================

    private static int partitionHoare(int[] A, int p, int r) {
        int pivot = A[p];   // il pivot è il primo elemento
        int i = p - 1;
        int j = r + 1;

        while (true) {
            // Avanza j verso sinistra finché A[j] >= pivot
            do { j--; } while (A[j] > pivot);
            // Avanza i verso destra finché A[i] <= pivot
            do { i++; } while (A[i] < pivot);

            if (i < j)
                swap(A, i, j);
            else
                return j;   // punto di divisione
        }
    }

    // =========================================================
    //  QUICKSORT DETERMINISTICO (con partizione di Lomuto)
    //  Caso medio: Θ(n lg n)
    //  Caso peggiore: Θ(n²) — array già ordinato o inversamente
    // =========================================================

    public static void quickSort(int[] A, int p, int r) {
        if (p < r) {
            int q = partitionLomuto(A, p, r);
            quickSort(A, p, q - 1);   // ricorri sul lato sinistro
            quickSort(A, q + 1, r);   // ricorri sul lato destro
        }
    }

    // =========================================================
    //  QUICKSORT RANDOMIZZATO
    //  Prima di partizionare, scambia A[r] con un elemento
    //  scelto a caso in A[p..r]. Questo rende il caso peggiore
    //  estremamente improbabile indipendentemente dall'input.
    //  Caso medio atteso: Θ(n lg n) su qualunque input.
    // =========================================================

    public static void quickSortRandom(int[] A, int p, int r) {
        if (p < r) {
            // Scegli un pivot casuale e portalo in A[r]
            int pivot = p + RNG.nextInt(r - p + 1);
            swap(A, pivot, r);

            int q = partitionLomuto(A, p, r);
            quickSortRandom(A, p, q - 1);
            quickSortRandom(A, q + 1, r);
        }
    }

    // =========================================================
    //  QUICKSORT CON HOARE (deterministico)
    //  Usa la partizione di Hoare: A[p] come pivot,
    //  leggermente diversa nella semantica dell'indice restituito
    // =========================================================

    public static void quickSortHoare(int[] A, int p, int r) {
        if (p < r) {
            int q = partitionHoare(A, p, r);
            quickSortHoare(A, p, q);      // nota: p..q (non p..q-1)
            quickSortHoare(A, q + 1, r);
        }
    }

    // =========================================================
    //  UTILITY
    // =========================================================

    private static void swap(int[] A, int i, int j) {
        int tmp = A[i]; A[i] = A[j]; A[j] = tmp;
    }

    // =========================================================
    //  MAIN — test e confronto delle tre varianti
    // =========================================================

    public static void main(String[] args) {
        int[] base = {12, 1, 8, 17, 25, 3, 44, 12, 9, 30};

        System.out.println("Array originale: " + Arrays.toString(base));

        // --- Quicksort deterministico (Lomuto) ---
        int[] a1 = base.clone();
        quickSort(a1, 0, a1.length - 1);
        System.out.println("Lomuto:          " + Arrays.toString(a1));

        // --- Quicksort randomizzato ---
        int[] a2 = base.clone();
        quickSortRandom(a2, 0, a2.length - 1);
        System.out.println("Randomizzato:    " + Arrays.toString(a2));

        // --- Quicksort Hoare ---
        int[] a3 = base.clone();
        quickSortHoare(a3, 0, a3.length - 1);
        System.out.println("Hoare:           " + Arrays.toString(a3));

        // --- Test caso peggiore: array già ordinato ---
        System.out.println("\n--- Caso peggiore deterministico: array già ordinato ---");
        int[] sorted = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("Prima:           " + Arrays.toString(sorted));
        quickSort(sorted, 0, sorted.length - 1);
        System.out.println("Dopo Lomuto:     " + Arrays.toString(sorted));
        // Con Lomuto su array ordinato: pivot sempre l'ultimo (già massimo)
        // → partizione sempre 0 | n-1 → Θ(n²) confronti

        // --- Stesso caso con randomizzato: il pivot casuale evita il degrado ---
        int[] sorted2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        quickSortRandom(sorted2, 0, sorted2.length - 1);
        System.out.println("Dopo Random:     " + Arrays.toString(sorted2));
    }
}
