package fpsscene.gl.primitives;

public class Point3f {
	float[] p = new float[3];

	public Point3f() {
		p[0] = p[1] = p[2] = 0.0f;
	}

	public Point3f(float x,float y,float z) {
		p[0] = x;
		p[1] = y;
		p[2] = z;
	}

	public void setX(float v){
		p[0] = v;
	}

	public void setY(float v){
		p[1] = v;
	}

	public void setZ(float v){
		p[2] = v;
	}

	public float getX(){
		return p[0];
	}

	public float getY(){
		return p[1];
	}

	public float getZ(){
		return p[2];
	}

	public float[] asArray(){
		return p;
	}

	public void set(float x, float y, float z) {
		p[0] = x;
		p[1] = y;
		p[2] = z;
	}

	
	@Override
	public String toString(){
		return "{"+p[0]+","+p[1]+","+p[2]+"}";
	}
}
