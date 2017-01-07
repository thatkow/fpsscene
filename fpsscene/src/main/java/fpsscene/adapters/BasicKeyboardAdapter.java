package fpsscene.adapters;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import fpsscene.fpsscene.Camera;

public class BasicKeyboardAdapter implements KeyListener {
	/**
	 * 
	 */
	private final Camera camera;

	private final Set<Integer> keyDown;

	private Thread thread;

	/**
	 * @param camera
	 */
	public BasicKeyboardAdapter(Camera camera) {
		this.camera = camera;
		this.keyDown = new HashSet<>();
		this.thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Date current = new Date();
				while(!Thread.interrupted()) {
					Date next = new Date();
					float diff_seconds = (float)(next.getTime() - current.getTime())/1000.0f;
					if(keyDown.contains(KeyEvent.VK_A )) {
						camera.applyMovement(-diff_seconds,0.0f,0.0f);
					}  
					if(keyDown.contains(KeyEvent.VK_D )) {
						camera.applyMovement(diff_seconds,0.0f,0.0f);
					}  
					if(keyDown.contains(KeyEvent.VK_W )) {
						camera.applyMovement(0.0f,0.0f,diff_seconds);
					} 
					if(keyDown.contains(KeyEvent.VK_S )) {
						camera.applyMovement(0.0f,0.0f,-diff_seconds);
					} 
					if(keyDown.contains(KeyEvent.VK_R )) {
						camera.applyMovement(0.0f,diff_seconds,0.0f);
					} 
					if(keyDown.contains(KeyEvent.VK_F )) {
						camera.applyMovement(0.0f,-diff_seconds,0.0f);
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		this.thread.start();
	}


	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int kc = e.getKeyCode();
		synchronized (keyDown) {
			keyDown.add(kc);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int kc = e.getKeyCode();
		synchronized (keyDown) {
			keyDown.remove(kc);
		}
	}
}