package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * {@code AbstractRayTracerProducer} implements {@code IRayTracerProducer}
 * interface and specifies object which are capable to create scene snapshots by
 * using ray-tracing technique. This abstract class offers implemented method
 * for producing such scenes, other objects extending this class has to
 * implement only {@code calculateRGB(parameters)} method used for color
 * calculations.
 * 
 * @author Domagoj
 *
 */
public abstract class AbstractRayTracerProducer implements IRayTracerProducer {

	@Override
	public void produce(Point3D eye, Point3D view, Point3D viewUp,
			double horizontal, double vertical, int width, int height,
			long requestNo, IRayTracerResultObserver observer) {

		System.out.println("Zapocinjem izracune...");

		short[] red = new short[width * height];
		short[] green = new short[width * height];
		short[] blue = new short[width * height];

		// normalized vector from eye position to view point
		Point3D og = view.sub(eye).normalize();
		// normalized view-up vector
		Point3D vuv = viewUp.normalize();

		Point3D yAxis = vuv.sub(og.scalarMultiply(og.scalarProduct(vuv)))
				.normalize();
		Point3D xAxis = og.vectorProduct(yAxis).normalize();

		Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2.0))
				.add(yAxis.scalarMultiply(vertical / 2.0));

		Scene scene = RayTracerViewer.createPredefinedScene();

		calculateRGB(red, green, blue, eye, xAxis, yAxis, screenCorner, scene,
				horizontal, vertical, width, height);

		System.out.println("Izračuni gotovi...");
		observer.acceptResult(red, green, blue, requestNo);
		System.out.println("Dojava gotova...");

	}

	/**
	 * Method which accepts arrays which represents intensity of red, blue and
	 * green color and calculates values for every pixel defined with other
	 * parameters.
	 * 
	 * @param red
	 *            Array of red intensities
	 * @param green
	 *            Array of green intensities
	 * @param blue
	 *            Array of blue intensities
	 * @param eye
	 *            Eye position vector
	 * @param xAxis
	 *            X axis vector
	 * @param yAxis
	 *            Y axis vector
	 * @param screenCorner
	 *            Screen corner defined by user view
	 * @param scene
	 *            Predefined scene
	 * @param horizontal
	 *            Horizontal width of observed space
	 * @param vertical
	 *            Vertical height of observed space
	 * @param width
	 *            Screen width
	 * @param height
	 *            Screen height
	 */
	protected abstract void calculateRGB(short[] red, short[] green,
			short[] blue, Point3D eye, Point3D xAxis, Point3D yAxis,
			Point3D screenCorner, Scene scene, double horizontal,
			double vertical, int width, int height);

	/**
	 * Method which accepts arrays which represents intensity of red, blue and
	 * green color and calculates values for every pixel defined with other
	 * parameters. This method fills arrays only from screen height specified
	 * with y-minimal, to height specified as y-maximal.
	 * 
	 * @param red
	 *            Array of red intensities
	 * @param green
	 *            Array of green intensities
	 * @param blue
	 *            Array of blue intensities
	 * @param eye
	 *            Eye position vector
	 * @param xAxis
	 *            X axis vector
	 * @param yAxis
	 *            Y axis vector
	 * @param screenCorner
	 *            Screen corner defined by user view
	 * @param scene
	 *            Predefined scene
	 * @param horizontal
	 *            Horizontal width of observed space
	 * @param vertical
	 *            Vertical height of observed space
	 * @param width
	 *            Screen width
	 * @param height
	 *            Screen height
	 * @param yMin
	 *            Start height index for calculation, inclusively
	 * @param yMax
	 *            End height index for calculation, exclusively
	 */
	protected void calculateRGBFromTo(short[] red, short[] green, short[] blue,
			Point3D eye, Point3D xAxis, Point3D yAxis, Point3D screenCorner,
			Scene scene, double horizontal, double vertical, int width,
			int height, int yMin, int yMax) {
		short[] rgb = new short[3];
		int offset = yMin * width;

		for (int y = yMin; y < yMax; y++) {
			for (int x = 0; x < width; x++) {
				Point3D xComponent = xAxis.scalarMultiply(x * horizontal
						/ (width - 1));
				Point3D yComponent = yAxis.scalarMultiply(y * vertical
						/ (height - 1));
				Point3D screenPoint = screenCorner.add(xComponent).sub(
						yComponent);

				Ray ray = Ray.fromPoints(eye, screenPoint);

				RayTracer.tracer(scene, ray, rgb);

				red[offset] = rgb[0] > 255 ? 255 : rgb[0];
				green[offset] = rgb[1] > 255 ? 255 : rgb[1];
				blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

				offset++;
			}
		}
	}

}
