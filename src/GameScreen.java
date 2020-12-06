import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;

import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class GameScreen extends JPanel implements Runnable {

	//UI관련 상수
	private static final int maxSandwichIngredientCount = 7; //사용자가 만들 수 있는 샌드위치의 최대 층 수 = 7(빵1(b1~b5), 치즈1(c1~c3), 채소1(v1~v3), 채소2(s1~s5), 고기1(m1~m4), 소스1(d1~d3))

	private static final int SandwichIngredientLeftSpace = 225; //샌드위치 메뉴 왼쪽 여백
	private static final int SandwichIngredientTopSpace = 480;  //샌드위치 메뉴 위쪽 여백
	private static final int SandwichIngredientItemSpace = 150; //샌드위치 메뉴 아이템 여백

	private static final int SandwichIngredientItemWidth = 150;  //샌드위치 메뉴 아이템 넓이
	private static final int SandwichIngredientItemHeight = 150; //샌드위치 메뉴 아이템 높이

	private static final int timerTopSpace = 280;   //타이머 위쪽 여백
	private static final int timerWidth = 300;      //타이머 넓이
	private static final int timerHeight = 15;      //타이머 높이
	
	private ImageIcon background;

	//로직 변수

	private int selectedSandwichMenuNumber; //선택한 샌드위치 메뉴
	private int maxSandwichMenuNumber;      //현재 샌드위치 재료 종류(최소 2)
	
	private int madeCount;  //현재까지 만든 개수
	private int orderCount; //목표 샌드위치 개수
	
	private int gameTimer; //타이머, 시간 줄어드는게 1초 단위가 아닌거같아..ㅎㅎ너무 빨리 줄어든다
	private int timeLimit; //제한시간

	private String[] sandwichIngredientImgPath = {
			"", "resource/ingredient/b1.png", "resource/ingredient/b2.png", "resource/ingredient/b3.png", "resource/ingredient/b4.png", "resource/ingredient/b5.png",
			"resource/ingredient/ct1.png", "resource/ingredient/ct2.png", "resource/ingredient/ct3.png",
			"resource/ingredient/vt1.png", "resource/ingredient/vt2.png", "resource/ingredient/vt3.png",
			"resource/ingredient/st1.png", "resource/ingredient/st2.png", "resource/ingredient/st3.png", "resource/ingredient/st4.png", "resource/ingredient/st5.png",
			"resource/ingredient/mt1.png", "resource/ingredient/mt2.png", "resource/ingredient/mt3.png", "resource/ingredient/mt4.png",
			"resource/ingredient/chili-sauce.png", "resource/ingredient/mayonaisse.png", "resource/ingredient/mustard.png",
			"resource/ingredient/wrap.png"};
	//예시 샌드위치 만들 때 사용됨                                  1~5          6~8         9~11         12~16       17~20        21~24
	private String[] sandwichIngredientName = {//빵1(b1~b5), 치즈1(c1~c3), 채소1(v1~v3), 채소2(s1~s5), 고기1(m1~m4), 소스1(d1~d3) 순서!
			"", "b1", "b2", "b3", "b4", "b5", "c1", "c2", "c3", "v1", "v2", "v3", "s1", "s2", "s3", "s4", "s5", "m1", "m2", "m3", "m4", "d2", "d3", "d1", "finish"};
	private int[] maxSandwichMenu = {5,3,3,5,5,4,3,1};
	private SandwichIngredient[] userSandwich;
	private int userSandwichCount;
	private int[] exSandwich;
	private int maxExSandwichNumber;
	private int highscore;
	
	private CustomMouse mouse;
	private GameManagement gameScreen;
	
	private Thread gameT;
	
	private boolean flag;

	/*method*/
	public GameScreen(CustomMouse inputMouseListener, GameManagement inputRootFrame, int inputTargetScore, int inputTimer, int record) {
		
		background = new ImageIcon("resource/background3.png");
		
		mouse 	  = inputMouseListener;
		gameScreen = inputRootFrame;
		highscore = record;

		gameTimer = inputTimer * 600;
		timeLimit  = inputTimer * 600;

		gameT = new Thread(this);
		gameT.start();

		userSandwich = new SandwichIngredient[maxSandwichIngredientCount];
		userSandwichCount = 0;

		maxSandwichMenuNumber = 6;
		madeCount = 0;
		
		flag = false;

		orderCount = inputTargetScore;
		
		createExampleSandwich();
		
		this.setLayout(new GridBagLayout());
	}//construct
	
	//private int bug = 0 ;
	//TO-DO 스레드 중지안되는 오류 수정
	
	public void clearExitScene() {
		if(gameT!=null) {
			gameT.interrupt();
			System.gc();
			flag = true;
		}
	}//method clearExitScene - 현재 씬을 말끔히 지워준다.
	
	@Override
	public void run() {
		try{
			while(!flag){
				gameTimer--;
				
				mousesandwichMenuEvent();
				
				repaint();
				revalidate();

				if(gameTimer<=0 && orderCount != madeCount) {
					System.out.println("1");
					clearExitScene();
					gameScreen.moveToResultScreen(false, madeCount, gameTimer, highscore);
				}
				gameT.sleep(1);
			}
		}catch(InterruptedException ex){
		} finally {
			System.out.println("gameScreen Thread dead");
		}
	}//thread

	
	private void mousesandwichMenuEvent() {
		int clickPositionX = mouse.getMouseClickXPos();
		int clickPositionY = mouse.getMouseClickYPos();
		int positionX = mouse.getMouseXPos();
		int positionY = mouse.getMouseYPos();

		maxSandwichMenuNumber = maxSandwichMenu[userSandwichCount];

		for(int i=1; i<=maxSandwichMenuNumber; i++){
			if(userSandwichCount == 7){
				i =3;
			}
			// && positionY
			if(positionX > SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace) &&
			   positionX < SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace)+SandwichIngredientItemWidth &&
			   positionY > SandwichIngredientTopSpace && positionY < SandwichIngredientTopSpace+SandwichIngredientItemHeight) {

				selectedSandwichMenuNumber = i;
			   //revalidate();
			   //repaint();
			}
			
			if(clickPositionX > SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace) &&
			   clickPositionX < SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace)+SandwichIngredientItemWidth &&
			   clickPositionY > SandwichIngredientTopSpace+35 && clickPositionY < SandwichIngredientTopSpace+SandwichIngredientItemHeight+35) {

				System.out.println("click: " + selectedSandwichMenuNumber);

				if(userSandwichCount == 7) {
					sendSandwich();
					if(orderCount == madeCount){
						System.out.println("3");
						clearExitScene();
						gameScreen.moveToResultScreen(true, madeCount, gameTimer, highscore);
						clearExitScene();
					}
					userSandwichCount = 0;
				}
				else {
					makeSandwich();
					userSandwichCount++;
				}
			}
		}
	}//method mousesandwichMenuEvent - 버거 메뉴의 마우스이벤트 처리

	
	private void createExampleSandwich() {
		Random random = new Random();

		maxExSandwichNumber = 7;//빵1(b1~b5), 치즈1(c1~c3), 채소1(v1~v3), 채소2(s1~s5), 고기1(m1~m4), 소스1(d1~d3)

		exSandwich = new int[maxExSandwichNumber];

		exSandwich[0] = 1 + random.nextInt(5); //빵, 1~5 중 1개
		exSandwich[1] = 6 + random.nextInt(3); //치즈, 6~8 중 1개
		exSandwich[2] = 9 + random.nextInt(3); //채소, 9~11 중 1개
		exSandwich[3] = 12 + random.nextInt(5);//채소, 12~16 중 1개
		exSandwich[4] = 12 + random.nextInt(5);//채소, 12~16 중 1개
		if(exSandwich[4]==exSandwich[3] && exSandwich[3] != 16){
			exSandwich[4]++;
		}
		else if(exSandwich[4]==exSandwich[3])
			exSandwich[4]--;
		exSandwich[5] = 17 + random.nextInt(4);//고기, 17~20 중 1개
		exSandwich[6] = 21 + random.nextInt(3);//소스, 21~23 중 1개

	}//method createExamplesandwich - 예시 샌드위치를 만든다, 그리는 부분(displayExSandwich)은 아래에 있음
	
	private void makeSandwich() {
		if(userSandwichCount < maxSandwichIngredientCount){
			if(userSandwich[userSandwichCount]==null)
				userSandwich[userSandwichCount] = new SandwichIngredient();

			if(userSandwichCount==0){
				userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber);
			}
			else if(userSandwichCount==1){
				userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber+5);
			}
			else if(userSandwichCount==2){
				userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber+8);
			}
			else if(userSandwichCount==3 || userSandwichCount==4){
				userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber+11);
			}
			else if(userSandwichCount==5){
				userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber+16);
			}
			else if(userSandwichCount==6){
				userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber+20);
			}

			gameScreen.playEffectSound("resource/sound/sandwich_stack.mp3");
		}
		else{
			System.out.println("You cannot stack any more");
		}
	}//method makeSandwich - 샌드위치 쌓기
	
	private void sendSandwich() {
		boolean solutionCheck=true;

		for(int i=0; i<userSandwichCount;i++){
			if(userSandwich[i].ingredientOrder!=exSandwich[i]){
				if(i==3 && userSandwich[i].ingredientOrder==exSandwich[i+1] && userSandwich[i+1].ingredientOrder==exSandwich[i]){
					i++;
					continue;
				}
				else {
					solutionCheck=false;
					break;
				}
			}
		}
		if(solutionCheck) {
			System.out.println("correct");
			madeCount += 1;
			gameScreen.playEffectSound("resource/sound/same_sandwich.mp3");
		}else{
			System.out.println("fail");
			gameScreen.playEffectSound("resource/sound/diff_sandwich.mp3");
		}
		createExampleSandwich();
	}//method sendsandwich - 현재 만든 샌드위치를 제출하고 정답 확인
	
	private void displayExSandwich(Graphics g) {
		if(maxExSandwichNumber > 0) {
			for(int i=0; i<maxExSandwichNumber; i++){
				ImageIcon sandwichIngredientImage = new ImageIcon("resource/menuItem/" + sandwichIngredientName[exSandwich[i]] + ".png");
				g.drawImage(sandwichIngredientImage.getImage(), 65, 110,190,195,this);
			}
		}
	}//method displayExamplesandwich - 만들어야 할 샌드위치 그리기
	
	private void displayUserSandwich(Graphics g) {
		if(userSandwichCount > 0) {
			for(int i=0; i<userSandwichCount; i++){
				int ingredientNumber = userSandwich[i].ingredientOrder;
				ImageIcon sandwichIngredientImage = new ImageIcon("resource/menuItem/" + sandwichIngredientName[ingredientNumber] + ".png");
				g.drawImage(sandwichIngredientImage.getImage(), 520, 90, this);
			}
		}
	}//method displayUsersandwich - 사용자가 만든 샌드위치를 화면에 그리기

	private void displayDone(Graphics g) {
		if(madeCount > 0) {
			for(int i=0; i<madeCount; i++){
				ImageIcon sandwichIngredientImage = new ImageIcon("resource/menuItem/" + sandwichIngredientName[24] + ".png");
				g.drawImage(sandwichIngredientImage.getImage(), 850 + 30*i, 300, 90, 90, this);
			}
		}
	}

	private void drawSandwich(Graphics g, int startNum){
		for(int i=1; i<=maxSandwichMenuNumber; i++) {

			if(i==selectedSandwichMenuNumber) {
				if(i==maxSandwichMenuNumber+1)
					g.setColor(Color.WHITE);
				else
					g.setColor(Color.orange);

				g.fillRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
			}
			ImageIcon menuItemImage = new ImageIcon(sandwichIngredientImgPath[i+startNum-1]);
			g.drawImage(menuItemImage.getImage(), SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, 150, 150,this);
		}
	}

	private void displayMenu(Graphics g) {
		if(userSandwichCount==0){//빵 고르기
			drawSandwich(g, 1);
		}
		else if(userSandwichCount==1){//치즈 고르기
			drawSandwich(g, 6);
		}
		else if(userSandwichCount==2){//채소 고르기 (v1~v3)
			drawSandwich(g, 9);
		}
		else if(userSandwichCount==3 || userSandwichCount==4){//채소 고르기 (s1~s5)
			drawSandwich(g, 12);
		}
		else if(userSandwichCount==5){//고기 고르기
			drawSandwich(g, 17);
		}
		else if(userSandwichCount==6){ //소스고르기
			drawSandwich(g, 21);
		}
		else if(userSandwichCount==7){
			ImageIcon menuItemImage = new ImageIcon(sandwichIngredientImgPath[24]);
			g.drawImage(menuItemImage.getImage(), (SandwichIngredientLeftSpace+20) + ((2)*SandwichIngredientItemSpace), (SandwichIngredientTopSpace-30), 200, 200,this);
		}

	}//method displayMenu - 화면 아래 샌드위치 재료들을 출력해준다

	private void displayUI(Graphics g) {
		Font font1 = new Font("Verdana", Font.PLAIN, 30);
		
		g.setFont(font1);
		g.setColor(Color.black);
		g.drawString(""+orderCount, this.getWidth() - 130 , 160);
		g.drawString(""+madeCount, this.getWidth() - 130 , 210);
		int mil  = gameTimer/ 60 % 10;
	    int sec  = gameTimer / 600 % 600;
	    
		g.drawString(sec + " : " + mil , this.getWidth()-130, 260);
		
		float test = ((float)gameTimer/timeLimit) * 100;
		
		g.setColor(Color.white);
		g.fillRect(this.getWidth() - 310, timerTopSpace, (timerWidth*(int)test)/100, timerHeight);
		g.setColor(Color.white);
		g.drawRect(this.getWidth() - 310, timerTopSpace, timerWidth, timerHeight);
	}
	
	private void displayBackUI(Graphics g) {
		g.drawImage(background.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	public void update(Graphics g) {
		displayBackUI(g);
		displayExSandwich(g);
		displayUserSandwich(g);
		displayDone(g);
		displayMenu(g);
		displayUI(g);
	}//method update - repaint시 화면 그리기
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g); 
		update(g);
	}//paint component
}

