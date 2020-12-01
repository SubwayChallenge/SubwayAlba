import javax.swing.*;

public class GameManagment extends JFrame {
	
	private GameManagment rootScreen; //53, 68, 82, 133
	private MainScreen mainScreen; //30, 31, 68, 70
	private GameScreen gameScreen; //34, 35, 36, 53, 55, 56
	private ChooseDifficulty chooseDifficulty; //39, 40, 82, 84
	private ResultScreen resultScreen; //43, 44, 96, 99
	
	BgmPlayer backgroundMusic; //115, 116, 117, 120, 134
	
	private CustomMouse mouseEventListener;
	
	public void clearFrame() { //51, 66, 80, 94
		
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
	
	public void moveToGameScreen(int targetScore) { //CS 266
		clearFrame();

		gameScreen = new GameScreen(mouseEventListener, rootScreen, targetScore, 60);
		setSize(1280, 720);
		addKeyListener(gameScreen);
		add(gameScreen);

		playBackgroundMusic("resource/sound/main_back.mp3");

		System.out.println("게임씬 이동");
		
		revalidate();
	}
	
	public void moveToMainScreen() { //146
		clearFrame();

		mainScreen = new MainScreen(1280, 720, mouseEventListener, rootScreen);
		setSize(1200, 720);
		add(mainScreen);

		playBackgroundMusic("resource/sound/main_back.mp3");
		
		System.out.println("메인씬 이동");
		
		revalidate();
	}
	
	public void moveToChooseDiffScreen() { //MS 89
		clearFrame();

		chooseDifficulty = new ChooseDifficulty(mouseEventListener, rootScreen);
		setSize(1200, 720);
		add(chooseDifficulty);

		playBackgroundMusic("resource/sound/clothing_back.mp3");
		
		System.out.println("난이도 고르기 이동");

		revalidate();
	}
	
	public void moveToResultScreen(boolean inputResult, int inputBurgerCount, int inputTimer) { //GS 140, 298
		clearFrame();

		resultScreen = new ResultScreen(inputResult, inputBurgerCount, inputTimer);
		setSize(1280, 720);
		//addKeyListener(gameScene);
		add(resultScreen);
		
		if(inputResult)
			playBackgroundMusic("resource/sound/mission_complete.mp3");
		else
			playBackgroundMusic("resource/sound/mission_fail.mp3");
		

		revalidate();
	}
	
	public void playEffectSound(String fileName) //GS 262, 275, 288, 291
	{
		BgmPlayer effectSound = new BgmPlayer(fileName);//쓰이지 않음
	}
	
	public void playBackgroundMusic(String fileName) { //58, 72, 86, 102, 104

		if(backgroundMusic != null){
			backgroundMusic.stop();
			backgroundMusic = null;
    	}

		backgroundMusic = new BgmPlayer(fileName);
    	
        /*
        AudioInputStream sound = AudioSystem.getAudioInputStream(new File(fileName));
        Clip clip = AudioSystem.getClip();
        clip.open(sound);
        clip.start();
        */
    }


	public GameManagment() {

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
		new GameManagment();
	}
}
