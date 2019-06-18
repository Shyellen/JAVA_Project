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
    public static final int WIDTH = 500;//크기조정필요
    public static final int HEIGHT = 500;
    public static final int X_START = 115;
    public static final int Y_START = 200;
    public static final int POINT_SIZE = 30;
   
    private String theText = "장애물 피하기 게임";
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
        setTitle("장애물 피하기 게임");

        getContentPane( ).setBackground(Color.BLACK);//배경색
        setLayout(new BorderLayout( ));
        
        JPanel buttonPanel = new JPanel( );
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setLayout(new FlowLayout( ));

        JButton startButton = new JButton("시작");
        startButton.setBackground(Color.WHITE);
        startButton.addActionListener(this);
        buttonPanel.add(startButton);

        JButton endButton = new JButton("종료");
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
    	
    	if(buttonString.equals("시작"))
        {
    		penColor = Color.WHITE;
    		fontObject = 
    	               new Font("Serif", Font.BOLD|Font.ITALIC, POINT_SIZE);
    	        theText = "게임 실행화면.";
        }
        
        else if(buttonString.equals("종료"))
        {
        	System.exit(0);
        }
        repaint( );
    }
}
