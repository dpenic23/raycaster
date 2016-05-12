package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Scene;

/**
 * {@code ParallelRayTracerProducer} extends {@code AbstractRayTracerProducer}
 * and provides concrete implementation of abstract method defined by its
 * super-class. This producer calculates color intensities parallel, dividing
 * screen height on certain number of parts.
 * 
 * <p>
 * This class is capable to create scene snapshots by using ray-tracing
 * technique and offers method for producing such scenes.
 * </p>
 * 
 * @author Domagoj
 *
 */
public class ParallelRayTracerProducer extends AbstractRayTracerProducer {

	/**
	 * Height limit used for determining when ray-tracer jobs will start
	 * computing actual data, instead of dividing jobs on other sub-problems.
	 */
	private static final int HEIGHT_LIMIT = 10;

	/**
	 * {@code ForkJoinPool} used for parallel color intensities calculation.
	 */
	ForkJoinPool pool;

	/**
	 * Creates new {@code ParallelRayTracerProducer} with initial
	 * {@code ForkJoinPool} used for parallel calculation. Number of created
	 * threads is equal to number of processors.
	 */
	public ParallelRayTracerProducer() {
		pool = new ForkJoinPool();
	}

	@Override
	protected void calculateRGB(short[] red, short[] green, short[] blue,
			Point3D eye, Point3D xAxis, Point3D yAxis, Point3D screenCorner,
			Scene scene, double horizontal, double vertical, int width,
			int height) {
		/**
		 * {@code RayTracerJob} extends {@code RecursiveAction} and defines
		 * single job which has to be done by some thread in order to calculate
		 * certain color intensities.
		 * 
		 * @author Domagoj
		 *
		 */
		class RayTracerJob extends RecursiveAction {
			/** Default serial version ID. */
			private static final long serialVersionUID = 1L;
			/** Start index from which colors are calculated. */
			int yMin;
			/** Ending index to which colors are calculated. */
			int yMax;

			/**
			 * Creates new {@code RayTracerJob} with specified parameters.
			 * 
			 * @param yMin
			 *            Starting height index
			 * @param yMax
			 *            Ending height index
			 */
			public RayTracerJob(int yMin, int yMax) {
				this.yMin = yMin;
				this.yMax = yMax;
			}

			@Override
			protected void compute() {
				if (yMax - yMin > HEIGHT_LIMIT) {
					int yMid = (yMax + yMin) / 2;
					RayTracerJob task1 = new RayTracerJob(yMin, yMid);
					RayTracerJob task2 = new RayTracerJob(yMid, yMax);

					invokeAll(task1, task2);
				} else {
					calculateRGBFromTo(red, green, blue, eye, xAxis, yAxis,
							screenCorner, scene, horizontal, vertical, width,
							height, yMin, yMax);
				}
			}

		}

		RayTracerJob task = new RayTracerJob(0, height);
		pool.invoke(task);
	}

}
