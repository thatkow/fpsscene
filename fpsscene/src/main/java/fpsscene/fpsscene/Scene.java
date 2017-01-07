package fpsscene.fpsscene;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public abstract class Scene implements GLEventListener {

	protected int fps = 60;

	protected abstract void glDisplay(GLAutoDrawable drawable);
	protected abstract boolean glRender(GLAutoDrawable drawable); // Return true if want to render another

	@Override
	public void display(GLAutoDrawable drawable) {
		long milliseconds_allowance = 1000 / fps;
		long before = System.currentTimeMillis();
		glDisplay(drawable);
		long after = System.currentTimeMillis();
		while (after - before < milliseconds_allowance) {
			if(!glRender(drawable))
				break;
			after = System.currentTimeMillis();
		}
	}
}
