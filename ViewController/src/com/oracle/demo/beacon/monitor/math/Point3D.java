package com.oracle.demo.beacon.monitor.math;

public class Point3D
{
    public final double x;
    public final double y;
    public final double z;

    public Point3D(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public String toString()
    {
        return ("x="+x+", y="+y+", z="+ z);
    }
    
    public boolean equals(Point3D point)
    {
        return this.x == point.x && this.y == point.y && this.z == point.z;
    }

    public double distance(Point3D other)
    {
        double dx = x - other.x;
        double dy = y - other.y;
        double dz = z - other.z;
        
        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    /*
     * The algorithm in this method is taken from http://mathworld.wolfram.com/Sphere-SphereIntersection.html.
     */
//    public static Point3D trilaterate(Point3D point1, double radius1, Point3D point2, double radius2, Point3D point3, double radius3, Point3D point4, double radius4, double epsilon)
//    {
//        // Quick check that we're working with valid input.
//        if (point1 == null || point2 == null || point3 == null || point4 == null
//            || radius1 < 0.0 || radius2 < 0.0 || radius3 < 0.0 || radius4 < 0.0)
//            return null;
//        
//        // Find intersections of spheres two at a time to get two circles to work with.
//        // TODO: What if getSphereSphereIntersecion is a sphere or empty set instead of a circle?
////        BeaconGeoObject sphere1sphere2Intersection = getSphereSphereIntersecion(point1, radius1, point2, radius2); 
////        BeaconGeoObject sphere3sphere4Intersection = getSphereSphereIntersecion(point3, radius3, point4, radius4); 
//        
//
//        return null;
//    }
    
}
