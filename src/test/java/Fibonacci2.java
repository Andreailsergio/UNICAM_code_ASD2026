/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 */
public class Fibonacci2 {

    /**
     * Calcola l'n-esimo numero di Fibonacci ricorsivamente.
     * Corrisponde all'algoritmo fibonacci2 delle slide.
     * Complessità: T(n) = O(2^n)
     * 
     * OUTPUT per n=20
     * F(1) = 1
     * F(2) = 1
     * F(3) = 2
     * F(4) = 3
     * F(5) = 5
     * F(6) = 8
     * F(7) = 13
     * F(8) = 21
     * F(9) = 34
     * F(10) = 55
     * F(11) = 89
     * F(12) = 144
     * F(13) = 233
     * F(14) = 377
     * F(15) = 610
     * F(16) = 987
     * F(17) = 1597
     * F(18) = 2584
     * F(19) = 4181
     * F(20) = 6765
     * 
     * questo algoritmo è estremamente lento: ogni chiamata genera due chiamate ricorsive, 
     * producendo un albero di ricorsione con costo T(n) = T(n−1) + T(n−2), che cresce come O(2ⁿ). 
     * Per n grande diventa inutilizzabile in pratica.
     */
    public static int fibonacci2(int n) {
        if (n <= 2) {
            return 1;
        } else {
            return fibonacci2(n - 1) + fibonacci2(n - 2);
        }
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 20; i++) {
            System.out.println("F(" + i + ") = " + fibonacci2(i));
        }
    }
}