import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

public class Begin extends JFrame 
                            implements ActionListener
{
    public static final int WIDTH = 500;//ũ�������ʿ�
    public static final int HEIGHT = 500;
    public static final int X_START = 115;
    public static final int Y_START = 200;
    public static final int POINT_SIZE = 30;
   
    private String theText = "��ֹ� ���ϱ� ����";
    private Color penColor = Color.WHITE;
    private Font fontObject = 
                      new Font("SansSerif", Font.PLAIN, POINT_SIZE);
    
    public static void main(String[] args)
    {
        Begin gui = new Begin( );
        gui.setVisible(true);
    }

    public Begin( )
    {
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("��ֹ� ���ϱ� ����");

        getContentPane( ).setBackground(Color.BLACK);//����
        setLayout(new BorderLayout( ));
        
        JPanel buttonPanel = new JPanel( );
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setLayout(new FlowLayout( ));

        JButton startButton = new JButton("����");
        startButton.setBackground(Color.WHITE);
        startButton.addActionListener(this);
        buttonPanel.add(startButton);

        JButton endButton = new JButton("����");
        endButton.setBackground(Color.WHITE);
        endButton.addActionListener(this);
        buttonPanel.add(endButton);
       
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        g.setFont(fontObject);
        g.setColor(penColor);
        g.drawString(theText, X_START, Y_START);
    }

    public void actionPerformed(ActionEvent e) 
    {
    	String buttonString = e.getActionCommand( );
    	
    	if(buttonString.equals("����"))
        {
    		penColor = Color.WHITE;
    		fontObject = 
    	               new Font("Serif", Font.BOLD|Font.ITALIC, POINT_SIZE);
    	        theText = "���� ����ȭ��.";
        }
        
        else if(buttonString.equals("����"))
        {
        	System.exit(0);
        }
        repaint( );
    }
}
