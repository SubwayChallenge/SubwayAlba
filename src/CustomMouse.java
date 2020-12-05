import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class CustomMouse implements MouseListener, MouseMotionListener {
	private int mouseXPos, mouseYPos;
	private int mouseClickXPos, mouseClickYPos;

	public int getMouseXPos() {//CS 275, GS 201, MS 72, 79
		return mouseXPos;
	}
	public int getMouseYPos() {//CS 276, GS 202, MS 72, 79
		return mouseYPos;
	}

	public int getMouseClickXPos() {
		int returnNumber = mouseClickXPos;
		mouseClickXPos = -1;

		return returnNumber;
	} 
	
	public int getMouseClickYPos() {
		int returnNumber = mouseClickYPos;
		mouseClickYPos = -1;

		return returnNumber;
	}
	
	public void mouseUp() {
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseClickXPos = e.getX();
		mouseClickYPos = e.getY();
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

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseXPos = e.getX();
		mouseYPos = e.getY();
	}
}
