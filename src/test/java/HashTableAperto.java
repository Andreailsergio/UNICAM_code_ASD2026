/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 * Tabella Hash ad INDIRIZZAMENTO APERTO
 *
 * HashTableAperto.java — Indirizzamento aperto
 * Implementa tutti e tre i metodi di scansione tramite un enum Probing:
 * enum (abbreviazione di enumeration) è un tipo speciale di classe Java che definisce un insieme fisso di costanti con nome.
 * Ci serve per creare un tipo personalizzato i cui unici valori validi sono LINEAR, QUADRATIC e DOUBLE.
 * MetodoFormula:
 * LINEAR       (h'(k) + i) mod m
 * QUADRATIC    (h'(k) + i + i²) mod m
 * DOUBLE       (h1(k) + i·h2(k)) mod m
 * 
 * Tombstone (DELETED): la cancellazione marca la cella invece di svuotarla, così la sequenza di ricerca Probe non si spezza
 * I tombstone ("lapidi") sono un valore sentinella speciale usato per marcare le celle cancellate in una tabella hash ad indirizzamento aperto.
 * un tombstone dice DELETED -> "qui c'era qualcosa, ma è stato cancellato, la catena non si interrompe, continua a cercare". 
 * Una cella EMPTY invece dice "qui non c'e' mai stato nulla, non è mai stato occupato, la catena finisce qui". 
 * Il search, se incontra il VUOTO, restituirebbe NOT FOUND e si arresterebbe. Col Tombstone invece prosegue nel ciclo.
 * Il tombstone viene riusato nell'insert per non sprecare slot: la prima cella DELETED incontrata viene salvata e riutilizzata per il nuovo elemento
 * Il demo riproduce esattamente l'esempio della figura 12.5 del Cormen (hashing doppio, m=13, chiave 14)
 * 
 * Tutti gli elementi risiedono nell'array stesso. Le collisioni vengono
 * risolte calcolando una sequenza di scansione (probing sequence) diversa
 * a seconda del metodo scelto.
 *
 * Tre metodi implementati:
 *   LINEAR   → h(k,i) = (h'(k) + i)         mod m
 *   QUADRATIC→ h(k,i) = (h'(k) + c1·i+c2·i²) mod m   (c1=1, c2=1, m primo)
 *   DOUBLE   → h(k,i) = (h1(k) + i·h2(k))   mod m    (m primo)
 *
 * Operazioni principali:
 *   insert(key)  → O(1/(1-α)) in media
 *   search(key)  → O(1/(1-α)) in media
 *   delete(key)  → marcatura con DELETED (tombstone)
 */
public class HashTableAperto {

    // ── Metodi di scansione ──────────────────────────────────────────────────
    public enum Probing { LINEAR, QUADRATIC, DOUBLE }

    // ── Sentinella per celle cancellate (tombstone) ──────────────────────────
    private static final int DELETED  = Integer.MIN_VALUE;
    private static final int EMPTY    = Integer.MAX_VALUE;   // valore sentinella "vuoto"
    /* 
    La scelta di Integer.MIN_VALUE e Integer.MAX_VALUE come sentinelle e' una scelta pratica e convenzionale, non obbligatoria. 
    La Tabella Hash - definita come int[] può contenere solo numeri interi (non null o altri valori speciali).
    Usando il minimo e il massimo assoluto nell'universo degli interi cerco di assicurarmi di non confondere sentinelle e chiavi reali
    disponendo di due valori i più lontani possibili da quelli che useremmo normalmente come chiavi.
    
    */

    // ── Campi ────────────────────────────────────────────────────────────────
    private final int       m;
    private final int[]     table;
    private int             n;            // elementi attivi
    private final Probing   method;

    // ── Costruttore ──────────────────────────────────────────────────────────
    public HashTableAperto(int size, Probing method) {
        this.m      = size;
        this.method = method;
        this.table  = new int[m];
        java.util.Arrays.fill(table, EMPTY);
    }

    // ── Funzioni hash ausiliarie ─────────────────────────────────────────────
    private int h1(int key) { return Math.abs(key) % m; }

    /** h2 deve essere primo rispetto a m → per m primo: h2 = 1 + (key mod (m-1)) */
    private int h2(int key) { return 1 + (Math.abs(key) % (m - 1)); }

    // ── Sequenza di scansione (probe) ────────────────────────────────────────
    private int probe(int key, int i) {
        switch (method) {
            case LINEAR:
                // h(k,i) = (h'(k) + i) mod m
                return (h1(key) + i) % m;

            case QUADRATIC:
                // h(k,i) = (h'(k) + i + i²) mod m   [c1=1, c2=1]
                return (h1(key) + i + i * i) % m;

            case DOUBLE:
                // h(k,i) = (h1(k) + i·h2(k)) mod m
                return (h1(key) + i * h2(key)) % m;

            default: throw new IllegalStateException();
        }
    }

    // ── Inserimento ──────────────────────────────────────────────────────────
    /**
     * Inserisce la chiave. Ritorna l'indice in cui è stata posta, oppure -1
     * se la tabella è piena.
     */
    public int insert(int key) {
        int firstDeleted = -1;
        for (int i = 0; i < m; i++) {
            // la funzione probe calcola l'hash h1
            // dopodiché il metodo LINEAR avanzerà di 1 per cercare i prossimi slot
            // nel metodo QUADRATIC il passo cresce in modo quadratico
            // con DOUBLE il passo dipende da h2
            int j = probe(key, i);
            if (table[j] == EMPTY) {
                // cella libera: inserisci qui (o nel primo tombstone trovato)
                // "se firstDeleted e' diverso da -1, assegna firstDeleted a pos: trovato Tombstone inserisco lì (indipendentemente se abbia trovato EMPTY);
                // altrimenti assegna j a pos: non ho trovato Tombstone e inserisco nello slot EMPTY trovato grazie al passo."
                int pos = (firstDeleted != -1) ? firstDeleted : j;
                table[pos] = key;
                n++;
                // %3d stampa un intero (decimal) in un campo da 3 caratteri es. " 10" (serve per il valore della chiave)
                // %2d un intero con 2 caratteri es. " 5" (serve per lo slot/posizione in cui è stata inserita la chiave)
                // %d un numero esatto intero, senza larghezza fissa (serve per il numero di collisioni verificatesi prima di trovare uno slot disponibile)
                // %s una stringa (che serve per l'enum Probing, cioè il metodo che si sta usando)
                System.out.printf("  [INSERT] key=%3d  -> slot[%2d]  (probe #%d, metodo=%s)%n",
                        key, pos, i, method);
                return pos;
            } else if (table[j] == DELETED && firstDeleted == -1) {
                firstDeleted = j;            // salva primo tombstone
            } else if (table[j] == key) {
                System.out.println("  [SKIP]   key=" + key + " già presente");
                return j;
            }
        }
        // tutti gli slot sono occupati o tombstone
        if (firstDeleted != -1) {
            table[firstDeleted] = key;
            n++;
            System.out.printf("  [INSERT] key=%3d  -> slot[%2d]  (tombstone, metodo=%s)%n",
                    key, firstDeleted, method);
            return firstDeleted;
        }
        System.out.println("  [ERROR]  Tabella piena, impossibile inserire key=" + key);
        return -1;
    }

    // ── Ricerca ──────────────────────────────────────────────────────────────
    /**
     * Cerca la chiave. Restituisce l'indice se trovata, -1 altrimenti.
     */
    public int search(int key) {
        for (int i = 0; i < m; i++) {
            int j = probe(key, i);
            if (table[j] == EMPTY) return -1;             // celle vuote = chiave assente
            if (table[j] == key)   return j;
            // se DELETED → continua la scansione
        }
        return -1;
    }

    // ── Cancellazione ────────────────────────────────────────────────────────
    /**
     * Cancella la chiave marcando la cella con DELETED (tombstone).
     * Restituisce true se trovata.
     */
    public boolean delete(int key) {
        for (int i = 0; i < m; i++) {
            int j = probe(key, i);
            if (table[j] == EMPTY)  return false;
            if (table[j] == key) {
                table[j] = DELETED;
                n--;
                return true;
            }
        }
        return false;
    }

    // ── Fattore di carico ────────────────────────────────────────────────────
    public double loadFactor() { return (double) n / m; }

    // ── Stampa ───────────────────────────────────────────────────────────────
    public void printAll() {
        System.out.printf("%n--- HashTableAperto [%s] (m=%d, n=%d, load factor a=%.2f) ---%n",
                method, m, n, loadFactor());
        for (int i = 0; i < m; i++) {
            String cell;
            if      (table[i] == EMPTY)   cell = "---";
            else if (table[i] == DELETED) cell = "DEL";
            else                          cell = String.valueOf(table[i]);
            System.out.printf("  slot[%2d]: %s%n", i, cell);
        }
    }

    // ── Demo ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        // Chiavi dal Cormen es. 12.4-1 (m=11, h(k)=k mod 11)
        int[] keys = {10, 22, 31, 4, 15, 28, 17, 88, 59};

        // ── 1. ISPEZIONE LINEARE ──────────────────────────────────────────────
        System.out.println("------------------------------------------");
        System.out.println("|  TABELLA HASH AD INDIRIZZAMENTO APERTO |");
        System.out.println("------------------------------------------");

        System.out.println("\n-> ISPEZIONE LINEARE (m=11)");
        HashTableAperto linear = new HashTableAperto(11, Probing.LINEAR);
        for (int k : keys) linear.insert(k);
        linear.printAll();

        System.out.println("\n--- Ricerca ---");
        for (int k : new int[]{31, 99}) {
            int idx = linear.search(k);
            System.out.println("  search(" + k + ") -> " +
                    (idx >= 0 ? "trovato in slot[" + idx + "]" : "NOT FOUND"));
        }

        System.out.println("\n--- Cancellazione di 22 ---");
        System.out.println("  delete(22): " + linear.delete(22));
        System.out.println("  search(22) dopo delete: " +
                (linear.search(22) >= 0 ? "trovato" : "NOT FOUND"));
        linear.printAll();

        // ── 2. ISPEZIONE QUADRATICA ───────────────────────────────────────────
        System.out.println("\n\n-> ISPEZIONE QUADRATICA (m=11, c1=1, c2=1)");
        HashTableAperto quadratic = new HashTableAperto(11, Probing.QUADRATIC);
        for (int k : keys) quadratic.insert(k);
        quadratic.printAll();

        // ── 3. HASHING DOPPIO ─────────────────────────────────────────────────
        // Cormen figura 12.5: m=13, h1(k)=k mod 13, h2(k)=1+(k mod 11)
        // Usiamo m=13 (primo) e le stesse chiavi
        System.out.println("\n\n-> HASHING DOPPIO (m=13)");
        System.out.println("   h1(k)=k mod 13   h2(k)=1+(k mod 12)");
        HashTableAperto dbl = new HashTableAperto(13, Probing.DOUBLE);
        for (int k : keys) dbl.insert(k);
        // Inserimento extra della chiave 14 (esempio Cormen figura 12.5)
        System.out.println("\n  --- inserimento chiave 14 (esempio Cormen fig.12.5) ---");
        dbl.insert(14);
        dbl.printAll();

        // ── Confronto fattori di carico ───────────────────────────────────────
        System.out.println("\n\n-> CONFRONTO FATTORI DI CARICO");
        System.out.printf("  Linear    load factor alfa = %.2f%n", linear.loadFactor());
        System.out.printf("  Quadratic load factor alfa = %.2f%n", quadratic.loadFactor());
        System.out.printf("  Double    load factor alfa = %.2f%n", dbl.loadFactor());
    }
}
