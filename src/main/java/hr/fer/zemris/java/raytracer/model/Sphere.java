package hr.fer.zemris.java.raytracer.model;

/**
 * Class {@code Sphere} models sphere in 3D coordinate system. Sphere is
 * characterized with its center {@code Point3D} and its radius. Sphere surface
 * is characterized by reflective and diffuse components represented by multiply
 * coefficients.
 * 
 * This class extends {@code GraphicalObject} and implements its abstract method
 * for finding intersection with {@code Ray}. Algorithm used for finding
 * intersection can be looked at this link:
 * 
 * <pre>
 * http://en.wikipedia.org/wiki/Line%E2%80%93sphere_intersection
 * </pre>
 * 
 * 
 * 
 * @see Point3D
 * 
 * @author Domagoj
 *
 */
public class Sphere extends GraphicalObject {

	/** Distance used for comparing {@code double} values. */
	public static final double DISTANCE = 1E-6;

	/** Center point of sphere in 3D coordinate system. */
	private Point3D center;

	/** Sphere radius. */
	private double radius;

	/** Coefficient for diffuse component for red color. */
	private double kdr;
	/** Coefficient for diffuse component for green color. */
	private double kdg;
	/** Coefficient for diffuse component for blue color. */
	private double kdb;
	/** Coefficient for reflective component for red color. */
	private double krr;
	/** Coefficient for reflective component for green color. */
	private double krg;
	/** Coefficient for reflective component for blue color. */
	private double krb;
	/** Coefficient n for reflective component. */
	private double krn;

	/**
	 * Creates new {@code Sphere} in 3D coordinate system with specified center
	 * point, radius and coefficients for reflective and diffuse light
	 * components.
	 * 
	 * @param center
	 *            Sphere center point
	 * @param radius
	 *            Sphere radius
	 * @param kdr
	 *            Coefficient for diffuse component for red color
	 * @param kdg
	 *            Coefficient for diffuse component for green color
	 * @param kdb
	 *            Coefficient for diffuse component for blue color
	 * @param krr
	 *            Coefficient for reflective component for red color
	 * @param krg
	 *            Coefficient for reflective component for green color
	 * @param krb
	 *            Coefficient for reflective component for blue color
	 * @param krn
	 *            Coefficient n for reflective component
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg,
			double kdb, double krr, double krg, double krb, double krn) {
		this.center = center.copy();
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {

		// algorithm for finding description is described in class documentation
		double underRoot = Math.pow(
				ray.start.sub(center).scalarProduct(ray.direction), 2)
				- Math.pow(ray.start.sub(center).norm(), 2)
				+ Math.pow(radius, 2);

		if (underRoot < 0 && Math.abs(underRoot) > DISTANCE) {
			return null;
		}

		double root = Math.sqrt(underRoot);
		double firstArg = -(ray.direction.scalarProduct(ray.start.sub(center)));

		double dist1 = firstArg - root;
		double dist2 = firstArg + root;

		if (dist1 < 0 && dist2 < 0) {
			return null;
		}

		double distance = determineDistance(dist1, dist2);

		Point3D intersection = ray.start.add(ray.direction
				.scalarMultiply(distance));

		return new RayIntersection(intersection, distance, isOuter(ray)) {

			@Override
			public Point3D getNormal() {
				return intersection.sub(center).normalize();
			}

			@Override
			public double getKrr() {
				return krr;
			}

			@Override
			public double getKrn() {
				return krn;
			}

			@Override
			public double getKrg() {
				return krg;
			}

			@Override
			public double getKrb() {
				return krb;
			}

			@Override
			public double getKdr() {
				return kdr;
			}

			@Override
			public double getKdg() {
				return kdg;
			}

			@Override
			public double getKdb() {
				return kdb;
			}
		};
	}

	/**
	 * Accepts two {@code double} distances and determines which one is smaller.
	 * Method returns minimal positive value.
	 * 
	 * @param dist1
	 *            First distance
	 * @param dist2
	 *            Second distance
	 * @return Minimal positive value
	 */
	private double determineDistance(double dist1, double dist2) {
		double distance;

		if (dist1 > 0 && dist2 < 0) {
			distance = dist1;
		} else if (dist1 < 0 && dist2 > 0) {
			distance = dist2;
		} else {
			distance = Math.min(dist1, dist2);
		}

		return distance;
	}

	/**
	 * Determines if intersection is outer or not. It is determined based on
	 * sphere equation in 3D coordinate system and {@code Ray} is presented as
	 * simple line.
	 * 
	 * @param ray
	 *            {@code Ray} to be provided which intersects this
	 *            {@code Sphere}
	 * @return {@code True} if intersection is outer, {@code false} otherwise
	 */
	private boolean isOuter(Ray ray) {
		if (Math.pow(ray.start.sub(center).norm(), 2) >= Math.pow(radius, 2)) {
			return true;
		} else {
			return false;
		}
	}

}
