import java.awt.*;
import java.awt.Font;

import javax.swing.*;

public class ChooseDifficulty extends JPanel implements Runnable{

	private static final long serialVersionUID = 1L; //파일 안에서 사용이 안되는데 이게 뭘까??
	
	int price;//130, 299, 301, 303, 306, 308, 310, 313, 315, 317 the price of items -> price 관련된거는 다 없애도 될 것 같아!
	int orders=0;//132, 272, 275, the number of orders for sandwiches
	//int easy=0;//166, 169, 173, 177, 185, 189, 193, 275     -> 우리는 셋 중 하나만 고르는 거니까 total 세는 변수 없앨게~
	//int normal=0;//199, 203, 207, 211, 222, 226, 230, 275
	//int hard=0;//236, 240, 244, 248, 257, 261, 265, 275


	ImageIcon open= new ImageIcon("resource/open1.png");
	ImageIcon open2= new ImageIcon("resource/open2.png");

	ImageIcon selectedDifficulty=new ImageIcon("resource/checked.png");//104, 누를 때 생기는 원

	//첫번째 줄
	//ImageIcon easy1= new ImageIcon("resource/tshirt.png");//74
	//ImageIcon easy2= new ImageIcon("resource/long.png");//77
	//ImageIcon easy3= new ImageIcon("resource/hoodie.png");//80
	//두번째 줄
	//ImageIcon normal1= new ImageIcon("resource/skirt.png");//84
	//ImageIcon normal2= new ImageIcon("resource/shortpants.png");//87
	//ImageIcon normal3= new ImageIcon("resource/long-pants.png");//90
	//세번째 줄
	//ImageIcon hard1=new ImageIcon("resource/logo-shoe.png");//94
	//ImageIcon hard2=new ImageIcon("resource/heels.png");//97
	//ImageIcon hard3=new ImageIcon("resource/slipper.png");//100

	ImageIcon backImg=new ImageIcon("resource/background2.png");//71


	//ImageIcon easy1Img,easy2Img,easy3Img;//각각 76,108 - 79,109 - 82,110
	//ImageIcon normal1Img,normal2Img,normal3Img;//각각 86,112 - 89,113 - 92,114
	//ImageIcon hard1Img,hard2Img,hard3Img;//각각 96,116 - 99,117 - 102,118
	ImageIcon openImg, open2Img;
	ImageIcon check;//106, 120, 121, 122
	//before - 74, 75, 77, 78, 80, 81, 84, 85, 87, 88, 90, 91, 94, 95, 97, 98, 100, 101, 104, 105
	//after - 75, 76, 78, 79, 81, 82, 85, 86, 88, 89, 91, 92, 95, 96, 98, 99, 101, 102, 105, 106
	Image before,after;
	//이거는 일단 그대로 놔뒀옹! col,row는 보통 많이 쓰니까 괜찮지 않을깡ㅎㅎ
	int row1=310;
	int col1=190;
	int col2=370;
	int col3=560;
	int check_col=-300;int check_row=-300;//draw a check ring for top
	//int check_col2=-300;int check_row2=-300;//draw a check ring for bottom
	//int check_col3=-300;int check_row3=-300;//draw a check ring for shoe
	//int startW=-300;int startH=-300;
	int level_x=-300;int level_y=-300;
	
	private CustomMouse mouse; //56, 143, 144, 281, 282
	private GameManagment rootScreen; //57, 272
	private Thread mouseEventT; //58, 59, 64, 65, 335, 339
	
	public ChooseDifficulty(CustomMouse inputMouseListener, GameManagment inputRootFrame) {
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
		//System.out.println(mouse.getMouseXPos()+","+mouse.getMouseYPos());
		Graphics g2=(Graphics)g;
		g2.drawImage(backImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);

		before=open.getImage();
		after=before.getScaledInstance(200, 150, java.awt.Image.SCALE_SMOOTH);
		openImg=new ImageIcon(after);

		before=open2.getImage();
		after=before.getScaledInstance(200, 150, java.awt.Image.SCALE_SMOOTH);
		open2Img= new ImageIcon(after);

		//선택했을 때 원 생기는 거
		before=selectedDifficulty.getImage();
		after=before.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
		check=new ImageIcon(after);

		g2.drawImage(openImg.getImage(),800,400,this);

		//easy 선택
		//before=easy1.getImage();
		//after=before.getScaledInstance(200, 150, java.awt.Image.SCALE_SMOOTH);
		//easy1Img=new ImageIcon(after);
		//before=easy2.getImage();
		//after=before.getScaledInstance(200, 150, java.awt.Image.SCALE_SMOOTH);
		//easy2Img=new ImageIcon(after);
		//before=easy3.getImage();
		//after=before.getScaledInstance(200, 150, java.awt.Image.SCALE_SMOOTH);
		//easy3Img=new ImageIcon(after);
		//normal 선택
		//before=normal1.getImage();
		//after=before.getScaledInstance(200, 150, java.awt.Image.SCALE_SMOOTH);
		//normal1Img=new ImageIcon(after);
		//before=normal2.getImage();
		///after=before.getScaledInstance(180, 150, java.awt.Image.SCALE_SMOOTH);
		//normal2Img=new ImageIcon(after);
		//before=normal3.getImage();
		//after=before.getScaledInstance(180, 150, java.awt.Image.SCALE_SMOOTH);
		//normal3Img=new ImageIcon(after);
		//hard 선택
		//before=hard1.getImage();
		//after=before.getScaledInstance(200, 150, java.awt.Image.SCALE_SMOOTH);
		//hard1Img=new ImageIcon(after);
		//before=hard2.getImage();
		//after=before.getScaledInstance(200, 150, java.awt.Image.SCALE_SMOOTH);
		//hard2Img=new ImageIcon(after);
		//before=hard3.getImage();
		//after=before.getScaledInstance(200, 150, java.awt.Image.SCALE_SMOOTH);
		//hard3Img=new ImageIcon(after);

		
		//g2.drawImage(easy1Img.getImage(),row1,col1,this);
		//g2.drawImage(easy2Img.getImage(), row2,col1,this);
		//g2.drawImage(easy3Img.getImage(),row3,col1,this);

		//g2.drawImage(normal1Img.getImage(),row1,col2,this);
		//g2.drawImage(normal2Img.getImage(), row2,col2,this);
		//g2.drawImage(normal3Img.getImage(),row3,col2,this);
		
		//g2.drawImage(hard1Img.getImage(),row1,col3,this);
		//g2.drawImage(hard2Img.getImage(), row2,col3,this);
		//g2.drawImage(hard3Img.getImage(),row3,col3,this);
		
		g2.drawImage(check.getImage(), check_row,check_col,this);
		//g2.drawImage(check.getImage(), check_row2,check_col2,this);
		//g2.drawImage(check.getImage(), check_row3,check_col3,this);

		Font font1 = new Font("Verdana", Font.PLAIN, 30);//font for price of item
		Font font2=new Font("Verdana",Font.PLAIN,25);//font for total hamburgers
		//Font font3=new Font("Verdana",Font.BOLD,80);//font for start button
		g2.setFont(font1);
		g2.setColor(Color.black);
		g2.drawString(""+price,level_x,level_y);//string for price of items

		g2.setFont(font2);
		g2.drawString("total: "+orders,20,200);//string for total hamburgers
		//g2.setFont(font3);
		//g2.setColor(Color.white);
		//g2.drawString("START",50, 550);
		//g2.setColor(Color.orange);
		//g2.drawString("START",startW,startH);
		
		 
	}

	public void mouseClickEvent() {
		int xx = mouse.getMouseClickXPos();
		int yy = mouse.getMouseClickYPos();
				
		//boolean easyFlag=false;//165
		//boolean normalFlag=false;//198, 218
		//boolean hardFlag=false;//235, 253

		boolean easy=((xx>=row1-70&&xx<=row1+70)&&(yy>=col1-56&&yy<=col1+56));
		boolean normal=((xx>=row1-70&&xx<=row1+70)&&(yy>=col2-56&&yy<=col2+56));
		boolean hard=((xx>=row1-70&&xx<=row1+70)&&(yy>=col3-56&&yy<=col3+56));

		boolean isOpen=((xx>=800+6&&xx<=800+198)&&(yy>=400+100&&yy<=400+169)); //270

		//boolean easy1=((xx>=row1-100&&xx<=row1+100)&&(yy>=col1-100&&yy<=col1+100)); //168, 184
		//boolean easy2=((xx>=row2-100&&xx<=row2+100)&&(yy>=col1-100&&yy<=col1+100)); //172, 188
		//boolean easy3=((xx>=row3-100&&xx<=row3+100)&&(yy>=col1-100&&yy<=col1+100)); //176, 192
		
		//boolean normal1=((xx>=row1-100&&xx<=row1+100)&&(yy>=col2-100&&yy<=col2+100)); //202, 221
		//boolean normal2=((xx>=row2-100&&xx<=row2+100)&&(yy>=col2-100&&yy<=col2+100)); //206, 225
		//boolean normal3=((xx>=row3-100&&xx<=row3+100)&&(yy>=col2-100&&yy<=col2+100)); //210, 229
		
		//boolean hard1=((xx>=row1-100&&xx<=row1+100)&&(yy>=col3-100&&yy<=col3+100)); //239, 256
		//boolean hard2=((xx>=row2-100&&xx<=row2+100)&&(yy>=col3-100&&yy<=col3+100)); //243, 260
		//boolean hard3=((xx>=row3-100&&xx<=row3+100)&&(yy>=col3-100&&yy<=col3+100)); //247, 264

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

		boolean isOpen=((xx>=800+6&&xx<=800+198)&&(yy>=400+100&&yy<=400+169)); //270

		//boolean easy1=((xx>=row1-100&&xx<=row1+100)&&(yy>=col1-100&&yy<=col1+100)); //298
		//boolean easy2=((xx>=row2-100&&xx<=row2+100)&&(yy>=col1-100&&yy<=col1+100)); //300
		//boolean easy3=((xx>=row3-100&&xx<=row3+100)&&(yy>=col1-100&&yy<=col1+100)); //302
		
		//boolean normal1=((xx>=row1-100&&xx<=row1+100)&&(yy>=col2-100&&yy<=col2+100)); //305
		//boolean normal2=((xx>=row2-100&&xx<=row2+100)&&(yy>=col2-100&&yy<=col2+100)); //307
		//boolean normal3=((xx>=row3-100&&xx<=row3+100)&&(yy>=col2-100&&yy<=col2+100)); //309
		
		//boolean hard1=((xx>=row1-100&&xx<=row1+100)&&(yy>=col3-100&&yy<=col3+100)); //312
		//boolean hard2=((xx>=row2-100&&xx<=row2+100)&&(yy>=col3-100&&yy<=col3+100)); //314
		//boolean hard3=((xx>=row3-100&&xx<=row3+100)&&(yy>=col3-100&&yy<=col3+100)); //316
		
		//boolean isStart=((xx>=30&&xx<=350)&&(yy>=450&&yy<=600));//320, check for the start

		if(easy){
			price=3;
			level_x=443;
			level_y=180;
		}

		//else를 꼭 여기에 넣어야지만 잘 작동된다..?
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

		/*
		else{
			startW=-300;
			startH=-300;
		}
		*/
		repaint();
	}

	@Override
	public void run() {
		try{
			while(!mouseEventT.isInterrupted()){
				mouseClickEvent();
				mouseMoveEvent();

				mouseEventT.sleep(2);
			}
		}catch(InterruptedException ex){	
		} finally {
			System.out.println("mouseEvent Thread dead");
		}
	}
	
}
