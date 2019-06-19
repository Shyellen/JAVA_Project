
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



public class game extends JFrame implements ActionListener
{
	public static final int WINDOW_WIDTH = 400;
	public static final int WINDOW_HEIGHT = 400;
	public static final int OBSTACLE_WIDTH = 50;
	public static final int OBSTACLE_HEIGHT = 50;
	public static final int PLAYER_WIDTH = 50;
	public static final int PLAYER_HEIGHT = 50;
	public static final int MOVE = 20;
	
	public static int PLAYER_X = 10; //10 <= PLAYER_X <= WINDOW_WIDTH-10
	public static int PLAYER_Y = WINDOW_HEIGHT-50-PLAYER_HEIGHT; //50 = 상단바+하단 버튼 패널
	public static int OBSTACLE_X1 = 30;
	public static int OBSTACLE_X2 = 200;
	public static int OBSTACLE_Y = 0; // OBSTACLE_Y <= PLAYER_Y
	public static long period = 500;
	public static int point = 0;
	public static int bestScore = 0; 
	
	public JButton leftButton;
	public JButton rightButton;
	
	public boolean start = false; //게임 시작 상태
	public boolean end = false; //게임 오버 상태
	
	public JLabel title;
	
	public static final int POINT_SIZE = 40;
	private String theText = "GAME OVER";
    private Color penColor = Color.RED;
    private Font fontObject1 = 
                      new Font("SansSerif", Font.PLAIN, POINT_SIZE);
    private Font fontObject2 = 
            new Font("돋움", Font.PLAIN, 20);
	
	public static void main(String[] args) {
		game play = new game();
		play.setVisible(true);
	}
	
	public game()
	{
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("장애물 피하기");
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);
        
        ImageIcon ic1  = new ImageIcon("C:\\Users\\Owner\\Downloads\\자바\\Java_Project\\src\\title.png");
        title  = new JLabel(ic1);
  
        add(title, BorderLayout.CENTER);
        
       JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        /*leftButton = new JButton("게임시작");
        leftButton.addActionListener(this);
        buttonPanel.add(leftButton);
        
        rightButton = new JButton("끝내기");
        rightButton.addActionListener(this);
        buttonPanel.add(rightButton);*/
        
        MyMouseListener listener = new MyMouseListener();        
        
        leftButton = new JButton("게임시작");
        leftButton.addActionListener(this);
        leftButton.addMouseListener(listener);
        leftButton.setBackground(Color.BLACK);
        leftButton.setForeground(Color.WHITE);
        leftButton.setBorderPainted(false);
        buttonPanel.add(leftButton);
        
        rightButton = new JButton("끝내기");
        rightButton.addActionListener(this);
        rightButton.addMouseListener(listener);
        rightButton.setBackground(Color.BLACK);
        rightButton.setForeground(Color.WHITE);
        rightButton.setBorderPainted(false);
        buttonPanel.add(rightButton);
        
        buttonPanel.setBackground(Color.BLACK);
        
        add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void playing() {
		double randomValue = Math.random();
		OBSTACLE_X1 = (int)(randomValue * (WINDOW_WIDTH-10-OBSTACLE_WIDTH))+10; //random 최소값은 10(맨 왼쪽), 최대값은 WINDOW_WIDTH-10에서 OBSTACLE_WIDTH를 뺀 값.(맨 오른쪽)
		while(!(OBSTACLE_X1 - OBSTACLE_X2 > PLAYER_WIDTH+20 || (OBSTACLE_X1 - OBSTACLE_X2)*(-1) > PLAYER_WIDTH+20)) {
			randomValue = Math.random();
			OBSTACLE_X2 = (int)(randomValue * (WINDOW_WIDTH-10-OBSTACLE_WIDTH))+10;
		}
		
		Timer m_timer = new Timer();
		TimerTask m_task = new TimerTask() {
			public void run() {
				if((PLAYER_Y <= OBSTACLE_Y+OBSTACLE_HEIGHT) //게임 오버
						&&((((OBSTACLE_X1+OBSTACLE_WIDTH>=PLAYER_X)&&(OBSTACLE_X1<=PLAYER_X)||(PLAYER_X+PLAYER_WIDTH >= OBSTACLE_X1)&&(OBSTACLE_X1>=PLAYER_X)))
						|| (((OBSTACLE_X2+OBSTACLE_WIDTH>=PLAYER_X)&&(OBSTACLE_X2<=PLAYER_X)||(PLAYER_X+PLAYER_WIDTH >= OBSTACLE_X2)&&(OBSTACLE_X2>=PLAYER_X))))) {
					try
					{
						 AudioInputStream stream = AudioSystem.getAudioInputStream(new File("C:\\Users\\Owner\\Downloads\\\\자바\\Java_Project\\src\\333785__projectsu012__8-bit-failure-sound.wav"));
				            Clip clip = AudioSystem.getClip();
				            clip.open(stream);
				            clip.start();
					}
					catch (Exception ex)
					{
					} 
					m_timer.cancel();
					start = false;
					end = true;
					repaint();
				}
				else if(OBSTACLE_Y >= WINDOW_HEIGHT-50-OBSTACLE_HEIGHT) { //현 장애물이 바닥에 닿았을 경우
					OBSTACLE_Y = 0; //장애물 위치 맨 위로
					period -= 25; //속도 증가
					if(period <= 50) //최저 속도는 50
						period = 50;
					point += 25; //점수 상승
					if(bestScore < point)
						bestScore = point;
					m_timer.cancel(); //타이머 중지
					repaint();
					playing(); //재시작
				}
				else { //OBSTACLE이 하강함.
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

        if (actionCommand.equals("끝내기")) {
            int result = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?", "End?", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.CLOSED_OPTION);
            else if (result == JOptionPane.YES_OPTION)
            	System.exit(0);
            else if(result == JOptionPane.NO_OPTION);
        }
        if (actionCommand.equals("게임시작"))
        {
        	remove(title);
        	
        	ImageIcon ic2  = new ImageIcon("C:\\Users\\Owner\\Downloads\\자바\\Java_Project\\src\\play.png");
            JLabel back  = new JLabel(ic2);
      
            add(back, BorderLayout.CENTER);
        }
        if (actionCommand.equals("게임시작") || actionCommand.equals("재시작")) { //초기값으로 변경.
        	try
			{
				 AudioInputStream stream = AudioSystem.getAudioInputStream(new File("C:\\Users\\Owner\\Downloads\\자바\\Java_Project\\src\\341695__projectsu012__coins-1.wav"));
		            Clip clip = AudioSystem.getClip();
		            clip.open(stream);
		            clip.start();
			}
			catch (Exception ex)
			{
			}
      
        	leftButton.setText("<=");
        	rightButton.setText("=>");
        	PLAYER_X = 10;
        	OBSTACLE_Y = 0;
        	point = 0;
        	period = 500;
        	start = true;
        	end = false;
        	repaint();
        	playing();
        }
        else if ((actionCommand.equals("=>"))) {
        	if(PLAYER_X >= WINDOW_WIDTH - 10 - PLAYER_WIDTH - MOVE)
        		PLAYER_X = WINDOW_WIDTH - 10 - PLAYER_WIDTH;
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
            g.drawString(point+"점", 30, WINDOW_HEIGHT-25);
            
            g.setColor(Color.BLUE);
            g.drawString("BEST SCORE: " + bestScore+"점", 270, WINDOW_HEIGHT-25);
        }
        else if (end == true) {
        	super.paint(g);
            g.setFont(fontObject1);
            g.setColor(penColor);
            g.drawString(theText, 80, 200);
        	g.setColor(Color.WHITE);
        	g.setFont(fontObject2);
            g.drawString("이번점수: " + point+"점", 135, 240);
            g.drawString("최고점수: " + bestScore+"점", 135, 260);
            leftButton.setText("재시작");
            rightButton.setText("끝내기");
        }
    }
	
	class MyMouseListener implements MouseListener{

	    @Override
	    public void mouseClicked(MouseEvent e) {
	    }

	    @Override
	    public void mousePressed(MouseEvent e) {
	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
	    }

	    @Override
	    public void mouseEntered(MouseEvent e) {
	        JButton b = (JButton)e.getSource();
	        b.setBackground(Color.WHITE);
	        b.setForeground(Color.BLACK);
	    }

	    @Override
	    public void mouseExited(MouseEvent e) {
	        JButton b = (JButton)e.getSource();
	        b.setBackground(Color.BLACK);
	        b.setForeground(Color.WHITE);
	    }
	}
}