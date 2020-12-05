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
	private static final int maxSandwichIngredientCount = 11;	//100, 253, 사용자가 만들 수 있는 샌드위치의 최대 층 수
	
	private static final int userSandwichBottomSpace = 320; //347, 사용자가 만든 샌드위치의 아래쪽 여백
	private static final int userSandwichWidth = 400;	//339, 사용자가 만든 샌드위치의 재료 넓이
	private static final int userSandwichHeight = 30;	//347, 사용자가 만든 샌드위치의 재료 높이
	
	private static final int exSandwichBottomSpace = 160; //323, 예시 샌드위치의 아래쪽 여백
	private static final int exSandwichWidth = 200; //310, 314, 예시 샌드위치의 재료 넓이
	private static final int exSandwichHeight = 10; //323, 예시 샌드위치의 재료 높이
	
	private static final int SandwichIngredientLeftSpace	= 180; //206, 207, 215, 216, 372, 383, 388 샌드위치 메뉴 왼쪽 여백
	private static final int SandwichIngredientTopSpace = 540; //208, 217, 372, 383, 388, 샌드위치 메뉴 위쪽 여백
	private static final int SandwichIngredientItemSpace = 130; //206, 207, 215, 216, 372, 383, 388, 샌드위치 메뉴 아이템 여백
	private static final int SandwichIngredientItemWidth = 120; //207, 216, 372, 383, 샌드위치 메뉴 아이템 넓이
	private static final int SandwichIngredientItemHeight = 120; //208, 217, 372, 383, 샌드위치 메뉴 아이템 높이
	
	private static final int timerLeftSpace = 280; //410, 412, 타이머 왼쪽 여백
	private static final int timerTopSpace = 30; //410, 412, 타이머 위쪽 여백
	private static final int timerWidth = 600; //410, 412, 타이머 넓이
	private static final int timerHeight = 30; //410, 412, 타이머 높이
	
	private ImageIcon background; //85, 416
	
	//로직 변수
	private int selectedSandwichMenuNumber; //97,164,165,167,171,172,174,178,210,219,221,259,375,379, 선택한 샌드위치 메뉴
	private int maxSandwichMenuNumber; //98,165,171,178,204,221,247,370,376, 현재 샌드위치 재료 종류(최소 2)
	
	private int madeCount; //103, 140, 287, 296, 298, 401, 현재까지 만든 개수
	private int orderCount; //107, 296, 399, 목표 샌드위치 개수
	
	private int gameTimer; //90,129,138,140,298,402,403,407, 타이머, 시간 줄어드는게 1초 단위가 아닌거같아..ㅎㅎ너무 빨리 줄어든다
	private int timeLimit; //91, 407, 제한시간
	
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
	private String sandwichIngredientImgPath[] = { //386
			"", "resource/menuItem/item1.png", "resource/menuItem/item2.png", "resource/menuItem/item3.png", "resource/menuItem/item4.png", "resource/menuItem/item5.png",
			"resource/menuItem/item6.png", "resource/menuItem/item7.png"};
	private String sandwichIngredientName[] =   { //322, 361
			"", "burgerbread_top", "burgerbread_bottom", "burgermeat", "burgercheese", "burgerlattuga", "burgertomato"};
	
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
		maxSandwichMenuNumber = 7;

		userSandwich = new SandwichIngredient[maxSandwichIngredientCount];
		userSandwichCount = 0;

		madeCount = 0;
		
		flag = false;

		orderCount = inputTargetScore;
		
		createExampleBurger();
		
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
					selectedSandwichMenuNumber = maxSandwichMenuNumber;
				else
					selectedSandwichMenuNumber--;
				break;
			}
			case KeyEvent.VK_RIGHT : {
				if(selectedSandwichMenuNumber >= maxSandwichMenuNumber)
					selectedSandwichMenuNumber = 1;
				else
					selectedSandwichMenuNumber++;
				break;
			}
			case KeyEvent.VK_SPACE :{
				if(selectedSandwichMenuNumber == maxSandwichMenuNumber)
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

				if(selectedSandwichMenuNumber == maxSandwichMenuNumber)
					sendSandwich();
				else
					makeSandwich();
			}
		}
		
		clickPositionX = -1;
		clickPositionY = -1;
	}//method mouseBurgerMenuEvent - 버거 메뉴의 마우스이벤트 처리

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	private void createExampleBurger() {
		Random random = new Random();

		maxExSandwichNumber = 3 + random.nextInt(10);

		exSandwich = new int[maxExSandwichNumber];

		exSandwich[0] = 2;
		exSandwich[maxExSandwichNumber-1] = 1;
		for(int i=1; i<=maxExSandwichNumber-2; i++){
			exSandwich[i] = 2 + random.nextInt(maxSandwichMenuNumber-2);
			//메뉴의 마지막은 제출메뉴가 되기때문에 -1을 해줌
		}
	}//method createExampleBurger - 예시 샌드위치를 만든다.
	
	private void makeSandwich() { //181,224
		if(userSandwichCount < maxSandwichIngredientCount){
			//System.out.println("샌드위치 생성");
			
			if(userSandwich[userSandwichCount]==null)
				userSandwich[userSandwichCount] = new SandwichIngredient();

			userSandwich[userSandwichCount].createSandwichIngredient(selectedSandwichMenuNumber);
			userSandwichCount++;

			rootScreen.playEffectSound("resource/sound/burger_stack.mp3");
			
		}
		else{
			System.out.println("You cannot stack any more");
		}
	}//method makeBurger - 샌드위치 쌓기
	
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
		
		createExampleBurger();
		
	}//method sendBurger - 현재 만든 샌드위치를 제출하고 정답 확인
	
	private void displayExSandwich(Graphics g) { //421
		int exampleBurgerPositionX;
		
		exampleBurgerPositionX = (this.getWidth()/10) - (exSandwichWidth/2);
		
		g.setColor(Color.BLACK);
		
		g.drawRect(	exampleBurgerPositionX-20, 10, exSandwichWidth + 60, 260);
		
		if(maxExSandwichNumber > 0) {
			for(int i=0; i<maxExSandwichNumber; i++){
				//System.out.println("i층 샌드위치 내용물 : " + exampleBurger[i]);
				//int tempColor = exampleBurger[i]; 
				//g.setColor(burgerColor[tempColor]);
				
				ImageIcon burgerLngredientImage = new ImageIcon("resource/exampleburger/" + sandwichIngredientName[exSandwich[i]] + ".png");
				g.drawImage(burgerLngredientImage.getImage(), exampleBurgerPositionX, exSandwichBottomSpace - (i*exSandwichHeight),this);
				
				/*
				g.fillRect(	exampleBurgerPositionX,
							exampleBurgerBottomSide - (i*exampleBurgerHeight),
							exampleBurgerWidth,
							exampleBurgerHeight);
				*/
				
			}
		}
	}//method displayExampleBurger - 만들어야 할 샌드위치 그리기
	
	private void displayUserSandwich(Graphics g) { //422
		int userBurgerPositionX;
		
		userBurgerPositionX = (this.getWidth()/2) - (userSandwichWidth/2);
		
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
				ImageIcon burgerLngredientImage = new ImageIcon("resource/" + sandwichIngredientName[ingredientNumber] + ".png");
				g.drawImage(burgerLngredientImage.getImage(), userBurgerPositionX, ingredientPositionY,this);
				//g.fillRect(userBurgerPositionX, ingredientPositionY, userBurgerWidth, userBurgerHeight);
				
			}
		}
	}//method displayUserBurger - 사용자가 만든 샌드위치를 화면에 그리기


	private void displayMenu(Graphics g) {
		for(int i=1; i<=maxSandwichMenuNumber; i++) {
			g.setColor(Color.BLACK);
			g.drawRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
			//drawRect(사각형선만 그림) - 시작x, 시작y, width, height
			
			if(i==selectedSandwichMenuNumber) {
				if(i==maxSandwichMenuNumber)
					g.setColor(Color.WHITE); //제출메뉴
				else
					g.setColor(sandwichColor[selectedSandwichMenuNumber]);
				
				//ImageIcon menuItemImage = new ImageIcon(burgerMenuItemImagePath[i] + ".png");
				//g.drawImage(menuItemImage.getImage(), burgerManuLeftSide + ((i-1)*burgerMenuItemGap), burgerMenuTopSide,this);
				g.fillRect(SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, SandwichIngredientItemWidth, SandwichIngredientItemHeight);
			}
			
			ImageIcon menuItemImage = new ImageIcon(sandwichIngredientImgPath[i]);
			//g.drawImage(menuItemImage.getImage(), this.getWidth()/2, burgerMenuTopSide, this);
			g.drawImage(menuItemImage.getImage(), SandwichIngredientLeftSpace + ((i-1)*SandwichIngredientItemSpace), SandwichIngredientTopSpace, this);
			
		}
	}//method displayMenu - 아래의 샌드위치 메뉴를 출력해준다

	private void displayUI(Graphics g) {
		Font font1 = new Font("Verdana", Font.PLAIN, 30);
		
		g.setFont(font1);
		g.setColor(Color.black);
		g.drawString("Order Count : ", this.getWidth() - 340 , 50);//string for price of items
		g.drawString(""+orderCount, this.getWidth() - 80 , 50);
		g.drawString("You made : ", this.getWidth() - 340 , 100);
		g.drawString(""+madeCount, this.getWidth() - 80 , 100);
		int sec  = gameTimer % 60;
	    int min  = gameTimer / 60 % 60;
	    
		g.drawString("Time left " + min + " : " + sec , this.getWidth()/2 - 360 , 100);//string for price of items
		
		float test = ((float)gameTimer/timeLimit) * 100;
		
		g.setColor(Color.red);
		g.fillRect(timerLeftSpace, timerTopSpace, (timerWidth*(int)test)/100, timerHeight);
		g.setColor(Color.black);
		g.drawRect(timerLeftSpace, timerTopSpace, timerWidth, timerHeight);
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

