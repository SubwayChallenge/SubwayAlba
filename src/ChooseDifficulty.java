import java.awt.*;
import java.awt.Font;

import javax.swing.*;

public class ChooseDifficulty extends JPanel implements Runnable{

	private static final long serialVersionUID = 1L;
	
	int price;
	int orders=0;//the number of orders for sandwiches

	ImageIcon open= new ImageIcon("resource/open1.png");
	ImageIcon open2= new ImageIcon("resource/open2.png");

	ImageIcon selectedDifficulty=new ImageIcon("resource/checked.png");//누를 때 생기는 원
	ImageIcon backImg=new ImageIcon("resource/background2.png");

	ImageIcon openImg, open2Img;
	ImageIcon check;

	Image before,after;

	int row1=310;
	int col1=190;
	int col2=370;
	int col3=560;
	int check_col=-300;int check_row=-300;

	int level_x=-300;int level_y=-300;
	
	private CustomMouse mouse;
	private GameManagement rootScreen;
	private Thread mouseEventT;
	
	public ChooseDifficulty(CustomMouse inputMouseListener, GameManagement inputRootFrame) {
		mouse = inputMouseListener;
		rootScreen = inputRootFrame;
		mouseEventT = new Thread(this);
		mouseEventT.start();
	}
	
	public void clearExitScene() {
		System.out.println("ClearExitScene");
		if(mouseEventT!=null) {
			mouseEventT.interrupt();
		}
	}
	
	public void paintComponent(Graphics g) {

		Graphics g2=(Graphics)g;
		g2.drawImage(backImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);

		before=open.getImage();
		after=before.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
		openImg=new ImageIcon(after);

		before=open2.getImage();
		after=before.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
		open2Img= new ImageIcon(after);

		before=selectedDifficulty.getImage();
		after=before.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
		check=new ImageIcon(after);

		g2.drawImage(openImg.getImage(),800,400,this);
		g2.drawImage(check.getImage(), check_row,check_col,this);

		Font font1 = new Font("Verdana", Font.PLAIN, 30);//font for price of item
		Font font2=new Font("Verdana",Font.PLAIN,25);//font for total sandwiches

		g2.setFont(font1);
		g2.setColor(Color.black);
		g2.drawString(""+price,level_x,level_y);//string for price of items

		g2.setFont(font2);
		g2.drawString("total: "+orders,20,200);//string for total hamburgers
	}

	public void mouseClickEvent() {
		int xx = mouse.getMouseClickXPos();
		int yy = mouse.getMouseClickYPos();

		boolean easy=((xx>=row1-70&&xx<=row1+70)&&(yy>=col1-56&&yy<=col1+56));
		boolean normal=((xx>=row1-70&&xx<=row1+70)&&(yy>=col2-56&&yy<=col2+56));
		boolean hard=((xx>=row1-70&&xx<=row1+70)&&(yy>=col3-56&&yy<=col3+56));

		boolean isOpen=((xx>=800+6&&xx<=800+156)&&(yy>=400+100&&yy<=400+169)); //270

		if(easy){
			orders=3;
			check_col=col1-110;
			check_row=row1-150;
		}

		if(normal){
			orders=6;
			check_col=col2-110;
			check_row=row1-150;
		}

		if(hard){
			orders=9;
			check_col=col3-110;
			check_row=row1-150;
		}

		if(isOpen){
			clearExitScene();
			rootScreen.moveToGameScreen(orders);
		}

		repaint();
	}

	public void mouseMoveEvent() {
		// TODO Auto-generated method stub
		int xx = mouse.getMouseXPos();
		int yy = mouse.getMouseYPos();

		boolean easy=((xx>=row1-70&&xx<=row1+70)&&(yy>=col1-56&&yy<=col1+56));
		boolean normal=((xx>=row1-70&&xx<=row1+70)&&(yy>=col2-56&&yy<=col2+56));
		boolean hard=((xx>=row1-70&&xx<=row1+70)&&(yy>=col3-56&&yy<=col3+56));

		boolean isOpen=((xx>=800+6&&xx<=800+156)&&(yy>=400+100&&yy<=400+169));

		if(easy){
			price=3;
			level_x=443;
			level_y=180;
		}

		else{
			level_x=-300;
			level_y=-300;
		}

		if(normal){
			price=6;
			level_x=443;
			level_y=360;
		}

		if(hard){
			price=9;
			level_x=443;
			level_y=545;
		}
		//if the mouse is on the 'start', 'start' color is change to orange 
		if(isOpen){
			openImg = open2Img;
			//startW=50;
			//startH=550;
		}

		repaint();
	}

	@Override
	public void run() {
		try{
			while(!mouseEventT.isInterrupted()){
				mouseClickEvent();
				mouseMoveEvent();

				mouseEventT.sleep(1);
			}
		}catch(InterruptedException ex){	
		} finally {
			System.out.println(" mouseEventThread dead");
		}
	}
}
