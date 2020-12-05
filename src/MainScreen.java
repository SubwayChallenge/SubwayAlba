import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class MainScreen extends JPanel implements Runnable{
	private int planeWidth, planeHeight;
	private CustomMouse mouse;
	private GameManagement mainScreen;

	private Image startBtnImg;
	private Image endImg;

	private Thread mainSceneT;
	private boolean flag;

	Image backgroundImg;
	
	public MainScreen(int screenWidth, int screenHeight, CustomMouse inputMouseListener, GameManagement inputMainScreen) {
		//super(); 안해준 이유?


		backgroundImg = new ImageIcon("resource/background.png").getImage();

		planeWidth  = screenWidth;
		planeHeight = screenHeight;
		mouse 	  = inputMouseListener;
		mainScreen = inputMainScreen;
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

		//System.out.println(mouse.getMouseXPos());
		if(mouse.getMouseXPos()>540 && mouse.getMouseXPos()<660 && mouse.getMouseYPos()>300 && mouse.getMouseYPos()<440) {
			startBtnImg = new ImageIcon("resource/startbutton2.png").getImage();
		}
		else{
			startBtnImg = new ImageIcon("resource/startbutton1.png").getImage();
		}

		if(mouse.getMouseXPos()>1060 && mouse.getMouseXPos()<1145 && mouse.getMouseYPos()>40 && mouse.getMouseYPos()<160) {
			endImg = new ImageIcon("resource/exit2.png").getImage();
		}
		else{
			endImg = new ImageIcon("resource/exit1.png").getImage();
		}

		if(mouse.getMouseClickXPos()>540 && mouse.getMouseClickXPos()<660 && mouse.getMouseClickYPos()>300 && mouse.getMouseClickYPos()<440) {
			clearExitScene();
			mainScreen.moveToChooseDiffScreen();
		}
		if(mouse.getMouseClickXPos()>1060 && mouse.getMouseClickXPos()<1145 && mouse.getMouseClickYPos()>40 && mouse.getMouseClickYPos()<160) {
			System.exit(0);
			try {
				mainSceneT.sleep(2000); //2초동안 잠시 정지시킨다
			} catch (Exception e) {
			}
			//System.exit(0); //종료 안됨
		}
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
		g2.drawImage(startBtnImg, 540, 280, 120, 120,this);
		g2.drawImage(endImg, 1060, 40, 80, 80, this);
	}
}
