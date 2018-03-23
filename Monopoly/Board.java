package esof322.pa4.team5;

import java.io.IOException;
import javax.swing.*;
import java.util.Scanner;



/**
 *
 * @author Derek Wallace, Garret Gershmel
 */
public class Board {
    private Scanner scan = new Scanner(System.in);
    public static int theme=0; 	//Theme the board will have, passed from drawnGUI setBoard
    public Player[] players;	//array of players
    private Space[] spaces;		//two different types due to passing issues
    private Property[] props;
    private String[] availableTokens;	//Names of tokens player can pick
    private double timeLeft = 0;    //implementation tbd-----CURRENTLY USING NUM TURNS
    private ChanceCC cardDeck;

    public Player current;	//Used by GUI

    public Board(){
        players=new Player[4];  //temp for testing
        spaces=new Space[40];   //40 spaces
        availableTokens = new String[]{"Thimble", "Battleship", "Car", "T-rex", "Dog", "Penguin"};
        runSetup();	//Setup spaces on board
        
        //getStartingPositions();	//Sets up players and places into GUI
        
    }
    public Board(boolean forTest){	//Made to allow easier testing, unnecessary TODO: delete
        spaces=new Space[40];
        props=new Property[4];
        for (int i=0;i<40;i++){
            spaces[i] = new Space();
        }
        

    }

    public void runSetup(){	//Initialize all spaces/properties and follow theme
        cardDeck = new ChanceCC(this);
        int railRoadRents[] = {25, 50, 100, 200};
        Object[] possibilities = {2, 3, 4};
        int tempNumPlayers = (int) JOptionPane.showInputDialog(JOptionPane.getRootFrame(),
                "Please enter the number of players",
                "Customized Dialog",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                2);
        players = new Player[tempNumPlayers];	//Not needed?
        for (int a =0;a<tempNumPlayers;a++){
            players[a] = new Player("","", this, a+1, false);     //TODO this doesn't need to send any information to the player, the player should set itself up to prepare for server/client version

        }

        drawnGUI.setBoard(this);	//Passes the GUI first board update and sets player pieces
        for (int i=0;i<40;i++){
            spaces[i] = new Space();
        }

        String[] names={"Go","Mediterranean Avenue","Community Chest","Baltic Avenue","Income Tax","Reading RailRoad","Oriental Avenue","Chance","Vermont Avenue","Connecticut Avenue",
                "Jail","St. Charles Place","Electric Company","States Avenue","Virginia Avenue","Pennsylvania Railroad","St. James Place","Community Chest", "Tennessee Avenue",
                "New York Avenue","Free Parking","Kentucky Avenue","Chance","Indiana Avenue","Illinois Avenue" ,"B. & O. Railroad","Atlantic Avenue","Ventnor Avenue","Water Works","Marvin Gardens","Go To Jail","Pacific Avenue",
                "North Carolina Avenue","Community Chest","Pennsylvania Avenue","Short Line Railroad","Chance","Park Place","Luxury Tax","Boardwalk"

        };
        
        if(theme==3){
            //splinter cell
            for(int sp=0;sp<names.length;sp++){
                if(sp%3==0)names[sp]="Spooky "+names[sp];
                else names[sp]="Suspicious "+names[sp];
            }
        }
        else if(theme==4){
            for(int sp=0;sp<names.length;sp++){
                names[sp]="Dire "+names[sp];
            }
        }
        else if(theme==5){
            for(int sp=0;sp<names.length;sp++){
                int rn=(int)(Math.random()*100);
                if (rn==30)names[sp]="Hunting Party";
                if(rn%3==0)names[sp]="Decrepit "+names[sp];
                if(rn%2==0)names[sp]="Broken "+names[sp];
                if(rn%5==0)names[sp]="Immortan Joe's "+names[sp];
            }
        }
        //some problems with setup: FIX
		//Works well and logic is all interwoven at this point
        spaces[0].setSpace(names[0], Space.FREESPACE);	//These all just set up the board
        props=new Property[40];
        int rentsA[] = {2, 10, 30, 90, 160, 250};
        props[1] = new Property(60, rentsA,1, 50);
        props[1].setSpace(names[1],Space.PROPERTY);
        spaces[1]=props[1];

        spaces[2].setSpace(names[2],Space.COMMUNITY_CHEST);

        int rentsB[] = {4, 20, 60, 180, 320, 450};
        props[3] = new Property(60, rentsB, 1,50);
        props[3].setSpace(names[3],Space.PROPERTY);
        spaces[3]=props[3];

        spaces[4].setSpace(names[4],Space.INCOME_TAX);

        spaces[5] = new Railroad(150, railRoadRents, 75);
        spaces[5].setSpace(names[5],Space.RAILROAD);

        int rentsC[] = {6, 30, 90, 270, 400, 550};
        props[6] = new Property(100, rentsC, 2, 50);
        props[6].setSpace(names[6],Space.PROPERTY);
        spaces[6]=props[6];

        spaces[7].setSpace(names[7],6);

        props[8] = new Property(100, rentsC,2, 50);
        props[8].setSpace(names[8],Space.PROPERTY);
        spaces[8]=props[8];

        int rentsD[] = {8, 40, 100, 300, 450, 600};
        props[9] = new Property(120, rentsD,2, 50);
        props[9].setSpace(names[9],Space.PROPERTY);
        spaces[9]=props[9];

        spaces[10].setSpace(names[10],Space.FREESPACE);

        int rentsE[] = {10, 50, 150, 450, 625, 750};
        props[11] = new Property(140, rentsE, 3, 100);
        props[11].setSpace(names[11],Space.PROPERTY);
        spaces[11]=props[11];

        int rentsU[] = {0};
        props[12] = new UtilitySpace(12, rentsU, -1, 150,this);
        props[12].setSpace(names[12],Space.UTILITY);
        spaces[12] = props[12];

        props[13] = new Property(140, rentsE, 3, 100);
        props[13].setSpace(names[13],Space.PROPERTY);
        spaces[13]=props[13];

        int rentsF[] = {12, 60, 180, 500, 700, 900};
        props[14] = new Property(160, rentsF, 3, 100);
        props[14].setSpace(names[14],Space.PROPERTY);
        spaces[14]=props[14];

        spaces[15] = new Railroad(150, railRoadRents, 75);
        spaces[15].setSpace(names[15],Space.RAILROAD);

        int rentsG[] = {14, 70, 200, 550, 750, 950};
        props[16] = new Property(180, rentsG, 4, 100);
        props[16].setSpace(names[16],Space.PROPERTY);
        spaces[16]=props[16];

        spaces[17].setSpace(names[17],Space.COMMUNITY_CHEST);

        props[18] = new Property(180, rentsG,4, 100);
        props[18].setSpace(names[18],Space.PROPERTY);
        spaces[18]=props[18];

        int rentsH[] = {16, 80, 220, 600, 800, 1000};
        props[19] = new Property(200, rentsH,4, 100);
        props[19].setSpace(names[19],Space.PROPERTY);
        spaces[19]=props[19];

        spaces[20].setSpace(names[20],Space.FREESPACE);

        int rentsI[] = {18, 90, 250, 700, 875, 1050};
        props[21] = new Property(220, rentsI,5, 150);
        props[21].setSpace(names[21],Space.PROPERTY);
        spaces[21]=props[21];

        spaces[22].setSpace(names[22],Space.CHANCE);

        props[23] = new Property(220, rentsI,5, 150);
        props[23].setSpace(names[23],Space.PROPERTY);
        spaces[23]=props[23];

        int rentsJ[] = {20, 100, 300, 750, 925, 1100};
        props[24] = new Property(240, rentsJ,5, 150);
        props[24].setSpace(names[24],Space.PROPERTY);
        spaces[24]=props[24];

        spaces[25] = new Railroad(150, railRoadRents, 75);
        spaces[25].setSpace(names[25],Space.RAILROAD);

        int rentsK[] = {22, 110, 330, 800, 975, 1150};
        props[26] = new Property(260, rentsK,6, 150);
        props[26].setSpace(names[26],Space.PROPERTY);
        spaces[26]=props[26];
        props[27] = new Property(260, rentsK,6, 150);
        props[27].setSpace(names[27],Space.PROPERTY);
        spaces[27]=props[27];

        props[28] = new UtilitySpace(28, rentsU, -1, 150,this);
        props[28].setSpace(names[28],2);
        spaces[28] = props[28];

        int rentsL[] = {24, 120, 360, 850, 1025, 1200};
        props[29] = new Property(280, rentsL,6, 150);
        props[29].setSpace(names[29],Space.PROPERTY);
        spaces[29]=props[29];
        spaces[30].setSpace(names[30], Space.GO_TO_JAIL);

        int rentsM[] = {26, 130, 390, 900, 1100, 1275};
        props[31] = new Property(300, rentsM, 7, 200);
        props[31].setSpace(names[31],Space.PROPERTY);
        spaces[31]=props[31];

        props[32] = new Property(300, rentsM,7, 200);
        props[32].setSpace(names[32],Space.PROPERTY);
        spaces[32]=props[32];

        spaces[33].setSpace(names[33], Space.COMMUNITY_CHEST);

        int rentsN[] = {28, 150, 450, 1000, 1200, 1400};
        props[34] = new Property(320, rentsM,7, 200);
        props[34].setSpace(names[34],Space.PROPERTY);
        spaces[34]=props[34];
        spaces[35] = new Railroad(150, railRoadRents, 75);
        spaces[35].setSpace(names[35],Space.RAILROAD);

        spaces[36].setSpace(names[36],Space.CHANCE);

        int rentsO[] = {35, 175, 500, 1100, 1300, 1500};
        props[37] = new Property(350, rentsO,8, 200);
        props[37].setSpace(names[37],Space.PROPERTY);
        spaces[37]=props[37];
        spaces[38].setSpace(names[38], Space.LUXURY_TAX);

        int rentsP[] = {50, 200, 600, 1400, 1700, 2000};
        props[39] = new Property(400, rentsP,8, 200);
        props[39].setSpace(names[39],Space.PROPERTY);
        spaces[39]=props[39];



    }
    public String[] getAvailableTokens(){	//For use with player choosing a token
        int count = 0;
        for (String availableToken : availableTokens) {
            if (!availableToken.contains("NA")) {
                count++;
            }
        }
        String unchosenTokens[] = new String[count];
        count = 0;
        for (int i=0; i<availableTokens.length; i++) {
            if (!availableTokens[i].contains("NA")) {
                unchosenTokens[count] = availableTokens[i];	//find the tokens no one has picked
                count++;
            }
        }
        return unchosenTokens; //return the unchosen tokens
    }
    public void removeToken(String tokenToRemove){	//Removes chosen tokens from pool
        for (int i = 0; i<availableTokens.length; i++){
            if (tokenToRemove.contentEquals(availableTokens[i])){
                availableTokens[i] = "NA" + availableTokens[i];
            }
        }
    }
    public Space getSpace(int spaceNum){	//Gets the space asked for
        return spaces[spaceNum];
    }
    public Property getProperty(int spacenum){	//Gets the property asked for, needed because of generality
        return props[spacenum];
    }

    public Player getOtherUtilOwner(UtilitySpace myUtil){	//Find who owns other utility, needed for calculating rents
        if (myUtil == spaces[12]){
            return spaces[28].deeded;
        }
        else {
            return  spaces[12].deeded;
        }
    }
    public void auction(Space space){	//If player doesn't buy property auction is called
        System.out.println("\nStarting auction for " + space.name);

        for (Player player1 : players) {
            player1.newBid();
        }
        int maxBid = 0;
        int playersBidding = players.length;
        while (playersBidding>1) {	//While there are still bidders, continue running auction
            for (Player player : players) {
                if (player.stillBidding()) {
                    int bid = player.bid(maxBid, space);
                    if (bid != -1) {	//If bid is valid, they become highest bidder
                        maxBid = bid;

                    } else {	//Player dropped from auction
                        playersBidding--;
                        if (playersBidding==1){	//Last bidder
                            break;
                        }
                    }
                }
            }
        }
        for (Player player : players) {  //Find the only player that didn't leave the auction
            if (player.stillBidding()) {
                player.buySpace(space,maxBid);	//Player gets property for auctionedPrice
            }
        }
        UIManager.put("OptionPane.cancelButtonText", "END GAME");
        UIManager.put("OptionPane.okButtonText", "OK");
    }

    public void getStartingPositions(){
        int playerRolls[] = new int[players.length];
        for (int i=0;i<players.length; i++){
            playerRolls[i] = Math.abs(players[i].rollDice(-1)); //Passes negative one because this is not an in game roll;
        }
        //Sort players by roll (Bubble sort)
        for (int i = 0; i < playerRolls.length; i++) {
            for (int j = 0; j < playerRolls.length; j++) {
                if (playerRolls[i] > playerRolls[j]){
                    int tempInt = playerRolls[i];
                    Player tempPlayer = players[i];
                    playerRolls[i] = playerRolls[j];
                    players[i] = players[j];
                    playerRolls[j] = tempInt;
                    players[j] = tempPlayer;
                }
            }
        }
        System.out.println("The order of players will be: ");
        for (Player player : players) {
            System.out.println("\t" + player.Name);
        }
        
    }
    public void runGame() throws IOException{	//Run game and its logic
        boolean continueGame = true;    //for future fixes & testing
        //Go through the now ordered array of players and have them run their turns
        while (true){
            for (Player player : players) {
                if (player.stillPlaying) {
                    current=player;
                    player.runTurn(); //If this returns -1 they are in jail, 10 is just visiting
                    timeLeft += 1;
                    if (timeLeft > 40) {
                        getWinner();
                        return;
                    }
                }
            }
        }
    }
    private void getWinner(){	//Find winner based off money
        int maxMoney = 0;
        Player winningPlayer = null;
        for (Player player : players){
            if (player.money > maxMoney){
                winningPlayer = player;
            }
        }
        if (winningPlayer!=null) {
            System.out.println(winningPlayer.Name + " won the game!");
        }
    }
    public Player[] getPlayers(){	//return players

        return players;
    }
    public void payAllFifty(Player player){	//Used for chance card, pays others $50
        for (Player otherPlayer : players){
            if (otherPlayer != player){
                player.money-=50;
                otherPlayer.money+=50;
            }
        }
    }
    public void collectAllFiftty(Player player){ //Used for chance card, get 50 from all
        for (Player otherPlayer : players){
            if (otherPlayer != player){
                player.money+=50;
                otherPlayer.money-=50;
            }
        }
    }
    public String pullChance(Player player){	//Player picks a chance card
        return cardDeck.pullChance(player);
    }
    public String pullCC(Player player){	//Player picks a community chest card
        return cardDeck.pullCC(player);
    }
    public boolean isMonopolized(Player player, Property property){
        for (Property possible : props){
            
            try{
                if (possible.getGroup() == property.getGroup() && possible.deeded != property.deeded){
                return false;
                }
            }
            catch(NullPointerException e){
              if (props.length<5)
                return true;
              System.out.println(props.length);
              return false;
            }
        }
        return true;
    }
}
