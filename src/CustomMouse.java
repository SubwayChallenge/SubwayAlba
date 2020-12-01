import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class CustomMouse implements MouseListener, MouseMotionListener {
	private int mouseXPos, mouseYPos;//X는 11, 73, Y는 14, 74
	private int mouseClickXPos, mouseClickYPos;// X는 18, 19, 38, Y는 25, 26, 39

	public int getMouseXPos() {//CS 275, GS 201, MS 72, 79
		return mouseXPos;
	}
	public int getMouseYPos() {//CS 276, GS 202, MS 72, 79
		return mouseYPos;
	}

	public int getMouseClickXPos() {//CS 137, GS 199, MS 86, 91
		int returnNumber = mouseClickXPos;
		mouseClickXPos = -1;

		return returnNumber;
	} 
	
	public int getMouseClickYPos() {//CS 138, GS 200, MS 86, 91
		int returnNumber = mouseClickYPos;
		mouseClickYPos = -1;

		return returnNumber;
	}
	
	public void mouseUp() {//쓰이지 않음
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {//쓰이지 않음
		// TODO Auto-generated method stub
		mouseClickXPos = e.getX();
		mouseClickYPos = e.getY();
	}

	@Override
	public void mousePressed(MouseEvent e) {//쓰이지 않음
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {//쓰이지 않음
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {//쓰이지 않음
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
