/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * HashTableChiuso.java — Indirizzamento chiuso (concatenazione)
 * 
 * Array di LinkedList<Entry> dove ogni Entry contiene una coppia (key, value)
 * Funzione hash: metodo della divisione h(k) = k mod m
 * insert: inserimento in testa alla lista del bucket → O(1); gestisce anche le chiavi duplicate aggiornando il valore
 * search / delete: scansione della lista del bucket → O(1 + α)
 * Il demo usa m=7 e le stesse chiavi dell'esercizio 12.4-1 del Cormen
 * inserendo 9 coppie key-value n=9; il fattore di carico risultante m/n = 9/7 = 1,285
 * 
 * Tabella Hash ad INDIRIZZAMENTO CHIUSO (Concatenazione / Chaining)
 *
 * Ogni slot della tabella contiene una lista concatenata di Entry (chiave, valore).
 * In caso di collisione, il nuovo elemento viene aggiunto in testa alla lista
 * del bucket corrispondente → inserimento O(1).
 *
 * Operazioni principali:
 *   insert(key, value)  → O(1) ammortizzato
 *   search(key)         → O(1 + α) dove α = n/m è il fattore di carico
 *   delete(key)         → O(1 + α)
 *   printAll()          → O(n + m)
 */
public class HashTableChiuso {

    // ── Struttura di un nodo ────────────────────────────────────────────────
    static class Entry {
        int key;
        int value;

        Entry(int key, int value) {
            this.key   = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "(key=" + key + ", val=" + value + ")";
        }
    }

    // ── Campi della tabella ─────────────────────────────────────────────────
    private final int m;                          // numero di bucket
    private final LinkedList<Entry>[] table;      // array di liste
    private int n;                                // numero totale di elementi

    // ── Costruttore ─────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    public HashTableChiuso(int buckets) {
        this.m     = buckets;
        this.table = new LinkedList[m];
        for (int i = 0; i < m; i++) {
            table[i] = new LinkedList<>();
        }
    }

    // ── Funzione hash (metodo della divisione) ───────────────────────────────
    private int hash(int key) {
        return Math.abs(key) % m;
    }

    // ── Inserimento ──────────────────────────────────────────────────────────
    /**
     * Inserisce la coppia (key, value). Se la chiave è già presente,
     * aggiorna sovrascrivendo il valore (gestione chiavi duplicate).
     */
    public void insert(int key, int value) {
        int idx = hash(key);
        /*
        * ci sono 7 bucket m=7; Per gli esempi di insert impostati:
        * la funzione hash per la prima key=10 restituisce: valore assoluto di 10 mod 7 = 3 (10/7 fa 1 e il resto è 3)
        * a seguire: per 22 -> 1 (22 diviso 7 fa 3 - 7 x 3 = 21 - resta 1 per arrivare a 22)
        * ancora: 31 -> 3 // 4 -> 4 // 15 -> 1 // 28 -> 0 // 17 -> 3 // 88 -> 4 // 5 -> 5
        */
        // controlla se la chiave esiste già
        for (Entry e : table[idx]) {
            if (e.key == key) {
                System.out.println("  [UPDATE] chiave " + key +
                        ": valore " + e.value + " -> " + value);
                e.value = value;
                return;
            }
        }
        // inserimento in testa alla lista del bucket
        // addFirst è un metodo della classe LinkedList di Java che inserisce un elemento in testa alla lista, cioè nella posizione 0, 
        // spostando tutti gli elementi esistenti di una posizione.
        table[idx].addFirst(new Entry(key, value));
        n++;
        System.out.println("  [INSERT] chiave=" + key + " valore=" + value +
                " -> bucket[" + idx + "]");
    }

    // ── Ricerca ──────────────────────────────────────────────────────────────
    /**
     * Restituisce il valore associato a key, oppure null se assente.
     */
    public Integer search(int key) {
        // calcola l'hash per capire in quale posizione del vettore di bucket andare a leggere tra i nodi esistenti (se esistenti) e associati
        int idx = hash(key);
        for (Entry e : table[idx]) {
            if (e.key == key) return e.value;
        }
        return null;
    }

    // ── Cancellazione ────────────────────────────────────────────────────────
    /**
     * Rimuove la chiave dalla tabella. Restituisce true se trovata.
     */
    public boolean delete(int key) {
        int idx = hash(key);
        // iterator() è un metodo di LinkedList (e in generale di tutte le collezioni Java) che restituisce un oggetto di tipo Iterator, 
        // cioè un cursore che permette di scorrere la lista elemento per elemento.
        //var it = table[idx].iterator();
        Iterator<Entry> it = table[idx].iterator();
        // hasNext() restituisce true se esiste ancora un elemento da visitare, false se si e' arrivati in fondo alla lista. 
        // Viene usato come condizione del while per sapere quando fermarsi.
        while (it.hasNext()) {
            // next() avanza il cursore al prossimo elemento e lo restituisce. Ad ogni chiamata si "consuma" un elemento della lista.
            if (it.next().key == key) {
                // remove() rimuove dalla lista l'elemento che e' stato restituito dall'ultima chiamata a next(). 
                // Questo e' il motivo per cui lo usiamo nel delete (quando troviamo il nodo che ha quella chiave).
                it.remove();
                n--;
                return true;
            }
        }
        return false;
    }

    // ── Fattore di carico ────────────────────────────────────────────────────
    public double loadFactor() { return (double) n / m; }

    // ── Stampa intera tabella ────────────────────────────────────────────────
    public void printAll() {
        System.out.println("\n--- HashTableChiuso (m=" + m + ", n=" + n +
                ", fattore di carico a=" + String.format("%.2f", loadFactor()) + ") ---");
        for (int i = 0; i < m; i++) {
            System.out.print("  bucket[" + i + "]: ");
            if (table[i].isEmpty()) {
                System.out.println("vuoto");
            } else {
                // System.out.println(table[i]);
                // commentato perchè generava righe tipo bucket[0]: [(28 ? 280)], usando il toString con un carattere UTF-8 non riconosciuto
                
                // invece stampiamo chiave e valore di ogni Entry separatamente e esplicitamente
                // usiamo StringBuilder, una classe Java che permette di costruire una stringa pezzo per pezzo in modo efficiente, 
                // appendendo caratteri e valori di variabili senza creare oggetti intermedi inutili.
                // Incrementare una semplice stringa sarebbe inefficiente:
                // In Java le stringhe sono immutabili: ogni volta che scrivi risultato + qualcosa, Java non modifica la stringa esistente 
                // ma ne crea una nuova in memoria copiando tutto il contenuto precedente piu' il nuovo pezzo.
                // StringBuilder invece mantiene internamente un buffer modificabile: ogni append aggiunge semplicemente in fondo al buffer senza copiare nulla. 
                // Solo alla fine, con toString(), viene creata la stringa definitiva.
                StringBuilder sb = new StringBuilder("[");
                for (int j = 0; j < table[i].size(); j++) {
                    // get(j) è un metodo di LinkedList che recupera e restituisce l'elemento alla posizione j della lista
                    // ovvero il nodo, da cui possiamo ricavare key e value
                    Entry e = table[i].get(j);
                    sb.append("(key=").append(e.key).append(", val=").append(e.value).append(")");
                    if (j < table[i].size() - 1) sb.append(", ");
                }
                sb.append("]");
                System.out.println(sb.toString());
            }
        }
    }

    // ── Demo ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("------------------------------------------");
        System.out.println("|  TABELLA HASH AD INDIRIZZAMENTO CHIUSO |");
        System.out.println("------------------------------------------\n");

        HashTableChiuso ht = new HashTableChiuso(7);

        // Inserimento delle chiavi (alcune causeranno collisioni)
        System.out.println("--- Inserimento ---");
        int[][] data = {{10,100},{22,200},{31,310},{4,40},{15,150},{28,280},{17,170},{88,880},{5,50}};
        // FOR-EACH scorre l'array bidimensionale data, e per ogni riga richiama INSERT passando chiave e valore
        for (int[] d : data) ht.insert(d[0], d[1]);
        /* 
        * è una scorciatoia/forma più compatta. Avremmo anche potuto scrivere:
        * for (int i = 0; i < data.length; i++) {
        * ht.insert(data[i][0], data[i][1]);}
        * o addirittura
        * ht.insert(10, 100);
        * ht.insert(22, 200);
        * ht.insert(31, 310);
        * // ... 6 altre righe
        */

        ht.printAll();

        // Aggiornamento di una chiave duplicata
        System.out.println("\n--- Aggiornamento chiave duplicata ---");
        ht.insert(22, 999);

        // Ricerca
        System.out.println("\n--- Ricerca ---");
        for (int k : new int[]{10, 31, 99}) {
            Integer v = ht.search(k);
            System.out.println("  search(" + k + ") -> " + (v != null ? v : "NOT FOUND"));
        }

        // Cancellazione
        System.out.println("\n--- Cancellazione ---");
        System.out.println("  delete(22): " + ht.delete(22));
        System.out.println("  delete(99): " + ht.delete(99));

        ht.printAll();
    }
}
