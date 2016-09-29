import javax.swing.*;

class Minhoquinha extends JFrame
{
	DrawPanel drawPanel = new DrawPanel();	

	Minhoquinha() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(drawPanel);	
		this.setVisible(true);
		this.setBounds(0, 0, DrawPanel.SIZE, DrawPanel.SIZE);
		//this.setResizable(false);
		
		this.pack();
	}
	
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Minhoquinha app = new Minhoquinha();
			}	
		});
	}
}
