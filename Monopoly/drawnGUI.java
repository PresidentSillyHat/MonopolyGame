package esof322.pa4.team5;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Derek Wallace
 */
public class drawnGUI extends JFrame{
    private static BufferedImage base;
    private static BufferedImage drawer;
    private static Player[] players;
    private static JPanel boardPanel;
    private static drawnGUI inst=null;
    private static Image dog,penguin,trex,thimble,battleship,car;
    private static JLabel playerMoney,playerName;
    private static JLabel currLoc, locHouses,locRent,locOwner;
    private static Image[] pieces;
    public static int btheme;
    
    public static drawnGUI getInstance() throws IOException{
        if (inst==null){
            inst = new drawnGUI();}
        inst.repaint();	//updates board when needed
        return inst;
    }
    
    public drawnGUI() throws IOException{	//The GUI Frame
        super("Let's Play Monopoly");
		//Picks a theme
        Object[] possibilities = {"Classic", "Gin","Splinter Cell", "Mad Max Chrome Edition", "Dire Edition (It's like monopoly, but dire.)"};
        String theme = (String) JOptionPane.showInputDialog(JOptionPane.getRootFrame(),
                    "Please select a theme",
                    "Theme Selection:",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    2);
		//Set theme based off choice
        if(theme.equals("Classic")){
            base= ImageIO.read(new File("board1000.jpg"));
            btheme=1;
        }
        else if(theme.equals("Gin")){
            base= ImageIO.read(new File("ginopoly1000.png"));
            btheme=2;
        }
        else if(theme.equals("Splinter Cell")){
            base= ImageIO.read(new File("Splintercell.png"));
            btheme=3;
        }
        else if(theme.equals("Dire Edition (It's like monopoly, but dire.)")){
            base= ImageIO.read(new File("direopoly.png"));
            btheme=4;
        }
        else{
            base= ImageIO.read(new File("Madmax.png"));
            btheme=5;
        }
		//paint resizing preserves better quality than java
        drawer=new BufferedImage(1000,1000,BufferedImage.TYPE_INT_ARGB);	//Drawer is buffer players move on
		//import pieces
        dog=ImageIO.read(new File("dog.png"));
        penguin=ImageIO.read(new File("penguin.png"));
        trex=ImageIO.read(new File("trex.png"));
        thimble=ImageIO.read(new File("thimble.png"));
        battleship=ImageIO.read(new File("battleship.png"));
        car=ImageIO.read(new File("car.png"));
        
        this.setLayout(new GridBagLayout());
        boardPanel=new JPanel(){
        
            @Override
            protected void paintComponent(Graphics g) {
                
                super.paintComponent(g);
                g.drawImage(base, 0, 0, null);	//base is Background
                g.drawImage(drawer, 0, 0, null);	//Drawer is player movement
                
            }
        };

    
        JPanel playerPanel = new JPanel();	//Panel player info goes on
        
        playerPanel.setVisible(true);
       
        playerPanel.setLayout(new GridBagLayout());	//Gridbag to allow more precise layout
        GridBagConstraints b=new GridBagConstraints();
        
        currLoc=(new JLabel("Opt1"));	//Players current location
        playerMoney=(new JLabel("Opt2"));	//Players money
        playerName=new JLabel("Name");	//Current player
        locHouses=new JLabel("Houses");
        locRent=new JLabel("Rent");
        locOwner=new JLabel("Owner");
        b.ipady=20;
        
        b.gridx=0;
        b.gridy=0;
        playerPanel.add(playerName,b);
        b.gridx=2;
        b.gridy=0;
        playerPanel.add(playerMoney,b);
        b.gridx=1;
        b.gridy=1;
        playerPanel.add(currLoc,b);
        b.gridx=0;
        b.gridy=2;
        playerPanel.add(locHouses,b);
        b.gridx=1;
        b.gridy=2;
        playerPanel.add(locRent,b);
        b.gridx=2;
        b.gridy=2;
        playerPanel.add(locOwner,b);
        
        GridBagConstraints c=new GridBagConstraints();
        c.weightx=0.8;
        c.ipadx=1000;
        c.ipady=1000;
        this.add(boardPanel, c);
        c.weightx=0.2;
        c.ipadx=50; //Funky spacing but it doesnt cut off board
        this.add(playerPanel, c);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500,1000);	//Good size for window bc of board
        this.setVisible(true);
    
    
    }
    
    public static void setBoard(Board update){   //Assigns pieces based off token and places at start
        players=update.getPlayers();
        pieces=new Image[players.length];
        for(int pn=0;pn<players.length;pn++){
            if (players[pn].Token.equals("Dog")){
                pieces[pn]=dog;
            }
            else if (players[pn].Token.equals("T-rex")){
                pieces[pn]=trex;
            }
            else if (players[pn].Token.equals("Battleship")){
                pieces[pn]=battleship;
            }
            else if (players[pn].Token.equals("Thimble")){
                pieces[pn]=thimble;
            }
            else if (players[pn].Token.equals("Penguin")){
                pieces[pn]=penguin;
            }
            else{
                pieces[pn]=car;
            }
        }
        int x=820,y=0;
        
        Graphics g=drawer.getGraphics();
        for(int pn=0;pn<players.length;pn++){	//Place all used pieces at Go
            y=800+pn*30;  //12 has overlap but fits spaces nicely
            g.drawImage(pieces[pn], x, y, null);
        }
        Board.theme=btheme; //Pass the board the Chosen theme
    }
    public static void updateBoard(Board update) throws IOException{	//Keeps GUI up to date with board
        players=update.getPlayers();
        if(Player.current.testPlayer==true)return;
        
        playerName.setText("Player: "+update.current.Name);	//Update player panel
        playerMoney.setText("Money: $"+update.current.money);
        currLoc.setText("Landed on: "+update.getSpace(update.current.Position).name);
        if(update.getSpace(update.current.Position).getType()==1)
            locHouses.setText("Houses: "+update.getProperty(update.current.Position).getHouses());	//Update player panel
        else
            locHouses.setText("No houses");
        locOwner.setText("Owner: "+update.getSpace(update.current.Position).deeded.Name);
        locRent.setText("Rent: "+update.getSpace(update.current.Position).landedAction(5));//number only matters for utilities, just a placeholder
        int x=0,y=0,i=0;
        for(int j=0;j<1000;j++){
         for(int k=0;k<1000;k++){
             drawer.setRGB(j, k,0xFF);  //clears previous positions so only current spots are displayed
         }   
        }
        Graphics g=drawer.getGraphics();
        for(int pn=0;pn<players.length;pn++){
            i=players[pn].Position;
            //logic to automate piece placing
            //0-9 is first row
            if (i>=0 && i<10){
                x=820-i*75;    //59 is perfect
                y=800+pn*30;  //12 has overlap but fits spaces nicely
                g.drawImage(pieces[pn], x, y, null);
                
            }
            //10-20 is left side

            else if(i>=10 && i<21){
                int j=i-10;
                x=10+pn*25;   //x and y switch incrementing at corners
                y=820-j*75;   //places near edge
                g.drawImage(pieces[pn], x, y, null);   //draw broke?
            }
            //21-30 top{
            else if(i<31 && i>20){
                int j=i-21;
                x=145+j*75;
                y=10+pn*25;
                g.drawImage(pieces[pn],x,y,null);
            }    
            //31-39 is right side
            else if(i>30 && i<40){
                int j=i-31;
                x=805+pn*25;
                y=145+j*75;
                g.drawImage(pieces[pn], x, y, null);
            }

            
        }
        drawnGUI.getInstance();	//Repaint board to show changes
        g.dispose();	//Free up space from Graphics g
        
    }
}
