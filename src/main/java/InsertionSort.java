/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

//package com.mycompany.asd01052026;

/**
 * @author Andrea_Sergiacomi
 * Immaginiamo un procedimento di questo tipo:
 * Ho un mazzo di carte alla mia destra.
 * -> con il ciclo FOR le prendo una ad una a partire da quella in prima posizione.
 * Con il ciclo WHILE metto le carte in un nuovo mazzo ordinato alla mia sinistra.
 * -> inserisco la nuova carta nella posizione più a sinistra possibile, procedendo finchè trovo nella posizione corrente (la key) una carta di valore maggiore.
 * -> alla prima iterazione naturalmente metto la carta nel primo posto del mazzo di sinistra.
 * -> con la successiva iterazione la metto più a sinistra, se non è maggiore, o la lascio in "pole position" se è maggiore/uguale, rispetto a quella che già c'era.
 * -> e così via.
 * 
 * NB: Java usa indici 0-based
 * 
 * Nell'Insertion Sort il ciclo esterno FOR fa avanzare il valore di j di 1 ad ogni iterazione e si esegue n-1 volte indipendentemente dai valori da ordinare.
 * Il ciclo interno WHILE ha un numero di iterazioni che dipende dai valori da ordinare:
 *    La variabile i viene inizializzata a j-1 e diminuisce di 1 ad ogni iterazione.
 *    Se il vettore è già ordinato per tutti i suoi elementi (o ha 1 solo elemento e dunque è ordinato per definizione A[i]=key, oppure è vuoto) è eseguito 0 volte.
 *    Altrimenti viene eseguito per qualunque valore nel mezzo fino a j-1 volte.
 */
public class InsertionSort {

    /**
     * Ordina l'array in ordine crescente usando Insertion Sort.
     * Il tempo di esecuzione è dominato dal ciclo WHILE interno
     * Complessità:
     * O(n^2) caso peggiore (vettore in ordine decrescente) / medio.
     * O(n)   caso migliore (vettore già ordinato in ordine crescente, oppure vuoto o con 1 solo elemento.
     *
     * @param A array di interi da ordinare (modificato in-place)
     */
    public static void insertionSort(int[] A) 
    {
        int quante_volte_FOR = 0;
        int quante_volte_WHILE_totale = 0;
        // scorro tutti i numeri nel "mazzo" di destra
        for (int j = 1; j < A.length; j++)
        {
            // metto in una variabile di appoggio (key) il valore dell'elemento da considerare (preso dal mazzo di destra)
            int key = A[j];
            quante_volte_FOR = quante_volte_FOR + 1;
            int quante_volte_WHILE_corrente = 0;
            // Inserisce A[j] nella sequenza ordinata A[0..j-1]
            int i = j - 1;
            // proseguo finché ci sono elementi da scorrere e il valore dell'elemento corrente è non maggiore di quello considerato
            while (i >= 0 && A[i] > key) 
            {
                quante_volte_WHILE_totale = quante_volte_WHILE_totale + 1;
                quante_volte_WHILE_corrente = quante_volte_WHILE_corrente + 1;
                // Sposta il valore nell'elemento corrente più a destra (successivo) 
                // -> è questa la riga che, date le condizioni del while, nel caso peggiore genera un'accumulazione di passaggi/tempo
                A[i + 1] = A[i];
                i--;
            }
            System.out.print("      il ciclo WHILE nello step FOR(" + j + ") corrente e' eseguito.. = " + quante_volte_WHILE_corrente + " volte\n");
            A[i + 1] = key;
        }
        System.out.print("   il ciclo FOR esterno viene eseguito....................... (n-1) = " + quante_volte_FOR + " volte\n");
        System.out.print("   il ciclo WHILE interno viene eseguito.. [al massimo (n-1)*(n-1)] = " + quante_volte_WHILE_totale + " volte\n");
    }

    public static void main(String[] args) {
        int[] A = {5, 2, 4, 6, 1, 3};
        //int[] A = {1, 2, 3, 4, 5, 6};
        //int[] A = {6, 5, 4, 3, 2, 1};
        //int[] A = {6};
        //int[] A = {};
        
        int n = 0;
        n=A.length;

        System.out.print("   il vettore A in input contiene n = " + n + " elementi\n\n");
        
        System.out.print("Prima:  ");
        for (int x : A) System.out.print(x + " ");
        System.out.println();
        System.out.println();

        insertionSort(A);

        System.out.print("\nDopo:   ");
        for (int x : A) System.out.print(x + " ");
    }
}
