package fpsscene.fpsscene;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GL3ES3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLES3;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.math.Matrix4;

import fpsscene.adapters.ApplyXY;
import fpsscene.adapters.BasicMovement;
import fpsscene.gl.primitives.ColoredTriangle;
import fpsscene.gl.primitives.Point2f;
import fpsscene.gl.primitives.Point3f;

public class AxisScene extends Scene implements ApplyXY , BasicMovement{

	private static String vertexShaderString = String.join("\n", 
			"#version 130\n",
			"",
			"in vec3 vertex_position;",
			"in vec3 vertex_colour;",
			"uniform mat4 view, proj;",
			"out vec3 colour;",
			"void main() {",
			"  colour = vertex_colour;",
			"  gl_Position =  proj * view * vec4 (vertex_position, 1.0);",
			"}"
			);

	private static String fragmentShaderString = String.join("\n", 
			"#version 130\n",
			"in vec3 colour;",
			"out vec4 frag_colour;",
			"void main() {",
			"  frag_colour = vec4 (colour, 1.0);",
			"}"
			);

	private int shaderProgram;
	int vertShader;
	int fragShader;
	int view_mat_location;
	int proj_mat_location;

	Matrix4 proj_mat;
	Matrix4 view_mat;

	float sens_rot;

	Point3f eye_default;
	Point3f up_default;
	Point2f rot_default;
	Point2f fov_default;
	Point3f eye;
	Point3f up;
	Point2f rot;
	Point2f fov;

	int axisVao[] = new int[1];
	private int axisLen;
	
	float near; // clipping plane
	float far; // clipping plane

	static final int COLOR_IDX = 0;
	static final int VERTICES_IDX = 1;
	private static final float DROT_FULL = 360.0f;
	private static final float DROT_QUART = DROT_FULL/4.0f;	
	private int width=1920;
	private int height=1080;
	
	public AxisScene() {
		this.eye_default = new Point3f(0.0f, 0.0f, 0.0f);
		this.fov_default = new Point2f(120.0f, 90.0f);
		this.rot_default = new Point2f(0.0f, 0.0f);
		this.up_default = new Point3f(0.0f, 1.0f, 0.0f);

		this.eye = eye_default;
		this.fov = fov_default;
		this.rot = rot_default;
		this.up = up_default;

		near = 0.01f;
		far = 1000000.0f;
		sens_rot = 0.03f;


		rot.set(138.869919f, 4.44001198f);	
		eye.set(-4.66594696f,3.20000124f,-5.04626369f);
		//		rot.set(167.31528f,0.0f);


		
		updateProjMat();
		updateViewMatrix();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		if(!gl.isGL3core()){
			Logger.getAnonymousLogger().log(Level.SEVERE, "GL3core not enabled");
		}
		vertShader = createShaderFromString(gl, AxisScene.vertexShaderString,GL2ES2.GL_VERTEX_SHADER);
		fragShader = createShaderFromString(gl, AxisScene.fragmentShaderString,GL2ES2.GL_FRAGMENT_SHADER);
		shaderProgram = gl.glCreateProgram();
		gl.glAttachShader(shaderProgram, vertShader);
		gl.glAttachShader(shaderProgram, fragShader);
		gl.glLinkProgram(shaderProgram);
		this.view_mat_location = gl.glGetUniformLocation(shaderProgram, "view");
		this.proj_mat_location = gl.glGetUniformLocation(shaderProgram, "proj");
		gl.glDeleteShader(vertShader);
		gl.glDeleteShader(fragShader);

		List<ColoredTriangle> triangles = new AxisTrianges(100).createAxisTriangles();
		float[] vertices = ColoredTriangle.verticesToArray(triangles);
		float[] colors = ColoredTriangle.colorsToArray(triangles);
		FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(vertices);
		FloatBuffer fbColors = Buffers.newDirectFloatBuffer(colors);

		int[] points_vbo = new int[1];
		gl.glGenBuffers(1, points_vbo,0);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, points_vbo[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, triangles.size() * 9 * Float.BYTES, fbVertices, GL.GL_STATIC_DRAW);
		int[] colours_vbo = new int[1];
		gl.glGenBuffers(1, colours_vbo,0);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, colours_vbo[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, triangles.size() * 9 * Float.BYTES, fbColors, GL.GL_STATIC_DRAW);
		gl.glGenVertexArrays(1, axisVao,0);
		gl.glBindVertexArray(axisVao[0]);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, points_vbo[0]);
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, 0L);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, colours_vbo[0]);
		gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 0, 0L);
		gl.glEnableVertexAttribArray(0);
		gl.glEnableVertexAttribArray(1);

		axisLen = triangles.size();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		System.out.println("cleanup, remember to release shaders");
		GL3 gl = drawable.getGL().getGL3();
		gl.glUseProgram(0);
		gl.glDetachShader(shaderProgram, vertShader);
		gl.glDeleteShader(vertShader);
		gl.glDetachShader(shaderProgram, fragShader);
		gl.glDeleteShader(fragShader);
		gl.glDeleteProgram(shaderProgram);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
		this.updateProjMat();
		GL3 gl = drawable.getGL().getGL3();
		gl.glViewport((width-height)/2,0,height,height);
	}

	@Override
	protected void glDisplay(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		gl.glClearColor(1, 1, 1, 1.0f);  
		gl.glClear(GL2ES2.GL_STENCIL_BUFFER_BIT | GL2ES2.GL_COLOR_BUFFER_BIT   |	GL2ES2.GL_DEPTH_BUFFER_BIT   );
		gl.glUseProgram(shaderProgram);
		gl.glUniformMatrix4fv(this.view_mat_location, 1, false, this.view_mat.getMatrix(), 0);
		gl.glUniformMatrix4fv(this.proj_mat_location, 1, true, this.proj_mat.getMatrix(), 0);
		gl.glBindVertexArray(axisVao[0]);
		gl.glDrawArrays(GL2ES2.GL_TRIANGLES, 0, 3 * axisLen); //Draw the vertices as triangle
		gl.glBindVertexArray(0);
		gl.glCullFace(GL2ES2.GL_NONE);  
		gl.glDisable(GL2ES2.GL_CULL_FACE);
	}


	private void updateViewMatrix() {
		Matrix4 T = new Matrix4();
		T.translate(-eye.getX(), -eye.getY(), -eye.getZ());
		Matrix4 yRot = new Matrix4();
		yRot.rotate((float)Math.toRadians(rot.getX()), 0.0f, 1.0f, 0.0f);
		Matrix4 xRot = new Matrix4();
		xRot.rotate((float)Math.toRadians(Math.cos(-Math.toRadians(rot.getX())) * rot.getY()), 1.0f, 0.0f, 0.0f);
		Matrix4 zRot = new Matrix4();
		zRot.rotate((float)Math.toRadians(Math.sin(Math.toRadians(rot.getX())) * rot.getY()), 0.0f, 0.0f, 1.0f);
		Matrix4 R = yRot;
		R.multMatrix(xRot);
		R.multMatrix(zRot);
		view_mat = T;
		view_mat.multMatrix(R);
	}


	@Override
	protected boolean glRender(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		return false;
	}

	private void updateProjMat() {
		float aspect = (float) width / (float) height; // aspect ratio
		float range = (float) Math.tan(Math.toRadians(fov.getX() * 0.5f));
		float proj_mat[] = new float[16];

		proj_mat[0] = 1.0f / (range * aspect);
		proj_mat[1] = 0.0f;
		proj_mat[2] = 0.0f;
		proj_mat[3] = 0.0f;

		proj_mat[4] = 0.0f;
		proj_mat[5] = 1.0f / range;
		proj_mat[6] = 0.0f;
		proj_mat[7] = 0.0f;

		proj_mat[8] = 0.0f;
		proj_mat[9] = 0.0f;
		proj_mat[10] = -(far + near) / (far - near);
		proj_mat[11] = -(2.0f * far * near) / (far - near);

		proj_mat[12] = 0.0f;
		proj_mat[13] = 0.0f;
		proj_mat[14] =-1.0f;
		proj_mat[15] = 0.0f;

		this.proj_mat = new Matrix4();
		this.proj_mat.multMatrix(proj_mat);
	}

	@Override
	public void applyXY(float x, float y) {		
		rot.setX(fmod(rot.getX() + x * sens_rot, DROT_FULL));
		rot.setY(Math.min(Math.max(rot.getY() + y * sens_rot, -DROT_QUART), DROT_QUART));
		updateViewMatrix();
	}

	private float fmod(float f, float m) {
		return ((f%m) + m) %m;
	}

	@Override
	public void translate(float x, float y, float z) {
		float deltax = z * (float)Math.sin(Math.toRadians(rot.getX())) + x * (float)Math.cos(Math.toRadians(rot.getX()));
		float deltaz = z * (float)Math.cos(Math.toRadians(rot.getX())) - x * (float)Math.sin(Math.toRadians(rot.getX()));
		eye.set( eye.getX()+deltax, eye.getY()+y, eye.getZ()+deltaz );
		updateViewMatrix();
		System.out.println(eye + rot.toString());
	}

	

	private int createShaderFromString(GL3 gl, String shaderCode,int type) {
		int shader =  gl.glCreateShader(type);;
		String[] vlines = new String[] { shaderCode };
		int[] vlengths = new int[] { vlines[0].length() };
		gl.glShaderSource(shader, vlines.length, vlines, vlengths, 0);
		gl.glCompileShader(shader);
		int[] compiled = new int[1];
		gl.glGetShaderiv(shader, GL2ES2.GL_COMPILE_STATUS, compiled,0);
		if(compiled[0]!=0){
			System.out.println("Horray! vertex shader compiled");
		} else {
			int[] logLength = new int[1];
			gl.glGetShaderiv(shader, GL2ES2.GL_INFO_LOG_LENGTH, logLength, 0);

			byte[] log = new byte[logLength[0]];
			gl.glGetShaderInfoLog(shader, logLength[0], (int[])null, 0, log, 0);

			System.err.println("Error compiling the vertex shader: " + new String(log));
			System.exit(1);
		}
		return shader;
	}
}
