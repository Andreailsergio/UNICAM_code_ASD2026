/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 */
public class MatrixMultiply {

    /**
     * Moltiplicazione standard tra due matrici quadrate n x n.
     * Pseudocodice Cormen - MATRIX-MULTIPLY(A, B):
     *   n = rows[A]
     *   sia C una matrice n x n
     *   for i = 1 to n
     *     for j = 1 to n
     *       c[i][j] = 0
     *       for k = 1 to n
     *         c[i][j] = c[i][j] + a[i][k] * b[k][j]
     *   return C
     *
     * La struttura è esattamente quella del Cormen: 
     * Complessità: Θ(n³)  →  tre cicli for annidati, dove il ciclo più interno accumula il prodotto scalare della riga i di A per la colonna j di B.
     * 
     * Output:
     * Matrice A:
     *    1   2   3
     *    4   5   6
     *    7   8   9
     * Matrice B:
     *    9   8   7
     *    6   5   4
     *    3   2   1
     * C = A x B:
     *   30  24  18
     *   84  69  54
     *  138 114  90
     * 
     */
    public static int[][] matrixMultiply(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = 0;
                for (int k = 0; k < n; k++) {
                    C[i][j] = C[i][j] + A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

    // Stampa una matrice
    public static void stampaMatrice(int[][] M) {
        for (int[] riga : M) {
            for (int val : riga) {
                System.out.printf("%4d", val);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] A = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        int[][] B = {
            {9, 8, 7},
            {6, 5, 4},
            {3, 2, 1}
        };

        System.out.println("Matrice A:");
        stampaMatrice(A);

        System.out.println("Matrice B:");
        stampaMatrice(B);

        int[][] C = matrixMultiply(A, B);

        System.out.println("C = A x B:");
        stampaMatrice(C);
    }
}
