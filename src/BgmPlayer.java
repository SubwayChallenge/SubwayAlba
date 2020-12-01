import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class BgmPlayer implements Runnable{
	
	private Thread playT; //이 파일 내에서만 쓰임
	private Player bgm; //이 파일 내에서만 쓰임
	private String filePath; //이 파일 내에서만 쓰임
	
	public BgmPlayer(String inputFilePath) {
		filePath = inputFilePath;
		playT = new Thread(this);
		playT.start();
		//System.out.println("asd");
	}
	
	public void stop() {
		bgm.close();
	}
	
	@Override
	public void run() {
		try {
			FileInputStream fis = new FileInputStream(new File(filePath));
			bgm = new Player(fis);
			bgm.play();
        }catch (Exception ex) {
        }
		// TODO Auto-generated method stub
	}
}
