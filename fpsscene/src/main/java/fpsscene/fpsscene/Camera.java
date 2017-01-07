package fpsscene.fpsscene;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import fpsscene.adapters.ApplyXY;
import fpsscene.adapters.BasicMovement;

public class Camera implements GLEventListener {
	private static final float multiplier = 0.01f;
	protected static float DROT_FULL = 360.0f;
	protected static float DROT_QUART = DROT_FULL/4.0f;
	protected static float DEFAULT_SENS_ROT =0.3f;
	protected static float ONE_DEG_IN_RAD = (2.0f * (float)Math.PI) / DROT_FULL;
	
	final ApplyXY applyxy;
	final BasicMovement basicMovement;
	
	public Camera(ApplyXY applyxy, BasicMovement basicMovement) {
		super();
		this.applyxy = applyxy;
		this.basicMovement = basicMovement;
	}

	public BasicMovement getBasicMovement() {
		return basicMovement;
	}

	public ApplyXY getApplyxy() {
		return applyxy;
	}
	
	public int getPrevMouseX() {
		return prevMouseX;
	}
	public void setPrevMouseX(int prevMouseX) {
		this.prevMouseX = prevMouseX;
	}
	public float getView_rotx() {
		return view_rotx;
	}
	public void setView_rotx(float view_rotx) {
		this.view_rotx = view_rotx;
	}
	public float getView_roty() {
		return view_roty;
	}
	public void setView_roty(float view_roty) {
		this.view_roty = view_roty;
	}
	public float getView_rotz() {
		return view_rotz;
	}
	public int getPrevMouseY() {
		return prevMouseY;
	}
	public void setPrevMouseY(int prevMouseY) {
		this.prevMouseY = prevMouseY;
	}
	float view_rotx = 20.0f;
	float view_roty = 30.0f;
	private final float view_rotz = 0.0f;
	int prevMouseX;
	int prevMouseY;
	
    float sens_rot;

	@Override
	public void init(GLAutoDrawable drawable) {
	
	}
	@Override
	public void dispose(GLAutoDrawable drawable) {
		
	}
	@Override
	public void display(GLAutoDrawable drawable) {

	}
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

	}

	public void applyMovement(float f, float g, float h) {
		this.basicMovement.translate(multiplier*f, multiplier*g, multiplier*h);
	}


}
