package com.pengrl.poker;

import java.util.ArrayList;
import java.util.List;

public class Person {

    private String name;
    private List<Card> cards = new ArrayList<>();
    private WinCard winCard;

    public Person(String name) {
        this.name = name;
    }

    public WinCard getWinCard() {
        return winCard;
    }

    public void setWinCard(WinCard winCard) {
        this.winCard = winCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "赢家是" + this.name + ",牌是：" + this.winCard.getCardType() + "(" + this.join(this.winCard.getCards()) + ")";
    }

    private static String join(List<Card> cards) {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.getColor().name + card.getTitle());
        }
        return sb.toString();
    }
}
