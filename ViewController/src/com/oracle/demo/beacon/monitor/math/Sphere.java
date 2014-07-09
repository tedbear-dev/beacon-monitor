package com.oracle.demo.beacon.monitor.math;

public class Sphere
{
    public final Point3D center;
    public final double radius;
    
    public Sphere(Point3D center, double radius)
    {
        this.center = center;
        this.radius = radius;
    }
    
    public Sphere(Sphere sphere)
    {
        this.center = sphere.center;
        this.radius = sphere.radius;
    }
    
    public boolean equals(Sphere sphere)
    {
        return this.center.equals(sphere.center) && this.radius == sphere.radius;
    }
    
    public boolean hasPointOnSurface(Point3D point, double epsilon)
    {
        // (x-x1)^2 + (y-y1)^2 + (z-z1)^2 = r^2
        return (Math.abs(
            (center.x-point.x)*(center.x-point.x) 
            + (center.y-point.y)*(center.y-point.y) 
            + (center.z-point.z)*(center.z-point.z) 
            - (radius*radius)) 
            < epsilon); 
    }
    
    public String toString()
    {
        return "{" + center.toString() + ", r=" + radius + "}";  
                        
    }

}
