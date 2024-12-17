package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.*;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;

public class Minesweeper {
	// declare a serialVersionUID field to avoid potential issues with serialization.
	 private static final long serialVersionUID = 1L;
	private class mineButton extends JButton
	{
		private static final long serialVersionUID = 1L;
		int r;
		int c;
		public mineButton(int r, int c)
		{
			this.r = r;
			this.c = c;
		}
	}
	JFrame frame = new JFrame();
	int width = 800;
	int height = 700;
	int row = 8;
	int column = 8;
	int ButtonClicked = 0;
	boolean Lose = false;
	int numOfMine = 10;
   
 	
	JPanel panel = new JPanel();
	JLabel label = new JLabel();
	JPanel board = new JPanel();
	JButton b = new JButton();
    Random random = new Random();
    JButton resetButton = new JButton("Reset");
    
	
	mineButton[][] numbutton = new mineButton[row][column];	
	ArrayList<mineButton> mineList ;
	Minesweeper(){   	
    	frame.setSize(width,height);
    	frame.setLocationRelativeTo(null);//it will open the window at the center of the screen
    	frame.setResizable(false);//to freeze the size of a frame, preventing the user from resizing it
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setLayout(new BorderLayout());
    	
    	label.setBackground(Color.darkGray);
    	label.setForeground(Color.LIGHT_GRAY);
    	label.setHorizontalAlignment(JLabel.CENTER);
    	label.setText("Minesweeper");
    	label.setFont(new Font("Arial", Font.BOLD, 50));
    	label.setOpaque(true);  //it allows the component to paint every pixel within its bounds    
    	 
    	panel.setLayout(new BorderLayout());
    	panel.add(label);
    	panel.add(resetButton, BorderLayout.EAST);
    	frame.add(panel, BorderLayout.NORTH);
    	
    	board.setLayout(new GridLayout(row, column));
    	board.setBackground(Color.gray);
    	frame.add(board);
    	//create the reset button with the action to click on it
    	resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        //when reset every things, we run the board again, make a new one
        createBoard();
        frame.setVisible(true);
        SetMine();
    }

	void createBoard() {
    	board.removeAll();//o remove all elements from a collection, leaving it empty, the main thing to help reset the game
    	// create the board
    	for(int r = 0; r< row; r ++)// create the board
    	{
    		for(int c = 0; c< column; c++)
    		{ 
    			
    			mineButton Button = new mineButton(r,c);// create the mine field to click
    			numbutton[r][c] = Button;
    			Button.setFocusable(false);
                Button.setMargin(new Insets(0, 0, 0, 0));
    			Button.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
    			//add action to the mouse
    			Button.addMouseListener(new MouseAdapter() {
    			@Override	
    			public void mousePressed(MouseEvent e)
    			{   
    				if(Lose)
    				{
    					return; // when we lose all the button will be disable
    				}
    				mineButton Button = (mineButton) e.getSource();	
    				//left click
    				if(e.getButton() == MouseEvent.BUTTON1) {
    				if(Button.getText()=="")
    				{
    					if(mineList.contains(Button))
    					{
    						MineReavel();
    					}
    					
    					else {
    						checkMine(Button.r, Button.c);
    					}   					
    				}}
    				//right click
    				else if(e.getButton() == MouseEvent.BUTTON3)
    				{
    					if(Button.getText()=="" && Button.isEnabled())//to make sure that the button is enable and hasn't been clicked
    				{
    					Button.setText("🚩");
    				}
    					else if(Button.getText()=="🚩")  
    					{
    						Button.setText("");
    					}
    					}   				
    			}    				
    			});   					
    			board.add(Button); 
    		}
    	}
    	
    	 
    	frame.setVisible(true);
    	SetMine();
    }
	
	void SetMine()
	{
		mineList = new ArrayList<mineButton>();		
		int mineSet = numOfMine;
		while (mineSet > 0)//set the location of the mine random every game
		{
			int r = random.nextInt(row);
			int c = random.nextInt(column);
			
			mineButton Button = numbutton[r][c];
			if(!mineList.contains(Button))// make sure to  it not set the same location
			{
				mineList.add(Button);
				mineSet -=1;
			}
		}
		
	}
	

	void MineReavel()
	{
		
		for(int i=0;i <mineList.size();i++)// count from 0 to 9 = 10 mines
		{
			mineButton action = mineList.get(i);//i = 10 mine
			action.setText("💣");
			Lose = true;
			label.setText("Game Over!");
		}
		
	}
	void checkMine(int r, int c)
	{
		if( r <0 || r>=column|| c<0|| c>=row)//make sure it will not appear outside the boundaries of the game board.
		{
			return ;// if it out of bound then we will stop checking the mine out of bound
		}
		mineButton Button = numbutton[r][c];
		if(!Button.isEnabled())//If the button is already disabled, which mean it the button will not work and return
		{
			return;
		}
		Button.setEnabled(false);// Disabling the button prevents it from being clicked again
		ButtonClicked +=1;// count the number of button have been clicked
		int mineFound = 0;
		//check top of the button you clicked
		mineFound += countMine(r -1, c-1);//top left
		mineFound += countMine(r -1, c);//top mid
		mineFound += countMine(r -1, c+1);//top right
		//check mid
		mineFound += countMine(r , c-1);//left
		mineFound += countMine(r , c+1);//right
		//check bottom
		mineFound += countMine(r +1, c-1);
		mineFound += countMine(r +1, c);
		mineFound += countMine(r +1, c+1);
		if(mineFound > 0)
		{
			Button.setText(Integer.toString(mineFound));// this the number will show you how many bomb around the button you clicked
		}
		else
		{
			Button.setText("");// this is where no mine and it will check around other button near it and show it since it if and else
			//check top
			checkMine(r -1, c-1);//top left
			checkMine(r -1, c);//top mid
			checkMine(r -1, c+1);//top right
			//check mid
			checkMine(r , c-1);
			checkMine(r , c+1);
			//check bottom
			checkMine(r +1, c-1);
			checkMine(r +1, c);
			checkMine(r +1, c+1);
		}
		if(ButtonClicked == row*column - mineList.size()) //8*8 - 10 mine = clear the game 
		{
			Lose = true;
			label.setText("you Win");
			
		}
	}
	int countMine(int r, int c)
	{
		if( r <0 || r>=column|| c<0|| c>=row)// check when click on the corner if it out of bound, the return 0
		{
			return 0;
		}
		if(mineList.contains(numbutton[r][c]))
		{
			return 1;
		}
		return 0;
	}
	 void resetGame() {
	        Lose = false;
	        ButtonClicked = 0;
	        label.setText("Minesweeper");
	        createBoard();
	        SetMine();
	    }
	
	
}

 
