import java.awt.*;
import java.awt.Font;

import javax.swing.*;

public class ChooseDifficulty extends JPanel implements Runnable{

	private static final long serialVersionUID = 1L;

	private int orders=0;//the number of orders for sandwiches
	private int levelNum =0;//level for game
	private int highscore;

	ImageIcon open= new ImageIcon("resource/open1.png");
	ImageIcon open2= new ImageIcon("resource/open2.png");

	ImageIcon selectedDifficulty=new ImageIcon("resource/checked.png");//104, 누를 때 생기는 원
	ImageIcon chooseErrorMessage=new ImageIcon("resource/errormsg.png");
	ImageIcon three = new ImageIcon("resource/three.png");
	ImageIcon five = new ImageIcon("resource/five.png");
	ImageIcon eight = new ImageIcon("resource/eight.png");
	ImageIcon backImg=new ImageIcon("resource/background2.png");//71

	ImageIcon openImg, open2Img;
	ImageIcon check;
	ImageIcon error;


	Image before,after;

	int row1=310;
	int col1=190;
	int col2=370;
	int col3=560;

	int check_col=-300;int check_row=-300;//draw a check ring for top
	int error_x=-300;int error_y=-300;
	int level_x=-300;int level_y=-300;
	
	private CustomMouse mouse;
	private GameManagement chooseDifficulty;
	private Thread mouseEventT;
	private boolean flag;
	
	public ChooseDifficulty(CustomMouse inputMouseListener, GameManagement inputRootFrame, int record) {
		mouse = inputMouseListener;
		chooseDifficulty = inputRootFrame;
		flag = false;
		highscore =record;
		System.out.println(highscore);

		mouseEventT = new Thread(this);
		mouseEventT.start();
	}
	
	public void clearExitScene() {
		System.out.println("ClearExitScene");
		if(mouseEventT!=null) {
			flag = true;
			mouseEventT.interrupt();
			System.gc();
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
		after=before.getScaledInstance(80,80, Image.SCALE_SMOOTH);
		check = new ImageIcon(after);

		//error message
		before=chooseErrorMessage.getImage();
		after=before.getScaledInstance(350,66,java.awt.Image.SCALE_SMOOTH);
		error=new ImageIcon(after);

		g2.drawImage(openImg.getImage(),800,400,this);
		g2.drawImage(check.getImage(), check_row,check_col,this);
		g2.drawImage(error.getImage(), error_x, error_y, this);


		Font font1 = new Font("Verdana", Font.PLAIN, 30);//font for price of item
		Font font2=new Font("Verdana",Font.PLAIN,25);//font for total sandwiches

		g2.setFont(font1);
		g2.setColor(Color.black);
		switch(levelNum){
			case 0:
				g2.drawImage(three.getImage(), level_x, level_y, this);
				break;
			case 1:
				g2.drawImage(five.getImage(), level_x, level_y, this);
				break;
			case 2:
				g2.drawImage(eight.getImage(), level_x, level_y, this);
				break;
		}
		g2.setFont(font2);

		g2.drawString("Total: "+orders,830,580);//string for total hamburgers

	}

	public void mouseClickEvent() {
		int xx = mouse.getMouseClickXPos();
		int yy = mouse.getMouseClickYPos();

		boolean easy=((xx>=row1-70&&xx<=row1+70)&&(yy>=col1-56&&yy<=col1+56));
		boolean normal=((xx>=row1-70&&xx<=row1+70)&&(yy>=col2-56&&yy<=col2+56));
		boolean hard=((xx>=row1-70&&xx<=row1+70)&&(yy>=col3-56&&yy<=col3+56));

		boolean isOpen=((xx>=800+6&&xx<=800+180)&&(yy>=400+100&&yy<=400+169)); //270

		if(easy){
			orders=3;
			check_col=col1-110;
			check_row=row1-150;
			error_x=-300;
			error_y=-300;
		}

		if(normal){
			orders=5;
			check_col=col2-110;
			check_row=row1-150;
			error_x=-300;
			error_y=-300;
		}

		if(hard){
			orders=8;
			check_col=col3-110;
			check_row=row1-150;
			error_x=-300;
			error_y=-300;
		}

		if(isOpen){
			if(orders==0){
				error_x=130;
				error_y=20;
			}
			else{
				clearExitScene();
				chooseDifficulty.moveToGameScreen(orders,highscore);
			}
		}
	}

	public void mouseMoveEvent() {
		// TODO Auto-generated method stub
		int xx = mouse.getMouseXPos();
		int yy = mouse.getMouseYPos();

		boolean easy=((xx>=row1-70&&xx<=row1+70)&&(yy>=col1-56&&yy<=col1+56));
		boolean normal=((xx>=row1-70&&xx<=row1+70)&&(yy>=col2-56&&yy<=col2+56));
		boolean hard=((xx>=row1-70&&xx<=row1+70)&&(yy>=col3-56&&yy<=col3+56));

		boolean isOpen=((xx>=800+6&&xx<=800+180)&&(yy>=400+100&&yy<=400+169)); //270

		if(easy){
			levelNum =0;
			level_x=350;
			level_y=30;
		}

		else{
			level_x=-300;
			level_y=-300;
		}

		if(normal){
			levelNum =1;
			level_x=350;
			level_y=200;
		}

		if(hard){
			levelNum =2;
			level_x=350;
			level_y=400;
		}
		//if the mouse is on the 'start', 'start' color is change to orange 
		if(isOpen){
			openImg = open2Img;
		}
	}

	@Override
	public void run() {
		try{
			while(!flag){
				mouseClickEvent();
				mouseMoveEvent();

				repaint();

				mouseEventT.sleep(1);
			}
		}catch(InterruptedException ex){	
		} finally {
			System.out.println(" mouseEventThread dead");
		}
	}
}
