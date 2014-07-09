package com.oracle.demo.beacon.monitor.math;

public class LinearEquation
{
    public final double m;
    public final double b;
    
    public LinearEquation(double m, double b)
    {
        this.m = m;
        this.b = b;
    }
    
    public double solve(double x)
    {
        return (m * x) + b;
    }
}
