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
	
	private static final int userSandwichBottomSpace = 320;  //사용자가 만든 샌드위치의 아래쪽 여백
	private static final int userSandwichWidth = 400;	     //사용자가 만든 샌드위치의 재료 넓이
	private static final int userSandwichHeight = 30;	     //사용자가 만든 샌드위치의 재료 높이
	
	private static final int exSandwichBottomSpace = 10; //예시 샌드위치의 아래쪽 여백
	private static final int exSandwichWidth = 200; 	 //예시 샌드위치의 재료 넓이
	private static final int exSandwichHeight = 10; 	 //예시 샌드위치의 재료 높이
	
	private static final int SandwichIngredientLeftSpace = 225; //샌드위치 메뉴 왼쪽 여백
	private static final int SandwichIngredientTopSpace = 500;  //샌드위치 메뉴 위쪽 여백
	private static final int SandwichIngredientItemSpace = 150; //샌드위치 메뉴 아이템 여백

	private static final int SandwichIngredientItemWidth = 150;  //샌드위치 메뉴 아이템 넓이
	private static final int SandwichIngredientItemHeight = 150; //샌드위치 메뉴 아이템 높이
	
	private static final int timerLeftSpace = 280;  //타이머 왼쪽 여백
	private static final int timerTopSpace = 320;   //타이머 위쪽 여백
	private static final int timerWidth = 300;      //타이머 넓이
	private static final int timerHeight = 30;      //타이머 높이
	
	private ImageIcon background;

	//로직 변수

	private int selectedSandwichMenuNumber; //선택한 샌드위치 메뉴
	private int maxSandwichMenuNumber;      //현재 샌드위치 재료 종류(최소 2)
	
	private int madeCount;  //현재까지 만든 개수
	private int orderCount; //목표 샌드위치 개수
	
	private int gameTimer; //타이머, 시간 줄어드는게 1초 단위가 아닌거같아..ㅎㅎ너무 빨리 줄어든다
	private int timeLimit; //제한시간

	private int phase = 0;

	private Color sandwichColor[] = {Color.WHITE, Color.pink, Color.orange, Color.DARK_GRAY, Color.YELLOW, Color.GREEN, Color.RED, Color.BLUE, Color.CYAN};
	//아래 재료 보여줄 때 사용됨
	private String sandwichIngredientImgPath[] = {
			"", "resource/ingredient/b1.png", "resource/ingredient/b2.png", "resource/ingredient/b3.png", "resource/ingredient/b4.png", "resource/ingredient/b5.png",
			"resource/ingredient/ct1.png", "resource/ingredient/ct2.png", "resource/ingredient/ct3.png",
			"resource/ingredient/vt1.png", "resource/ingredient/vt2.png", "resource/ingredient/vt3.png",
			"resource/ingredient/st1.png", "resource/ingredient/st2.png", "resource/ingredient/st3.png", "resource/ingredient/st4.png", "resource/ingredient/st5.png",
			"resource/ingredient/mt1.png", "resource/ingredient/mt2.png", "resource/ingredient/mt3.png", "resource/ingredient/mt4.png",
			"resource/ingredient/chili-sauce.png", "resource/ingredient/mayonaisse.png", "resource/ingredient/mustard.png",
			"resource/ingredient/wrap.png"};
	//예시 샌드위치 만들 때 사용됨                                  1~5          6~8         9~11         12~16       17~20        21~24
	private String sandwichIngredientName[] = {//빵1(b1~b5), 치즈1(c1~c3), 채소1(v1~v3), 채소2(s1~s5), 고기1(m1~m4), 소스1(d1~d3) 순서!
			"", "b1", "b2", "b3", "b4", "b5", "c1", "c2", "c3", "v1", "v2", "v3", "s1", "s2", "s3", "s4", "s5", "m1", "m2", "m3", "m4", "d2", "d3", "d1", "finish"};
	
	private SandwichIngredient userSandwich[];
	private int userSandwichCount;
	private int userSandwichNum[];
	private int exSandwich[];
	private int maxExSandwichNumber;
	
	private CustomMouse mouse;
	private GameManagement rootScreen;
	
	private Thread gameT;
	
	private boolean flag;

	/*method*/
	public GameScreen(CustomMouse inputMouseListener, GameManagement inputRootFrame, int inputTargetScore, int inputTimer) {
		
		background = new ImageIcon("resource/background3.png");
		
		mouse 	  = inputMouseListener;
		rootScreen = inputRootFrame;
		
		gameTimer = inputTimer * 600;
		timeLimit  = inputTimer * 600;

		gameT = new Thread(this);
		gameT.start();
		//다른 스레드간 접근과 애매모호해지는걸 방지해 클래스내에서 스레드를 관리하는것이 좋음

		selectedSandwichMenuNumber = 1;
		maxSandwichMenuNumber = 7; //빵1(b1~b5), 치즈1(c1~c3), 채소1(v1~v3), 채소2(s1~s5), 고기1(m1~m4), 소스1(d1~d3)->총 7개


		userSandwich = new SandwichIngredient[maxSandwichIngredientCount];
		userSandwichCount = 0;

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
//			gameThread.interrupt(); 
//			System.gc();
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
				
				if(gameTimer<=0) {
					clearExitScene();
					rootScreen.moveToResultScreen(false, madeCount, gameTimer);
				}
				gameT.sleep(1);
			}
		}catch(InterruptedException ex){
			
		} finally {
			System.out.println("gameScene Thread dead");
		}
		
	}//thread

	
	private void mousesandwichMenuEvent() {
		int clickPositionX = mouse.getMouseClickXPos();
		int clickPositionY = mouse.getMouseClickYPos();
		int positionX = mouse.getMouseXPos();
		int positionY = mouse.getMouseYPos();
		
		for(int i=1; i<=maxSandwichMenuNumber; i++){
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
			   clickPositionY > SandwichIngredientTopSpace && clickPositionY < SandwichIngredientTopSpace+SandwichIngredientItemHeight) {

				System.out.println("click: " + selectedSandwichMenuNumber);

				if(phase == 7) {
					sendSandwich();
					phase=0;
				}
				else {
					makeSandwich();
					phase++;
				}
			}
		}
		clickPositionX = -1;
		clickPositionY = -1;
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

			if(phase==0){
				userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber);
				userSandwichCount++;
			}
			else if(phase==1){
				userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber+5);
				userSandwichCount++;
			}
			else if(phase==2){
				userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber+8);
				userSandwichCount++;
			}
			else if(phase==3 || phase==4){
				userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber+11);
				userSandwichCount++;
			}
			else if(phase==5){
				userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber+16);
				userSandwichCount++;
			}
			else if(phase==6){
				userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber+20);
				userSandwichCount++;
			}

			rootScreen.playEffectSound("resource/sound/sandwich_stack.mp3");
		}
		else{
			System.out.println("You cannot stack any more");
		}
	}//method makeSandwich - 샌드위치 쌓기
	
	private void sendSandwich() {
		int temp=0;
		boolean solutionCheck=true;

		if(userSandwichCount != maxExSandwichNumber){
			System.out.println("fail");

			rootScreen.playEffectSound("resource/sound/not_correct.mp3");
		}
		else{
			for(int i=0; i<userSandwichCount;i++){

				if(phase==3 && userSandwich[i].ingredientOrder!=exSandwich[i]){
					if(userSandwich[i].ingredientOrder!=exSandwich[i+1]){
						solutionCheck=false;
						break;
					}
					else
						continue;
				}
				else if(phase==4 && userSandwich[i].ingredientOrder!=exSandwich[i]){
					if(userSandwich[i].ingredientOrder!=exSandwich[i-1]){
						solutionCheck=false;
						break;
					}
					else
						continue;
				}
				else if(userSandwich[i].ingredientOrder!=exSandwich[i]){
					solutionCheck=false;
					break;
				}
			}
			if(solutionCheck) {
				System.out.println("correct");
				madeCount += 1;
				rootScreen.playEffectSound("resource/sound/correct.mp3");
			}else{
				System.out.println("fail");
				rootScreen.playEffectSound("resource/sound/not_correct.mp3");
			}
			//검사
		}
		if(madeCount >= orderCount) {
			clearExitScene();
			rootScreen.moveToResultScreen(true, madeCount, gameTimer);
		}

		userSandwichCount = 0;

		createExampleSandwich();
		
	}//method sendsandwich - 현재 만든 샌드위치를 제출하고 정답 확인
	
	private void displayExSandwich(Graphics g) {
		
		if(maxExSandwichNumber > 0) {
			for(int i=0; i<maxExSandwichNumber; i++){

				ImageIcon sandwichIngredientImage = new ImageIcon("resource/menuItem/" + sandwichIngredientName[exSandwich[i]] + ".png");
				g.drawImage(sandwichIngredientImage.getImage(), 50, 100,250,250,this);
			}
		}
	}//method displayExamplesandwich - 만들어야 할 샌드위치 그리기
	
	private void displayUserSandwich(Graphics g) {

		if(userSandwichCount > 0) {
			for(int i=0; i<userSandwichCount; i++){
				//System.out.println("i층 샌드위치 내용물 : " + examplesandwich[i]);
				int ingredientNumber = userSandwich[i].ingredientOrder;			//재료 순서
				int ingredientPositionY = userSandwich[i].ingredientYPos;
				int targetPositionY = userSandwichBottomSpace - (i*userSandwichHeight); //최종 위치

				ingredientPositionY = ingredientPositionY + 10;

				if(ingredientPositionY + 10 > targetPositionY){
					ingredientPositionY = targetPositionY;
				}else{
				}//샌드위치 애니메이션

				userSandwich[i].ingredientYPos = ingredientPositionY;
				ImageIcon sandwichIngredientImage = new ImageIcon("resource/menuItem/" + sandwichIngredientName[ingredientNumber] + ".png");
				g.drawImage(sandwichIngredientImage.getImage(), 520, 90, this);
			}
		}
	}//method displayUsersandwich - 사용자가 만든 샌드위치를 화면에 그리기

	private void drawSandwich(Graphics g, int startNum, int endNum){//사용자가 만든 샌드위치 그려주는 부분
		for(int i=startNum; i<=endNum; i++) {

			if(i==selectedSandwichMenuNumber) {
				if(i==maxSandwichMenuNumber)
					g.setColor(Color.WHITE);
				else
					g.setColor(sandwichColor[selectedSandwichMenuNumber]);

				g.fillRect(SandwichIngredientLeftSpace + ((i-startNum)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
			}
			ImageIcon menuItemImage = new ImageIcon(sandwichIngredientImgPath[i]);
			g.drawImage(menuItemImage.getImage(), SandwichIngredientLeftSpace + ((i-startNum)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, 150, 150,this);
		}
	}

	private void displayMenu(Graphics g) {

		if(phase==0){//빵 고르기
			drawSandwich(g, 1, 5);
		}
		else if(phase==1){//치즈 고르기
			drawSandwich(g, 6, 8);
		}
		else if(phase==2){//채소 고르기 (v1~v3)
			drawSandwich(g, 9, 11);
		}
		else if(phase==3 || phase==4){//채소 고르기 (s1~s5)
			drawSandwich(g, 12, 16);
		}
		else if(phase==5){//고기 고르기
			drawSandwich(g, 17, 20);
		}
		else if(phase==6){ //소스고르기
			drawSandwich(g, 21, 24);
		}
		else if(phase==7){ //phase=8인 경우, finish 누르는 거를 위해서ㅎㅎ
			//drawSandwich(g, 21, 24);
			ImageIcon menuItemImage = new ImageIcon(sandwichIngredientImgPath[24]);
			g.drawImage(menuItemImage.getImage(), (SandwichIngredientLeftSpace+20) + ((2)*SandwichIngredientItemSpace), (SandwichIngredientTopSpace-30), 200, 200,this);
		}

	}//method displayMenu - 화면 아래 샌드위치 재료들을 출력해준다


	private void displayUI(Graphics g) {
		Font font1 = new Font("Verdana", Font.PLAIN, 30);
		
		g.setFont(font1);
		g.setColor(Color.black);
		g.drawString("  Order Count : ", this.getWidth() - 340 , 200);//string for price of items
		g.drawString(""+orderCount, this.getWidth() - 80 , 200);
		g.drawString("  You made : ", this.getWidth() - 340 , 250);
		g.drawString(""+madeCount, this.getWidth() - 80 , 250);
		int sec  = gameTimer % 60;
	    int min  = gameTimer / 60 % 60;
	    
		g.drawString("  Time left   " + min + " : " + sec , this.getWidth() - 340 , 300);//string for price of items
		
		float test = ((float)gameTimer/timeLimit) * 100;
		
		g.setColor(Color.red);
		g.fillRect(this.getWidth() - 325, timerTopSpace, (timerWidth*(int)test)/100, timerHeight);
		g.setColor(Color.black);
		g.drawRect(this.getWidth() - 325, timerTopSpace, timerWidth, timerHeight);
	}
	
	private void displayBackUI(Graphics g) {
		g.drawImage(background.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	public void update(Graphics g) {
		displayBackUI(g);
		displayExSandwich(g);
		displayUserSandwich(g);
		displayMenu(g);
		displayUI(g);
	}//method update - repaint시 화면 그리기
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g); 
		update(g);
	}//paint component
}

