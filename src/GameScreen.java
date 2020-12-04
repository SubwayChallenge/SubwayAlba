import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class GameScreen extends JPanel implements KeyListener,Runnable {

	//UI관련 상수
	private static final int maxSandwichIngredientCount = 7;	//100, 253, 사용자가 만들 수 있는 샌드위치의 최대 층 수 = 7(빵1(b1~b5), 치즈1(c1~c3), 채소1(v1~v3), 채소2(s1~s5), 고기1(m1~m4), 소스1(d1~d3))
	
	private static final int userSandwichBottomSpace = 320; //347, 사용자가 만든 샌드위치의 아래쪽 여백
	private static final int userSandwichWidth = 400;	//339, 사용자가 만든 샌드위치의 재료 넓이
	private static final int userSandwichHeight = 30;	//347, 사용자가 만든 샌드위치의 재료 높이
	
	private static final int exSandwichBottomSpace = 10; //323, 예시 샌드위치의 아래쪽 여백
	private static final int exSandwichWidth = 200; //310, 314, 예시 샌드위치의 재료 넓이
	private static final int exSandwichHeight = 10; //323, 예시 샌드위치의 재료 높이
	
	private static final int SandwichIngredientLeftSpace = 180; //206, 207, 215, 216, 372, 383, 388 샌드위치 메뉴 왼쪽 여백
	private static final int SandwichIngredientTopSpace = 510; //208, 217, 372, 383, 388, 샌드위치 메뉴 위쪽 여백
	private static final int SandwichIngredientItemSpace = 130; //206, 207, 215, 216, 372, 383, 388, 샌드위치 메뉴 아이템 여백

	private static final int SandwichIngredientItemWidth = 120; //207, 216, 372, 383, 샌드위치 메뉴 아이템 넓이
	private static final int SandwichIngredientItemHeight = 120; //208, 217, 372, 383, 샌드위치 메뉴 아이템 높이
	
	private static final int timerLeftSpace = 280; //410, 412, 타이머 왼쪽 여백
	private static final int timerTopSpace = 320; //410, 412, 타이머 위쪽 여백
	private static final int timerWidth = 300; //410, 412, 타이머 넓이
	private static final int timerHeight = 30; //410, 412, 타이머 높이
	
	private ImageIcon background; //85, 416
	
	//로직 변수
	private int selectedSandwichMenuNumber; //97,164,165,167,171,172,174,178,210,219,221,259,375,379, 선택한 샌드위치 메뉴
	private int maxSancwichMenuNumber; //98,165,171,178,204,221,247,370,376, 현재 샌드위치 재료 종류(최소 2)
	
	private int madeCount; //103, 140, 287, 296, 298, 401, 현재까지 만든 개수
	private int orderCount; //107, 296, 399, 목표 샌드위치 개수
	
	private int gameTimer; //90,129,138,140,298,402,403,407, 타이머, 시간 줄어드는게 1초 단위가 아닌거같아..ㅎㅎ너무 빨리 줄어든다
	private int timeLimit; //91, 407, 제한시간

	private int phase = 1;

	/*

	 토핑데이터 목록

	 1 - 아래 빵
	 2 - 위 빵
	 3 - 고기패티
	 4 - 치즈
	 5 - 양상추
	 6 - 토마토
	 
	*/
	
	private Color sandwichColor[] = {Color.WHITE, Color.pink, Color.orange, Color.DARK_GRAY, Color.YELLOW, Color.GREEN, Color.RED, Color.BLUE, Color.CYAN}; //379
	//아래 재료 보여줄 때 사용됨
	private String sandwichIngredientImgPath[] = { //386
			"", "resource/ingredient/b1.png", "resource/ingredient/b2.png", "resource/ingredient/b3.png", "resource/ingredient/b4.png", "resource/ingredient/b5.png",
			"resource/ingredient/ct1.png", "resource/ingredient/ct2.png", "resource/ingredient/ct3.png",
			"resource/ingredient/vt1.png", "resource/ingredient/vt2.png", "resource/ingredient/vt3.png",
			"resource/ingredient/st1.png", "resource/ingredient/st2.png", "resource/ingredient/st3.png", "resource/ingredient/st4.png", "resource/ingredient/st5.png",
			"resource/ingredient/mt1.png", "resource/ingredient/mt2.png", "resource/ingredient/mt3.png", "resource/ingredient/mt4.png",
			"resource/ingredient/chili-sauce.png", "resource/ingredient/mayonaisse.png", "resource/ingredient/mustard.png",
			"resource/ingredient/finish.png"};
	//예시 샌드위치 만들 때 사용됨                                  1~5          6~8         9~11         12~16       17~20        21~24
	private String sandwichIngredientName[] = {//322, 361, 빵1(b1~b5), 치즈1(c1~c3), 채소1(v1~v3), 채소2(s1~s5), 고기1(m1~m4), 소스1(d1~d3) 순서!
			"", "b1", "b2", "b3", "b4", "b5", "c1", "c2", "c3", "v1", "v2", "v3", "s1", "s2", "s2", "s4", "s5", "m1", "m2", "m3", "m4", "d1", "d2", "d3",
			"finish"};
	
	private SandwichIngredient userSandwich[]; //100,256,257,259,273,279,344,345,346,358
	private int userSandwichCount; //101,253,256,257,259,260,273,278,301,341,342
	
	private int exSandwich[]; //242,244,245,247,279,322
	private int maxExSandwichNumber; //240,242,245,246,273,316,317
	
	private CustomMouse mouse;
	private GameManagment rootScreen; //88,140,262,275,288,291,298
	
	private Thread gameT; //93,94,118,143
	
	private boolean flag; //105,121,128

	/*method*/
	public GameScreen(CustomMouse inputMouseListener, GameManagment inputRootFrame, int inputTargetScore, int inputTimer) {
		
		background = new ImageIcon("resource/background3.png");
		
		mouse 	  = inputMouseListener;
		rootScreen = inputRootFrame;
		
		gameTimer = inputTimer * 60;
		timeLimit  = inputTimer * 60;

		gameT = new Thread(this);
		gameT.start();
		//다른 스레드간 접근과 애매모호해지는걸 방지해 클래스내에서 스레드를 관리하는것이 좋음

		selectedSandwichMenuNumber = 1;
		maxSancwichMenuNumber = 7; //빵1(b1~b5), 치즈1(c1~c3), 채소1(v1~v3), 채소2(s1~s5), 고기1(m1~m4), 소스1(d1~d3)->총 7개

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
				
				mouseBurgerMenuEvent();
				
				repaint();
				revalidate();
				//1초에 한번 - 1000
				//1초에 60번 - 17
				
				if(gameTimer<=0) {
					clearExitScene();
					rootScreen.moveToResultScreen(false, madeCount, gameTimer);
				}

				gameT.sleep(2);
			}
		}catch(InterruptedException ex){
			
		} finally {
			System.out.println("gameScene Thread dead");
		}
		
	}//thread

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT : {
				if(selectedSandwichMenuNumber <= 1)
					selectedSandwichMenuNumber = maxSancwichMenuNumber;
				else
					selectedSandwichMenuNumber--;
				break;
			}
			case KeyEvent.VK_RIGHT : {
				if(selectedSandwichMenuNumber >= maxSancwichMenuNumber)
					selectedSandwichMenuNumber = 1;
				else
					selectedSandwichMenuNumber++;
				break;
			}
			case KeyEvent.VK_SPACE :{
				if(selectedSandwichMenuNumber == maxSancwichMenuNumber)
					sendSandwich();
				else
					makeSandwich();
				break;
			}
			case KeyEvent.VK_UP : {
				break;
			}
			case KeyEvent.VK_DOWN : {
				break;
			}
			case KeyEvent.VK_ENTER : {
				clearExitScene();
			}
		}
		//revalidate();
		//repaint();
	}//method keyPressed - 버거메뉴에 대한 키이벤트 처리
	
	private void mouseBurgerMenuEvent() {
		int clickPositionX = mouse.getMouseClickXPos();
		int clickPositionY = mouse.getMouseClickYPos();
		int positionX = mouse.getMouseXPos();
		int positionY = mouse.getMouseYPos();
		
		for(int i=1; i<=maxSancwichMenuNumber; i++){
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

				if(selectedSandwichMenuNumber == maxSancwichMenuNumber)
					sendSandwich();
				else
					makeSandwich();

				phase++;
			}
		}
		
		clickPositionX = -1;
		clickPositionY = -1;
	}//method mouseBurgerMenuEvent - 버거 메뉴의 마우스이벤트 처리

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	private void createExampleSandwich() {
		Random random = new Random();

		maxExSandwichNumber = 7;//7, 빵1(b1~b5), 치즈1(c1~c3), 채소1(v1~v3), 채소2(s1~s5), 고기1(m1~m4), 소스1(d1~d3)

		exSandwich = new int[maxExSandwichNumber];

		exSandwich[0] = 1 + random.nextInt(5); //빵, 1~5 중 1개
		exSandwich[1] = 6 + random.nextInt(3); //치즈, 6~8 중 1개
		exSandwich[2] = 9 + random.nextInt(3);//채소, 9~11 중 1개
		exSandwich[3] = 12 + random.nextInt(5);//채소, 12~16 중 1개
		exSandwich[4] = 12 + random.nextInt(5);//채소, 12~16 중 1개
		exSandwich[5] = 17 + random.nextInt(4);//고기, 17~20 중 1개
		exSandwich[6] = 21 + random.nextInt(3);//소스, 21~23 중 1개

	}//method createExampleBurger - 예시 샌드위치를 만든다, 그리는 부분(displayExSandwich)은 아래에 있음
	
	private void makeSandwich() { //181,224
		if(userSandwichCount < maxSandwichIngredientCount){
			//System.out.println("makeSandwich check");
			
			if(userSandwich[userSandwichCount]==null)
				userSandwich[userSandwichCount] = new SandwichIngredient();

			userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber);
			userSandwichCount++;//

			rootScreen.playEffectSound("resource/sound/burger_stack.mp3");
			
		}
		else{
			System.out.println("You cannot stack any more");
		}
	}//method makeSandwich - 샌드위치 쌓기
	
	private void sendSandwich() { //179,222
		boolean solutionCheck = true;
		
		if(userSandwichCount != maxExSandwichNumber || userSandwich[0].ingredientOrder != 2 || userSandwich[maxExSandwichNumber-1].ingredientOrder != 1){
			System.out.println("fail");
			rootScreen.playEffectSound("resource/sound/not_correct.mp3");
		}
		else{
			for(int i=0; i<userSandwichCount; i++) {
				if(userSandwich[i].ingredientOrder != exSandwich[i]){
					solutionCheck = false;
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
		
	}//method sendBurger - 현재 만든 샌드위치를 제출하고 정답 확인
	
	private void displayExSandwich(Graphics g) { //421
		int exampleBurgerPositionX;
		
		exampleBurgerPositionX = (this.getWidth()/10) - (exSandwichWidth/2);
		
		g.setColor(Color.BLACK);
		
		g.drawRect(	exampleBurgerPositionX-20, 10, exSandwichWidth + 60, 260);
		
		if(maxExSandwichNumber > 0) {
			for(int i=0; i<maxExSandwichNumber; i++){

				ImageIcon burgerIngredientImage = new ImageIcon("resource/menuItem/" + sandwichIngredientName[exSandwich[i]] + ".png");
				g.drawImage(burgerIngredientImage.getImage(), 10, exSandwichBottomSpace - (i*exSandwichHeight),this);
				
			}
		}
	}//method displayExampleBurger - 만들어야 할 샌드위치 그리기
	
	private void displayUserSandwich(Graphics g) { //422
		int userSandwichPositionX;

		userSandwichPositionX = (this.getWidth()/2) - (userSandwichWidth/2);
		
		if(userSandwichCount > 0) {
			for(int i=0; i<userSandwichCount; i++){
				//System.out.println("i층 샌드위치 내용물 : " + exampleBurger[i]);
				int ingredientNumber = userSandwich[i].ingredientOrder;			//재료 순서
				int ingredientPositionX = userSandwich[i].ingredientXPos;
				int ingredientPositionY = userSandwich[i].ingredientYPos;
				int targetPositionY = userSandwichBottomSpace - (i*userSandwichHeight); //최종 위치
				
				ingredientPositionY = ingredientPositionY + 10;
				
				if(ingredientPositionY + 10 > targetPositionY){
					ingredientPositionY = targetPositionY;
				}else{
//					repaint();
//					revalidate();
				}//샌드위치 애니메이션

				userSandwich[i].ingredientYPos = ingredientPositionY;
			
				//g.setColor(burgerColor[ingredientNumber]);
				ImageIcon burgerIngredientImage = new ImageIcon("resource/menuItem/" + sandwichIngredientName[ingredientNumber] + ".png");
				g.drawImage(burgerIngredientImage.getImage(), 520, 130, this);
				//g.fillRect(userBurgerPositionX, ingredientPositionY, userBurgerWidth, userBurgerHeight);
				
			}
		}
	}//method displayUserBurger - 사용자가 만든 샌드위치를 화면에 그리기
	
	private void displayMenu(Graphics g) {

		if(phase==1){
			for(int i=1; i<=5; i++) {//maxSancwichMenuNumber
				g.setColor(Color.BLACK);
				g.drawRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
				//drawRect(사각형선만 그림) - 시작x, 시작y, width, height
				if(i==selectedSandwichMenuNumber) {
					if(i==maxSancwichMenuNumber)
						g.setColor(Color.WHITE); //제출메뉴
					else
						g.setColor(sandwichColor[selectedSandwichMenuNumber]);

					//ImageIcon menuItemImage = new ImageIcon(burgerMenuItemImagePath[i] + ".png");
					//g.drawImage(menuItemImage.getImage(), burgerManuLeftSide + ((i-1)*burgerMenuItemGap), burgerMenuTopSide,this);
					g.fillRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
				}
				ImageIcon menuItemImage = new ImageIcon(sandwichIngredientImgPath[i]);
				//g.drawImage(menuItemImage.getImage(), this.getWidth()/2, burgerMenuTopSide, this);
				g.drawImage(menuItemImage.getImage(), SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, 120, 120,this);
			}
		}
		else if(phase==2){
			for(int i=1; i<=3; i++) {//maxSancwichMenuNumber
				g.setColor(Color.BLACK);
				g.drawRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
				//drawRect(사각형선만 그림) - 시작x, 시작y, width, height
				if(i==selectedSandwichMenuNumber) {
					if(i==maxSancwichMenuNumber)
						g.setColor(Color.WHITE); //제출메뉴
					else
						g.setColor(sandwichColor[selectedSandwichMenuNumber]);

					//ImageIcon menuItemImage = new ImageIcon(burgerMenuItemImagePath[i] + ".png");
					//g.drawImage(menuItemImage.getImage(), burgerManuLeftSide + ((i-1)*burgerMenuItemGap), burgerMenuTopSide,this);
					g.fillRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
				}
				ImageIcon menuItemImage = new ImageIcon(sandwichIngredientImgPath[i+5]);
				//g.drawImage(menuItemImage.getImage(), this.getWidth()/2, burgerMenuTopSide, this);
				g.drawImage(menuItemImage.getImage(), SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, 120, 120,this);
			}
		}
		else if(phase==3){
			for(int i=1; i<=3; i++) {//maxSancwichMenuNumber
				g.setColor(Color.BLACK);
				g.drawRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
				//drawRect(사각형선만 그림) - 시작x, 시작y, width, height
				if(i==selectedSandwichMenuNumber) {
					if(i==maxSancwichMenuNumber)
						g.setColor(Color.WHITE); //제출메뉴
					else
						g.setColor(sandwichColor[selectedSandwichMenuNumber]);

					//ImageIcon menuItemImage = new ImageIcon(burgerMenuItemImagePath[i] + ".png");
					//g.drawImage(menuItemImage.getImage(), burgerManuLeftSide + ((i-1)*burgerMenuItemGap), burgerMenuTopSide,this);
					g.fillRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
				}
				ImageIcon menuItemImage = new ImageIcon(sandwichIngredientImgPath[i+8]);
				//g.drawImage(menuItemImage.getImage(), this.getWidth()/2, burgerMenuTopSide, this);
				g.drawImage(menuItemImage.getImage(), SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, 120, 120,this);
			}
		}
		else if(phase==4){
			for(int i=1; i<=5; i++) {//maxSancwichMenuNumber
				g.setColor(Color.BLACK);
				g.drawRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
				//drawRect(사각형선만 그림) - 시작x, 시작y, width, height
				if(i==selectedSandwichMenuNumber) {
					if(i==maxSancwichMenuNumber)
						g.setColor(Color.WHITE); //제출메뉴
					else
						g.setColor(sandwichColor[selectedSandwichMenuNumber]);

					//ImageIcon menuItemImage = new ImageIcon(burgerMenuItemImagePath[i] + ".png");
					//g.drawImage(menuItemImage.getImage(), burgerManuLeftSide + ((i-1)*burgerMenuItemGap), burgerMenuTopSide,this);
					g.fillRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
				}
				ImageIcon menuItemImage = new ImageIcon(sandwichIngredientImgPath[i+11]);
				//g.drawImage(menuItemImage.getImage(), this.getWidth()/2, burgerMenuTopSide, this);
				g.drawImage(menuItemImage.getImage(), SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, 120, 120,this);
			}
		}
		else if(phase==5){
			for(int i=1; i<=4; i++) {//maxSancwichMenuNumber
				g.setColor(Color.BLACK);
				g.drawRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
				//drawRect(사각형선만 그림) - 시작x, 시작y, width, height
				if(i==selectedSandwichMenuNumber) {
					if(i==maxSancwichMenuNumber)
						g.setColor(Color.WHITE); //제출메뉴
					else
						g.setColor(sandwichColor[selectedSandwichMenuNumber]);

					//ImageIcon menuItemImage = new ImageIcon(burgerMenuItemImagePath[i] + ".png");
					//g.drawImage(menuItemImage.getImage(), burgerManuLeftSide + ((i-1)*burgerMenuItemGap), burgerMenuTopSide,this);
					g.fillRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
				}
				ImageIcon menuItemImage = new ImageIcon(sandwichIngredientImgPath[i+16]);
				//g.drawImage(menuItemImage.getImage(), this.getWidth()/2, burgerMenuTopSide, this);
				g.drawImage(menuItemImage.getImage(), SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, 120, 120,this);
			}
		}
		else{ //phase==6인 경우
			for(int i=1; i<=4; i++) {//maxSancwichMenuNumber
				g.setColor(Color.BLACK);
				g.drawRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
				//drawRect(사각형선만 그림) - 시작x, 시작y, width, height
				if(i==selectedSandwichMenuNumber) {
					if(i==4)
						g.setColor(Color.WHITE); //제출메뉴
					else
						g.setColor(sandwichColor[selectedSandwichMenuNumber]);

					//ImageIcon menuItemImage = new ImageIcon(burgerMenuItemImagePath[i] + ".png");
					//g.drawImage(menuItemImage.getImage(), burgerManuLeftSide + ((i-1)*burgerMenuItemGap), burgerMenuTopSide,this);
					g.fillRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
				}
				ImageIcon menuItemImage = new ImageIcon(sandwichIngredientImgPath[i+20]);
				//g.drawImage(menuItemImage.getImage(), this.getWidth()/2, burgerMenuTopSide, this);
				g.drawImage(menuItemImage.getImage(), SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, 120, 120,this);
			}
			phase=1; //다시 빵부터 시작
		}

		//고쳐보쟝!!

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

