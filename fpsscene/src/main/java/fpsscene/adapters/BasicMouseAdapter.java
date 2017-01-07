package fpsscene.adapters;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.validation.constraints.NotNull;

import com.jogamp.nativewindow.util.Point;

import fpsscene.fpsscene.Camera;

public class BasicMouseAdapter implements MouseMotionListener , MouseListener, MouseWheelListener{

	@NotNull
	private final Camera camera;

	@NotNull
	private final Component component;

	@NotNull
	private final Robot robot;

	private boolean centeringMouse;



	public BasicMouseAdapter(Camera camera, Component  component, Robot robot) {
		super();
		this.camera = camera;
		this.component = component;
		this.robot = robot;
		centeringMouse = false;
	}

	private void centerMouse() {
		Point center = getCenter();
		this.robot.mouseMove(center.getX(), center.getY());
	}

	private Point getCenter() {
		return new Point( 
				this.component.getLocationOnScreen().x + this.component.getWidth()/2,
				this.component.getLocationOnScreen().y + this.component.getHeight()/2
				);
	}

	public boolean isCenteringMouse() {
		return centeringMouse;		
	}

	public void setCenteringMouse(boolean centeringMouse) {
		this.centeringMouse = centeringMouse;
		if(centeringMouse) {
			centerMouse();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point mDiff = getCenter();
		mDiff.set(
				mDiff.getX() - MouseInfo.getPointerInfo().getLocation().x,
				mDiff.getY() - MouseInfo.getPointerInfo().getLocation().y
				);
		this.camera.getApplyxy().applyXY(mDiff.getX(), mDiff.getY());
		if(centeringMouse)
			centerMouse();
		e.consume();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public static void setMouseVisibility(boolean visible, JFrame frame){
		if(!visible) {

			// Transparent 16 x 16 pixel cursor image.
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

			// Create a new blank cursor.
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
					cursorImg, new java.awt.Point(0, 0), "blank cursor");

			// Set the blank cursor to the JFrame.
			frame.getContentPane().setCursor(blankCursor);
		}else{
			frame.getContentPane().setCursor(null);	
		}
	}
}