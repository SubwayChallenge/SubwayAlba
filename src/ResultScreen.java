import javax.swing.*;

import java.awt.event.*;
import java.awt.*;


public class ResultScreen extends JPanel {
	Container contentPane;
	//ImageIcon retry = new ImageIcon("resource/retry.png");
	ImageIcon successImg = new ImageIcon("resource/success.png"); //49
	ImageIcon failImg = new ImageIcon("resource/failback.png"); //배경화면 로딩, 64
	private boolean results; //20, 47
	//JButton btn 	= new JButton("resource/retry.png");
	
	private int resultTime;//21, 54, 55
	private int resultSandwichCount;//22, 65
	
	ResultScreen(boolean gameResult, int sandwichCount, int leftTime){
		//this.setLayout(new GridBagLayout());

		results = gameResult;//여기에만 쓰임
		resultTime = leftTime;//여기에만 쓰임
		resultSandwichCount = sandwichCount;//여기에만 쓰임
	}
	
	/*
	void creatMenu() {
		JMenuBar mb = new JMenuBar();
		JMenuItem [] menuItem = new JMenuItem[2];
		String [] itemTitle = {"Retry", "Exit"};
		JMenu fileMenu = new JMenu("Menu");
		
		for(int i=0; i<menuItem.length; i++) 
		{
			menuItem[i] = new JMenuItem(itemTitle[i]);
			
		fileMenu.add(menuItem[i]);
		}		
		mb.add(fileMenu);
		setJMenuBar(mb);
	}
	*/
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Font resultFont = new Font("Verdana", Font.PLAIN, 30); //52, 62줄
		
		if(results) {
			
			g.drawImage(successImg.getImage(), 0, 0, getWidth(), getHeight(), this);
			
			g.setFont(resultFont);
			g.setColor(Color.red);
			
			int sec  = resultTime % 60;
		    int min  = resultTime / 60 % 60;
		    
			g.drawString(min + " : " + sec  + " left! You made it!", this.getWidth()/2 - 160 , 300);//string for price of items
			
		}
		else{
			g.setFont(resultFont);
			g.setColor(Color.red);
			
			g.drawImage(failImg.getImage(), 0, 0, getWidth(), getHeight(), this);
			g.drawString("You made "+ resultSandwichCount + " sandwiches", this.getWidth()/2 - 260 , 100);
		}
	}
}
