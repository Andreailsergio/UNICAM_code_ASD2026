/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 * 
 * Coda (Queue) - implementazione basata su array circolare.
 *
 * Fedele allo pseudocodice del Cormen, cap. 10.1:
 *   ENQUEUE(Q, x)
 *   DEQUEUE(Q)
 *
 * La coda è realizzata con un array Q[0..n-1] (0-based in Java invece
 * di 1-based come nel testo). L'array è gestito in modo CIRCOLARE:
 * quando head o tail raggiungono la fine dell'array, ripartono da 0
 * tramite l'operazione modulo.
 *
 * Convenzione (adatta al rilevamento di overflow/underflow):
 *   - size == 0       → coda vuota  (underflow su dequeue)
 *   - size == n       → coda piena  (overflow su enqueue)
 *   - head            → indice del prossimo elemento da estrarre (CIMA = TESTA)
 *   - tail            → indice della prossima posizione libera (FONDO = CODA)
 *
 * Cormen (versione 1-based, array Q[1..n]):
 *   ENQUEUE(Q, x):
 *     Q[tail[Q]] = x
 *     if tail[Q] == length[Q] then tail[Q] = 1
 *     else tail[Q] = tail[Q] + 1
 *
 *   DEQUEUE(Q):
 *     x = Q[head[Q]]
 *     if head[Q] == length[Q] then head[Q] = 1
 *     else head[Q] = head[Q] + 1
 *     return x
 *
 * Complessità di entrambe le operazioni: Θ(1)
 * 
 * Rilevamento overflow/underflow. 
 * Il Cormen originale usa la condizione head == tail per la coda vuota, ma questo crea ambiguità con la coda piena. 
 * La soluzione adottata qui — un contatore size separato — è la più pulita e corrisponde all'approccio suggerito nell'esercizio 10.1-4 
 * per gestire entrambi gli errori in modo esplicito.
 * 
 */
public class Coda {
 
    // ---------------------------------------------------------------
    // Eccezioni personalizzate
    // ---------------------------------------------------------------
 
    /** Lanciata da dequeue() quando la coda è vuota. */
    public static class QueueUnderflowException extends RuntimeException {
        public QueueUnderflowException() {
            super("underflow: la coda e' vuota");
        }
    }
 
    /** Lanciata da enqueue() quando la coda ha raggiunto la capacità massima. */
    public static class QueueOverflowException extends RuntimeException {
        public QueueOverflowException() {
            super("overflow: la coda e' piena");
        }
    }
 
    // ---------------------------------------------------------------
    // Campi interni
    // ---------------------------------------------------------------
 
    private final int[] Q;   // array che memorizza gli elementi
    private int head;        // indice del primo elemento (testa)
    private int tail;        // indice della prossima posizione libera (coda)
    private int size;        // numero di elementi presenti
 
    // ---------------------------------------------------------------
    // Costruttore
    // ---------------------------------------------------------------
 
    /**
     * Crea una coda di capacità massima n.
     * Corrisponde all'array Q[1..n] del Cormen (qui 0-based).
     *
     * @param n capacità massima della coda
     */
    public Coda(int n) {
        Q    = new int[n];
        head = 0;
        tail = 0;
        size = 0;
    }
 
    // ---------------------------------------------------------------
    // Operazioni di interrogazione
    // ---------------------------------------------------------------
 
    /**
     * Restituisce true se la coda non contiene alcun elemento.
     * Corrisponde alla condizione head[Q] == tail[Q] del Cormen
     * (interpretata con il contatore size per semplicità).
     *
     * Complessità: Θ(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }
 
    /**
     * Restituisce true se la coda ha raggiunto la capacità massima.
     *
     * Complessità: Θ(1)
     */
    public boolean isFull() {
        return size == Q.length;
    }
 
    // ---------------------------------------------------------------
    // Operazioni principali (dal Cormen)
    // ---------------------------------------------------------------
 
    /**
     * ENQUEUE(Q, x)
     * Inserisce x in fondo alla coda.
     * Lancia QueueOverflowException se la coda è piena.
     *
     * Cormen (con controllo overflow aggiunto, esercizio 10.1-4):
     *   if head[Q] == tail[Q] + 1   (coda piena)
     *       error "overflow"
     *   Q[tail[Q]] = x
     *   if tail[Q] == length[Q]
     *       then tail[Q] = 1
     *       else tail[Q] = tail[Q] + 1
     *
     * Complessità: Θ(1)
     */
    public void enqueue(int x) {
        if (isFull()) {
            throw new QueueOverflowException();
        }
        Q[tail] = x;
        // avanzamento circolare di tail
        if (tail == Q.length - 1) {
            tail = 0;
        } else {
            tail = tail + 1;
        }
        size++;
    }
 
    /**
     * DEQUEUE(Q)
     * Rimuove e restituisce l'elemento in testa alla coda.
     * Lancia QueueUnderflowException se la coda è vuota.
     *
     * Cormen (con controllo underflow aggiunto, esercizio 10.1-4):
     *   if head[Q] == tail[Q]   (coda vuota)
     *       error "underflow"
     *   x = Q[head[Q]]
     *   if head[Q] == length[Q]
     *       then head[Q] = 1
     *       else head[Q] = head[Q] + 1
     *   return x
     *
     * Complessità: Θ(1)
     */
    public int dequeue() {
        if (isEmpty()) {
            throw new QueueUnderflowException();
        }
        int x = Q[head];
        // avanzamento circolare di head
        if (head == Q.length - 1) {
            head = 0;
        } else {
            head = head + 1;
        }
        size--;
        return x;
    }
 
    // ---------------------------------------------------------------
    // Metodi di utilità
    // ---------------------------------------------------------------
 
    /** Restituisce l'elemento in testa senza rimuoverlo. */
    public int first() {
        if (isEmpty()) {
            throw new QueueUnderflowException();
        }
        return Q[head];
    }
 
    /** Numero di elementi attualmente nella coda. */
    public int size() {
        return size;
    }
 
    /**
     * Rappresentazione testuale della coda (dalla testa alla coda).
     * La freccia indica l'ordine di uscita: testa → ... → fondo.
     */
    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("testa -> [");
        for (int i = 0; i < size; i++) {
            // usare il MOD modulo (head + i) % Q.length permette la circolarità: 
            // quando head e tail raggiungono la fine dell'array ripartono da 0.
            // È esattamente la stessa logica usata in enqueue (righe 143-146) e dequeue (righe 173-176) per far avanzare tail e head.
            sb.append(Q[(head + i) % Q.length]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
 
    // ---------------------------------------------------------------
    // Main di test
    // ---------------------------------------------------------------
 
    public static void main(String[] args) {
 
        System.out.println("=== Test Coda (Cormen cap. 10.1) ===\n");
 
        // Sequenza dell'esercizio 10.1-3 del Cormen:
        //   ENQUEUE(Q,4), ENQUEUE(Q,1), ENQUEUE(Q,3),
        //   DEQUEUE(Q), ENQUEUE(Q,8), DEQUEUE(Q)
        // su una coda Q[1..6]
        Coda coda = new Coda(6);
        System.out.println("STO LAVORANDO CON LA CODA coda:\n");
        System.out.println("isEmpty()  - iniziamo: la Coda e' vuota?   -> " + coda.isEmpty()); // true
 
        coda.enqueue(4);
        System.out.println("enqueue(4)    -> " + coda);
        // sempre per l'ovveride della funzione toString, ad ogni comando per default stampo anche gli elementi nella struttura
 
        coda.enqueue(1);
        System.out.println("enqueue(1)    -> " + coda);
 
        coda.enqueue(3);
        System.out.println("enqueue(3)    -> " + coda);
 
        int val = coda.dequeue();
        System.out.println("dequeue()     -> restituisce " + val + " da eliminare, coda: " + coda);
 
        coda.enqueue(8);
        System.out.println("enqueue(8)    -> " + coda);
 
        val = coda.dequeue();
        System.out.println("dequeue()     -> restituisce " + val + " da eliminare, coda: " + coda);
 
        System.out.println("\nQuanti elementi contiene la struttura dati? size()        -> " + coda.size());   // 2
        System.out.println("Qual e' l'elemento in testa? first()       -> " + coda.first());   // 3
        System.out.println("isEmpty()  - ora la Coda e' vuota?   -> " + coda.isEmpty()); // false
 
        // --- Test avanzamento circolare ---
        /*
         * Il test verifica che l'array funzioni correttamente come buffer circolare, cioè che head e tail ripartano da 0 quando arrivano in fondo.
         * Ecco cosa succede passo per passo, partendo dalla coda da 6 posti dopo la sequenza principale del test:
         * Stato iniziale del test di circolarità: la coda contiene [3, 8] con head=2, tail=4, size=2.
         * indici:  0   1   2   3   4   5
         * array: [ 4 | 1 | 3 | 8 | _ | _ ]
         *                  ↑       ↑
         *                head=2   tail=4
         * I primi due slot (0 e 1) sono stati occupati da 4 e 1, già estratti. Sono "sporchi", ma non importa: head=2 fa sì che vengano ignorati.
         * Due dequeue() estraggono 3 e 8, portando head=4, tail=4, size=0 — coda vuota.
         * indici:  0   1   2   3   4   5
         * array: [ 4 | 1 | 3 | 8 | _ | _ ]
         *                          ↑
         *                      head=tail=4
         * enqueue(10), enqueue(20), enqueue(30) inseriscono a partire dalla posizione 4:
         * indici:  0   1   2   3    4    5
         * array: [ 4 | 1 | 3 | 8 | 10 | 20 ]
         *                           ↑
         *                         head=4
         * dopo enqueue(30), tail va a 5+1 → torna a 0 (wrap-around!)
         * indici:  0   1   2   3    4    5
         * array: [ 30| 1 | 3 | 8 | 10 | 20 ]
         *           ↑                    ↑
         *         tail=0              head=4
         * Qui avviene la circolarità: tail era a 5 (ultimo slot), incrementa e ricomincia da 0, sovrascrivendo il vecchio valore 4 che non serve più.
         * L'ultimo dequeue() estrae Q[head] = Q[4] = 10, e head avanza a 5. 
         * La coda ora contiene [20, 30] che fisicamente occupa le posizioni 5 e 0 dell'array — cioè "a cavallo" della fine e dell'inizio. 
         * Il toString() li stampa comunque nell'ordine corretto grazie al calcolo (head + i) % Q.length.
         * L'operazione (head + i) % Q.length, infatti, trasforma la sequenza lineare i++ in una sequenza circolare 
         * (evitando di andare fuori dimensione quando si supera l'ultimo indice):
         * i=0 → (4 + 0) % 6 = 4   → Q[4] = 10
         * i=1 → (4 + 1) % 6 = 5   → Q[5] = 20
         * i=2 → (4 + 2) % 6 = 0   → Q[0] = 30  ← ricomincia da capo
         * Senza la gestione circolare, dopo poche operazioni si esaurirebbe lo spazio all'estremità destra dell'array 
         * anche se i primi slot sono stati liberati dai dequeue().
        */
        System.out.println("\n--- Test circolarita' ---");
        System.out.println(" SVUOTO E RIEMPIO LA CODA coda:\n");
        // Svuota la coda e riempi di nuovo per verificare il wrap-around di head e tail sull'array
        coda.dequeue();
        coda.dequeue();
        System.out.println("dopo due dequeue: " + coda + " (vuota)");
        coda.enqueue(10);
        coda.enqueue(20);
        coda.enqueue(30);
        System.out.println("faccio tre nuovi enqueue 10,20,30: " + coda);
        System.out.println("dequeue()     -> " + coda.dequeue() + ", coda: " + coda);
        System.out.println(" tail era a 5 (ultimo slot) e l'incremento lo porta a 0 (primo slot):\n");
 
        // --- Test underflow ---
        System.out.println("\n--- Test underflow ---");
        Coda codaVuota = new Coda(3);
        System.out.println("STO LAVORANDO CON LA CODA codaVuota:\n");
        try {
            codaVuota.dequeue();
        } catch (QueueUnderflowException e) {
            System.out.println("Eccezione catturata: " + e.getMessage());
        }
 
        // --- Test overflow ---
        System.out.println("\n--- Test overflow ---");
        Coda codaPiccola = new Coda(2);
        System.out.println("STO LAVORANDO CON LA CODA codaPiccola:\n");
        codaPiccola.enqueue(100);
        codaPiccola.enqueue(200);
        try {
            codaPiccola.enqueue(300); // capacità massima 2
        } catch (QueueOverflowException e) {
            System.out.println("Eccezione catturata: " + e.getMessage());
        }
    }
}
