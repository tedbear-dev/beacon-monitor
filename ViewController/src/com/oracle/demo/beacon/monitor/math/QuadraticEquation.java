package com.oracle.demo.beacon.monitor.math;

public class QuadraticEquation
{
    public final double a;
    public final double b;
    public final double c;
    
    /**
     * Represents a quadratic equation in the form ax^2 + bx + c = 0
     * 
     * @param a
     * @param b
     * @param c
     */
    public QuadraticEquation(double a, double b, double c)
    {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    /**
     * Solves the quadratic equation using the quadratic formula.
     * 
     * @return
     */
    double[] solve()
    {
        double[] x;
        double radicand = (b*b)-(4*a*c);
        
        if (radicand < 0)
        {
            x = new double[0];
        }
        else
        {
            double root = Math.sqrt(radicand);
            
            if (root == 0.0)
            {
                x = new double[1];
                x[0] = (-b + root)/(2*a); 
            }
            else
            {
                x = new double[2];
                x[0] = (-b + root)/(2*a); 
                x[1] = (-b - root)/(2*a); 
            }
        }
        
        return x;
    }
}
