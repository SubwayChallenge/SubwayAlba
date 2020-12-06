import java.awt.*;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class ResultScreen extends JPanel implements Runnable {
	Container contentPane;

	ImageIcon successImg = new ImageIcon("resource/success.png");
	ImageIcon failImg = new ImageIcon("resource/failed.png");
	private boolean results;
	private GameManagement resultScreen;

	private Thread resultScreenT;
	private int resultTime;
	private int highscore;
	private int resultSandwichCount;
	private CustomMouse mouse;
	private Image retryBtn;
	private boolean flag;
	private int Highscore;

	public ResultScreen(CustomMouse inputMouseListener, GameManagement inputRootFrame, boolean gameResult, int sandwichCount, int leftTime, int record){
		mouse 	  = inputMouseListener;

		highscore =record;
		results = gameResult;
		resultTime = leftTime;
		highscore = record;
		resultSandwichCount = sandwichCount;
		resultScreen = inputRootFrame;

		resultScreenT=new Thread(this);
		resultScreenT.start();


		flag = false;
	}
	public void clearExitScene() {
		if(resultScreenT!=null) {
			resultScreenT.interrupt();
			System.gc();
			flag = true;
		}
	}//method clearExitScene - 현재 씬을 말끔히 지워준다.

	public void run() {
		try {
			while (!flag) {
				buttonEvent();

				repaint();
				revalidate();

				resultScreenT.sleep(2);
			}
		} catch (InterruptedException ex) {
		} finally {
			System.out.println("resultScreen Thread dead");
		}
	}

	private void buttonEvent() {
		if(mouse.getMouseXPos()>850 && mouse.getMouseXPos()<950 && mouse.getMouseYPos()>100 && mouse.getMouseYPos()<300) {
			retryBtn = new ImageIcon("resource/again2.png").getImage();
		}
		else{
			retryBtn = new ImageIcon("resource/again1.png").getImage();
		}

		if(mouse.getMouseClickXPos()>850 && mouse.getMouseClickXPos()<950 && mouse.getMouseClickYPos()>100 && mouse.getMouseClickYPos()<300) {
			clearExitScene();
			resultScreen.moveToStartScreen(highscore);
		}
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Font resultFont = new Font("Verdana", Font.PLAIN, 30);
		
		if(results) {
			
			g.drawImage(successImg.getImage(), 0, 0, getWidth(), getHeight(), this);
			g.drawImage(retryBtn, 800, 100, 200, 200, this);
			g.setFont(resultFont);
			g.setColor(Color.white);

		    if(highscore == 0){
		    	highscore = resultTime;
			}
		    else if(resultTime > highscore){
				highscore = resultTime;
			}
			int mil  = highscore/ 60 % 10;
			int sec  = highscore / 600 % 600;

			int mil2  = resultTime/ 60 % 10;
			int sec2  = resultTime / 600 % 600;

			g.drawString( sec + " : " + mil, 1050 , 30);
			g.setColor(Color.yellow);
			g.drawString(sec2 + " : " + mil2, 1050 , 65);

		}
		else{
			g.setFont(resultFont);
			g.setColor(Color.black);
			
			g.drawImage(failImg.getImage(), 0, 0, getWidth(), getHeight(), this);
			g.drawImage(retryBtn, 800, 100, 200, 200, this);
			g.drawString("You made only "+ resultSandwichCount + " sandwiches", (this.getWidth() / 3) - 10, 620); //목표 - 만든수량 할 것임
		}
	}
}
