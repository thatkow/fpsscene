package fpsscene.fpsscene;

import java.util.ArrayList;
import java.util.List;

import fpsscene.gl.primitives.ColoredTriangle;

public class AxisTrianges {
	int total;

	public AxisTrianges(int total) {
		super();
		this.total = total;
	}
	public List<ColoredTriangle> createAxisTriangles() {
		List<ColoredTriangle> triangles  = new ArrayList<>();

		/*
		 * Create arrows
		 */
		// Positive z
		for (int j = 1; j < (total + 1); j++) {
			triangles.add(new ColoredTriangle(
					0.0f, 0.0f, (float) j + 1.0f,
					-0.5f, 0.0f, (float) j,
					0.5f, 0.0f, (float) j ,

					0.0f, 0.0f, 1.0f,
					1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f
					));
		}

		// Neg z
		for (int j = 1; j < (total + 1); j++) {
			triangles.add(new ColoredTriangle(
					0.0f, 0.0f, -((float) j + 1.0f),
					-0.5f, 0.0f, -(float) j,
					0.5f, 0.0f, -(float) j,

					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 0.0f,
					0.0f, 0.0f, 0.0f
					));
		}

		// Pos y
		for (int j = 1; j < (total + 1); j++) {
			triangles.add(new ColoredTriangle(
					0.0f, (float) j + 1.0f, 0.0f,
					-0.5f, (float) j, 0.0f,
					0.5f, (float) j, 0.0f,

					0.0f, 1.0f, 0.0f,
					1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f  ));
		}

		// neg y
		for (int j = 1; j < (total + 1); j++) {
			triangles.add(new ColoredTriangle(
					0.0f, -((float) j + 1.0f), 0.0f,
					-0.5f, -(float) j, 0.0f,
					0.5f, -(float) j, 0.0f,

					0.0f, 1.0f, 0.0f,
					0.0f, 0.0f, 0.0f,
					0.0f, 0.0f, 0.0f  ));
		}

		// pos x
		for (int j = 1; j < (total + 1); j++) {
			triangles.add(new ColoredTriangle(
					(float) j + 1.0f, 0.0f, 0.0f,
					(float) j, 0.0f, -0.5f,
					(float) j, 0.0f, 0.5f,

					1.0f, 0.0f, 0.0f,
					1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f  ));
		}

		// neg x
		for (int j = 1; j < (total + 1); j++) {
			triangles.add(new ColoredTriangle(
					-((float) j + 1.0f), 0.0f, 0.0f,
					-(float) j, 0.0f, -0.5f,
					-(float) j, 0.0f, 0.5f,

					1.0f, 0.0f, 0.0f,
					0.0f, 0.0f, 0.0f,
					0.0f, 0.0f, 0.0f  ));
		}
		return triangles;
	}
}
