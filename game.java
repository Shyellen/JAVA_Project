import java.io.File; // ���� �ҷ�����
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
import javax.sound.sampled.AudioInputStream; // ��� ����
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.awt.event.MouseEvent; // ���콺 �̺�Ʈ -> ��ư ���콺�����ϸ� �ٲ�°� ����� ���� ����
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent; // key event (Ű���� ��/������ Ű�� �����ϼ�����)
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
	public static int PLAYER_Y = WINDOW_HEIGHT-50-PLAYER_HEIGHT; //50 = ��ܹ�+�ϴ� ��ư �г�
	public static int OBSTACLE_X1 = 30;
	public static int OBSTACLE_X2 = 200;
	public static int OBSTACLE_Y = 0; // OBSTACLE_Y <= PLAYER_Y
	public static long period = 500;
	public static int point = 0;
	public static int bestScore = 0; 
	
	public JButton leftButton;
	public JButton rightButton;
	
	public boolean start = false; //���� ���� ����
	public boolean end = false; //���� ���� ����
	
	public JLabel title; // title�� title �̹��� ����, ���� ���� �� ��������� ���� ��
	
    private Font fontObject1 = 
                      new Font("SansSerif", Font.PLAIN, 40);
    private Font fontObject2 = 
            new Font("����", Font.PLAIN, 20);
	
	public static void main(String[] args) {
		game play = new game();
		play.setVisible(true);
	}
	
	public game()
	{
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("��ֹ� ���ϱ�");
        setLocationRelativeTo(null); // â �߾ӿ� ����
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);
        
        setResizable(false); // â ũ�� ����
        
        ImageIcon ic1  = new ImageIcon("title.png"); // title �̹����� image icon���� ������,
        title  = new JLabel(ic1); // ���� ������ title �󺧿� �������
  
        add(title, BorderLayout.CENTER);
        
       JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        MyMouseListener listener = new MyMouseListener();    // mouselistener�� ����ϱ����� �߰�
        key lis = new key();  // keylistener ������� �߰�
        
        addKeyListener(lis);
        
        leftButton = new JButton("���ӽ���");
        leftButton.addActionListener(this);
        leftButton.addMouseListener(listener);
        leftButton.addKeyListener(lis); // �ȳ־��ָ� keylistner �� �ȵ��ư�.... ��Ŀ�� �����ε�
        leftButton.setBackground(Color.BLACK); //�ʱ� �÷�����
        leftButton.setForeground(Color.WHITE); // ���ڻ�
        leftButton.setFocusPainted(false); // ��ư ���� �׵θ� ����
        leftButton.setBorderPainted(false); // ��ư �׵θ� ����
        
        buttonPanel.add(leftButton);
        
        rightButton = new JButton("������");
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
		OBSTACLE_X1 = (int)(randomValue * (WINDOW_WIDTH-10-OBSTACLE_WIDTH))+10; //random �ּҰ��� 10(�� ����), �ִ밪�� WINDOW_WIDTH-10���� OBSTACLE_WIDTH�� �� ��.(�� ������)
		while(!(OBSTACLE_X1 - OBSTACLE_X2 > PLAYER_WIDTH+20 || (OBSTACLE_X1 - OBSTACLE_X2)*(-1) > PLAYER_WIDTH+20)) {
			randomValue = Math.random();
			OBSTACLE_X2 = (int)(randomValue * (WINDOW_WIDTH-10-OBSTACLE_WIDTH))+10;
		}
		
		Timer m_timer = new Timer();
		TimerTask m_task = new TimerTask() {
			public void run() {
				if((PLAYER_Y <= OBSTACLE_Y+OBSTACLE_HEIGHT) //���� ����
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
				else if(OBSTACLE_Y >= WINDOW_HEIGHT-50-OBSTACLE_HEIGHT) { //�� ��ֹ��� �ٴڿ� ����� ���
					OBSTACLE_Y = 0; //��ֹ� ��ġ �� ����
					period -= 25; //�ӵ� ����
					if(period <= 50) //���� �ӵ��� 50
						period = 50;
					point += 25; //���� ���
					if(bestScore < point) // �ǽð����� �ְ��� �ݿ�
						bestScore = point;
					m_timer.cancel(); //Ÿ�̸� ����
					repaint();
					playing(); //�����
				}
				else { //OBSTACLE�� �ϰ���.
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

        if (actionCommand.equals("������")) {
            int result = JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "End?", JOptionPane.YES_NO_OPTION); // �˾�â ����
            if(result == JOptionPane.CLOSED_OPTION);// yes�� ����, �������� �ƹ� �� ����
            else if (result == JOptionPane.YES_OPTION)
            	System.exit(0);
            else if(result == JOptionPane.NO_OPTION);
        }
        if (actionCommand.equals("���ӽ���"))
        {
        	remove(title); // ó�� Ÿ��Ʋ �̹����� ����ִ� �� ����
        	
        	ImageIcon ic2  = new ImageIcon("play.png"); // �÷��� �� ��׶��� �����.
            JLabel back  = new JLabel(ic2); // ���� �Ʒ� ���ӽ���||����ۿ� ������ ������ �������� ���� ���ӽ��ۿ��� �־���
      
            add(back, BorderLayout.CENTER);
        }
        if (actionCommand.equals("���ӽ���") || actionCommand.equals("�����")) { //�ʱⰪ���� ����.
        	try // �����Ҷ� ��� ���
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
            g.drawString(point+"��", 30, WINDOW_HEIGHT-17);
            
            g.setColor(Color.GRAY);
            g.drawString("BEST SCORE: " + bestScore+"��", 270, WINDOW_HEIGHT-17);
        }
        else if (end == true) {
        	super.paint(g);
            g.setFont(fontObject1);
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 80, 200);
        	g.setColor(Color.WHITE);
        	g.setFont(fontObject2);
            g.drawString("�̹�����: " + point+"��", 135, 240);
            g.drawString("�ְ�����: " + bestScore+"��", 135, 260);
            leftButton.setText("�����");
            rightButton.setText("������");
        }
    }
	
	class MyMouseListener implements MouseListener{ // ���콺������

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
	    public void mouseEntered(MouseEvent e) { //�Ʒ� ��ư�鿡 ���콺�� �����ٴ�� ��� ���, ���� ���������� �ٲ�
	        JButton b = (JButton)e.getSource();
	        b.setBackground(Color.WHITE);
	        b.setForeground(Color.BLACK);
	    }

	    @Override
	    public void mouseExited(MouseEvent e) { // �ٽ� ���� �ʱ�ġ�� ���ư�
	        JButton b = (JButton)e.getSource();
	        b.setBackground(Color.BLACK);
	        b.setForeground(Color.WHITE);
	    }
	}
	
	class key implements KeyListener{ // Ű������, Ű���忡�� �Է¹޾Ƽ�

        @Override
        public void keyPressed(KeyEvent e) {
        	if(start == true) {
        		if( e.getKeyCode() == 39 ) { // ������ (39), =>Ű�� ����
        			if(PLAYER_X >= WINDOW_WIDTH - 10 - PLAYER_WIDTH - MOVE)
        				PLAYER_X = WINDOW_WIDTH - 10 - PLAYER_WIDTH;
        			else
        				PLAYER_X += MOVE;
        			rightButton.setBackground(Color.WHITE);
        	        rightButton.setForeground(Color.BLACK);
        			repaint();
        		}
        		if( e.getKeyCode() == 37 ) { // ����(37), <=Ű�� ����
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
        	if( e.getKeyCode() == 39 ) { // ������ (39), =>Ű�� ����
    			rightButton.setBackground(Color.BLACK);
    	        rightButton.setForeground(Color.WHITE);
    			repaint();
    		}
    		if( e.getKeyCode() == 37 ) { // ����(37), <=Ű�� ����
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