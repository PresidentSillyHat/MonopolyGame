package esof322.pa4.team5;

import java.util.Collections;
import java.util.Stack;

/**
 * Created by Garret Gershmel on 12/4/2017.
 * This code can be used freely with no restrictions
 */
public class ChanceCC {
    Stack<Card> chanceCards;
    Stack<Card> ccCards;
    Board board;

    public ChanceCC(Board board){
        this.board = board;
        chanceCards = new Stack<>();
        ccCards = new Stack<>();
        buildDecks();
    }

    public String pullChance(Player player){
        Card top = chanceCards.pop();
        if (top==null){
            buildChance();
            top = chanceCards.pop();
        }
        return determine(player, top);
    }

    private String determine(Player player, Card top) {
        if (top.space>0){
            player.Position = top.space;
            return top.name;
        } else if (top.space == -1){
            player.money+=top.value;
            return top.name;
        } else if (top.space == -5){
            if (player.Position > 25 && player.Position<35){
                player.Position = 35;
            } else if (player.Position > 15){
                player.Position = 25;
            } else if (player.Position > 5){
                player.Position = 15;
            } else {
                player.Position = 5;
            }
            return top.name;
        } else if (top.space == -10){
            player.hasGetOut = true;
            return top.name;
        } else if (top.space == -11){
            player.inJail = true;
            player.Position=10;
            return top.name;
        } else if (top.space == -3){
            player.Position = player.Position - 3;
            return top.name;
        } else if (top.space==-50){
            board.payAllFifty(player);
            return top.name;
        } else if (top.space==-55){
            board.collectAllFiftty(player);
            return top.name;
        } else if (top.space==-66){
            player.payForHouses();
        }
        return top.name;
    }

    public String pullCC(Player player){
        Card top = ccCards.pop();
        if (top==null){
            buildCC();
            top = ccCards.pop();
        }
        return determine(player, top);
    }

    private void buildDecks(){
        buildChance();
        buildCC();
    }

    private void buildChance(){
        Card go = new Card("Advance to Go", 0, 0);
        chanceCards.push(go);
        Card illinois = new Card("Advance to Illinois Ave.", 24, 0);
        chanceCards.push(illinois);
        Card charles = new Card("Advance to St. Charles Place", 11, 0);
        chanceCards.push(charles);
        Card elec = new Card("Advance to the electric company", 12, 0);
        chanceCards.push(elec);
        Card railroad = new Card("Advance to nearest railroad", -5, 0);
        chanceCards.push(railroad);
        chanceCards.push(railroad);
        Card dividend = new Card("Bank pays you dividend of 50", -1, 50);
        chanceCards.push(dividend);
        Card getOut = new Card("Get out of jail free", -10, 0);
        chanceCards.push(getOut);
        Card back3 = new Card("Go back 3 spaces", -3, 0);
        chanceCards.push(back3);
        Card goToJail = new Card("Go to jail", -11, 0);
        chanceCards.push(goToJail);
        Card generalRepairs = new Card("Pay for each house", -66, 0);
        chanceCards.push(generalRepairs);
        Card poorTax = new Card("Pay poor tax of $15", -1, -15);
        chanceCards.push(poorTax);
        Card rr = new Card("Take a trip on the reading railroad", 5, 0);
        chanceCards.push(rr);
        Card bw = new Card("Take a walk on boardwalk", 39, 0);
        chanceCards.push(bw);
        Card chairman = new Card("Pay each player $50", -50, 0);
        chanceCards.push(chairman);
        Card loan = new Card("Your loan matures", -1, 150);
        chanceCards.push(loan);
        Card cw = new Card("You won a crossword competition", -1, 100);
        chanceCards.push(cw);

        Collections.shuffle(chanceCards);
    }
    private void buildCC(){
        Card go = new Card("Advance to Go", 0, 0);
        ccCards.push(go);
        Card berror = new Card("Bank error in your favor +200", -1, 200);
        chanceCards.push(berror);
        Card doctor = new Card("Doctor's fees, pay $50", -1, -50);
        chanceCards.push(doctor);
        Card stock = new Card("From sale of stock you get $50", -1, 50);
        chanceCards.push(stock);
        Card getOut = new Card("Get out of jail free", -10, 0);
        ccCards.push(getOut);
        Card goToJail = new Card("Go to jail", -11, 0);
        ccCards.push(goToJail);
        Card collectFifty = new Card("Collect 50 from every player", -55, 0);
        ccCards.push(collectFifty);
        Card xmas = new Card("Christmas fund matures collect $100", -1, 100);
        ccCards.push(xmas);
        Card refund = new Card("Income tax refund of $20", -1, 20);
        ccCards.push(refund);
        Card life = new Card("Life insurance matures collect $100", -1, 100);
        ccCards.push(life);
        Card hosp = new Card("Pay hospital fees $100", -1, -100);
        ccCards.push(hosp);
        Card school = new Card("Pay school fees of $150", -1, -150);
        ccCards.push(school);
        Card cons = new Card("Consultant fee collect $25", -1, 25);
        ccCards.push(cons);
        Card generalRepairs = new Card("Pay for each house", -66, 0);
        ccCards.push(generalRepairs);
        Card beauty = new Card("Won beauty contest $10", -1, 10);
        ccCards.push(beauty);
        Card inherit = new Card("You inherit $100", -1, 100);
        ccCards.push(inherit);

        Collections.shuffle(ccCards);
    }
}

class Card{
    String name;
    int space;
    int value;

    public Card(String name, int space, int value){
        this.name = name;
        this.space = space;
    }
}