package com.pengrl.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Poker {

    public List<Card> init() {
        List<Card> cardList = new ArrayList<>();
        for (int i = 1; i < 14; i++) {
            for (Card.Color color : Card.Color.values()) {
                cardList.add(new Card(i, color));
            }
        }
        return cardList;
    }

    public void shuffle(List<Card> cards) {
        Collections.shuffle(cards);
    }

    public Card send(List<Card> cards, boolean qie) {
        if (qie) {
            cards.remove(0);
        }
        return cards.remove(0);
    }

    private static List<Card> common = new ArrayList<>(5);

    public static void main(String[] args) {
        List<Card> royarFlush = new ArrayList<>();
        royarFlush.add(new Card( 2, Card.Color.DIAMLOND));
        royarFlush.add(new Card( 3, Card.Color.CLUB));
        royarFlush.add(new Card( 3, Card.Color.DIAMLOND));
        royarFlush.add(new Card( 2, Card.Color.HEART));
        royarFlush.add(new Card( 2, Card.Color.SPADE));
        TexasHoldEmpoker empoker1 = new TexasHoldEmpoker(royarFlush);
        WinCard winCard = empoker1.getWinCard();
        Person p1 = new Person("阿亮");
        p1.setWinCard(winCard);
        System.out.println(p1);


        Poker poker = new Poker();
        List<Card> cardList = poker.init();
        List<Person> people = new ArrayList<>();
        int personSize = 8;
        for (int i = 0; i < personSize; i++) {
            Person person = new Person("玩家--" + i);
            people.add(person);
        }
        poker.shuffle(cardList);
        for (int i = 0; i < 2; i++) {
            for (Person person : people) {
                person.getCards().add(poker.send(cardList, false));
            }
        }
        for (Person person : people) {
            System.out.println(person.getName() + "的牌是：" + join(person.getCards()));
        }
        for (int i = 0; i < 3; i++) {
            common.add(poker.send(cardList, false));
        }
        for (int i = 0; i < 2; i++) {
            common.add(poker.send(cardList, true));
        }
        System.out.println("公共牌是：" + join(common));
        System.out.println("------");
        for (Person person : people) {
            List<Card> combine = new ArrayList<>(person.getCards());
            combine.addAll(common);
            TexasHoldEmpoker empoker = new TexasHoldEmpoker(combine);
            person.setWinCard(empoker.getWinCard());
            System.out.println(person);
        }

        System.out.println("-----");
        System.out.println("-----");
        System.out.println("-----");
        List<Person> winner = poker.getWinner(people);
        for (Person p : winner) {
            System.out.println(p);
        }
    }

    public List<Person> getWinner(List<Person> list) {
        Collections.sort(list, (p1, p2) -> {
            WinCard w1 = p1.getWinCard();
            WinCard w2 = p2.getWinCard();
            if (w1.getCardType().compareTo(w2.getCardType()) == 0) {
                return w1.getSum() - w2.getSum();
            } else {
                return w1.getCardType().compareTo(w2.getCardType());
            }
        });
        List<Person> winner = new ArrayList<>();
        Person firstWinner = list.get(0);
        winner.add(firstWinner);
        for (int i = 1; i < list.size(); i++) {
            Person next = list.get(i);
            if (firstWinner.equals(next)) {
                winner.add(next);
            }
        }
        return winner;
    }

    private static String join(List<Card> cards) {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.getColor().name + card.getTitle());
        }
        return sb.toString();
    }
}
