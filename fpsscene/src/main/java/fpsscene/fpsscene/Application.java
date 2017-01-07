package fpsscene.fpsscene;

import java.awt.AWTException;
import java.awt.Robot;

import javax.swing.JFrame;
import javax.validation.constraints.NotNull;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import fpsscene.adapters.BasicMouseAdapter;

public class Application implements GLEventListener {

	@NotNull
	private Scene scene;

	public Application(Scene scene) {
		super();
		this.scene = scene;
	}


	public static void main(String[] args) throws AWTException {

		GLProfile glp = GLProfile.get(GLProfile.GL3);
		final GLCapabilitiesImmutable glcaps = (GLCapabilitiesImmutable) new GLCapabilities(glp);
		
		if(!glp.isGL3()){
			System.err.println("GL3 not supported on this system");
			System.exit(1);					
		}
		
		JFrame frame = new JFrame("Gear Demo");
		frame.setSize(800,640);
		frame.setLayout(new java.awt.BorderLayout());

		final Animator animator = new Animator();
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
		
		
		
		
		GLCanvas canvas = new GLCanvas(glcaps);
		animator.add(canvas);
		
		AxisScene scene = new AxisScene();
		Camera camera = new Camera(scene,scene);
		Console console = new Console(camera, scene,canvas, new Robot());
		final GLEventListener application = console;
		
		canvas.addGLEventListener(application);

		frame.add(canvas, java.awt.BorderLayout.CENTER);
		frame.validate();

		frame.setVisible(true);
		animator.start();
		console.getGearsMouse().setCenteringMouse(true);
		BasicMouseAdapter.setMouseVisibility(false, frame);
	}


	@Override
	public void init(GLAutoDrawable drawable) {
		scene.init(drawable);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		scene.reshape(drawable, x, y, width, height) ;
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		scene.dispose(drawable);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		scene.display(drawable);
	}


}
