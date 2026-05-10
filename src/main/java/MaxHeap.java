/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 * 
 * Alcune note di progettazione:
 * 1) L'array è 1-based internamente (indice 0 inutilizzato): 
 * questo rende le formule parent = i/2, left = 2i, right = 2i+1 identiche allo pseudocodice del Cormen, senza correzioni di offset. 
 * 
 * 2) heapSort è statico perché opera su un array esterno — costruisce un MaxHeap temporaneo, 
 * poi riusa la stessa rappresentazione per ordinare in loco. 
 * 
 * 3) Le operazioni della coda di priorità lanciano eccezioni controllate (NoSuchElementException, IllegalArgumentException, IllegalStateException) 
 * invece di silenziare gli errori, come richiesto da una buona pratica Java.
 * 
 * 
 * OUTPUT
 * === BUILD-HEAP e HEAPIFY ===
 * Dopo buildHeap: [16, 14, 10, 8, 7, 9, 3, 2, 4, 1]
 *                           16
 *              14                         10
 *       8             7              9         3
 *   2      4       1
 * 
 * === HEAPSORT ===
 * Prima:  [5, 13, 2, 25, 7, 17, 20, 8, 4]
 * Dopo:   [2, 4, 5, 7, 8, 13, 17, 20, 25]
 * 
 * === CODA CON PRIORITÀ ===
 * Dopo 5 insert:       [15, 13, 9, 5, 12]
 * maximum():           15
 * extractMax():        15
 * Heap dopo extract:   [13, 12, 9, 5]
 * Dopo increaseKey(3,20): [20, 12, 13, 5]   // increaseKey(3,20) agisce sul nodo in posizione 3 (indice 1-based) contenente 9 e aggiorna il valore a 20
 *                                           // poi con un while scambio le posizioni per ripristinare le proprietà dell'heap 
 *                                           // (riportando 20 - che è il nuovo massimo - in radice)
 * extractMax():        20
 * extractMax():        13
 * Heap finale:         [12, 5]
 * 
 */
import java.util.Arrays;
import java.util.NoSuchElementException;

public class MaxHeap {

    private int[] A;        // array che rappresenta lo heap (indice 1-based)
    private int heapSize;   // numero di elementi attualmente nello heap
    private int length;     // dimensione dell'array allocato

    // =========================================================
    //  COSTRUTTORI
    // =========================================================

    /** Costruisce un heap vuoto con capacità massima data. */
    public MaxHeap(int capacity) {
        this.A = new int[capacity + 1]; // indice 1-based: A[1..n]
        this.heapSize = 0;
        this.length = capacity;
    }

    /** Costruisce un heap a partire da un array esistente (0-based in input). */
    public MaxHeap(int[] input) {
        this.length = input.length;
        this.A = new int[length + 1];
        for (int i = 0; i < length; i++) A[i + 1] = input[i];
        this.heapSize = length;
        buildHeap();
    }

    // =========================================================
    //  NAVIGAZIONE — PARENT, LEFT, RIGHT
    // =========================================================

    private int parent(int i) { return i / 2; }
    private int left(int i)   { return 2 * i; }
    private int right(int i)  { return 2 * i + 1; }

    private void swap(int i, int j) {
        int tmp = A[i]; A[i] = A[j]; A[j] = tmp;
    }

    // =========================================================
    //  HEAPIFY — O(lg n)
    //  Precondizione: i sottoalberi left(i) e right(i) sono heap.
    //  Postcondizione: il sottoalbero radicato in i è uno heap.
    // =========================================================

    public void heapify(int i) {
        int l = left(i);
        int r = right(i);
        int largest;

        // Trova il più grande tra A[i], A[left(i)], A[right(i)]
        if (l <= heapSize && A[l] > A[i])
            largest = l;
        else
            largest = i;

        if (r <= heapSize && A[r] > A[largest])
            largest = r;

        // Se A[i] non è il massimo, scambia e ricorri
        if (largest != i) {
            swap(i, largest);
            heapify(largest);
        }
    }

    // =========================================================
    //  BUILD-HEAP — O(n)
    //  Converte l'array in uno heap chiamando heapify bottom-up
    //  su tutti i nodi interni (le foglie sono già heap banali).
    // =========================================================

    public void buildHeap() {
        // I nodi da heapSize/2+1 a heapSize sono foglie, si parte dai nodi interni
        for (int i = heapSize / 2; i >= 1; i--) {
            heapify(i);
        }
    }

    // =========================================================
    //  HEAPSORT — O(n lg n)  [metodo statico, opera su array esterno]
    //  1. Costruisce uno heap con buildHeap  — O(n)
    //  2. Estrae n-1 volte il massimo       — O(n lg n)
    // =========================================================

    public static void heapSort(int[] input) {
        MaxHeap h = new MaxHeap(input);     // buildHeap chiamato internamente

        // Ad ogni iterazione la radice (massimo) viene spostata in fondo
        // e heapSize viene ridotto di 1
        for (int i = h.heapSize; i >= 2; i--) {
            h.swap(1, i);                   // porta il massimo in A[i]
            h.heapSize--;                   // esclude A[i] dallo heap
            h.heapify(1);                   // ripristina la proprietà heap
        }

        // Copia il risultato nell'array originale (converti da 1-based a 0-based)
        for (int i = 0; i < input.length; i++) input[i] = h.A[i + 1];
    }

    // =========================================================
    //  CODA CON PRIORITÀ
    // =========================================================

    /**
     * MAXIMUM — O(1)
     * Restituisce (senza rimuovere) l'elemento con chiave massima.
     * Il massimo è sempre nella radice A[1].
     */
    public int maximum() {
        if (heapSize < 1) throw new NoSuchElementException("Heap vuoto");
        return A[1];
    }

    /**
     * EXTRACT-MAX — O(lg n)
     * Rimuove e restituisce l'elemento massimo.
     * Sostituisce la radice con l'ultimo elemento, decrementa heapSize,
     * poi chiama heapify(1) per ripristinare la proprietà heap.
     */
    public int extractMax() {
        if (heapSize < 1) throw new NoSuchElementException("Heap vuoto");

        int max = A[1];             // salva il massimo
        A[1] = A[heapSize];         // porta l'ultimo elemento alla radice
        heapSize--;                 // riduce lo heap
        heapify(1);                 // ripristina la proprietà heap
        return max;
    }

    /**
     * INCREASE-KEY — O(lg n)
     * Aumenta la chiave dell'elemento in posizione i al valore key.
     * Poi "fa risalire" l'elemento verso la radice finché la proprietà
     * heap non è soddisfatta (come nell'insertion sort).
     */
    public void increaseKey(int i, int key) {
        if (key < A[i])
            throw new IllegalArgumentException(
                "La nuova chiave " + key + " è minore di quella attuale " + A[i]);

        A[i] = key;

        // Risali verso la radice finché il padre è più piccolo
        while (i > 1 && A[parent(i)] < A[i]) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    /**
     * INSERT — O(lg n)
     * Inserisce un nuovo elemento con chiave key.
     * Aggiunge una foglia con chiave -∞, poi chiama increaseKey.
     */
    public void insert(int key) {
        if (heapSize >= length)
            throw new IllegalStateException("Heap pieno");

        heapSize++;
        A[heapSize] = Integer.MIN_VALUE;   // inserisce con chiave -∞
        increaseKey(heapSize, key);        // porta la chiave al valore corretto
    }

    // =========================================================
    //  UTILITY
    // =========================================================

    public int size()    { return heapSize; }
    public boolean isEmpty() { return heapSize == 0; }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOfRange(A, 1, heapSize + 1));
    }

    // =========================================================
    //  MAIN — test di tutte le procedure
    // =========================================================

    public static void main(String[] args) {

        System.out.println("=== BUILD-HEAP e HEAPIFY ===");
        int[] arr = {4, 1, 3, 2, 16, 9, 10, 14, 8, 7};
        MaxHeap h = new MaxHeap(arr);
        System.out.println("Dopo buildHeap: " + h);   // [16,14,10,8,7,9,3,2,4,1]

        System.out.println("\n=== HEAPSORT ===");
        int[] toSort = {5, 13, 2, 25, 7, 17, 20, 8, 4};
        System.out.println("Prima:  " + Arrays.toString(toSort));
        heapSort(toSort);
        System.out.println("Dopo:   " + Arrays.toString(toSort));

        System.out.println("\n=== CODA CON PRIORITÀ ===");
        MaxHeap pq = new MaxHeap(10);
        pq.insert(15);
        pq.insert(13);
        pq.insert(9);
        pq.insert(5);
        pq.insert(12);
        System.out.println("Dopo 5 insert:       " + pq);

        System.out.println("maximum():           " + pq.maximum());

        System.out.println("extractMax():        " + pq.extractMax());
        System.out.println("Heap dopo extract:   " + pq);

        pq.increaseKey(3, 20);  // aumenta A[3] a 20
        System.out.println("Dopo increaseKey(3,20): " + pq);

        System.out.println("extractMax():        " + pq.extractMax());
        System.out.println("extractMax():        " + pq.extractMax());
        System.out.println("Heap finale:         " + pq);
    }
}
