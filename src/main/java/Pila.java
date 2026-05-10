/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 * Pila (Stack) - implementazione basata su array.
 *
 * Fedele allo pseudocodice del Cormen, cap. 10.1:
 *   STACK-EMPTY(S)
 *   PUSH(S, x)
 *   POP(S)
 *
 * La pila è realizzata con un array S[0..n-1] (indici 0-based in Java
 * invece di 1-based come nel testo). L'attributo top tiene traccia
 * dell'indice dell'elemento inserito più di recente.
 * - top == -1  →  pila vuota
 * - top == n-1 →  pila piena
 *
 * Gestione degli errori:
 *   - POP su pila vuota  → StackUnderflowException
 *   - PUSH su pila piena → StackOverflowException
 */

public class Pila {
 
    // ---------------------------------------------------------------
    // Eccezioni personalizzate (corrispondono agli "error" del Cormen)
    // ---------------------------------------------------------------
 
    /** Lanciata da pop() quando la pila è vuota. */
    public static class StackUnderflowException extends RuntimeException {
        public StackUnderflowException() {
            super("underflow: la pila e' vuota");
        }
    }
 
    /** Lanciata da push() quando la pila ha raggiunto la capacità massima. */
    public static class StackOverflowException extends RuntimeException {
        public StackOverflowException() {
            super("overflow: la pila e' piena");
        }
    }
 
    // ---------------------------------------------------------------
    // Campi interni
    // ---------------------------------------------------------------
 
    private final int[] S;   // array che memorizza gli elementi
    private int top;         // indice dell'elemento in cima (-1 = vuota)
 
    // ---------------------------------------------------------------
    // Costruttore
    // ---------------------------------------------------------------
 
    /**
     * Crea una pila di capacità massima n.
     * Corrisponde all'array S[1..n] del Cormen (qui 0-based).
     *
     * @param n capacità massima della pila
     */
    public Pila(int n) {
        S   = new int[n];
        top = -1;           // pila inizialmente vuota (nel Cormen: top[S] = 0)
    }
 
    // ---------------------------------------------------------------
    // Le 3 operazioni principali (dal Cormen): tutte di costo Θ(1)
    // ---------------------------------------------------------------
 
    /**
     * STACK-EMPTY(S)
     * Restituisce true se la pila non contiene alcun elemento, altrimenti false.
     *
     * Cormen:
     *   if top[S] == 0
     *       return TRUE
     *   else return FALSE
     *
     * Complessità: Θ(1)
     */
    public boolean stackEmpty() {
        return top == -1;
    }
 
    /**
     * PUSH(S, x)
     * Inserisce x in cima alla pila.
     * Lancia StackOverflowException se la pila è piena.
     *
     * Cormen (con controllo overflow aggiunto, esercizio 10.1-4):
     *   if top[S] == length[S]
     *       error "overflow"
     *   top[S] = top[S] + 1
     *   S[top[S]] = x
     *
     * Complessità: Θ(1)
     */
    public void push(int x) {
        if (top == S.length - 1) {
            throw new StackOverflowException();
        }
        top = top + 1;
        S[top] = x;
    }
 
    /**
     * POP(S)
     * Rimuove e restituisce l'elemento in cima alla pila.
     * Lancia StackUnderflowException se la pila è vuota.
     *
     * Cormen:
     *   if STACK-EMPTY(S)
     *       error "underflow"
     *   else
     *       top[S] = top[S] - 1
     *       return S[top[S] + 1]
     *
     * Complessità: Θ(1)
     */
    public int pop() {
        if (stackEmpty()) {
            throw new StackUnderflowException();
        }
        top = top - 1;
        return S[top + 1];  // l'elemento rimosso è ancora in memoria ma
                             // non fa più parte della pila (top è sceso)
    }
 
    // ---------------------------------------------------------------
    // Metodi di utilità
    // ---------------------------------------------------------------
 
    /** Restituisce l'elemento in cima senza rimuoverlo (peek). */
    public int peek() {
        if (stackEmpty()) {
            throw new StackUnderflowException();
        }
        return S[top];
    }
 
    /** Numero di elementi attualmente nella pila. */
    public int size() {
        return top + 1;
    }
 
    /** Rappresentazione testuale della pila (dal fondo alla cima). */
    @Override
    public String toString() {
        if (stackEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i <= top; i++) {
            sb.append(S[i]);
            if (i < top) sb.append(", ");
        }
        sb.append("] <- cima");
        return sb.toString();
    }
 
    // ---------------------------------------------------------------
    // Main di test
    // ---------------------------------------------------------------
 
    public static void main(String[] args) {
 
        System.out.println("=== Test Pila (Cormen cap. 10.1) ===\n");
 
        Pila pila = new Pila(6);
 
        // Sequenza dell'esercizio 10.1-1 del Cormen:
        //   PUSH(S,4), PUSH(S,1), PUSH(S,3), POP(S),
        //   PUSH(S,8), POP(S)
        System.out.println("STO LAVORANDO CON LA PILA pila:\n");
        System.out.println("stackEmpty() - iniziamo: la Pila e' vuota? -> " + pila.stackEmpty()); // true
 
        pila.push(4);
        System.out.println("push(4)  -> " + pila);
        // è equivalente a scrivere 
        // System.out.println("push(4)  → " + pila.toString());
        // Java chiama toString() in modo implicito ogni volta che un oggetto viene concatenato a una stringa con +. 
        // Poiché nella classe Pila è stato fatto l'override di toString(), viene eseguita quella versione personalizzata della funzione, 
        // che scorre l'array da S[0] fino a S[top] e costruisce la stringa contenente gli elementi della pila
 
        pila.push(1);
        System.out.println("push(1)  -> " + pila);
 
        pila.push(3);
        System.out.println("push(3)  -> " + pila);
 
        int val = pila.pop();
        System.out.println("pop()    -> restituisce " + val + " da cancellare, pila: " + pila);
 
        pila.push(8);
        System.out.println("push(8)  -> " + pila);
 
        val = pila.pop();
        System.out.println("pop()    -> restituisce " + val + " da cancellare, pila: " + pila);
         
        pila.push(15);
        System.out.println("push(15)  -> " + pila);
 
        System.out.println("\nstackEmpty() - ora che ho riempito: la Pila e' vuota? -> " + pila.stackEmpty()); // false
        
        System.out.println("Infatti, la dimensione e': size()   -> " + pila.size()); // 3
        System.out.println("L'elemento in cima ora e': peek()   -> " + pila.peek()); // 15
 
        // --- Test underflow ---
        System.out.println("\n--- Test underflow ---\n");
        Pila pilaVuota = new Pila(3);
        System.out.println("STO LAVORANDO CON LA PILA pilaVuota:\n");
        try {
            pilaVuota.pop();
        } catch (StackUnderflowException e) {
            System.out.println("Eccezione catturata: " + e.getMessage());
        }
 
        // --- Test overflow ---
        System.out.println("\n--- Test overflow ---\n");
        Pila pilaPiccola = new Pila(2);
        System.out.println("STO LAVORANDO CON LA PILA pilaPilapiccola:\n");
        pilaPiccola.push(10);
        pilaPiccola.push(20);
        try {
            pilaPiccola.push(30); // la pila ha capacità 2
        } catch (StackOverflowException e) {
            System.out.println("Eccezione catturata: " + e.getMessage());
        }
    }
}
