/*
 * 
 */
package com.oracle.demo.beacon.monitor.math;

public class Point2D
{
    public final double x;
    public final double y;

    public Point2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public String toString()
    {
        return ("x="+x+", y="+y);
    }

    public double distance(Point2D other)
    {
        double dx = x - other.x;
        double dy = y - other.y;
        
        return Math.sqrt((dy * dy) + (dx * dx));
    }
    
    /*
     * The algorithm in this method is taken from http://mathworld.wolfram.com/Circle-CircleIntersection.html and
     * http://mathworld.wolfram.com/RadicalLine.html.
     * Variable names correspond to symbols in the documentation.
     */
    public static Point2D trilaterate(Point2D point1, double radius1, Point2D point2, double radius2, Point2D point3, double radius3, double epsilon)
    {
        // TODO: Enhance how epsilon is used. Might be beneficial to use epsilon^2 in some situations.
        
        // Quick check that we're working with valid input.
        if (point1 == null || point2 == null || point3 == null
            || radius1 < 0.0 || radius2 < 0.0 || radius3 < 0.0)
            return null;
        
        double R1 = radius1;  // Radius of first circle
        double R2 = radius2;  // Radius of second circle
        double R3 = radius3;  // Radius of third circle
        
        // Distance form point1 to point2
        double d = point1.distance(point2);
        
        // Check if circles don't intersect. If the first two don't intersect, there's no point
        // in checking the third.
        if (d > (R1 + R2))
            return null;

        // Check if one circle is contained in the other. If so, we won't be able to get a unique intersection.
        if (d < Math.abs(R1 - R2))
            return null;

        // Check if point1 and point2 are the same point. If point1 and point2 are the same, the only way we'll 
        // have a solution is if R1 == R2 and the third circle intersects at one point.
        Point2D[] intersectionPoints;
        if (d == 0)
        {
            // If we have the same center but different radii, there are no cases
            // where the third circle intersects in one point.
            if (Math.abs(R1 - R2) > epsilon)
                return null;

            // We are now down to two (or fewer) unique circles. The two circles
            // intersect in one point if the distance between the points is equal
            // to the sum of the radii.
            intersectionPoints = Point2D.getIntersectionPoints(point1, R1, point3, R3);
        }
        else
            intersectionPoints = Point2D.getIntersectionPoints(point1, R1, point2, R2);
            
        // Determine if the third circle intersects with either of intersectionPoint1 or intersectionPoint2,
        // (the intersection points of the first two circles), but not both, unless they are the same point.
        if (intersectionPoints[0].distance(intersectionPoints[1]) < epsilon)
        {
            if (Math.abs(intersectionPoints[0].distance(point3) - radius3) < epsilon)
                return intersectionPoints[0];
            else
                return null;
        }
        else if (Math.abs(intersectionPoints[0].distance(point3) - radius3) < epsilon)
        {
            if (Math.abs(intersectionPoints[1].distance(point3) - radius3) < epsilon)
                return null;
            
            return intersectionPoints[0];
        }
        else if (Math.abs(intersectionPoints[1].distance(point3) - radius3) < epsilon)
            return intersectionPoints[1];
        else
            return null;
    }
    
    private static Point2D[] getIntersectionPoints(Point2D point1, double R1, Point2D point2, double R2)
    {
        double d = point1.distance(point2);
        double dsquared = d * d;
        double R1squared = R1 * R1;
        double R2squared = R2 * R2;
        
        // Distance from point1 to radical line. If two circles intersect in two points, then 
        // the radical line is the line passing through the points of intersection. This line is
        // perpendicular to the line joining centers of the circles.
        double x = (d == 0 ? (dsquared + R1squared - R2squared) : (dsquared + R1squared - R2squared) / (2.0 * d));
        double xsquared = x * x;
        
        // 1/2 length of segment on the radical line that connects the two points of intersection.  
        double y = (R1squared < xsquared) ? 0 : Math.sqrt(R1squared - xsquared); 
                        
        // Determine the coordinates of the intersection of the radical line with the center line.
        // The following are equal ratios:
        // x::d
        // x-axis delta from point1 to the intersection point::overall x-axis delta from point1 to point2
        // y-axis delta from point1 to the intersection point::overall y-axis delta from point1 to point2
        double dx = point2.x - point1.x;    // delta x
        double dy = point2.y - point1.y;    // delta y
        
        // TODO: What if d == 0?
        Point2D radicalIntersection = new Point2D(point1.x + (dx * x / d), point1.y + (dy * x / d));

        // Determine the intersection points of the first two circles.
        double xOffset = -dy * (y / d);
        double yOffset = dx * (y / d);
        
        Point2D[] intersetionPoints = new Point2D[2];
        intersetionPoints[0] = new Point2D(radicalIntersection.x + xOffset, radicalIntersection.y + yOffset);
        intersetionPoints[1] = new Point2D(radicalIntersection.x - xOffset, radicalIntersection.y - yOffset);
        
        return intersetionPoints;
    }

}