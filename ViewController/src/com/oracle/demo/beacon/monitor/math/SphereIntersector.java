package com.oracle.demo.beacon.monitor.math;

public class SphereIntersector
{
    /**
     * Returns the two 3D points of intersection of three spheres.
     * 
     * @param sphereA
     * @param sphereB
     * @param sphereC
     * @param epsilon
     * @return
     */
    public static Point3D[] getIntersection(Sphere sphereA, Sphere sphereB, Sphere sphereC, double epsilon)
    {
        // The algorithm used follows the procedure documented at 
        // http://mathforum.org/library/drmath/view/63138.html. Briefly,
        // 1. Subtract the equation for sphere 2 from the equation for sphere 1, giving a linear relation 
        //    in x, y, and z. 
        // 2. Subtract the equation for sphere 3 from the equation for sphere 1, giving a linear relation 
        //    in x, y, and z. 
        // 3. Solve the linear system of two equations for x in terms of z.  
        // 4. Solve the linear system of two equations for y in terms of z.  
        // 5. Substitute the equations for x and y from steps 3 and 4 into any of the equations of the spheres.
        //    This will give a quadratic equation in terms of z alone.
        // 6. Solve the quadratic equation to get the two z values.
        // 7. Compute the values of x by subsituting the values of z into the equation found in step 3. 
        // 8. Compute the values of y by subsituting the values of z into the equation found in step 4. 
        //
        // Not all work is shown. Where undocumented, the formulas used were obtained using simple (though 
        // sometimes lengthy) algebra. 

        // TODO: Divide by 0 errors
        // TODO: Handle concentric spheres
        // TODO: Handle disjoint spheres
        // TODO: epsilon
        
        Sphere sphere1;
        Sphere sphere2;
        Sphere sphere3;

        // For the algorithm to work without divide by zero errors, we must have
        // sphere1.center.x != sphere2.center.x and sphere1.center.y != sphere2.center.y
        // Swap spheres until this holds true.
        if (sphereA.center.x != sphereB.center.x && sphereA.center.y != sphereB.center.y)
        {
            sphere1 = sphereA;
            sphere2 = sphereB;
            sphere3 = sphereC;
        }
        else if (sphereA.center.x != sphereC.center.x && sphereA.center.y != sphereC.center.y)
        {
            sphere1 = sphereA;
            sphere2 = sphereC;
            sphere3 = sphereB;
        }
        else if (sphereB.center.x != sphereC.center.x && sphereB.center.y != sphereC.center.y)
        {
            sphere1 = sphereB;
            sphere2 = sphereC;
            sphere3 = sphereA;
        }
        else
        {
            // TODO: The algorithm can be enhanced to work around the problem. 
            return null;
        }
        
        double x1 = sphere1.center.x;
        double x1x1 = x1*x1;
        double x2 = sphere2.center.x;
        double x2x2 = x2*x2;
        double x3 = sphere3.center.x;
        double x3x3 = x3*x3;
        double R1 = sphere1.radius;
        double R1R1 = R1 * R1;
        
        double y1 = sphere1.center.y;
        double y1y1 = y1*y1;
        double y2 = sphere2.center.y;
        double y2y2 = y2*y2;
        double y3 = sphere3.center.y;
        double y3y3 = y3*y3;
        double R2 = sphere2.radius;
        double R2R2 = R2 *R2;
        
        double z1 = sphere1.center.z;
        double z1z1 = z1*z1;
        double z2 = sphere2.center.z;
        double z2z2 = z2*z2;
        double z3 = sphere3.center.z;
        double z3z3 = z3*z3;
        double R3 = sphere3.radius;
        double R3R3 = R3 * R3;
        
        // Starting out we have 3 equations:
        // (1) (x-x1)^2 + (y-y1)^2 + (z-z1)^2 = r1^2
        // (2) (x-x2)^2 + (y-y2)^2 + (z-z2)^2 = r2^2
        // (3) (x-x3)^2 + (y-y3)^2 + (z-z3)^2 = r3^2
        
        
        // Step 1: Subtract the equation for sphere 2 from the equation for sphere 1, giving a linear relation 
        // in x, y, and z. The subtraction eliminates the quadratic powers of x, y, and z. linearEqAConstant 
        // is constant for the given input.
        // (A): (x1-x2)x + (y1-y2)y + (z1-z2)z = linearEqAConstant
        double linearEqAConstant = (R2R2 - R1R1 - x2x2 - y2y2 - z2z2 + x1x1 + y1y1 + z1z1) / 2.0;
        
        // Step 2: Subtract the equation for sphere 3 from the equation for sphere 1, giving a linear relation 
        // in x, y, and z. The subtraction eliminates the quadratic powers of x, y, and z. linearEqBConstant 
        // is constant for the given input.
        // (B): (x1-x3)x + (y1-y3)y + (z1-z3)z = linearEqBConstant
        double linearEqBConstant = (R3R3 - R1R1 - x3x3 - y3y3 - z3z3 + x1x1 + y1y1 + z1z1) / 2.0;
        
        // We now have the following linear system of two equations in terms of x, y, and z:
        // (A): (x1-x2)x + (y1-y2)y + (z1-z2)z - linearEqAConstant = 0
        // (B): (x1-x3)x + (y1-y3)y + (z1-z3)z - linearEqBConstant = 0
        
        // 3. Solve the linear system of two equations for x in terms of z. 
        // Get rid of the y variable by multiplying both sides of (A) by (y1-y3) / (y1-y2)
        // then setting (A) = (B) and solving for x.
        double yFactor = (y1-y3) / (y1-y2);
        double denominatorEqX = (x1-x2) * yFactor - (x1-x3);
        double coefficientEqX = (-(z1-z2) * yFactor + (z1-z3)) / denominatorEqX;
        double constantEqX = (linearEqAConstant * yFactor - linearEqBConstant) / denominatorEqX;
        LinearEquation xLinear = new LinearEquation(coefficientEqX, constantEqX);
        
        // 4. Solve the linear system of two equations for y in terms of z. 
        // Get rid of the x variable by multiplying both sides of (A) by (x1-x3) / (x1-x2)
        // then setting (A) = (B) and solving for x.
        double xFactor = (x1-x3) / (x1-x2);
        double denominatorEqY = (y1-y2) * xFactor - (y1-y3);
        double coefficientEqY = (-(z1-z2) * xFactor + (z1-z3)) / denominatorEqY;
        double constantEqY = (linearEqAConstant * xFactor - linearEqBConstant) / denominatorEqY;
        LinearEquation yLinear = new LinearEquation(coefficientEqY, constantEqY);
        
        // 5. Substitute the equations for x and y from steps 3 and 4 into the equation for the first sphere.
        //    This will give a quadratic equation in terms of z alone.
        double a = 1 + (coefficientEqX*coefficientEqX) + (coefficientEqY*coefficientEqY);
        double b = 2 * (((constantEqX - x1) * coefficientEqX) + ((constantEqY - y1) * coefficientEqY) - z1);
        double c = x1x1 + y1y1 + z1z1 - R1R1 + (constantEqX*constantEqX) - (2*x1*constantEqX) + (constantEqY*constantEqY) - (2*y1*constantEqY);
        QuadraticEquation quadraticEquation = new QuadraticEquation(a, b, c);
        
        // 6. Solve the quadratic equation to get the two z values.
        double z[] = quadraticEquation.solve();
        
        // 7. Compute the values of x by subsituting the values of z into the equation found in step 3. 
        // 8. Compute the values of y by subsituting the values of z into the equation found in step 4. 
        Point3D[] intersection = new Point3D[z.length];
        for (int i = 0; i < z.length; i++)
        {
            intersection[i] = new Point3D(xLinear.solve(z[i]), yLinear.solve(z[i]), z[i]);
        }
        
        return intersection;
    }

    /**
     * Returns the 3D point of intersection of four spheres.
     * 
     * @param sphere1
     * @param sphere2
     * @param sphere3
     * @param sphere4
     * @param epsilon
     * @return
     */
    public static Point3D getIntersection(Sphere sphere1, Sphere sphere2, Sphere sphere3, Sphere sphere4, double epsilon)
    {
        Point3D[] intersection = getIntersection(sphere1, sphere2, sphere3, epsilon);
        
        for (int i = 0; i < intersection.length; i++)
        {
            if (sphere4.hasPointOnSurface(intersection[i], epsilon))
                return intersection[i];
        }
        
        return null;
        
    }
}


// TODO: All of the equals operators need epsilon built in.

// Two spheres can intersect 
// * in a sphere (they are the same sphere) 
// * in a circle
// * in a point
// * not at all
//public XX getIntersection(Sphere other)
//{
//    // Point3D point1, double radius1, Point3D point2, double radius2
//    double R1 = this.radius;
//    double R2 = other.radius;
//    
//    double d = this.center.distance(other.center);
//    
//    // Check if spheres don't intersect at all.
//    if (d > (R1 + R2))
//        return null;
//    
//    // Check to see if one sphere is contained within the other.
//    if (d < Math.abs(R1 - R2))
//        return null;
//
//    // If the spheres are the same, then the intersection is the sphere.
//    if (this.equals(other))
//        return new Sphere(other);
//    
//    // If the spheres have the same center but different radii, then they do not intersect.
//    if (this.center == other.center)
//        return null;
//    
//    
//    
//    return null;
//}
