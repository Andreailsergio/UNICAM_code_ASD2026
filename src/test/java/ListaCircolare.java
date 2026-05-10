/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 */
/**
 * Lista circolare doppiamente concatenata con sentinella.
 *
 * Fedele allo pseudocodice del Cormen, cap. 10.2 (figura 11.4):
 *   LIST-SEARCH(L, k)
 *   LIST-INSERT(L, x)
 *   LIST-DELETE(L, x)
 *
 * Struttura:
 *   - Ogni nodo ha tre campi: key, next, prev.
 *   - La sentinella nil[L] è un nodo fittizio sempre presente:
 *       nil.next  → primo elemento reale (o nil stessa se lista vuota)
 *       nil.prev  → ultimo elemento reale (o nil stessa se lista vuota)
 *   - La lista è CIRCOLARE: l'ultimo nodo punta a nil con next,
 *     e nil punta all'ultimo nodo con prev.
 *   - Grazie alla sentinella, insert e delete non hanno mai
 *     bisogno di controllare i casi limite (testa/coda vuota)
 *     ed hanno le stesse righe di codice perché indipendenti
 *     da dove si sta operando: in testa, nella coda o in mezzo.
 *
 * Complessità:
 *   LIST-INSERT      → Θ(1)  (effettuando l'inserimento in testa)
 *   LIST-DELETE      → Θ(1)  (ricevendo direttamente il puntatore al nodo (Nodo x))
 *                              ... anche se in realtà, per ottenere il puntatore, devo prima fare un search con la chiave data in input ...
 *   LIST-DELETEBYKEY → Θ(n)  (dovendo prima richiamare listSearch per trovare il nodo data la chiave (int k)
 *   LIST-SEARCH      → Θ(n)   nel caso peggiore
 */
public class ListaCircolare {
 
    // ---------------------------------------------------------------
    // Classe interna: nodo della lista
    // ogni nodo è strutturato per contenere: 
    // - una chiave key (il valore memorizzato)
    // - un puntatore al nodo successivo
    // - un puntatore al nodo precedente
    // ---------------------------------------------------------------
 
    static class Nodo {
        int   key;
        Nodo  next;
        Nodo  prev;
 
        Nodo(int key) {
            this.key  = key;
            this.next = null;
            this.prev = null;
        }
 
        /** Costruttore per la sentinella (chiave convenzionale 0). */
        Nodo() {
            this.key  = 0;
            this.next = null;
            this.prev = null;
        }
    }
 
    // ---------------------------------------------------------------
    // Campo: la sentinella nil[L]
    // ---------------------------------------------------------------
 
    /**
     * Nodo sentinella nil[L].
     *
     * Cormen (figura 11.4):
     *   "nil[L] è disposto tra la testa e la coda.
     *    next[nil[L]] punta alla testa,
     *    prev[nil[L]] punta alla coda."
     *
     * Lista vuota: nil.next == nil  e  nil.prev == nil
     */
    private final Nodo nil;
 
    // ---------------------------------------------------------------
    // Costruttore
    // ---------------------------------------------------------------
 
    /**
     * Crea una lista vuota inizializzando la sentinella che punta
     * a se stessa in entrambe le direzioni.
     */
    public ListaCircolare() {
        nil      = new Nodo();   // sentinella
        nil.next = nil;          // lista vuota: nil punta a se stessa
        nil.prev = nil;
    }
 
    // ---------------------------------------------------------------
    // Operazioni principali (dal Cormen)
    // ---------------------------------------------------------------
 
    /**
     * LIST-SEARCH(L, k)
     * Cerca il primo nodo con chiave k, restituisce il nodo oppure null.
     *
     * Cormen:
     *   x = next[nil[L]]
     *   while x ≠ nil[L] and key[x] ≠ k
     *       do x = next[x]
     *   return x
     *
     * Complessità: Θ(n) nel caso peggiore.
     */
    public Nodo listSearch(int k) {
        Nodo x = nil.next;                     // primo elemento reale (dopo la sentinella)
        while (x != nil && x.key != k) {
            x = x.next;
        }
        return (x == nil) ? null : x;          // null se non trovato: 
                                               // operatore ternario di Java, una forma compatta di un if-else che restituisce un valore.
                                               // condizione ? valore_se_vera : valore_se_falsa
                                               // se x è la sentinella, restituisci null, altrimenti restituisci x
                                               // esattamente equivalente a:
                                               /* 
                                                * if (x == nil) {
                                                *     return null;
                                                * } else {
                                                *     return x;
                                                * }
                                               */
    }
 
    /**
     * LIST-INSERT(L, x)
     * Inserisce un nuovo nodo con chiave k in testa alla lista.
     * Restituisce il nodo inserito.
     *
     * Cormen:
     *   next[x]        = next[nil[L]]
     *   prev[next[x]]  = x
     *   next[nil[L]]   = x
     *   prev[x]        = nil[L]
     *
     * Grazie alla sentinella il codice funziona identicamente
     * sia che la lista sia vuota sia che contenga già elementi:
     * non servono controlli speciali.
     *
     * Complessità: Θ(1)
     */
    public Nodo listInsert(int k) {
        Nodo x    = new Nodo(k);
        x.next        = nil.next;   // il nuovo nodo x punterà al vecchio primo elemento, cioè quello che era puntato dal next della sentinella (nil)
                                    // (INSERIMENTO IN TESTA) -> per cui la lista verosimilmente non sarà ordinata
        x.next.prev   = x;          // il vecchio primo (puntato da x.next) punta indietro a x
        nil.next      = x;          // la sentinella punta al nuovo primo
        x.prev        = nil;        // x punta indietro alla sentinella
        return x;
    }
 
    /**
     * LIST-DELETE(L, x)
     * Rimuove il nodo x dalla lista aggiornando i puntatori.
     *
     * Cormen:
     *   next[prev[x]] = next[x]
     *   prev[next[x]] = prev[x]
     *
     * Anche qui la sentinella elimina qualunque caso limite:
     * se x è il primo elemento, prev[x] == nil[L] e la prima riga aggiorna next[nil[L]] (cioè head);
     * se x è l'ultimo, next[x] == nil[L] e la seconda riga aggiorna prev[nil[L]].
     *
     * Complessità: Θ(1)
     */
    public void listDelete(Nodo x) {
        x.prev.next = x.next;   // il predecessore salta x (e punta al successivo nodo dopo x)
        x.next.prev = x.prev;   // il successore salta x (e punta al nodo precedente di x)
        // x è ora isolato - diventa irraggiungibile; il GC (Garbage Collector) di Java lo raccoglierà nel prossimo ciclo di raccolta
        // -> non c'è bisogno di fare il free della memoria, 
        // ci penserà il GC a farlo scomparire
        // queste due prossime righe di codice (che fanno puntare a null entrambi i puntatori del nodo eliminato) 
        // non sarebbero strettente necessarie ma è una buona pratica
        // azzerando i puntatori interni di x si evita che il nodo eliminato mantenga riferimenti ad altri nodi ancora vivi nella lista
        // il che potrebbe in certi scenari rallentare il GC o causare comportamenti inattesi 
        // se qualcuno conservasse per errore un riferimento a x dopo la cancellazione.
        x.next = null;
        x.prev = null;
    }
 
    /**
     * Versione di listDelete che riceve la chiave invece del puntatore.
     * Richiede prima una listSearch → costo Θ(n).
     * Lancia IllegalArgumentException se la chiave non è presente.
     */
    public void listDeleteByKey(int k) {
        Nodo x = listSearch(k);
        if (x == null) {
            throw new IllegalArgumentException(
                "chiave " + k + " non trovata nella lista");
        }
        listDelete(x);
    }
 
    // ---------------------------------------------------------------
    // Metodi di utilità
    // ---------------------------------------------------------------
 
    /** Restituisce true se la lista non contiene elementi reali. */
    public boolean isEmpty() {
        return nil.next == nil;  // c'è solo la sentinella che come successore punta al nulla
    }
 
    /** Numero di elementi (scansione lineare incrementando un contatore di 1). */
    public int size() {
        int count = 0;
        Nodo x = nil.next;
        while (x != nil) { count++; x = x.next; }
        return count;
    }
 
    /**
     * Stampa la lista dalla testa alla coda, costruendo una stringa che rappresenta visivamente la struttura con le frecce bidirezionali.
     * 
     */
    @Override
    public String toString() {
        // se la lista è vuota
        if (isEmpty()) return "nil <-> [vuota] <-> nil";
 
        // altrimenti percorso in avanti
        // Si usa uno StringBuilder invece di concatenare stringhe con + 
        // perché ogni + su una String crea un nuovo oggetto in memoria — con n elementi si creerebbero n oggetti temporanei. 
        // StringBuilder accumula tutto in un unico buffer e crea la stringa finale solo alla fine con .toString().
        StringBuilder avanti = new StringBuilder("nil <-> ");
        // si parte dal primo elemento reale (dopo la sentinella)
        Nodo x = nil.next;
        // finché non si ritorna alla sentinella
        while (x != nil) {
            // appende la chiave al nodo corrente
            avanti.append(x.key);
            // serve per non mettere la freccia <-> dopo l'ultimo elemento reale (che viene invece messa fuori dal ciclo)
            if (x.next != nil) avanti.append(" <-> ");
            // avanza nel ciclo
            x = x.next;
        }
        avanti.append(" <-> nil");
        return avanti.toString();
    }
 
    // ---------------------------------------------------------------
    // Main di test
    // ---------------------------------------------------------------
    
    public static void main(String[] args) {
 
        System.out.println("=== Test ListaCircolare con sentinella (Cormen Par. 10.2) ===\n");
 
        ListaCircolare L = new ListaCircolare();
 
        // --- INSERT ---
        // Il Cormen inserisce sempre in testa, quindi l'ordine
        // di stampa è l'inverso dell'ordine di inserimento.
        System.out.println("-- listInsert --");
        L.listInsert(1);
        System.out.println("insert(1)  -> " + L);
 
        L.listInsert(4);
        System.out.println("insert(4)  -> " + L);
 
        L.listInsert(16);
        System.out.println("insert(16) -> " + L);
 
        L.listInsert(9);
        System.out.println("insert(9)  -> " + L);
        // lista attesa: nil ↔ 9 ↔ 16 ↔ 4 ↔ 1 ↔ nil
        // (figura 11.3 del Cormen: {1, 4, 9, 16})
 
        System.out.println("\nsize() -> " + L.size());   // 4
 
        // --- SEARCH ---
        System.out.println("\n-- listSearch --");
        Nodo trovato = L.listSearch(4);
        System.out.println("search(4)  -> " +
            (trovato != null ? "trovato, key=" + trovato.key : "non trovato"));
 
        Nodo nonTrovato = L.listSearch(7);
        System.out.println("search(7)  → " +
            (nonTrovato != null ? "trovato" : "non trovato"));
 
        // --- DELETE con puntatore diretto ---
        System.out.println("\n-- listDelete (con puntatore) --");
        // Eliminiamo il nodo con chiave 4 usando il puntatore restituito
        // dalla search (costo Θ(1) per la delete stessa)
        Nodo da_cancellare = L.listSearch(4);
        L.listDelete(da_cancellare);
        System.out.println("delete(4)  -> " + L);
 
        // --- DELETE per chiave ---
        System.out.println("\n-- listDeleteByKey --");
        L.listDeleteByKey(9);
        System.out.println("delete(9)  -> " + L);
 
        L.listDeleteByKey(1);
        System.out.println("delete(1)  -> " + L);
 
        L.listDeleteByKey(16);
        System.out.println("delete(16) -> " + L);
 
        System.out.println("\nisEmpty() -> " + L.isEmpty()); // true
 
        // --- Errore: cancellazione chiave inesistente ---
        System.out.println("\n-- Test chiave inesistente --");
        try {
            L.listDeleteByKey(99);
        } catch (IllegalArgumentException e) {
            System.out.println("Eccezione: " + e.getMessage());
        }
 
        // --- Verifica circolarità dei puntatori ---

        /* 
        *  stampa la lista sia in avanti seguendo next, sia al contrario sefuendo prev a partire da nil.prev
        *  i test è verificato quando gli output restituiti sono l'uno il rovescio dell'altro
        *  confermando la coerenza di tutti i puntatori bidirezionali
        */
        System.out.println("\n-- Verifica puntatori circolari --");
        L.listInsert(10);
        L.listInsert(20);
        L.listInsert(30);
        System.out.println("lista: " + L);
 
        // percorso inverso (da nil.prev risalendo con prev)
        // Verifica la correttezza dei puntatori prev percorrendo al contrario 
        // con lo stesso meccanismo dello StringBuilder usato per la stampa in avanti nel toString().
        StringBuilder indietro = new StringBuilder("nil <-> ");
        // si parte dal nodo puntato dal precedente della sentinella
        Nodo y = L.nil.prev;
        // finché non si ripunta alla sentinella
        while (y != L.nil) {
            indietro.append(y.key);
            if (y.prev != L.nil) indietro.append(" <-> ");
            y = y.prev;
        }
        indietro.append(" <-> nil");
        System.out.println("al rovescio: " + indietro);
        // deve essere l'ordine inverso rispetto alla stampa normale
    }
}