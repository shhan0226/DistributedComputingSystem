package main;

import java.io.IOException;
import javax.swing.UIManager;

import ui.MainView;



public class main {	
	public static void main(String args[]) throws IOException {
		try {
			System.out.println("[Main.java] main()");

			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

		} catch (Exception e) {

			System.out.println("[Main.java] Error !");
			e.printStackTrace();

		}

		MainView mainView = MainView.getInstance();		
		// MainView mv = new MainView();
		
		
	}

}