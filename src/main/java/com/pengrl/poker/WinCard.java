package com.pengrl.poker;

import java.util.Collections;
import java.util.List;

public class WinCard {

    private List<Card> cards;

    private CardType cardType;

    private int sum;

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public WinCard(List<Card> cards, CardType cardType) {
        Collections.sort(cards, (c1, c2) -> c2.getNum() - c1.getNum());
        this.cards = cards;
        Card firstCard = cards.get(0);
        this.cardType = cardType;
        boolean isStraight = cardType == CardType.STRAIGHT || cardType == CardType.STRAIGHT_FLUSH;
        for (Card card : cards) {
            int value = card.getNum();
            if (value == 1) {
                if (firstCard.getNum() == 5 && isStraight) {
                    value = 1;
                } else {
                    value = 14;
                }
            }
            this.sum += value;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WinCard) {
            WinCard card = (WinCard) obj;
            return this.cardType.equals(card.getCardType()) && this.sum == card.getSum();
        }
        return false;
    }

    public enum CardType {
        ROYAR_FLUSH,
        STRAIGHT_FLUSH,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        FLUSH,
        STRAIGHT,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }
}
