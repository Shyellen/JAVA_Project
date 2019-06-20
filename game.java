import java.io.File; // 파일 불러오기
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
import javax.sound.sampled.AudioInputStream; // 브금 삽입
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.awt.event.MouseEvent; // 마우스 이벤트 -> 버튼 마우스오버하면 바뀌는걸 만들기 위해 삽입
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent; // key event (키보드 왼/오른쪽 키로 움직일수있음)
import java.awt.event.KeyListener;



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
	
	public JLabel title; // title에 title 이미지 삽입, 게임 시작 후 지우기위해 위로 뺌
	
    private Font fontObject1 = 
                      new Font("SansSerif", Font.PLAIN, 40);
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
        setLocationRelativeTo(null); // 창 중앙에 띄우기
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);
        
        setResizable(false); // 창 크기 고정
        
        ImageIcon ic1  = new ImageIcon("title.png"); // title 이미지를 image icon으로 가져옴,
        title  = new JLabel(ic1); // 위에 빼놓은 title 라벨에 집어넣음
  
        add(title, BorderLayout.CENTER);
        
       JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        MyMouseListener listener = new MyMouseListener();    // mouselistener를 사용하기위해 추가
        key lis = new key();  // keylistener 사용위해 추가
        
        addKeyListener(lis);
        
        leftButton = new JButton("게임시작");
        leftButton.addActionListener(this);
        leftButton.addMouseListener(listener);
        leftButton.addKeyListener(lis); // 안넣어주면 keylistner 가 안돌아감.... 포커스 문제인듯
        leftButton.setBackground(Color.BLACK); //초기 컬러설정
        leftButton.setForeground(Color.WHITE); // 글자색
        leftButton.setFocusPainted(false); // 버튼 내용 테두리 지움
        leftButton.setBorderPainted(false); // 버튼 테두리 지움
        
        buttonPanel.add(leftButton);
        
        rightButton = new JButton("끝내기");
        rightButton.addActionListener(this);
        rightButton.addMouseListener(listener);
        rightButton.addKeyListener(lis);
        rightButton.setBackground(Color.BLACK);
        rightButton.setForeground(Color.WHITE);
        rightButton.setFocusPainted(false);
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
						 AudioInputStream stream = AudioSystem.getAudioInputStream(new File("333785__projectsu012__8-bit-failure-sound.wav"));
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
					if(bestScore < point) // 실시간으로 최고기록 반영
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
            int result = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?", "End?", JOptionPane.YES_NO_OPTION); // 팝업창 띄우기
            if(result == JOptionPane.CLOSED_OPTION);// yes면 종료, 나머지는 아무 일 없음
            else if (result == JOptionPane.YES_OPTION)
            	System.exit(0);
            else if(result == JOptionPane.NO_OPTION);
        }
        if (actionCommand.equals("게임시작"))
        {
        	remove(title); // 처음 타이틀 이미지가 들어있는 라벨 지움
        	
        	ImageIcon ic2  = new ImageIcon("play.png"); // 플레이 중 백그라운드 만들기.
            JLabel back  = new JLabel(ic2); // 저기 아래 게임시작||재시작에 넣으면 투명도가 겹쳐져서 따로 게임시작에만 넣어줌
      
            add(back, BorderLayout.CENTER);
        }
        if (actionCommand.equals("게임시작") || actionCommand.equals("재시작")) { //초기값으로 변경.
        	try // 시작할때 브금 띠롱
			{
				 AudioInputStream stream = AudioSystem.getAudioInputStream(new File("341695__projectsu012__coins-1.wav"));
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
        	
        	g.setColor(Color.GRAY);
            g.drawString(point+"점", 30, WINDOW_HEIGHT-17);
            
            g.setColor(Color.GRAY);
            g.drawString("BEST SCORE: " + bestScore+"점", 270, WINDOW_HEIGHT-17);
        }
        else if (end == true) {
        	super.paint(g);
            g.setFont(fontObject1);
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 80, 200);
        	g.setColor(Color.WHITE);
        	g.setFont(fontObject2);
            g.drawString("이번점수: " + point+"점", 135, 240);
            g.drawString("최고점수: " + bestScore+"점", 135, 260);
            leftButton.setText("재시작");
            rightButton.setText("끝내기");
        }
    }
	
	class MyMouseListener implements MouseListener{ // 마우스리스너

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
	    public void mouseEntered(MouseEvent e) { //아래 버튼들에 마우스를 가져다대면 배경 흰색, 글자 검정색으로 바뀜
	        JButton b = (JButton)e.getSource();
	        b.setBackground(Color.WHITE);
	        b.setForeground(Color.BLACK);
	    }

	    @Override
	    public void mouseExited(MouseEvent e) { // 다시 떼면 초기치로 돌아감
	        JButton b = (JButton)e.getSource();
	        b.setBackground(Color.BLACK);
	        b.setForeground(Color.WHITE);
	    }
	}
	
	class key implements KeyListener{ // 키리스너, 키보드에서 입력받아서

        @Override
        public void keyPressed(KeyEvent e) {
        	if(start == true) {
        		if( e.getKeyCode() == 39 ) { // 오른쪽 (39), =>키와 동일
        			if(PLAYER_X >= WINDOW_WIDTH - 10 - PLAYER_WIDTH - MOVE)
        				PLAYER_X = WINDOW_WIDTH - 10 - PLAYER_WIDTH;
        			else
        				PLAYER_X += MOVE;
        			rightButton.setBackground(Color.WHITE);
        	        rightButton.setForeground(Color.BLACK);
        			repaint();
        		}
        		if( e.getKeyCode() == 37 ) { // 왼쪽(37), <=키와 동일
        			if(PLAYER_X <= MOVE+10)
                		PLAYER_X = 10;
                	else
                		PLAYER_X -= MOVE;
        			leftButton.setBackground(Color.WHITE);
        	        leftButton.setForeground(Color.BLACK);
                    repaint();
        		}
        	}
        }

        @Override
        public void keyReleased(KeyEvent e) {
        	if( e.getKeyCode() == 39 ) { // 오른쪽 (39), =>키와 동일
    			rightButton.setBackground(Color.BLACK);
    	        rightButton.setForeground(Color.WHITE);
    			repaint();
    		}
    		if( e.getKeyCode() == 37 ) { // 왼쪽(37), <=키와 동일
    			leftButton.setBackground(Color.BLACK);
    	        leftButton.setForeground(Color.WHITE);
                repaint();
    		}
        }

        @Override
        public void keyTyped(KeyEvent e) {
        	
        }
                    
            
    }       
}