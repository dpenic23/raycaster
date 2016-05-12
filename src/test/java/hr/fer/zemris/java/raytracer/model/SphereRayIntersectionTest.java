package hr.fer.zemris.java.raytracer.model;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * {@code SphereRayIntersectionTest} is testing class for {@code Sphere} method
 * which calculates {@code RayIntersection} between that sphere and {@code Ray}
 * provided as argument. It tests some simple intersections and cases where
 * there is no intersection.
 * 
 * @author Domagoj
 *
 */
public class SphereRayIntersectionTest {

	@Test
	public void testSphereRayIntersectionVol1() {
		Sphere sphere = createSphere(new Point3D(0, 0, 0), 10);
		Ray ray = new Ray(new Point3D(20, 0, 0), new Point3D(-1, 0, 0));

		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertTrue(pointEqual(intersection.getPoint(), new Point3D(10, 0, 0)));
		assertTrue(intersection.isOuter());
	}

	@Test
	public void testSphereRayIntersectionVol2() {
		Sphere sphere = createSphere(new Point3D(0, 3, 0), 10);
		Ray ray = new Ray(new Point3D(0, -5, 10), new Point3D(0, 1, 0));

		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertTrue(pointEqual(intersection.getPoint(), new Point3D(0, 3, 10)));
		assertTrue(intersection.isOuter());
	}

	@Test
	public void testSphereRayIntersectionVol3() {
		Sphere sphere = createSphere(new Point3D(5, 5, 0), 2);
		Ray ray = new Ray(new Point3D(5, 5, 0), new Point3D(0, 0, 1));

		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertTrue(pointEqual(intersection.getPoint(), new Point3D(5, 5, 2)));
		assertFalse(intersection.isOuter());
	}

	@Test
	public void testSphereRayIntersectionVol4() {
		Sphere sphere = createSphere(new Point3D(5, 5, 0), 2);
		Ray ray = new Ray(new Point3D(0, 0, 0), new Point3D(0, 0, 1));

		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertTrue(intersection == null);
	}

	@Test
	public void testSphereRayIntersectionVol5() {
		Sphere sphere = createSphere(new Point3D(5, 5, 5), 2);
		Ray ray = Ray.fromPoints(new Point3D(0, 0, 0), new Point3D(1, 1, 1));

		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertTrue(pointEqual(intersection.getPoint(), new Point3D(3.845299462,
				3.845299462, 3.845299462)));
		assertTrue(intersection.isOuter());
	}

	@Test
	public void testSphereRayIntersectionVol6() {
		Sphere sphere = createSphere(new Point3D(0, 3, 0), 3.5);
		Ray ray = new Ray(new Point3D(0, -5, 3.5), new Point3D(0, 1, 0));

		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
		assertTrue(pointEqual(intersection.getPoint(), new Point3D(0, 3, 3.5)));
		assertTrue(intersection.isOuter());
	}

	@Test
	public void checkSphereDefaultCoefficients() {
		Sphere sphere = createSphere(new Point3D(0, 3, 0), 3.5);
		Ray ray = new Ray(new Point3D(0, -5, 3.5), new Point3D(0, 1, 0));

		RayIntersection intersection = sphere.findClosestRayIntersection(ray);

		assertTrue(Math.abs(intersection.getKdr()) < Sphere.DISTANCE);
		assertTrue(Math.abs(intersection.getKdg()) < Sphere.DISTANCE);
		assertTrue(Math.abs(intersection.getKdb()) < Sphere.DISTANCE);
		assertTrue(Math.abs(intersection.getKrr()) < Sphere.DISTANCE);
		assertTrue(Math.abs(intersection.getKrg()) < Sphere.DISTANCE);
		assertTrue(Math.abs(intersection.getKrb()) < Sphere.DISTANCE);
		assertTrue(Math.abs(intersection.getKrn()) < Sphere.DISTANCE);
	}

	@Test
	public void checkSphereRayIntersectionNormal() {
		Sphere sphere = createSphere(new Point3D(0, 0, 0), 1);
		Ray ray = new Ray(new Point3D(0, -5, 0), new Point3D(0, 1, 0));

		RayIntersection intersection = sphere.findClosestRayIntersection(ray);

		assertTrue(pointEqual(intersection.getNormal(), new Point3D(0, -1, 0)));
	}

	/**
	 * Creates {@code Sphere} with specified center {@code Point3D} and radius.
	 * {@code Sphere} coefficients are set as default one to value 0.
	 * 
	 * @param center
	 *            {@code Sphere} center point
	 * @param radius
	 *            {@code Sphere} radius
	 * @return New {@code Sphere} with specified parameters and initial
	 *         coefficients
	 */
	private static Sphere createSphere(Point3D center, double radius) {
		return new Sphere(center, radius, 0, 0, 0, 0, 0, 0, 0);
	}

	/**
	 * Determines if two points are equal within 3D coordinate system. Method
	 * compares values on all coordinate axis.
	 * 
	 * @param point1
	 *            First point
	 * @param point2
	 *            Second point
	 * @return {@code True} if points are equal, {@code false} otherwise
	 */
	private static boolean pointEqual(Point3D point1, Point3D point2) {
		return doubleEqual(point1.x, point2.x, Sphere.DISTANCE)
				&& doubleEqual(point1.y, point2.y, Sphere.DISTANCE)
				&& doubleEqual(point1.z, point2.z, Sphere.DISTANCE);
	}

	/**
	 * Compares if two {@code double} values are equal within specified
	 * distance.
	 * 
	 * @param arg1
	 *            First {@code double} value
	 * @param arg2
	 *            Second {@code double} value
	 * @param distance
	 *            Distance used in comparison
	 * @return {@code True} if values are equal, {@code false} otherwise
	 */
	private static boolean doubleEqual(double arg1, double arg2, double distance) {
		return Math.abs(arg1 - arg2) < distance;
	}

}
