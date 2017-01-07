package fpsscene.fpsscene;

import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.awt.AWTKeyAdapter;
import com.jogamp.newt.event.awt.AWTMouseAdapter;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLProfile;


public class GearsScene extends Scene {


	private int gear1=0, gear2=0, gear3=0;
	private float angle = 0.0f;
	private final int swapInterval;
	

	public GearsScene() {
		super();
		this.swapInterval = 1;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		System.err.println("Gears: Init: "+drawable);
		// Use debug pipeline
		// drawable.setGL(new DebugGL(drawable.getGL()));

		GL2 gl = drawable.getGL().getGL2();

		System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
		System.err.println("INIT GL IS: " + gl.getClass().getName());
		System.err.println("GL_VENDOR: " + gl.glGetString(GL2.GL_VENDOR));
		System.err.println("GL_RENDERER: " + gl.glGetString(GL2.GL_RENDERER));
		System.err.println("GL_VERSION: " + gl.glGetString(GL2.GL_VERSION));

		float pos[] = { 5.0f, 5.0f, 10.0f, 0.0f };
		float red[] = { 0.8f, 0.1f, 0.0f, 0.7f };
		float green[] = { 0.0f, 0.8f, 0.2f, 0.7f };
		float blue[] = { 0.2f, 0.2f, 1.0f, 0.7f };

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_DEPTH_TEST);

		/* make the gears */
		if(0>=gear1) {
			gear1 = gl.glGenLists(1);
			gl.glNewList(gear1, GL2.GL_COMPILE);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, red, 0);
			gear(gl, 1.0f, 4.0f, 1.0f, 20, 0.7f);
			gl.glEndList();
			System.err.println("gear1 list created: "+gear1);
		} else {
			System.err.println("gear1 list reused: "+gear1);
		}

		if(0>=gear2) {
			gear2 = gl.glGenLists(1);
			gl.glNewList(gear2, GL2.GL_COMPILE);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, green, 0);
			gear(gl, 0.5f, 2.0f, 2.0f, 10, 0.7f);
			gl.glEndList();
			System.err.println("gear2 list created: "+gear2);
		} else {
			System.err.println("gear2 list reused: "+gear2);
		}

		if(0>=gear3) {
			gear3 = gl.glGenLists(1);
			gl.glNewList(gear3, GL2.GL_COMPILE);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, blue, 0);
			gear(gl, 1.3f, 2.0f, 0.5f, 10, 0.7f);
			gl.glEndList();
			System.err.println("gear3 list created: "+gear3);
		} else {
			System.err.println("gear3 list reused: "+gear3);
		}

		gl.glEnable(GL2.GL_NORMALIZE);

	
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		System.err.println("Gears: Dispose");
		setGears(0, 0, 0);
	}

	private void setGears(int i, int j, int k) {
		this.gear1 = i;
		this.gear2 = j;
		this.gear3 = k;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.err.println("Gears: Reshape "+x+"/"+y+" "+width+"x"+height);
		GL2 gl = drawable.getGL().getGL2();

		gl.setSwapInterval(swapInterval);

		float h = (float)height / (float)width;

		gl.glMatrixMode(GL2.GL_PROJECTION);

		gl.glLoadIdentity();
		gl.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -40.0f);
	}

	@Override
	protected void glDisplay(GLAutoDrawable drawable) {
		// Turn the gears' teeth
		angle += 2.0f;

		// Get the GL corresponding to the drawable we are animating
		GL2 gl = drawable.getGL().getGL2();

		// Place the first gear and call its display list
		gl.glPushMatrix();
		gl.glTranslatef(-3.0f, -2.0f, 0.0f);
		gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
		gl.glCallList(gear1);
		gl.glPopMatrix();

		// Place the second gear and call its display list
		gl.glPushMatrix();
		gl.glTranslatef(3.1f, -2.0f, 0.0f);
		gl.glRotatef(-2.0f * angle - 9.0f, 0.0f, 0.0f, 1.0f);
		gl.glCallList(gear2);
		gl.glPopMatrix();

		// Place the third gear and call its display list
		gl.glPushMatrix();
		gl.glTranslatef(-3.1f, 4.2f, 0.0f);
		gl.glRotatef(-2.0f * angle - 25.0f, 0.0f, 0.0f, 1.0f);
		gl.glCallList(gear3);
		gl.glPopMatrix();

		
	}

	@Override
	protected boolean glRender(GLAutoDrawable drawable) {
		return false;
	}


	public static void gear(GL2 gl,
			float inner_radius,
			float outer_radius,
			float width,
			int teeth,
			float tooth_depth)
	{
		int i;
		float r0, r1, r2;
		float angle, da;
		float u, v, len;

		r0 = inner_radius;
		r1 = outer_radius - tooth_depth / 2.0f;
		r2 = outer_radius + tooth_depth / 2.0f;

		da = 2.0f * (float) Math.PI / teeth / 4.0f;

		gl.glShadeModel(GL2.GL_FLAT);

		gl.glNormal3f(0.0f, 0.0f, 1.0f);

		/* draw front face */
		gl.glBegin(GL2.GL_QUAD_STRIP);
		for (i = 0; i <= teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
			if(i < teeth)
			{
				gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
				gl.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
			}
		}
		gl.glEnd();

		/* draw front sides of teeth */
		gl.glBegin(GL2.GL_QUADS);
		for (i = 0; i < teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2.0f * da), r2 * (float)Math.sin(angle + 2.0f * da), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
		}
		gl.glEnd();

		/* draw back face */
		gl.glBegin(GL2.GL_QUAD_STRIP);
		for (i = 0; i <= teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), -width * 0.5f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
		}
		gl.glEnd();

		/* draw back sides of teeth */
		gl.glBegin(GL2.GL_QUADS);
		for (i = 0; i < teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), -width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2 * da), r2 * (float)Math.sin(angle + 2 * da), -width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), -width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
		}
		gl.glEnd();

		/* draw outward faces of teeth */
		gl.glBegin(GL2.GL_QUAD_STRIP);
		for (i = 0; i < teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
			u = r2 * (float)Math.cos(angle + da) - r1 * (float)Math.cos(angle);
			v = r2 * (float)Math.sin(angle + da) - r1 * (float)Math.sin(angle);
			len = (float)Math.sqrt(u * u + v * v);
			u /= len;
			v /= len;
			gl.glNormal3f(v, -u, 0.0f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), -width * 0.5f);
			gl.glNormal3f((float)Math.cos(angle), (float)Math.sin(angle), 0.0f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2 * da), r2 * (float)Math.sin(angle + 2 * da), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2 * da), r2 * (float)Math.sin(angle + 2 * da), -width * 0.5f);
			u = r1 * (float)Math.cos(angle + 3 * da) - r2 * (float)Math.cos(angle + 2 * da);
			v = r1 * (float)Math.sin(angle + 3 * da) - r2 * (float)Math.sin(angle + 2 * da);
			gl.glNormal3f(v, -u, 0.0f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), -width * 0.5f);
			gl.glNormal3f((float)Math.cos(angle), (float)Math.sin(angle), 0.0f);
		}
		gl.glVertex3f(r1 * (float)Math.cos(0), r1 * (float)Math.sin(0), width * 0.5f);
		gl.glVertex3f(r1 * (float)Math.cos(0), r1 * (float)Math.sin(0), -width * 0.5f);
		gl.glEnd();

		gl.glShadeModel(GL2.GL_SMOOTH);

		/* draw inside radius cylinder */
		gl.glBegin(GL2.GL_QUAD_STRIP);
		for (i = 0; i <= teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glNormal3f(-(float)Math.cos(angle), -(float)Math.sin(angle), 0.0f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
		}
		gl.glEnd();
	}

	

}
