package hr.fer.zemris.java.raytracer;

import java.util.List;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;

/**
 * {@code RayTracer} class offers public method
 * {@link #tracer(Scene, Ray, short[])} which traces specified {@code Ray} and
 * sets color intensities on calculated values.
 * 
 * @see Scene
 * @see Ray
 * 
 * @author Domagoj
 *
 */
public class RayTracer {

	/**
	 * Default ambient color used for ambient light component.
	 */
	private static final int AMBIENT = 15;

	/**
	 * Traces specified {@code Ray} given as argument and searches for
	 * intersections with objects which are defined in specified {@code Scene}.
	 * Resulting color intensities are stored in given array as {@code short}
	 * numbers.
	 * 
	 * @param scene
	 *            {@code Scene} instance containing graphical objects and light
	 *            sources
	 * @param ray
	 *            {@code Ray} instance which is traced by this method inside
	 *            scene space
	 * @param rgb
	 *            Array of values representing color intensities
	 */
	public static void tracer(Scene scene, Ray ray, short[] rgb) {
		setRGB(rgb, 0, 0, 0);

		RayIntersection intersection = getClosestIntersection(scene.getObjects(), ray);

		if (intersection == null) {
			// there is no intersection, color is black
			setRGB(rgb, 0, 0, 0);
		} else {
			determineColorForIntersection(scene, intersection, rgb, ray);
		}
	}

	/**
	 * Returns closes intersection with specified {@code Ray} with any graphical
	 * object contained by list given as argument. If there is no intersection,
	 * {@code null} value is returned.
	 * 
	 * @param objects
	 *            Graphical objects positioned in scene
	 * @param ray
	 *            Ray which is being traced
	 * @return {@code RayIntersection} with closest object, {@code null} if it
	 *         does not exist
	 */
	private static RayIntersection getClosestIntersection(List<GraphicalObject> objects, Ray ray) {
		RayIntersection closest = null;

		for (GraphicalObject object : objects) {
			RayIntersection current = object.findClosestRayIntersection(ray);

			if (current == null) {
				// there is no intersection
				continue;
			}

			if (closest == null) {
				closest = current;
			} else {
				if (current.getDistance() < closest.getDistance()) {
					closest = current;
				}
			}
		}

		return closest;
	}

	/**
	 * Determines color for specified {@code RayIntersection} and as a result
	 * fills specified array with color intensity components. If intersection is
	 * obscured by any other object in specified {@code Scene}, this pixel will
	 * be painted in ambient color.
	 * 
	 * @param scene
	 *            {@code Scene} with positioned graphical objects and light
	 *            sources
	 * @param intersection
	 *            {@code RayIntersection} with eye view and current object
	 * @param rgb
	 *            Array containing values representing color intensities
	 * @param ray
	 *            {@code Ray} with start in eye point and end in intersection
	 *            point
	 */
	private static void determineColorForIntersection(Scene scene, RayIntersection intersection, short[] rgb, Ray ray) {
		setRGB(rgb, AMBIENT, AMBIENT, AMBIENT);

		// determine intensity for every light source in scene
		for (LightSource source : scene.getLights()) {
			Ray fromSource = Ray.fromPoints(source.getPoint(), intersection.getPoint());

			// check if intersection is obscured by another object
			RayIntersection currentIntersection = getClosestIntersection(scene.getObjects(), fromSource);

			if (currentIntersection != null) {
				double dist1 = source.getPoint().sub(currentIntersection.getPoint()).norm();
				double dist2 = source.getPoint().sub(intersection.getPoint()).norm();
				if (dist1 + 0.1 < dist2) {
					continue;
				}
			}

			Point3D intersectionToSource = source.getPoint().sub(intersection.getPoint()).normalize();
			Point3D normal = intersection.getNormal();
			Point3D reflective = normal.scalarMultiply(intersectionToSource.scalarProduct(normal) * 2)
					.sub(intersectionToSource);
			Point3D intersectionToEye = ray.start.sub(intersection.getPoint()).normalize();

			double scalarLN = Math.max(intersectionToSource.scalarProduct(normal), 0);
			double scalarRVtoN = Math.pow(Math.max(reflective.scalarProduct(intersectionToEye), 0),
					intersection.getKrn());

			double red = rgb[0]
					+ (source.getR() * (intersection.getKdr() * scalarLN + intersection.getKrr() * scalarRVtoN));
			double green = rgb[1]
					+ (source.getG() * (intersection.getKdg() * scalarLN + intersection.getKrg() * scalarRVtoN));
			double blue = rgb[2]
					+ (source.getB() * (intersection.getKdb() * scalarLN + intersection.getKrb() * scalarRVtoN));

			setRGB(rgb, (int) red, (int) green, (int) blue);
		}

	}

	/**
	 * Set RGB components on specified values.
	 * 
	 * @param rgb
	 *            Array representing color intensities
	 * @param r
	 *            Red component
	 * @param g
	 *            Green component
	 * @param b
	 *            Blue component
	 */
	private static void setRGB(short[] rgb, int r, int g, int b) {
		rgb[0] = (short) r;
		rgb[1] = (short) g;
		rgb[2] = (short) b;
	}

}
