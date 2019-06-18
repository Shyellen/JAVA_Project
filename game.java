
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

public class game extends JFrame implements ActionListener
{
	public static final int WINDOW_WIDTH = 400;
	public static final int WINDOW_HEIGHT = 400;
	public static final int OBSTACLE_WIDTH = 50;
	public static final int OBSTACLE_HEIGHT = 50;
	public static final int PLAYER_WIDTH = 50;
	public static final int PLAYER_HEIGHT = 50;
	public static final int MOVE = 20;
	public static int PLAYER_X = 10;
	public static int PLAYER_Y = 350-PLAYER_HEIGHT;
	public static int OBSTACLE_X1 = 30;
	public static int OBSTACLE_X2 = 200;
	public static int OBSTACLE_Y = 0;
	public static long period = 500;
	public static int point = 0;
	
	public JButton leftButton;
	public JButton rightButton;
	
	public boolean start = false;
	public boolean end = false;
	
	
	public static void main(String[] args) {
		game play = new game();
		play.setVisible(true);
	}
	
	public game()
	{
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("장애물 피하기"); 
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        leftButton = new JButton("게임시작");
        leftButton.addActionListener(this);
        buttonPanel.add(leftButton);
        
        rightButton = new JButton("끝내기");
        rightButton.addActionListener(this);
        buttonPanel.add(rightButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void playing() {
		double randomValue = Math.random();
		OBSTACLE_X1 = (int)(randomValue * 340)+10;
		while((OBSTACLE_X1 - OBSTACLE_X2 < 70 && OBSTACLE_X1 - OBSTACLE_X2 > 0)|| (OBSTACLE_X1 - OBSTACLE_X2 < 70 && OBSTACLE_X2 - OBSTACLE_X1 < 70)) {
			randomValue = Math.random();
			OBSTACLE_X2 = (int)(randomValue * 340)+10;
		}
		
		Timer m_timer = new Timer();
		TimerTask m_task = new TimerTask() {
			public void run() {
				if((PLAYER_Y <= OBSTACLE_Y+50)
						&&((((OBSTACLE_X1+50>=PLAYER_X)&&(OBSTACLE_X1<=PLAYER_X)||(PLAYER_X+50 >= OBSTACLE_X1)&&(OBSTACLE_X1>=PLAYER_X)))
						|| (((OBSTACLE_X2+50>=PLAYER_X)&&(OBSTACLE_X2<=PLAYER_X)||(PLAYER_X+50 >= OBSTACLE_X2)&&(OBSTACLE_X2>=PLAYER_X))))) {
					m_timer.cancel();
					start = false;
					end = true;
					repaint();
				}
				else if(OBSTACLE_Y >= 300) {
					double randomValue = Math.random();
					OBSTACLE_X1 = (int)(randomValue * 340)+10;
					randomValue = Math.random();
					OBSTACLE_X2 = (int)(randomValue * 340)+10;
					OBSTACLE_Y = 0;
					period -= 25;
					point += 25;
					if(period <= 75)
						period = 50;
					m_timer.cancel();
					playing();
					repaint();
				}
				else {
					OBSTACLE_Y += 30;
					repaint();
				}
			}
		};
		m_timer.schedule(m_task, 300, period);
	}
	
	public void actionPerformed(ActionEvent e)
    {
        String actionCommand = e.getActionCommand( );

        if (actionCommand.equals("끝내기"))
            System.exit(0);
        else if (actionCommand.equals("게임시작") || actionCommand.equals("재시작")) { //초기값으로 변경.
        	leftButton.setText("<=");
        	rightButton.setText("=>");
        	PLAYER_X = 10;
        	OBSTACLE_X1 = 10;
        	OBSTACLE_Y = 0;
        	point = 0;
        	period = 500;
        	start = true;
        	end = false;
        	repaint();
        	playing();
        }
        else if ((actionCommand.equals("=>"))) {
        	if(PLAYER_X >= 320)
        		PLAYER_X = 340;
        	else
        		PLAYER_X += MOVE;	
            repaint();
        }
        else if ((actionCommand.equals("<="))) {
        	if(PLAYER_X <= MOVE+10)
        		PLAYER_X = 10;
        	else
        		PLAYER_X -= MOVE;	
            repaint();
        }
    }
	
	public void paint(Graphics g)
    {
        super.paint(g);
        
        if(start == true) {
        	 g.setColor(Color.BLUE);
             g.fillRect(PLAYER_X, PLAYER_Y, PLAYER_WIDTH, PLAYER_HEIGHT);
             
        	g.setColor(Color.RED);
        	g.fillRect(OBSTACLE_X1, OBSTACLE_Y, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
        	g.fillRect(OBSTACLE_X2, OBSTACLE_Y, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
        	
        	
        	
        	g.setColor(Color.BLUE);
            g.drawString(point+"점", 30, 375);
        }
        else if (end == true) {
        	g.setColor(Color.WHITE);
            g.drawString(point+"점", 10, 50);
            leftButton.setText("재시작");
            rightButton.setText("끝내기");
        }
    }
}