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
 * === BUBBLE SORT ===
  casuale   → prima: [12,1,8,17,25,3,44,12,9,30]   dopo: [1,3,8,9,12,12,17,25,30,44]
  ordinato  → prima: [1,3,8,9,12,17,25,30,44,100]  dopo: [1,3,8,9,12,17,25,30,44,100]
  tartaruga → prima: [10,20,30,40,50,60,70,80,90,1] dopo: [1,10,20,30,40,50,60,70,80,90]

 * === SHAKER SORT ===
  casuale   → prima: [12,1,8,17,25,3,44,12,9,30]   dopo: [1,3,8,9,12,12,17,25,30,44]
  ordinato  → prima: [1,3,8,9,12,17,25,30,44,100]  dopo: [1,3,8,9,12,17,25,30,44,100]
  tartaruga → prima: [10,20,30,40,50,60,70,80,90,1] dopo: [1,10,20,30,40,50,60,70,80,90]
 * 
 * Il punto chiave è sul caso tartaruga: 
 * con il bubble sort, il valore 1 si trova in fondo e deve risalire di un posto a sinistra per ogni passata completa — servono 9 passate. 
 * Con lo shaker sort invece la mezza-passata da destra verso sinistra lo trascina immediatamente in posizione nella prima iterazione del while, 
 * riducendo drasticamente il lavoro. 
 * I due left++ e right-- che si aggiornano ad ogni round garantiscono che le zone già ordinate ai due estremi non vengano mai riesaminate.
 */
import java.util.Arrays;

public class BubbleAndShakerSort {

    // =========================================================
    //  BUBBLE SORT
    // =========================================================

    /**
     * Bubble sort ottimizzato con flag "modified".
     *
     * Invariante: dopo la passata i, gli ultimi i elementi
     * sono in posizione definitiva.
     *
     * Complessità:
     *   - Caso migliore:  Θ(n)   — array già ordinato (flag ferma al 1° giro)
     *   - Caso peggiore:  Θ(n²)  — array ordinato al contrario
     */
    public static void bubbleSort(int[] A) {
        int n = A.length;
        for (int i = 0; i < n - 1; i++) {
            boolean modified = false;

            // Ogni passata "fa galleggiare" il massimo del tratto A[0..n-1-i]
            // verso destra; il limite si restringe di 1 ad ogni giro
            for (int j = 0; j < n - 1 - i; j++) {
                if (A[j] > A[j + 1]) {
                    swap(A, j, j + 1);
                    modified = true;
                }
            }

            // Se non è stato fatto nessuno scambio l'array è già ordinato
            if (!modified) return;
        }
    }

    // =========================================================
    //  SHAKER SORT  (Cocktail Sort)
    // =========================================================

    /**
     * Shaker sort: variante bidirezionale del bubble sort.
     *
     * Ogni "round" è composto da due mezze-passate:
     *   1. da sinistra verso destra → porta il massimo a destra
     *   2. da destra verso sinistra → porta il minimo a sinistra
     *
     * Questo risolve il problema del bubble sort con i valori
     * piccoli posizionati in fondo all'array ("tartarughe"):
     * nel bubble sort si spostano di un solo posto a sinistra
     * per passata, qui vengono trascinati rapidamente a sinistra
     * nella mezza-passata inversa.
     *
     * Complessità: stessa complessità asintotica del bubble sort,
     * ma in pratica più veloce su array parzialmente ordinati
     * o con "tartarughe".
     */
    public static void shakerSort(int[] A) {
        int left  = 0;          // limite sinistro della zona non ordinata
        int right = A.length - 1; // limite destro della zona non ordinata

        while (left < right) {
            boolean modified = false;

            // --- Mezza-passata sinistra → destra ---
            // fa salire il massimo fino in fondo (posizione 'right')
            for (int j = left; j < right; j++) {
                if (A[j] > A[j + 1]) {
                    swap(A, j, j + 1);
                    modified = true;
                }
            }
            right--; // il massimo è ora in posizione definitiva

            // --- Mezza-passata destra → sinistra ---
            // fa scendere il minimo fino all'inizio (posizione 'left')
            for (int j = right; j > left; j--) {
                if (A[j] < A[j - 1]) {
                    swap(A, j, j - 1);
                    modified = true;
                }
            }
            left++; // il minimo è ora in posizione definitiva

            // Se nessun round ha prodotto scambi, l'array è ordinato
            if (!modified) return;
        }
    }

    // =========================================================
    //  UTILITY
    // =========================================================

    // Lo swap esegue lo scambio tra due elementi utilizzando una variabile temporanea intermedia
    private static void swap(int[] A, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }

    // =========================================================
    //  MAIN — confronto sui tre casi tipici
    // =========================================================

    public static void main(String[] args) {
        // Caso 1: array casuale
        int[] casuale   = {12, 1, 8, 17, 25, 3, 44, 12, 9, 30};

        // Caso 2: array già ordinato (caso migliore per entrambi)
        int[] ordinato  = {1, 3, 8, 9, 12, 17, 25, 30, 44, 100};

        // Caso 3: "tartaruga" — valore piccolo in fondo
        // Bubble sort impiega molte passate; shaker sort lo risolve in 1 giro
        int[] tartaruga = {10, 20, 30, 40, 50, 60, 70, 80, 90, 1};

        System.out.println("=== BUBBLE SORT ===");
        provaAlgoritmo("casuale  ", casuale.clone(),   BubbleAndShakerSort::bubbleSort);
        provaAlgoritmo("ordinato ", ordinato.clone(),  BubbleAndShakerSort::bubbleSort);
        provaAlgoritmo("tartaruga", tartaruga.clone(), BubbleAndShakerSort::bubbleSort);

        System.out.println("\n=== SHAKER SORT ===");
        provaAlgoritmo("casuale  ", casuale.clone(),   BubbleAndShakerSort::shakerSort);
        provaAlgoritmo("ordinato ", ordinato.clone(),  BubbleAndShakerSort::shakerSort);
        provaAlgoritmo("tartaruga", tartaruga.clone(), BubbleAndShakerSort::shakerSort);
    }

    @FunctionalInterface
    interface SortAlgorithm { void sort(int[] a); }

    private static void provaAlgoritmo(String nome, int[] arr, SortAlgorithm algo) {
        System.out.print("  " + nome + " -> prima:  " + Arrays.toString(arr));
        algo.sort(arr);
        System.out.println("  dopo: " + Arrays.toString(arr));
    }
}