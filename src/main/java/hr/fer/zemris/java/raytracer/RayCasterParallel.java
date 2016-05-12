package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * {@code RayCasterParallel} is command line application which shows a
 * predefined scene with spheres modeled by {@code Sphere} class and multiply
 * light sources modeled by {@code LightSource} class. This program is using
 * {@code ParallelRayTracerProducer} which calculates pixel intensities
 * parallel, number of threads created is equal to number of processors. Result
 * is shown on screen after calculation is done.
 * 
 * @author Domagoj
 *
 */
public class RayCasterParallel {

	/**
	 * Method called once program is run.Arguments are described below.
	 * 
	 * @param args
	 *            Command line arguments, not used
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(new ParallelRayTracerProducer(), new Point3D(10,
				0, 0), new Point3D(0, 0, 0), new Point3D(0, 0, 10), 20, 20);
	}

}
