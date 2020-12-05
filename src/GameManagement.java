import javax.swing.*;

public class GameManagement extends JFrame {
	
	private GameManagement rootScreen;
	private MainScreen mainScreen;
	private GameScreen gameScreen;
	private ChooseDifficulty chooseDifficulty;
	private ResultScreen resultScreen;
	
	BgmPlayer backgroundMusic;
	
	private CustomMouse mouseEventListener;
	
	public void clearFrame() {
		
		if(mainScreen != null) {
			remove(mainScreen);
		}
		
		if(gameScreen != null) {
			removeKeyListener(gameScreen);
			remove(gameScreen);
		}
		
		if(chooseDifficulty != null) {
			remove(chooseDifficulty);
		}
		
		if(resultScreen != null) {
			remove(resultScreen);
		}
		
		repaint();
	}
	
	public void moveToGameScreen(int targetScore) {
		clearFrame();

		gameScreen = new GameScreen(mouseEventListener, rootScreen, targetScore, 60);
		setSize(1280, 720);
		addKeyListener(gameScreen);
		add(gameScreen);

		playBackgroundMusic("resource/sound/main_back.mp3");

		System.out.println("게임씬 이동");
		
		revalidate();
	}
	
	public void moveToMainScreen() {
		clearFrame();

		mainScreen = new MainScreen(1280, 720, mouseEventListener, rootScreen);
		setSize(1200, 720);
		add(mainScreen);

		playBackgroundMusic("resource/sound/main_back.mp3");
		
		System.out.println("메인씬 이동");
		
		revalidate();
	}
	
	public void moveToChooseDiffScreen() {
		clearFrame();

		chooseDifficulty = new ChooseDifficulty(mouseEventListener, rootScreen);
		setSize(1200, 720);
		add(chooseDifficulty);

		playBackgroundMusic("resource/sound/clothing_back.mp3");
		
		System.out.println("난이도 고르기 이동");

		revalidate();
	}
	
	public void moveToResultScreen(boolean inputResult, int inputsandwichCount, int inputTimer) {
		clearFrame();

		resultScreen = new ResultScreen(inputResult, inputsandwichCount, inputTimer);
		setSize(1280, 720);
		//addKeyListener(gameScene);
		add(resultScreen);
		
		if(inputResult)
			playBackgroundMusic("resource/sound/mission_complete.mp3");
		else
			playBackgroundMusic("resource/sound/mission_fail.mp3");
		

		revalidate();
	}
	
	public void playEffectSound(String fileName)
	{
		BgmPlayer effectSound = new BgmPlayer(fileName);
	}
	
	public void playBackgroundMusic(String fileName) {

		if(backgroundMusic != null){
			backgroundMusic.stop();
			backgroundMusic = null;
    	}

		backgroundMusic = new BgmPlayer(fileName);
    }

	public GameManagement() {

		rootScreen = this;
		backgroundMusic = null;
		
		setTitle("SUBWAY_ALBA");
		setSize(640, 800);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		mouseEventListener = new CustomMouse();
		addMouseListener(mouseEventListener);
	    addMouseMotionListener(mouseEventListener);

		moveToMainScreen();
	}
	
	public static void main(String[] args) {
		new GameManagement();
	}
}
