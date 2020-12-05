import java.awt.*;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class ResultScreen extends JPanel {
	Container contentPane;

	ImageIcon successImg = new ImageIcon("resource/success.png");
	ImageIcon failImg = new ImageIcon("resource/failed.png");
	private boolean results;
	
	private int resultTime;
	private int resultSandwichCount;
	private CustomMouse mouse;
	private Image retryBtn;
	
	ResultScreen(boolean gameResult, int sandwichCount, int leftTime){

		results = gameResult;
		resultTime = leftTime;
		resultSandwichCount = sandwichCount;

		CustomMouse inputMouseListener = new CustomMouse();
		mouse 	  = inputMouseListener;
	}

	private void buttonEvent() {
		if(mouse.getMouseXPos()>1060 && mouse.getMouseXPos()<1145 && mouse.getMouseYPos()>650 && mouse.getMouseYPos()<670) {
			retryBtn = new ImageIcon("resource/again2.png").getImage();
		}
		else{
			retryBtn = new ImageIcon("resource/again1.png").getImage();
		}
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Font resultFont = new Font("Verdana", Font.PLAIN, 30);
		
		if(results) {
			
			g.drawImage(successImg.getImage(), 0, 0, getWidth(), getHeight(), this);
			
			g.setFont(resultFont);
			g.setColor(Color.white);
			
			int sec  = resultTime % 60;
		    int min  = resultTime / 60 % 60; // 목표시간 빼기 결과시간 할 것임
		    
			g.drawString( min + " : " + sec, 1125 , 30);
			g.setColor(Color.yellow);
			g.drawString(min + " : " + sec, 1125 , 65);

		}
		else{
			g.setFont(resultFont);
			g.setColor(Color.green);
			
			g.drawImage(failImg.getImage(), 0, 0, getWidth(), getHeight(), this);
			g.drawImage(retryBtn, 500, 500, 80, 80, this);
			g.drawString("You made only "+ resultSandwichCount + " sandwiches", this.getWidth() / 3 , 630); //목표 - 만든수량 할 것임
		}
	}
}
