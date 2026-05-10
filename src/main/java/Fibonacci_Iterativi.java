/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.asd01052026;

/**
 *
 * @author Andrea_Sergiacomi
 */
public class Fibonacci_Iterativi 
{

    /**
     * fibonacci1 — formula chiusa con rapporto aureo φ.
     * Dalla slide Merelli: return (1/√5) * (φ^n - φ_hat^n)
     *
     * T(n) = O(1)  →  tempo costante, nessun ciclo né ricorsione
     *
     * ATTENZIONE: NON CORRETTO per n grandi a causa degli
     * errori di arrotondamento in virgola mobile.
     * Esempio: fibonacci1(18) = 2583  (corretto: 2584)
     */
    public static long fibonacci1(int n) {
        double phi    = (1 + Math.sqrt(5)) / 2;   // φ  ≈ 1.618
        double phiHat = (1 - Math.sqrt(5)) / 2;   // φ̂ ≈ -0.618
        return Math.round((Math.pow(phi, n) - Math.pow(phiHat, n)) / Math.sqrt(5));
    }

    /**
     * fibonacci3 — versione iterativa corretta.
     * Non fa parte delle slide ma è la soluzione pratica:
     * calcola F(n) con un semplice ciclo, senza ricorsione
     * e senza errori di arrotondamento.
     *
     * T(n) = Θ(n)  →  un solo ciclo lineare
     */
    public static long fibonacci3(int n) {
        if (n <= 2) return 1;
        long prev2 = 1; // F(n-2)
        long prev1 = 1; // F(n-1)
        long curr  = 0;
        for (int i = 3; i <= n; i++) {
            curr  = prev1 + prev2;
            prev2 = prev1;
            prev1 = curr;
        }
        return curr;
    }

    public static void main(String[] args) {
        System.out.println("n  | fibonacci1 (formula) | fibonacci3 (iterativo) | Corretto?");
        System.out.println("---|----------------------|------------------------|----------");
        for (int n = 1; n <= 20; n++) {
            long f1 = fibonacci1(n);
            long f3 = fibonacci3(n);
            String ok = (f1 == f3) ? "✓" : "✗  <-- ERRORE";
            System.out.printf("%2d | %20d | %22d | %s%n", n, f1, f3, ok);
        }
    }
}
