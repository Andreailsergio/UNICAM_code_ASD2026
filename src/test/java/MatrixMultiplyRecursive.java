/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 * 
 * OUTPUT:
 * Output:
 * A:
 *     1    2    3    4
 *     5    6    7    8
 *     9    1    2    3
 *     4    5    6    7
 * B:
 *     8    7    6    5
 *     4    3    2    1
 *     1    2    3    4
 *     5    6    7    8
 * C = Strassen(A, B):
 *    39   43   47   51
 *   111  115  119  123
 *    93   88   83   78
 *    93   97  101  105
 * 
 * I 4 passi del Cormen sono espliciti nel codice. 
 * Il guadagno rispetto a MATRIX-MULTIPLY è che invece di 8 moltiplicazioni ricorsive se ne usano solo 7 (P1…P7), 
 * a costo di qualche addizione e sottrazione extra in Θ(n²). 
 * Questo abbassa la ricorrenza da T(n) = 8T(n/2) + Θ(n²) → Θ(n³) a T(n) = 7T(n/2) + Θ(n²) → Θ(n^log₂7) ≈ Θ(n^2.807). 
 * Come nota il Cormen, l'algoritmo è di interesse principalmente teorico: 
 * la grande costante nascosta lo rende conveniente solo per matrici molto grandi e dense.
 * 
 */
public class MatrixMultiplyRecursive {

    // -------------------------------------------------------
    // Operazioni di supporto su matrici
    // -------------------------------------------------------

    /** Somma elemento per elemento: C = A + B */
    static int[][] add(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }

    /** Sottrazione elemento per elemento: C = A - B */
    static int[][] sub(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    /**
     * Estrae la sottomatrice n/2 x n/2 a partire da (rigaOff, colOff).
     * Usata per dividere A e B nelle quattro sottomatrici:
     *   A = | A11  A12 |     B = | B11  B12 |
     *       | A21  A22 |         | B21  B22 |
     */
    static int[][] split(int[][] M, int rigaOff, int colOff) {
        int half = M.length / 2;
        int[][] S = new int[half][half];
        for (int i = 0; i < half; i++)
            for (int j = 0; j < half; j++)
                S[i][j] = M[i + rigaOff][j + colOff];
        return S;
    }

    /**
     * Copia una sottomatrice half x half nella posizione (rigaOff, colOff)
     * della matrice risultato n x n.
     */
    static void merge(int[][] C, int[][] S, int rigaOff, int colOff) {
        int half = S.length;
        for (int i = 0; i < half; i++)
            for (int j = 0; j < half; j++)
                C[i + rigaOff][j + colOff] = S[i][j];
    }

 // -------------------------------------------------------
    // STRASSEN - MATRIX-MULTIPLY-RECURSIVE
    // Cormen cap. 4 (4a ed.) / cap. 28 (3a ed.)
    //
    // Divide A e B in 4 sottomatrici n/2 x n/2.
    // Calcola 7 prodotti P1..P7 (invece di 8).
    // Ricombina per ottenere C11, C12, C21, C22.
    //
    // Ricorrenza: T(n) = 7T(n/2) + Θ(n²)
    // Soluzione:  T(n) = Θ(n^log₂7) ≈ Θ(n^2.807)
    // -------------------------------------------------------
    public static int[][] strassen(int[][] A, int[][] B) {
        int n = A.length;

        // Caso base: matrice 1x1
        if (n == 1) {
            return new int[][]{{A[0][0] * B[0][0]}};
        }

        // --- PASSO 1: Divide ---
        // Suddivide A e B in quattro sottomatrici n/2 x n/2
        int[][] A11 = split(A, 0,      0);
        int[][] A12 = split(A, 0,      n/2);
        int[][] A21 = split(A, n/2,    0);
        int[][] A22 = split(A, n/2,    n/2);

        int[][] B11 = split(B, 0,      0);
        int[][] B12 = split(B, 0,      n/2);
        int[][] B21 = split(B, n/2,    0);
        int[][] B22 = split(B, n/2,    n/2);

        // --- PASSO 2 e 3: Impera ---
        // Calcola le 7 matrici prodotto di Strassen
        // (invece delle 8 della strategia naive)
        int[][] P1 = strassen(A11,            sub(B12, B22));
        int[][] P2 = strassen(add(A11, A12),  B22);
        int[][] P3 = strassen(add(A21, A22),  B11);
        int[][] P4 = strassen(A22,            sub(B21, B11));
        int[][] P5 = strassen(add(A11, A22),  add(B11, B22));
        int[][] P6 = strassen(sub(A12, A22),  add(B21, B22));
        int[][] P7 = strassen(sub(A11, A21),  add(B11, B12));

        // --- PASSO 4: Combina ---
        // Ricombina i prodotti per ottenere le 4 sottomatrici di C
        // C11 = P5 + P4 - P2 + P6
        // C12 = P1 + P2
        // C21 = P3 + P4
        // C22 = P5 + P1 - P3 - P7
        int[][] C11 = add(sub(add(P5, P4), P2), P6);
        int[][] C12 = add(P1, P2);
        int[][] C21 = add(P3, P4);
        int[][] C22 = sub(sub(add(P5, P1), P3), P7);

        // Assembla C dalle quattro sottomatrici
        int[][] C = new int[n][n];
        merge(C, C11, 0,   0);
        merge(C, C12, 0,   n/2);
        merge(C, C21, n/2, 0);
        merge(C, C22, n/2, n/2);

        return C;
    }

    // Stampa una matrice
    static void stampa(int[][] M) {
        for (int[] riga : M) {
            for (int v : riga)
                System.out.printf("%5d", v);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // n deve essere potenza di 2
        int[][] A = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 1, 2, 3},
            {4, 5, 6, 7}
        };
        int[][] B = {
            {8, 7, 6, 5},
            {4, 3, 2, 1},
            {1, 2, 3, 4},
            {5, 6, 7, 8}
        };

        System.out.println("A:"); stampa(A);
        System.out.println("B:"); stampa(B);

        int[][] C = strassen(A, B);
        System.out.println("C = Strassen(A, B):"); stampa(C);
    }
}