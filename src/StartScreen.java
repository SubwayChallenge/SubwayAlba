import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class StartScreen extends JPanel implements Runnable{
	private CustomMouse mouse;
	private GameManagement startScreen;

	private Image startBtnImg;

	private Thread startScreenT;
	private boolean flag;

	private int highscore=0;
	Image backgroundImg;
	
	public StartScreen(int screenWidth, int screenHeight, CustomMouse inputMouseListener, GameManagement inputStartScreen, int record) {

		backgroundImg = new ImageIcon("resource/background.png").getImage();

		highscore =record;
		System.out.println(highscore);
		mouse 	  = inputMouseListener;
		startScreen = inputStartScreen;
		flag = false;

		startScreenT = new Thread(this);
		startScreenT.start();
	}

	public void clearExitScene() {
		if(startScreenT != null) {
			flag = true;
			startScreenT.interrupt();
			System.gc();
		}
	}


	public void run() {
		try{
			while(!flag){
				buttonEvent();
				
				repaint();
				revalidate();

				startScreenT.sleep(2);
			}
		}catch(InterruptedException ex){	
		} finally {
			System.out.println("startScreen Thread dead");
		}
	}
	
	private void buttonEvent() {

		//System.out.println(mouse.getMouseXPos());
		if(mouse.getMouseXPos()>540 && mouse.getMouseXPos()<660 && mouse.getMouseYPos()>300 && mouse.getMouseYPos()<440) {
			startBtnImg = new ImageIcon("resource/startbutton2.png").getImage();
		}
		else{
			startBtnImg = new ImageIcon("resource/startbutton1.png").getImage();
		}

		if(mouse.getMouseClickXPos()>540 && mouse.getMouseClickXPos()<660 && mouse.getMouseClickYPos()>300 && mouse.getMouseClickYPos()<440) {
			startScreen.playEffectSound("resource/sound/button_sound.mp3");
			clearExitScene();
			startScreen.moveToChooseDiffScreen(highscore);
		}
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
		g2.drawImage(startBtnImg, 540, 280, 120, 120,this);
	}
}
