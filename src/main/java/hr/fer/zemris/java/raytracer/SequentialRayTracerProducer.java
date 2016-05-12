package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Scene;

/**
 * {@code SequentialRayTracerProducer} extends {@code AbstractRayTracerProducer}
 * and provides concrete implementation of abstract method defined by its
 * super-class. This producer calculates color intensities sequentially,
 * starting from height index 0 to height - 1.
 * 
 * <p>
 * This class is capable to create scene snapshots by using ray-tracing
 * technique and offers method for producing such scenes.
 * </p>
 * 
 * @author Domagoj
 *
 */
public class SequentialRayTracerProducer extends AbstractRayTracerProducer {

	@Override
	protected void calculateRGB(short[] red, short[] green, short[] blue,
			Point3D eye, Point3D xAxis, Point3D yAxis, Point3D screenCorner,
			Scene scene, double horizontal, double vertical, int width,
			int height) {
		calculateRGBFromTo(red, green, blue, eye, xAxis, yAxis, screenCorner,
				scene, horizontal, vertical, width, height, 0, height);
	}

}
