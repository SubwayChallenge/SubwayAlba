import javax.swing.*;

public class GameManagement extends JFrame {
	
	private GameManagement rootScreen;
	private StartScreen startScreen;
	private GameScreen gameScreen;
	private ChooseDifficulty chooseDifficulty;
	private ResultScreen resultScreen;
	BgmPlayer backgroundMusic;
	private int highscore;
	
	private CustomMouse mouseEventListener;
	
	public void clearFrame() {
		
		if(startScreen != null) {
			remove(startScreen);
		}

		if(chooseDifficulty != null) {
			remove(chooseDifficulty);
		}
		
		if(resultScreen != null) {
			remove(resultScreen);
		}
		
		repaint();
	}
	
	public void moveToStartScreen(int record) {
		clearFrame();
		highscore = record;
		startScreen = new StartScreen(1280, 720, mouseEventListener, rootScreen, record);
		setSize(1200, 720);
		add(startScreen);

		playBackgroundMusic("resource/sound/background_music.mp3");
		
		System.out.println("메인씬 이동");
		
		revalidate();
	}

	public void moveToChooseDiffScreen(int record) {
		clearFrame();

		chooseDifficulty = new ChooseDifficulty(mouseEventListener, rootScreen,record);
		setSize(1200, 720);
		add(chooseDifficulty);

		playBackgroundMusic("resource/sound/choose_level.mp3");
		
		System.out.println("난이도 고르기 이동");

		revalidate();
	}

	public void moveToGameScreen(int targetScore, int record) {
		clearFrame();

		gameScreen = new GameScreen(mouseEventListener, rootScreen, targetScore, 60,record);
		setSize(1200, 720);
		add(gameScreen);

		playBackgroundMusic("resource/sound/background_music.mp3");

		System.out.println("게임씬 이동");

		revalidate();
	}

	public void moveToResultScreen(boolean inputResult, int inputsandwichCount, int inputTimer, int record) {
		clearFrame();

		resultScreen = new ResultScreen(mouseEventListener,rootScreen, inputResult, inputsandwichCount, inputTimer, record);
		setSize(1200, 720);
		add(resultScreen);
		
		if(inputResult)
			playBackgroundMusic("resource/sound/mission_complete.mp3");
		else
			playBackgroundMusic("resource/sound/mission_fail.mp3");

		System.out.println("결과씬 이동");

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

		moveToStartScreen(highscore);
	}
	
	public static void main(String[] args) {
		new GameManagement();
	}
}
