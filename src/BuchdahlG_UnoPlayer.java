import java.util.ArrayList;
import java.util.List;

public class BuchdahlG_UnoPlayer implements UnoPlayer {

    UnoPlayer.Color blue = Color.BLUE;
    UnoPlayer.Color red = Color.RED;
    UnoPlayer.Color yellow = Color.YELLOW;
    UnoPlayer.Color green = Color.GREEN;

    UnoPlayer.Rank number = Rank.NUMBER;
    UnoPlayer.Rank skip = Rank.SKIP;
    UnoPlayer.Rank reverse = Rank.REVERSE;
    UnoPlayer.Rank drawTwo = Rank.DRAW_TWO;
    UnoPlayer.Rank wildDrawFour = Rank.WILD_D4;
    UnoPlayer.Rank wild = Rank.WILD;

    /**
     * play - This method is called when it's your turn and you need to
     * choose what card to play.
     *
     * The hand parameter tells you what's in your hand. You can call
     * getColor(), getRank(), and getNumber() on each of the cards it
     * contains to see what it is. The color will be the color of the card,
     * or "Color.NONE" if the card is a wild card. The rank will be
     * "Rank.NUMBER" for all numbered cards, and another value (e.g.,
     * "Rank.SKIP," "Rank.REVERSE," etc.) for special cards. The value of
     * a card's "number" only has meaning if it is a number card.
     * (Otherwise, it will be -1.)
     *
     * The upCard parameter works the same way, and tells you what the 
     * up card (in the middle of the table) is.
     *
     * The calledColor parameter only has meaning if the up card is a wild,
     * and tells you what color the player who played that wild card called.
     *
     * Finally, the state parameter is a GameState object on which you can 
     * invoke methods if you choose to access certain detailed information
     * about the game (like who is currently ahead, what colors each player
     * has recently called, etc.)
     *
     * You must return a value from this method indicating which card you
     * wish to play. If you return a number 0 or greater, that means you
     * want to play the card at that index. If you return -1, that means
     * that you cannot play any of your cards (none of them are legal plays)
     * in which case you will be forced to draw a card (this will happen
     * automatically for you.)
     */
    public int play(List<Card> hand, Card upCard, Color calledColor, GameState state)
    {

        int[] handSizes = state.getNumCardsInHandsOfUpcomingPlayers();
        int smallestHand = handSizes[0];
        for (int i = 1; i< handSizes.length-1; i++){
            if (handSizes[i] < smallestHand){
                smallestHand = handSizes[i];
            }
        }
        int myHand = handSizes[3];


        // Dump high points if losing bad
        if (smallestHand <= 2 && myHand > 4){
            if (this.playWildIfPossible(hand) != -1) {
                return this.playWildIfPossible(hand);
            }
        }
        // if person ahead of you is winning
        if (handSizes[0] < myHand){
            if (this.playSkipD2IfPossible(hand, upCard, calledColor) != -1){
                return this.playSkipD2IfPossible(hand, upCard, calledColor);
            }
        }

        // if person ahead of you is ahead of person behind you
        if (handSizes[0] < handSizes[2]){
            if (this.playReverseIfPossible(hand, upCard, calledColor) != -1){
                return this.playReverseIfPossible(hand, upCard, calledColor);
            }
        }

        if (handContainsValidNumberCard(hand, upCard, calledColor)){
            return (playValidNumberCard(hand, upCard, calledColor));
        }
        else {
            if (trySpecials(hand, upCard, calledColor) != -1){
                return trySpecials(hand, upCard, calledColor);
            }else{
                return -1;
            }
        }
    }

    private Color neededColor(Card upCard, Color calledColor){
      if (upCard.getRank().equals(wild) || upCard.getRank().equals(wildDrawFour)) {
          return calledColor;
      }else{
          return upCard.getColor();
      }
    }

    private boolean canPlay(Card card, Card upCard, Color calledColor) {
        return (card.getColor().equals(neededColor(upCard, calledColor))
                || (card.getNumber() == upCard.getNumber()) && card.getRank().equals(number))
                || (!(card.getRank().equals(number)) && card.getRank().equals(upCard.getRank()));
    }

    private boolean handContainsValidNumberCard(List<Card> hand, Card upCard, Color calledColor){
        for(Card card: getNumberCards(hand)) {
            if (canPlay(card, upCard, calledColor)) {
                return true;
            }
        }
        return false;
    }

    private int playValidNumberCard(List<Card> hand, Card upCard, Color calledColor){
        ArrayList<Card> cards = getNumberCards(hand);
        int maxNo = 20;
        Card result = null;
        for(Card card: cards){
            if (canPlay(card, upCard, calledColor) && card.getNumber() < maxNo){
                result = card;
                maxNo = card.getNumber();
            }
        }
        return hand.indexOf(result);
    }

    private int trySpecials(List<Card> hand, Card upCard, Color calledColor){
        if (this.playReverseIfPossible(hand, upCard, calledColor) != -1){
            return this.playReverseIfPossible(hand, upCard, calledColor);
        }
        if (this.playSkipD2IfPossible(hand, upCard, calledColor) != -1){
            return this.playSkipD2IfPossible(hand, upCard, calledColor);
        }
        if (this.playWildIfPossible(hand) != -1) {
            return this.playWildIfPossible(hand);
        }
        return -1;
    }

    private int playWildIfPossible(List<Card> hand){
        int result = -1;
        for (Card card : hand){
            if (card.getRank().equals(wildDrawFour)) {result = hand.indexOf(card);}
        }
        if (result != -1) {return result;}
        for (Card card : hand){
            if (card.getRank().equals(wild)) {result = hand.indexOf(card);}
        }
        return result;
    }

    private int playSkipD2IfPossible(List<Card> hand, Card upCard, Color calledColor){
        int result = -1;
        for (Card card : hand){
            if (((card.getRank().equals(skip)&&upCard.getRank().equals(skip))
                    || (card.getRank().equals(drawTwo) && upCard.getRank().equals(drawTwo)))
                    && canPlay(card, upCard, calledColor))
                {result = hand.indexOf(card);}
        }
        return result;
    }

    private int playReverseIfPossible(List<Card> hand, Card upCard, Color calledColor){
        int result = -1;
        for (Card card : hand){
            if ((card.getRank().equals(reverse)) && canPlay(card, upCard, calledColor)) {result = hand.indexOf(card);}
        }
        return result;
    }

    private ArrayList<Card> getNumberCards(List<Card> cards){
        ArrayList<Card> result = new ArrayList<Card>();
        for (Card card : cards){
            if (card.getNumber()<20){result.add(card);}
        }
        return result;
    }

    /**
     * callColor - This method will be called when you have just played a
     * wild card, and is your way of specifying which color you want to
     * change it to.
     *
     * You must return a valid Color value from this method. You must not
     * return the value Color.NONE under any circumstances.
     */
    public Color callColor(List<Card> hand)
    {
        // THIS IS WHERE YOUR AMAZING CODE GOES
        int greens =0;
        int reds = 0;
        int yellows = 0;
        int blues = 0;
        for (Card card: hand){
            if (card.getColor().equals(red)) {reds += 1;}
            if (card.getColor().equals(green)) {greens += 1;}
            if (card.getColor().equals(blue)) {blues += 1;}
            if (card.getColor().equals(yellow)) {yellows += 1;}
        }
        if (greens > reds && greens > yellows && greens > blues) {return green;}
        else if (reds > yellows && reds > blues) { return red;}
        else if (yellows > blues) {return yellow;}
        else return blue;
    }
}