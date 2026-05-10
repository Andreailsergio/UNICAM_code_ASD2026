/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 * 
 * La proprietà fondamentale che rende l'inorder speciale per un BST è questa: 
 * per ogni nodo x, tutti i nodi nel sottoalbero sinistro hanno chiave < x.key, e tutti nel destro hanno chiave > x.key. 
 * Visitare prima il sinistro, poi la radice, poi il destro rispetta esattamente questo ordine, 
 * producendo sempre i valori in sequenza crescente — Θ(n) perché ogni nodo viene visitato esattamente una volta.
 * Le tre visite differiscono solo nel momento in cui viene stampata la radice: 
 * prima dei figli (preorder), tra i figli (inorder), dopo i figli (postorder). 
 * Nel codice questo si traduce semplicemente nello spostare la riga System.out.print 
 * prima, in mezzo, o dopo le due chiamate ricorsive.
 * 
 * Ci sono anche gli algoritmi per massimo, minimo, successore, predecessore
 * 
 * C'è ricerca e inserimento
 * 
 * La cancellazione in un BST gestisce tre casi distinti secondo il Cormen. 
 * Il cuore dell'implementazione è la procedura ausiliaria transplant 
 * che sostituisce un sottoalbero con un altro aggiornando il puntatore del padre.
 * 
 */

import java.util.NoSuchElementException;

public class BST {

    // =========================================================
    //  NODO
    // =========================================================

    private static class Node {
        int key;
        Node left, right, parent;

        Node(int key) {
            this.key = key;
        }
    }

    private Node root;

    // =========================================================
    //  INSERT — O(h)
    //  Inserisce una chiave mantenendo la proprietà BST.
    //  Scende a sinistra se key < nodo corrente, a destra altrimenti.
    // =========================================================

    public void insert(int key) {
        Node z = new Node(key);
        Node y = null;   // padre del punto di inserimento
        Node x = root;

        // Scendi fino a trovare il posto giusto
        while (x != null) {
            y = x;
            if (z.key < x.key)
                x = x.left;
            else
                x = x.right;
        }

        z.parent = y;

        if (y == null)
            root = z;              // albero era vuoto
        else if (z.key < y.key)
            y.left = z;
        else
            y.right = z;
    }

    // =========================================================
    //  VISITA IN ORDINE SIMMETRICO — INORDER  O(n)
    //  Visita: sottoalbero sinistro → radice → sottoalbero destro
    //  Produce l'output in ordine CRESCENTE — proprietà chiave del BST.
    //  Dal Cormen: INORDER-TREE-WALK(x)
    // =========================================================

    public void inorder() {
        System.out.print("Inorder:    ");
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(Node x) {
        if (x != null) {
            inorderRec(x.left);    // 1. visita sottoalbero sinistro
            System.out.print(x.key + " ");  // 2. stampa la radice
            inorderRec(x.right);   // 3. visita sottoalbero destro
        }
    }

    // =========================================================
    //  VISITA IN ORDINE ANTICIPATO — PREORDER  O(n)
    //  Visita: radice → sottoalbero sinistro → sottoalbero destro
    //  Utile per: copiare/serializzare l'albero, valutare espressioni.
    // =========================================================

    public void preorder() {
        System.out.print("Preorder:   ");
        preorderRec(root);
        System.out.println();
    }

    private void preorderRec(Node x) {
        if (x != null) {
            System.out.print(x.key + " ");  // 1. stampa la radice
            preorderRec(x.left);            // 2. visita sinistro
            preorderRec(x.right);           // 3. visita destro
        }
    }

    // =========================================================
    //  VISITA IN ORDINE DIFFERITO — POSTORDER  O(n)
    //  Visita: sottoalbero sinistro → sottoalbero destro → radice
    //  Utile per: cancellare l'albero, calcolare dimensioni/altezza.
    // =========================================================

    public void postorder() {
        System.out.print("Postorder:  ");
        postorderRec(root);
        System.out.println();
    }

    private void postorderRec(Node x) {
        if (x != null) {
            postorderRec(x.left);           // 1. visita sinistro
            postorderRec(x.right);          // 2. visita destro
            System.out.print(x.key + " ");  // 3. stampa la radice
        }
    }

    // =========================================================
    //  VERSIONE ITERATIVA DELL'INORDER — O(n)
    //  Usa uno stack esplicito invece della ricorsione.
    //  Utile per alberi molto profondi (evita stack overflow).
    // =========================================================

    public void inorderIterativo() {
        System.out.print("Inorder it: ");
        java.util.Deque<Node> stack = new java.util.ArrayDeque<>();
        Node curr = root;

        while (curr != null || !stack.isEmpty()) {
            // Scendi fino alla foglia più a sinistra
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            // Risali: processa il nodo e vai a destra
            curr = stack.pop();
            System.out.print(curr.key + " ");
            curr = curr.right;
        }
        System.out.println();
    }

    // =========================================================
    //  OPERAZIONI AUSILIARIE
    // =========================================================

    /** MINIMUM — O(h): scende sempre a sinistra */
    public int minimum() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node x = root;
        while (x.left != null) x = x.left;
        return x.key;
    }

    /** MAXIMUM — O(h): scende sempre a destra */
    public int maximum() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node x = root;
        while (x.right != null) x = x.right;
        return x.key;
    }

    /** SEARCH — O(h) RESTITUISCE SOLO TROVATO True O NON TROVATO False */
    public boolean search(int key) {
        Node x = root;
        while (x != null) {
            if (key == x.key) return true;
            x = (key < x.key) ? x.left : x.right;
        }
        return false;
    }

    /** HEIGHT — O(n) */
    public int height() {
        return heightRec(root);
    }

    private int heightRec(Node x) {
        if (x == null) return -1;   // convenzione: albero vuoto ha altezza -1
        return 1 + Math.max(heightRec(x.left), heightRec(x.right));
    }

        // =========================================================
    //  SEARCH — O(h): trova il nodo con chiave data e lo restituisce
    // =========================================================

        private Node search_PreSuc(int key) {
        Node x = root;
        while (x != null) {
            if (key == x.key)       return x;
            else if (key < x.key)   x = x.left;
            else                    x = x.right;
        }
        return null;
    }
        // =========================================================
    //  MINIMUM / MAXIMUM — O(h)
    //  Usati internamente da successor e predecessor.
    // =========================================================

    private Node minimum_PreSuc(Node x) {
        while (x.left != null) x = x.left;
        return x;
    }

    private Node maximum_PreSuc(Node x) {
        while (x.right != null) x = x.right;
        return x;
    }
    
    // =========================================================
    //  SUCCESSOR — O(h)
    //  Dal Cormen: TREE-SUCCESSOR(x)
    //
    //  Caso 1: x ha figlio destro
    //          → il successore è il MINIMO del sottoalbero destro
    //            (il più piccolo tra i valori maggiori di x)
    //
    //  Caso 2: x NON ha figlio destro
    //          → risali verso la radice finché si incontra
    //            un antenato y tale che x sia nel suo sottoalbero SINISTRO.
    //            Quel y è il successore (il primo antenato più grande).
    //          → se non esiste (x è il massimo) → restituisce null
    // =========================================================

    private Node successor(Node x) {
        // Caso 1: esiste il figlio destro
        if (x.right != null)
            return minimum_PreSuc(x.right);

        // Caso 2: risali finché x non è figlio sinistro
        Node y = x.parent;
        // poco intuitivo - ma si risale finché si è ancora figli destri
        while (y != null && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;   // null se x era il massimo
    }

    // =========================================================
    //  PREDECESSOR — O(h)
    //  Simmetrico a SUCCESSOR.
    //
    //  Caso 1: x ha figlio sinistro
    //          → il predecessore è il MASSIMO del sottoalbero sinistro
    //
    //  Caso 2: x NON ha figlio sinistro
    //          → risali finché si incontra un antenato y tale che
    //            x sia nel suo sottoalbero DESTRO.
    //          → se non esiste (x è il minimo) → restituisce null
    // =========================================================

    private Node predecessor(Node x) {
        // Caso 1: esiste il figlio sinistro
        if (x.left != null)
            return maximum_PreSuc(x.left);

        // Caso 2: risali finché x non è figlio destro
        Node y = x.parent;
        // poco intuitivo - ma si risale finché si è ancora figli sinistri
        while (y != null && x == y.left) {
            x = y;
            y = y.parent;
        }
        return y;   // null se x era il minimo
    }

    // =========================================================
    //  API PUBBLICA — riceve la chiave, restituisce il valore
    // =========================================================

    public Integer successor(int key) {
        Node x = search_PreSuc(key);
        if (x == null)
            throw new NoSuchElementException("Chiave " + key + " non trovata");
        Node s = successor(x);
        return (s == null) ? null : s.key;  // null = non esiste (x è il massimo)
    }

    public Integer predecessor(int key) {
        Node x = search_PreSuc(key);
        if (x == null)
            throw new NoSuchElementException("Chiave " + key + " non trovata");
        Node p = predecessor(x);
        return (p == null) ? null : p.key;  // null = non esiste (x è il minimo)
    }


    // =========================================================
    //  TRANSPLANT — O(1)
    //  Sostituisce il sottoalbero radicato in u con quello in v.
    //  Aggiorna il padre di u in modo che punti a v.
    //  NON aggiorna v.left, v.right: lo fa il chiamante.
    //  Dal Cormen: TRANSPLANT(T, u, v)
    // =========================================================

    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            // u era la radice → v diventa la nuova radice
            root = v;
        } else if (u == u.parent.left) {
            // u era figlio sinistro → v prende il suo posto
            u.parent.left = v;
        } else {
            // u era figlio destro → v prende il suo posto
            u.parent.right = v;
        }
        // Aggiorna il puntatore al padre di v (se v non è null)
        if (v != null)
            v.parent = u.parent;
    }

    // =========================================================
    //  DELETE — O(h)
    //  Dal Cormen: TREE-DELETE(T, z)
    //
    //  CASO 1: z non ha figlio sinistro
    //          → sostituisci z con il suo figlio destro (anche null)
    //
    //  CASO 2: z non ha figlio destro (ma ha figlio sinistro)
    //          → sostituisci z con il suo figlio sinistro
    //
    //  CASO 3: z ha entrambi i figli
    //          → trova il successore y = minimo del sottoalbero destro
    //          → se y non è figlio diretto di z, prima estrai y
    //             dalla sua posizione e metti il figlio destro di y
    //             al suo posto, poi fai prendere a y il figlio destro di z
    //          → infine sostituisci z con y e dagli il figlio sinistro di z
    /* 
    * Il caso 3 è quello che richiede più attenzione. 
    * La scelta di usare il successore (minimo del sottoalbero destro) come sostituto di z 
    * garantisce che la proprietà BST sia preservata: 
    * il successore è maggiore di tutto il sottoalbero sinistro di z 
    * (perché è maggiore di z stesso) e minore o uguale a tutto il resto del sottoalbero destro 
    * (perché è il minimo di quel sottoalbero). 
    * Il controllo if (y.parent != z) distingue i due sottocasi: 
    * - se il successore è già figlio diretto di z non serve scollegarlo prima, 
    * - altrimenti bisogna prima estrarlo dalla sua posizione corrente con transplant(y, y.right) 
    *   prima di inserirlo al posto di z.
    *
    */
    // =========================================================

    public void delete(int key) {
        Node z = search_PreSuc(key);
        if (z == null) {
            System.out.println("Chiave " + key + " non trovata.");
            return;
        }
        delete(z);
    }

    private void delete(Node z) {

        if (z.left == null) {
            // ── CASO 1: nessun figlio sinistro ──
            // z potrebbe avere figlio destro oppure essere una foglia.
            // In entrambi i casi transplant(z, z.right) funziona:
            // se z è foglia, z.right è null e il padre di z punta a null.
            transplant(z, z.right);

        } else if (z.right == null) {
            // ── CASO 2: solo figlio sinistro ──
            transplant(z, z.left);

        } else {
            // ── CASO 3: z ha entrambi i figli ──
            // Il successore di z è il minimo del sottoalbero destro.
            // Andrà a prendere il posto di z mantenendo la proprietà BST.
            Node y = minimum_PreSuc(z.right);   // y = successore di z

            if (y.parent != z) {
                // y NON è figlio diretto di z:
                // prima estrai y dalla sua posizione corrente,
                // sostituendolo con il suo unico eventuale figlio destro
                // (y non ha figlio sinistro per definizione di minimo)
                transplant(y, y.right);
                // ora collega y al sottoalbero destro di z
                y.right = z.right;
                y.right.parent = y;
            }
            // sostituisci z con y
            transplant(z, y);
            // collega y al sottoalbero sinistro di z
            y.left = z.left;
            y.left.parent = y;
        }
    }    
    
    // stampa in ordine - per verificare dopo cancellazione
    public void inorder_VerDel(String label) {
        System.out.print(label + ": ");
        inorderRec(root);
        System.out.println();
    }
    
    // =========================================================
    //  MAIN — test
    // =========================================================

    public static void main(String[] args) {

        System.out.println("=== ESEMPIO CORMEN ===\n");
        BST t = new BST();

        // Inserimenti — l'ordine determina la forma dell'albero
        int[] keys = {15, 7, 33, 3, 10, 25, 40};
        for (int k : keys) t.insert(k);

        /*
         * Albero risultante:
         *
         *         15
         *        /  \
         *       7    33
         *      / \   / \
         *     3  10 25  40
         */

        System.out.println("=== Visite dell'albero t ===");
        t.inorder();           // → 3 7 10 15 25 33 40  (ordine crescente!)
        t.preorder();          // → 15 7 3 10 33 25 40
        t.postorder();         // → 3 10 7 25 40 33 15
        t.inorderIterativo();  // → 3 7 10 15 25 33 40  (stesso risultato di inorder ricorsivo)

        System.out.println("\n=== Operazioni di base ===");
        System.out.println("Minimo:    " + t.minimum());   // 3
        System.out.println("Massimo:   " + t.maximum());   // 40
        System.out.println("Altezza:   " + t.height());    // 2
        System.out.println("Search 10: " + t.search(10));  // true
        System.out.println("Search 99: " + t.search(99));  // false
        
                System.out.println("\n--- Successori ---");
        // Caso 1: nodo con figlio destro
        System.out.println("succ(15)  = " + t.successor(15));   // 25  (min del sottoalbero destro di 15)
        // Caso 2: nodo senza figlio destro → risali
        System.out.println("succ(10) = " + t.successor(10));  // 15 (primo antenato di cui 10 è a sinistra)
        // Caso estremo: massimo dell'albero
        System.out.println("succ(40) = " + t.successor(40));  // null (40 è il massimo)

        System.out.println("\n--- Predecessori ---");
        // Caso 1: nodo con figlio sinistro
        System.out.println("pred(15)  = " + t.predecessor(15));   // 10  (max del sottoalbero sinistro di 15)
        // Caso 2: nodo senza figlio sinistro → risali
        System.out.println("pred(25)  = " + t.predecessor(25));   // 15  (primo antenato di cui 25 è a destra)
        // Caso estremo: minimo dell'albero
        System.out.println("pred(3)  = " + t.predecessor(3));   // null (3 è il minimo)
        
                System.out.println("\n\n=== ESEMPIO SLIDE MERELLI ===\n");
        BST t2 = new BST();

        // Inserimenti — l'ordine determina la forma dell'albero
        int[] keys2 = {8, 5, 18, 6, 15, 9, 17, 16};
        for (int k2 : keys2) t2.insert(k2);

        /*
         * Albero risultante:
         *
         *           8
         *        /     \
         *       5      18
         *        \     /  
         *         6   15 
         *             / \
         *            9  17
         *               /
         *              16
         */

        System.out.println("=== Visite dell'albero t2 ===");
        t2.inorder();           // → 5 6 8 9 15 16 17 18  (ordine crescente!)
        t2.preorder();          // → 8 5 6 18 15 9 17 16
        t2.postorder();         // → 6 5 9 16 17 15 18 8
        t2.inorderIterativo();  // → 5 6 8 9 15 16 17 18  (stesso risultato del ricorsivo)

        System.out.println("\n=== Operazioni di base ===");
        System.out.println("Minimo:    " + t2.minimum());   // 5
        System.out.println("Massimo:   " + t2.maximum());   // 18
        System.out.println("Altezza:   " + t2.height());    // 4
        System.out.println("Search 10: " + t2.search(10));  // false
        System.out.println("Search 9: " + t2.search(9));  // true
        
                        System.out.println("\n--- Successori ---");
        // Caso 1: nodo con figlio destro
        System.out.println("succ(8)  = " + t2.successor(8));   // 9  (min del sottoalbero destro di 8)
        // Caso 2: nodo senza figlio destro → risali
        System.out.println("succ(16) = " + t2.successor(16));  // 17 (primo antenato di cui 16 è a sinistra)
        // Caso estremo: massimo dell'albero
        System.out.println("succ(18) = " + t2.successor(18));  // null (18 è il massimo)

        System.out.println("\n--- Predecessori ---");
        // Caso 1: nodo con figlio sinistro
        System.out.println("pred(8)  = " + t2.predecessor(8));   // 6  (max del sottoalbero sinistro di 8)
        // Caso 2: nodo senza figlio sinistro → risali
        System.out.println("pred(16)  = " + t2.predecessor(16));   // 15  (primo antenato di cui 16 è a destra)
        // Caso estremo: minimo dell'albero
        System.out.println("pred(5)  = " + t2.predecessor(5));   // null (5 è il minimo)

                                System.out.println("\n--- CANCELLAZIONI ---");
// ── CASO 1a: foglia (nessun figlio) ──
        // 16 è foglia: transplant(16, null) → padre 17 punta a null
        System.out.println("\n[CASO 1 - foglia] delete(16)");
        t2.delete(16);
        t2.inorder_VerDel("Dopo delete(16)");
        // 5 6 8 9 15 17 18
         /*
         * Albero risultante:
         *
         *           8
         *        /     \
         *       5      18
         *        \     /  
         *         6   15 
         *             / \
         *            9  17
         */

        // ── CASO 1b: solo figlio destro ──
        // 5 ha solo figlio destro
        // Usiamo 6 che è foglia
        System.out.println("\n[CASO 1 - solo figlio destro] delete(5)");
        t2.delete(5);
        t2.inorder_VerDel("Dopo delete(5)");
        // 6 8 9 15 17 18
        /*
         * Albero risultante:
         *
         *           8
         *        /     \
         *       6      18
         *              /  
         *             15 
         *             / \
         *            9  17
         */

        // ── CASO 2: solo figlio sinistro ──
        // 18 ha solo figlio sinistro (15): transplant(18, 15)
        System.out.println("\n[CASO 2 - solo figlio sinistro] delete(18)");
        t2.delete(18);
        t2.inorder_VerDel("Dopo delete(18)");
        // 6 8 9 15 17
                /*
         * Albero risultante:
         *
         *           8
         *        /     \
         *       6      15
         *             / \
         *            9  17
         */

        // ── CASO 3a: due figli, successore è figlio diretto ──
        // 15 ha due figli (9, 17). successore = min(17) = 17 → figlio diretto
        // transplant(15, 17), poi 17.left = 9
        System.out.println("\n[CASO 3a - due figli, succ. figlio diretto] delete(15)");
        t2.delete(15);
        t2.inorder_VerDel("Dopo delete(15)");
        // 6 8 9 17
        /*
         * Albero risultante:
         *
         *           8
         *        /     \
         *       6      17
         *             / 
         *            9  
         */

        // ── Cancellazione della radice ──
        System.out.println("\n[radice] delete(8)");
        t2.delete(8);
        t2.inorder_VerDel("Dopo delete(8)");
        // 6 9 17
                /*
         * Albero risultante:
         *
         *           9
         *        /     \
         *       6      17
         *             
         *            
         */
                
        // ── Chiave inesistente ──
        System.out.println("\n[chiave non presente] delete(99)");
        t2.delete(99);
        
        System.out.println("\n[RIEMPIO DI NUOVO IL BST] aggiungo altri 2 livelli di sottorami");
        
        // Inserimenti — l'ordine determina la forma dell'albero
        int[] keys3 = {2, 7, 1, 3, 8, 15, 20, 11, 18, 35};
        for (int k3 : keys3) t2.insert(k3);
        /*
         * Albero risultante:
         *
         *              9
         *         /         \
         *       6            17
         *     /   \        /    \
         *    2     7      15    20
         *   / \     \    /     /   \
         *  1   3     8  11    18   35       
         *            
         */
        t2.inorder_VerDel("Nuovo albero risultante dopo inserimenti");
        // 1, 2, 3, 6, 7, 8, 9, 11, 15, 17, 18, 20, 35
        
        // ── CASO 3b: due figli, successore NON è figlio diretto ──
        // 17 ha due figli (figlio dx = 20).
        // ma successore di 17 è 18 (figlio sx di 20)
        System.out.println("\n[CASO 3b - due figli, succ. NON figlio diretto] delete(17)");
        t2.delete(17);
        t2.inorder_VerDel("Dopo delete(17)");
        // 1, 2, 3, 6, 7, 8, 9, 11, 15, 18, 20, 35
             /*
         * Albero risultante:
         *
         *              9
         *         /         \
         *       6            18
         *     /   \        /    \
         *    2     7      15    20
         *   / \     \    /        \
         *  1   3     8  11         35       
         *            
         */   

        
    }
}