import java.awt.Color;

import javax.swing.JFrame;


public class Main {

	public static void main(String[] args) {
	  JFrame obj = new JFrame();
        Gameplay game = new Gameplay();
        
        obj.setBounds(10, 10, 800, 630);
        obj.setTitle("Tank Battle");
          obj.setLocationRelativeTo(null); 
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(game);
        obj.setVisible(true);
	}

}
