
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Color;

public class game extends JFrame implements ActionListener
{
	public static final int WINDOW_WIDTH = 400;
	public static final int WINDOW_HEIGHT = 400;
	public static final int OBSTACLE_WIDTH = 50;
	public static final int OBSTACLE_HEIGHT = 50;
	public static final int PLAYER_WIDTH = 50;
	public static final int PLAYER_HEIGHT = 50;
	public static final int MOVE = 30;
	public static int PLAYER_X = 10;
	public static int PLAYER_Y = 303;
	public static int OBSTACLE_X = 200;
	public static int OBSTACLE_Y = 40;
	public static int point = 0;
	public JButton leftButton;
	public JButton rightButton;
	public static long delay = 500; 
	public boolean start = false;
	
	public static void main(String[] args) {
		game play = new game();
		play.setVisible(true);
	}
	
	public game()
	{
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("MY GAME"); 
        setLayout(new BorderLayout( ));
        getContentPane( ).setBackground(Color.BLACK);
        
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
		//obstacle이 움직이는 거, player 좌표랑 obstacle 좌표가 겹쳤을 때 상황 구현
		//OBSTACLE_X는 랜덤생성.(10~340)
	}
	
	public void actionPerformed(ActionEvent e)
    {
        String actionCommand = e.getActionCommand( );

        if (actionCommand.equals("끝내기"))
            System.exit(0);
        else if (actionCommand.equals("게임시작") || actionCommand.equals("재시작")) {
        	//초기값으로 변경.
        	leftButton.setText("<=");
        	rightButton.setText("=>");
        	PLAYER_X = 10;
        	PLAYER_Y = 303;
        	OBSTACLE_X = 200;
        	OBSTACLE_Y = 40;
        	point = 0;
        	delay = 500;
        	start = true;
        	repaint();
        	//playing();
        }
        else if ((actionCommand.equals("=>"))) {
        	if(PLAYER_X > 320)
        		PLAYER_X = 340;
        	else
        		PLAYER_X += MOVE;	
            repaint();
        }
        else if ((actionCommand.equals("<="))) {
        	if(PLAYER_X < MOVE+10)
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
        	g.fillRect(OBSTACLE_X, OBSTACLE_Y, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
        	
        	g.setColor(Color.WHITE);
            g.drawString(point+"점", 10, 50);
        }
    }
}