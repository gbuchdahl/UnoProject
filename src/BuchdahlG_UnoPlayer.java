import java.util.ArrayList;
import java.util.List;

public class BuchdahlG_UnoPlayer implements UnoPlayer {
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
        int myHand = handSizes[3]


        // Dump high points if losing bad
        if (smallestHand <= 2 && myHand > 4){
            if (this.playWildIfPossible(hand) != -1) {
                return this.playWildIfPossible(hand);
            }

        }







        if (handContainsValidNumberCard(hand, upCard, calledColor)){
            return (playValidNumberCard(hand, upCard, calledColor));
        }
        else {
            if (this.playWildIfPossible(hand) != -1) {
                return this.playWildIfPossible(hand);
            }
            return -1;
        }
    }

    private Color neededColor(Card upCard, Color calledColor){
      if (upCard.getRank().equals(Rank.WILD) || upCard.getRank().equals(Rank.WILD_D4)) {
          return calledColor;
      }else{
          return upCard.getColor();
      }
    }

    private boolean canPlay(Card card, Card upCard, Color calledColor) {
        return (card.getColor().equals(neededColor(upCard, calledColor)) || card.getNumber() == upCard.getNumber())
                || (!card.getRank().equals(Rank.NUMBER) && card.getRank().equals(upCard.getRank()));
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

    private int playWildIfPossible(List<Card> hand){
        int result = -1;
        for (Card card : hand){
            if (card.getRank().equals(Rank.WILD_D4)) { result = hand.indexOf(card);}
        }
        if (result != -1) {return result;}
        for (Card card : hand){
            if (card.getRank().equals(Rank.WILD)) { result = hand.indexOf(card);}
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
        return Color.RED;
    }
}