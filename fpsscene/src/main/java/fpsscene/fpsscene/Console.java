package fpsscene.fpsscene;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Robot;

import javax.validation.constraints.NotNull;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;

import fpsscene.adapters.BasicKeyboardAdapter;
import fpsscene.adapters.BasicMouseAdapter;

public class Console implements GLEventListener {

	@NotNull
	final Camera camera;
	@NotNull
	final Scene scene;
	@NotNull
	final Component component;
	@NotNull
	final Robot robot;

	final BasicMouseAdapter gearsMouse;
	final BasicKeyboardAdapter gearsKeys;

	public Console(Camera camera, Scene scene, Component component, Robot robot) {
		super();
		this.camera = camera;
		this.scene = scene;
		this.component = component;
		this.robot = robot;
		this.gearsMouse  = new BasicMouseAdapter(camera, component, robot);
		this.gearsKeys = new  BasicKeyboardAdapter(camera);
	}

	public BasicMouseAdapter getGearsMouse() {
		return gearsMouse;
	}

	public BasicKeyboardAdapter getGearsKeys() {
		return gearsKeys;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		camera.init(drawable);
		scene.init(drawable);

		component.addMouseMotionListener(gearsMouse);
		component.addMouseListener(gearsMouse);
		component.addMouseWheelListener(gearsMouse);
		component.addKeyListener(gearsKeys);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		camera.dispose(drawable);
		scene.dispose(drawable);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Special handling for the case where the GLJPanel is translucent
		// and wants to be composited with other Java 2D content
		if (GLProfile.isAWTAvailable() &&
				(drawable instanceof com.jogamp.opengl.awt.GLJPanel) &&
				!((com.jogamp.opengl.awt.GLJPanel) drawable).isOpaque() &&
				((com.jogamp.opengl.awt.GLJPanel) drawable).shouldPreserveColorBufferIfTranslucent()) {
			gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
		} else {
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		}
		camera.display(drawable);
		scene.display(drawable);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		camera.reshape(drawable, x, y, width, height);
		scene.reshape(drawable, x, y, width, height);
	}

}
