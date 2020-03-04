package com.pengrl.poker;

import java.util.*;

public class TexasHoldEmpoker {

    private List<List<Card>> sortBySameNumCards;
    private List<Card> cards;
    private List<Rule> rules;

    public TexasHoldEmpoker(List<Card> cards) {
        this.cards = cards;
        this.sortBySameNumCards = sortBySameCardCount(cards);
        this.rules = new ArrayList<>();
        rules.add(new FourOfAKind());
        rules.add(new FullHouse());
        rules.add(new FlushOrStraight());
        rules.add(new ThreeOfAKind());
        rules.add(new Pairs());
        rules.add(new HighCard());
    }

    public WinCard getWinCard() {
        for (Rule rule : this.rules) {
            WinCard winCard = rule.bingo(cards);
            if (winCard != null) {
                return winCard;
            }
        }
        return null;
    }

    interface Rule {
        WinCard bingo(List<Card> cards);
    }

    class FourOfAKind implements Rule {
        @Override
        public WinCard bingo(List<Card> cards) {
            List<Card> winCards = new ArrayList<>();
            List<Card> firstCards = sortBySameNumCards.get(0);
            Card maxCard = sortBySameNumCards.get(1).get(0);
            if (firstCards.size() == 4) {
                winCards.addAll(firstCards);
                for (int i = 2; i < sortBySameNumCards.size(); i++) {
                    Card card = sortBySameNumCards.get(i).get(0);
                    if (maxCard.getNum() < card.getNum()) {
                        maxCard = card;
                    }
                }
                winCards.add(maxCard);
                return new WinCard(winCards, WinCard.CardType.FOUR_OF_A_KIND);
            }
            return null;
        }
    }

    class FullHouse implements Rule {
        @Override
        public WinCard bingo(List<Card> cards) {
            List<Card> firstCards = sortBySameNumCards.get(0);
            List<Card> secondCards = sortBySameNumCards.get(1);
            if (firstCards.size() == 3 && secondCards.size() == 2) {
                List<Card> winCards = new ArrayList<>();
                winCards.addAll(firstCards);
                winCards.addAll(secondCards);
                return new WinCard(winCards, WinCard.CardType.FULL_HOUSE);
            }
            return null;
        }
    }




    public List<List<Card>> sortBySameCardCount(List<Card> cards) {
        Map<Integer, List<Card>> cardMap = new HashMap<>();
        for (Card card : cards) {
            List<Card> cardList = cardMap.get(card.getNum());
            if (cardList == null) {
                cardList = new ArrayList<>();
            }
            cardList.add(card);
            cardMap.put(card.getNum(), cardList);
        }
        List<List<Card>> cardsList = new ArrayList<>(cardMap.values());
        Collections.sort(cardsList, (c1, c2) -> c2.size() - c1.size());
        return cardsList;
    }

    class FlushOrStraight implements Rule {
        @Override
        public WinCard bingo(List<Card> cards) {
            WinCard flush = flush(cards);
            WinCard straight = straight(cards);
            if (flush != null && straight != null) {
                if (straight != null && flush.getSum() == straight.getSum()) {
                    if (flush.getSum() == (10 + 11 + 12 + 13 + 14)) {
                        flush.setCardType(WinCard.CardType.ROYAR_FLUSH);
                    } else {
                        flush.setCardType(WinCard.CardType.STRAIGHT_FLUSH);
                    }
                    return flush;
                } else {
                    return flush;
                }
            } else if (flush != null) {
                return flush;
            } else if (straight != null) {
                return straight;
            }
            return null;
        }

        public WinCard flush(List<Card> cards) {
            Map<Card.Color, List<Card>> map = new HashMap<>();
            for (Card card : cards) {
                List<Card> cardList = map.get(card.getColor());
                if (cardList == null) {
                    cardList = new ArrayList<>();
                }
                cardList.add(card);
                map.put(card.getColor(), cardList);
            }
            for (List<Card> cardList : map.values()) {
                if (cardList.size() >= 5) {
                    Collections.sort(cardList, (c1, c2) -> compator(c1, c2));
                    List<Card> winCards = new ArrayList<>(cardList.subList(0, 5));
                    return new WinCard(winCards, WinCard.CardType.FLUSH);
                }
            }
            return null;
        }

        public WinCard straight(List<Card> cards) {
            Collections.sort(cards, (c1, c2) -> c2.getNum() - c1.getNum());
            Card firstCard = cards.get(0);
            int size = cards.size();
            Card lastCard = cards.get(size - 1);
            // 判断是否顶顺
            if (firstCard.getNum() == 13 && lastCard.getNum() == 1) {
                List<Card> winCards = new ArrayList<>();
                winCards.add(firstCard);
                winCards.add(lastCard);
                Card previousCard = firstCard;
                for (int i = 1; i < size - 1; i++) {
                    Card next = cards.get(i);
                    if (next.getNum() == previousCard.getNum()) {
                        continue;
                    }
                    if (next.getNum() == previousCard.getLastNum()) {
                        previousCard = next;
                        winCards.add(next);
                        if (winCards.size() == 5) {
                            return new WinCard(winCards, WinCard.CardType.STRAIGHT);
                        }
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                List<Card> winCards = new ArrayList<>();
                Card one = cards.get(i);
                winCards.add(one);
                Card previous = one;
                for (int j = 1; j < size; j++) {
                    Card two = cards.get(j);
                    if (previous.getNum() == two.getNum()) {
                        continue;
                    } else if (previous.getLastNum() == two.getNum()) {
                        previous = two;
                        winCards.add(two);
                        if (winCards.size() == 5) {
                            return new WinCard(winCards, WinCard.CardType.STRAIGHT);
                        }
                    } else {
                        winCards.clear();
                        break;
                    }
                }
            }
            return null;
        }
    }

    class ThreeOfAKind implements Rule {
        @Override
        public WinCard bingo(List<Card> cards) {
            List<Card> firstCards = sortBySameNumCards.get(0);
            if (firstCards.size() == 3) {
                Collections.sort(sortBySameNumCards, (c1, c2) -> c2.get(0).getNum() - c1.get(0).getNum());
                List<Card> winCards = new ArrayList<>();
                winCards.addAll(firstCards);
                winCards.add(sortBySameNumCards.get(1).get(0));
                winCards.add(sortBySameNumCards.get(2).get(0));
                return new WinCard(winCards, WinCard.CardType.THREE_OF_A_KIND);
            }
            return null;
        }
    }

    class Pairs implements Rule {
        @Override
        public WinCard bingo(List<Card> cards) {
            List<List<Card>> pairCards = new ArrayList<>();
            for (List<Card> cardList : sortBySameNumCards) {
                if (cardList.size() == 2) {
                    pairCards.add(cardList);
                }
            }
            if (pairCards.isEmpty()) {
                return null;
            }
            List<Card> winCards = new ArrayList<>();
            int pairSize = pairCards.size();
            WinCard.CardType cardType;
            if (pairSize > 1) {
                Collections.sort(pairCards, (c1, c2) -> {
                    Card card1 = c1.get(0);
                    Card card2 = c2.get(0);
                    return compator(card1, card2);
                });
                winCards.addAll(pairCards.get(0));
                winCards.addAll(pairCards.get(1));
                cardType = WinCard.CardType.TWO_PAIR;
            } else {
                winCards.addAll(pairCards.get(0));
                cardType = WinCard.CardType.ONE_PAIR;
            }
            Collections.sort(cards, (c1, c2) -> compator(c1, c2));
            for (Card card : cards) {
                if (!winCards.contains(card)) {
                    winCards.add(card);
                    if (winCards.size() >= 5) {
                        return new WinCard(winCards, cardType);
                    }
                }
            }
            return null;
        }
    }

    class HighCard implements Rule {

        @Override
        public WinCard bingo(List<Card> cards) {
            Collections.sort(cards, (c1, c2) -> compator(c1, c2));
            List<Card> winCards = new ArrayList<>(cards.subList(0, 5));
            return new WinCard(winCards, WinCard.CardType.HIGH_CARD);
        }
    }

    private int compator(Card card1, Card card2) {
        if (card1.getNum() == 1) {
            return -1;
        } else if (card2.getNum() == 1) {
            return 1;
        }
        return card2.getNum() - card1.getNum();
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 10,11,12,13);
        Collections.shuffle(list);
        for (Integer i : list) {
            System.out.print(i + "-");
        }
        System.out.println("----");
        Collections.sort(list, (c1, c2) -> {
            if (c2 == 1) {
                return 1;
            } else if (c1 == 1) {
                return -1;
            }
            return c2 - c1;
        });
        for (Integer i : list) {
            System.out.print(i + "-");
        }
    }
}
