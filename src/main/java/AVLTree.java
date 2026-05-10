/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 * 
 * Il punto chiave dell'AVL è che ogni nodo mantiene aggiornata la propria altezza. 
 * Il fattore di bilanciamento bf = altezza(sinistro) - altezza(destro) deve essere sempre in {-1, 0, +1}. 
 * Quando un inserimento lo porta a ±2, si applica la rotazione corretta in base alla "forma" dello sbilanciamento. 
 * I casi LL e RR richiedono una sola rotazione; 
 * i casi LR e RL ne richiedono due 
 * perché il sottoalbero problematico va prima "raddrizzato" con una rotazione nel verso opposto, 
 * poi bilanciato con una rotazione nel verso giusto. 
 * Tutto il meccanismo si risolve in O(log n) 
 * perché le rotazioni avvengono lungo il cammino radice-foglia percorso durante l'inserimento.
 * 
 */
public class AVLTree {

    // =========================================================
    //  NODO AVL
    //  In più rispetto al BST: il campo 'height' che permette
    //  di calcolare il fattore di bilanciamento (balance factor).
    // =========================================================

    private static class Node {
        int key;
        int height;          // altezza del sottoalbero radicato qui
        Node left, right;

        Node(int key) {
            this.key    = key;
            this.height = 1;  // un nodo appena inserito è foglia, altezza 1
        }
    }

    private Node root;

    // =========================================================
    //  UTILITY: altezza e fattore di bilanciamento
    // =========================================================

    private int height(Node n) {
        return (n == null) ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    /**
     * Fattore di bilanciamento = altezza(sinistro) - altezza(destro)
     * Proprietà AVL: |bf| <= 1 per ogni nodo.
     * bf > 1  → sbilanciato a sinistra
     * bf < -1 → sbilanciato a destra
     */
    private int bf(Node n) {
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }

    // =========================================================
    //  LE QUATTRO ROTAZIONI
    // =========================================================

    /**
     * ROTAZIONE SINISTRA (Left Rotate) su nodo x
     *
     *      x                y
     *     / \              / \
     *    T1   y    →      x   T3
     *        / \         / \
     *       T2  T3      T1  T2
     *
     * y diventa la nuova radice del sottoalbero.
     * La proprietà BST è preservata: T1 < x < T2 < y < T3
     */
    private Node rotateLeft(Node x) {
        Node y  = x.right;   // y è il figlio destro di x
        Node T2 = y.left;    // sottoalbero sinistro di y

        // esegui la rotazione
        y.left  = x;
        x.right = T2;

        // aggiorna le altezze (prima x, poi y perché y è ora sopra)
        updateHeight(x);
        updateHeight(y);

        System.out.println("  -> rotateLeft su " + x.key + " (nuovo radice: " + y.key + ")");
        return y;  // y è la nuova radice
    }

    /**
     * ROTAZIONE DESTRA (Right Rotate) su nodo y
     *
     *       y              x
     *      / \            / \
     *     x   T3   →    T1   y
     *    / \                / \
     *   T1  T2             T2  T3
     *
     * x diventa la nuova radice del sottoalbero.
     */
    private Node rotateRight(Node y) {
        Node x  = y.left;    // x è il figlio sinistro di y
        Node T2 = x.right;   // sottoalbero destro di x

        // esegui la rotazione
        x.right = y;
        y.left  = T2;

        // aggiorna le altezze
        updateHeight(y);
        updateHeight(x);

        System.out.println("  -> rotateRight su " + y.key + " (nuovo radice: " + x.key + ")");
        return x;  // x è la nuova radice
    }

    // =========================================================
    //  BILANCIAMENTO: sceglie quale rotazione applicare
    //
    //  CASO LL (Left-Left):
    //    Inserimento nel sottoalbero SINISTRO del figlio SINISTRO
    //    → una sola rotateRight
    //
    //  CASO RR (Right-Right):
    //    Inserimento nel sottoalbero DESTRO del figlio DESTRO
    //    → una sola rotateLeft
    //
    //  CASO LR (Left-Right):
    //    Inserimento nel sottoalbero DESTRO del figlio SINISTRO
    //    → rotateLeft sul figlio sinistro, poi rotateRight sulla radice
    //
    //  CASO RL (Right-Left):
    //    Inserimento nel sottoalbero SINISTRO del figlio DESTRO
    //    → rotateRight sul figlio destro, poi rotateLeft sulla radice
    // =========================================================

    private Node balance(Node n) {
        updateHeight(n);
        int b = bf(n);

        // ── CASO LL: sbilanciato a sinistra, figlio sinistro pesante a sinistra ──
        if (b > 1 && bf(n.left) >= 0) {
            System.out.println("  Caso LL su nodo " + n.key);
            return rotateRight(n);
        }

        // ── CASO LR: sbilanciato a sinistra, figlio sinistro pesante a destra ──
        if (b > 1 && bf(n.left) < 0) {
            System.out.println("  Caso LR su nodo " + n.key);
            n.left = rotateLeft(n.left);   // prima rotazione: left sul figlio
            return rotateRight(n);          // seconda rotazione: right sulla radice
        }

        // ── CASO RR: sbilanciato a destra, figlio destro pesante a destra ──
        if (b < -1 && bf(n.right) <= 0) {
            System.out.println("  Caso RR su nodo " + n.key);
            return rotateLeft(n);
        }

        // ── CASO RL: sbilanciato a destra, figlio destro pesante a sinistra ──
        if (b < -1 && bf(n.right) > 0) {
            System.out.println("  Caso RL su nodo " + n.key);
            n.right = rotateRight(n.right); // prima rotazione: right sul figlio
            return rotateLeft(n);            // seconda rotazione: left sulla radice
        }

        return n;  // già bilanciato
    }

    // =========================================================
    //  INSERT — O(log n)
    //  Inserisce ricorsivamente, poi risale bilanciando.
    // =========================================================

    public void insert(int key) {
        System.out.println("insert(" + key + ")");
        root = insert(root, key);
    }

    private Node insert(Node n, int key) {
        // 1. Inserimento BST standard
        if (n == null) return new Node(key);

        if (key < n.key)
            n.left  = insert(n.left,  key);
        else if (key > n.key)
            n.right = insert(n.right, key);
        else
            return n;  // duplicato: ignorato

        // 2. Risalendo, bilancia ogni nodo sul cammino
        return balance(n);
    }

    // =========================================================
    //  UTILITY: inorder e stampa struttura
    // =========================================================

    public void inorder() {
        System.out.print("  Inorder: ");
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(Node n) {
        if (n != null) {
            inorderRec(n.left);
            System.out.print(n.key + "(bf=" + bf(n) + ") ");
            inorderRec(n.right);
        }
    }

    /** Stampa l'albero ruotato di 90° (radice a sinistra, destro in alto) */
    public void printTree() {
        System.out.println("  Struttura albero (radice a sinistra):");
        printRec(root, "", true);
    }

    private void printRec(Node n, String indent, boolean last) {
        if (n != null) {
            System.out.println(indent + (last ? "|__ " : "|-- ")
                + n.key + " (h=" + n.height + ", bf=" + bf(n) + ")");
            String newIndent = indent + (last ? "    " : "|   ");
            printRec(n.left,  newIndent, false);
            printRec(n.right, newIndent, true);
        }
    }

    // =========================================================
    //  MAIN — un test per ciascun caso di rotazione
    // =========================================================

    public static void main(String[] args) {

        // ── CASO LL: inserimenti crescenti a sinistra ──
        System.out.println("-------------------------------");
        System.out.println("CASO LL - rotazione semplice destra");
        System.out.println("-------------------------------");
        System.out.println("Inserisco 30, 20, 10 - il 10 sbilancia 30 a sinistra (LL)");
        AVLTree t1 = new AVLTree();
        t1.insert(30);
        t1.insert(20);
        t1.insert(10);  // → sbilancia 30: bf=2, figlio sin. bf=1 → LL → rotateRight(30)
        t1.printTree();
        t1.inorder();
        /*
         * Prima del bilanciamento:      Dopo rotateRight(30):
         *     30 (bf=2)                     20
         *    /                             /  \
         *   20 (bf=1)           →        10   30
         *  /
         * 10
         */

        // ── CASO RR: inserimenti decrescenti a destra ──
        System.out.println("\n-------------------------------");
        System.out.println("CASO RR - rotazione semplice sinistra");
        System.out.println("-------------------------------");
        System.out.println("Inserisco 10, 20, 30 - il 30 sbilancia 10 a destra (RR)");
        AVLTree t2 = new AVLTree();
        t2.insert(10);
        t2.insert(20);
        t2.insert(30);  // → sbilancia 10: bf=-2, figlio dx bf=-1 → RR → rotateLeft(10)
        t2.printTree();
        t2.inorder();
        /*
         * Prima:           Dopo rotateLeft(10):
         * 10 (bf=-2)              20
         *   \                    /  \
         *   20 (bf=-1)  →      10   30
         *     \
         *     30
         */

        // ── CASO LR: sbilanciamento con forma a "gomito" sinistro-destro ──
        System.out.println("\n-------------------------------");
        System.out.println("CASO LR - doppia rotazione (left poi right)");
        System.out.println("-------------------------------");
        System.out.println("Inserisco 30, 10, 20 - il 20 sbilancia 30 (LR)");
        AVLTree t3 = new AVLTree();
        t3.insert(30);
        t3.insert(10);
        t3.insert(20);  // → sbilancia 30: bf=2, figlio sin. bf=-1 → LR
        t3.printTree(); //   1. rotateLeft(10) → 2. rotateRight(30)
        t3.inorder();
        /*
         * Prima:           Dopo rotateLeft(10):   Dopo rotateRight(30):
         * 30 (bf=2)              30                      20
         *  /                    /                       /  \
         * 10 (bf=-1)   →       20           →        10   30
         *   \                 /
         *   20               10
         */

        // ── CASO RL: sbilanciamento con forma a "gomito" destro-sinistro ──
        System.out.println("\n-------------------------------");
        System.out.println("CASO RL - doppia rotazione (right poi left)");
        System.out.println("-------------------------------");
        System.out.println("Inserisco 10, 30, 20 - il 20 sbilancia 10 (RL)");
        AVLTree t4 = new AVLTree();
        t4.insert(10);
        t4.insert(30);
        t4.insert(20);  // → sbilancia 10: bf=-2, figlio dx bf=1 → RL
        t4.printTree(); //   1. rotateRight(30) → 2. rotateLeft(10)
        t4.inorder();
        /*
         * Prima:           Dopo rotateRight(30):  Dopo rotateLeft(10):
         * 10 (bf=-2)             10                      20
         *    \                     \                     /  \
         *    30 (bf=1)   →         20        →        10   30
         *    /                       \
         *   20                       30
         */

        // ── INSERIMENTO SEQUENZA LUNGA: verifica bilanciamento automatico ──
        System.out.println("\n-------------------------------");
        System.out.println("INSERIMENTO SEQUENZA [1..7] - tutti i casi possibili");
        System.out.println("-------------------------------");
        AVLTree t5 = new AVLTree();
        for (int i = 1; i <= 7; i++) t5.insert(i);
        t5.printTree();
        t5.inorder();
        // Senza AVL sarebbe una catena lineare h=6.
        // Con AVL l'altezza rimane 3 = ⌊log₂(7)⌋ + 1
    }
}
/*
* O U T P U T
* 
-------------------------------
CASO LL - rotazione semplice destra
-------------------------------
Inserisco 30, 20, 10 - il 10 sbilancia 30 a sinistra (LL)
insert(30)
insert(20)
insert(10)
  Caso LL su nodo 30
  -> rotateRight su 30 (nuovo radice: 20)
  Struttura albero (radice a sinistra):
|__ 20 (h=2, bf=0)
    |-- 10 (h=1, bf=0)
    |__ 30 (h=1, bf=0)
  Inorder: 10(bf=0) 20(bf=0) 30(bf=0) 

-------------------------------
CASO RR - rotazione semplice sinistra
-------------------------------
Inserisco 10, 20, 30 - il 30 sbilancia 10 a destra (RR)
insert(10)
insert(20)
insert(30)
  Caso RR su nodo 10
  -> rotateLeft su 10 (nuovo radice: 20)
  Struttura albero (radice a sinistra):
|__ 20 (h=2, bf=0)
    |-- 10 (h=1, bf=0)
    |__ 30 (h=1, bf=0)
  Inorder: 10(bf=0) 20(bf=0) 30(bf=0) 

-------------------------------
CASO LR - doppia rotazione (left poi right)
-------------------------------
Inserisco 30, 10, 20 - il 20 sbilancia 30 (LR)
insert(30)
insert(10)
insert(20)
  Caso LR su nodo 30
  -> rotateLeft su 10 (nuovo radice: 20)
  -> rotateRight su 30 (nuovo radice: 20)
  Struttura albero (radice a sinistra):
|__ 20 (h=2, bf=0)
    |-- 10 (h=1, bf=0)
    |__ 30 (h=1, bf=0)
  Inorder: 10(bf=0) 20(bf=0) 30(bf=0) 

-------------------------------
CASO RL - doppia rotazione (right poi left)
-------------------------------
Inserisco 10, 30, 20 - il 20 sbilancia 10 (RL)
insert(10)
insert(30)
insert(20)
  Caso RL su nodo 10
  -> rotateRight su 30 (nuovo radice: 20)
  -> rotateLeft su 10 (nuovo radice: 20)
  Struttura albero (radice a sinistra):
|__ 20 (h=2, bf=0)
    |-- 10 (h=1, bf=0)
    |__ 30 (h=1, bf=0)
  Inorder: 10(bf=0) 20(bf=0) 30(bf=0) 

-------------------------------
INSERIMENTO SEQUENZA [1..7] - tutti i casi possibili
-------------------------------
insert(1)
insert(2)
insert(3)
  Caso RR su nodo 1
  -> rotateLeft su 1 (nuovo radice: 2)
insert(4)
insert(5)
  Caso RR su nodo 3
  -> rotateLeft su 3 (nuovo radice: 4)
insert(6)
  Caso RR su nodo 2
  -> rotateLeft su 2 (nuovo radice: 4)
insert(7)
  Caso RR su nodo 5
  -> rotateLeft su 5 (nuovo radice: 6)
  Struttura albero (radice a sinistra):
|__ 4 (h=3, bf=0)
    |-- 2 (h=2, bf=0)
    |   |-- 1 (h=1, bf=0)
    |   |__ 3 (h=1, bf=0)
    |__ 6 (h=2, bf=0)
        |-- 5 (h=1, bf=0)
        |__ 7 (h=1, bf=0)
  Inorder: 1(bf=0) 2(bf=0) 3(bf=0) 4(bf=0) 5(bf=0) 6(bf=0) 7(bf=0) 
* 
*/
