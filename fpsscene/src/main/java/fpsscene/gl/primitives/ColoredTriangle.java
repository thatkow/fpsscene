package fpsscene.gl.primitives;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ColoredTriangle {
	float[] verticies = new float[9];
	float[] color = new float[9];
	public ColoredTriangle(
			float v0X,float v0Y,float v0Z,
			float v1X,float v1Y,float v1Z,
			float v2X,float v2Y,float v2Z,

			float c0X,float c0Y,float c0Z,
			float c1X,float c1Y,float c1Z,
			float c2X,float c2Y,float c2Z			
			) {
		verticies[0] = v0X;
		verticies[1] = v0Y;
		verticies[2] = v0Z;
		verticies[3] = v1X;
		verticies[4] = v1Y;
		verticies[5] = v1Z;
		verticies[6] = v2X;
		verticies[7] = v2Y;
		verticies[8] = v2Z;
		color[0] = c0X;          
		color[1] = c0Y;          
		color[2] = c0Z;          
		color[3] = c1X;          
		color[4] = c1Y;          
		color[5] = c1Z;          
		color[6] = c2X;          
		color[7] = c2Y;          
		color[8] = c2Z;          
	}
	public float[] getVerticies() {
		return verticies;
	}
	public void setVerticies(float[] verticies) {
		this.verticies = verticies;
	}
	public float[] getColor() {
		return color;
	}
	public void setColor(float[] color) {
		this.color = color;
	}


	public static float[] verticesToArray(List<ColoredTriangle> triangles){
		float[] vertices = new float[triangles.size() * 9];
		IntStream.range(0,triangles.size()).forEach(i -> {
			IntStream.range(0,9).forEach(j -> {
				vertices[i*9+j] = triangles.get(i).getVerticies()[j];
			});
		});
		
//		List<Float> vertices = Arrays.asList(triangles.stream().map(e -> e.getVerticies()).toArray(size -> new Float[size]));
//		return vertices.stream().mapToDouble(i->i).toArray();
		return vertices;
	}
	public static float[] colorsToArray(List<ColoredTriangle> triangles) {
		float[] colors = new float[triangles.size() * 9];
		IntStream.range(0,triangles.size()).forEach(i -> {
			IntStream.range(0,9).forEach(j -> {
				colors[i*9+j] = triangles.get(i).getColor()[j];
			});
		});
		
//		List<Float> vertices = Arrays.asList(triangles.stream().map(e -> e.getVerticies()).toArray(size -> new Float[size]));
//		return vertices.stream().mapToDouble(i->i).toArray();
		return colors;
	}
	
	@Override
	public String toString(){
		return String.join("\n", 
				"verticies: ("+verticies[0]+","+verticies[1]+","+verticies[2]+"),\tcolor("+color[0]+","+color[1]+","+color[2]+")",
				"           ("+verticies[3]+","+verticies[4]+","+verticies[5]+"),\t     ("+color[3]+","+color[4]+","+color[5]+")",
				"           ("+verticies[6]+","+verticies[7]+","+verticies[8]+"),\t     ("+color[6]+","+color[7]+","+color[8]+")\n"
				);
	}
}
