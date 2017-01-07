package fpsscene.gl.primitives;

public class Point2f {
	float[] p = new float[2];

	public Point2f() {
		p[0] = p[1] = 0.0f;
	}

	public Point2f(float x,float y) {
		p[0] = x;
		p[1] = y;
	}
	
	public void setX(float v){
		p[0] = v;
	}

	public void setY(float v){
		p[1] = v;
	}

	public float getX(){
		return p[0];
	}

	public float getY(){
		return p[1];
	}

	public float[] asArray(){
		return p;
	}
	
	@Override
	public String toString(){
		return "("+p[0]+","+p[1]+")";
	}

	public void set(float x, float y) {
		p[0] = x;
		p[1] = y;
	}
	
}
