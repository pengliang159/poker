package com.pengrl.poker;

public class Card {

    private Integer num;
    private Color color;
    private String title;

    private Integer lastNum;
    private Integer nextNum;

    public Card(int num, Color color) {
        this.num = num;
        this.color = color;
        this.title = String.valueOf(num);
        this.lastNum = num - 1;
        this.nextNum = num + 1;
        if (num == 11) {
            this.title = "J";
        } else if (num == 12) {
            this.title = "Q";
        } else if (num == 13) {
            this.title = "K";
            this.nextNum = 1;
        } else if (num == 1) {
            this.title = "A";
            this.lastNum = 13;
        }
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLastNum() {
        return lastNum;
    }

    public void setLastNum(Integer lastNum) {
        this.lastNum = lastNum;
    }

    public Integer getNextNum() {
        return nextNum;
    }

    public void setNextNum(Integer nextNum) {
        this.nextNum = nextNum;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card card1 = (Card) obj;
            return this.num == card1.getNum() && this.color.equals(card1.getColor());
        }
        return false;
    }

    public enum Color {
        SPADE("黑桃"),
        HEART("红桃"),
        CLUB("梅花"),
        DIAMLOND("方块"),
        ;

        public final String name;

        Color(String name) {
            this.name = name;
        }
    }
}
