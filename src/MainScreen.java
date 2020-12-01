import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class MainScreen extends JPanel implements Runnable{
	private int planeWidth, planeHeight; //각각 39,40 줄(다른 파일에는 없음)
	private CustomMouse mouse; //41, 74, 81, 88, 93 줄
	private GameManagment mainScreen; //42, 91

	private Image startBtnImg; //75, 78, 102
	private Image endImg; //82, 85,103

	private Thread mainSceneT;//45, 46, 50, 52, 65
	private boolean flag; //43, 51, 59

	Image backgroundImg; //34, 98
	Image mainLogo; //35, 99
	
	public MainScreen(int screenWidth, int screenHeight, CustomMouse inputMouseListener, GameManagment inputMainScreen) {
		//super();


		backgroundImg = new ImageIcon("resource/background.jpg").getImage();
		//mainLogo = new ImageIcon("resource/logo.png").getImage();

		planeWidth  = screenWidth; //여기에만 쓰임
		planeHeight = screenHeight; //여기에만 쓰임
		mouse 	  = inputMouseListener; //여기에만 쓰임
		mainScreen = inputMainScreen; //여기에만 쓰임
		flag = false;

		mainSceneT = new Thread(this);
		mainSceneT.start();
	}

	public void clearExitScene() {
		if(mainSceneT != null) {
			flag = true;
			mainSceneT.interrupt();
			System.gc();
		}
	}

	public void run() {
		try{
			while(!flag){
				buttonEvent();
				
				repaint();
				revalidate();

				mainSceneT.sleep(2);
			}
		}catch(InterruptedException ex){	
		} finally {
			System.out.println("mainScene Thread dead");
		}
	}
	
	private void buttonEvent() {
		if(mouse.getMouseXPos()>353 && mouse.getMouseXPos()<927 && mouse.getMouseYPos()>455 && mouse.getMouseYPos()<718) {
			startBtnImg = new ImageIcon("resource/startbutton2.png").getImage();
		}
		else{
			startBtnImg = new ImageIcon("resource/startbutton1.png").getImage();
		}

		if(mouse.getMouseXPos()>1100 && mouse.getMouseXPos()<1180 && mouse.getMouseYPos()>30 && mouse.getMouseYPos()<110) {
			endImg = new ImageIcon("resource/exit2.png").getImage();
		}
		else{
			endImg = new ImageIcon("resource/exit1.png").getImage();
		}

		if(mouse.getMouseClickXPos()>353 && mouse.getMouseClickXPos()<927 && mouse.getMouseClickYPos()>455 && mouse.getMouseClickYPos()<718) {
			System.out.println("click");
			clearExitScene();
			mainScreen.moveToChooseDiffScreen();
		}
		else if(mouse.getMouseClickXPos()>1170 && mouse.getMouseClickXPos()<1250 && mouse.getMouseClickYPos()>30 && mouse.getMouseClickYPos()<110) {
			System.exit(0);
		}
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
		g2.drawImage(mainLogo, 400, 70, 430, 380, this);
		g2.drawImage(startBtnImg, 330, 455, this);
		g2.drawImage(endImg, 1100, 30, this);
	}
}
