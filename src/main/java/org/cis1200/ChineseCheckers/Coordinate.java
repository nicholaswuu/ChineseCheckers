package org.cis1200.ChineseCheckers;

public class Coordinate implements Comparable<Coordinate> {

    private int x;
    private int y;
    public Coordinate(int x0, int y0) {
        x = x0;
        y = y0;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int compareTo(Coordinate c) {
        if (this.x < c.x) {
            return -1;
        } else if (this.x > c.x) {
            return 1;
        } else if (this.y < c.y) {
            return -1;
        } else if (this.y > c.y) {
            return 1;
        }
        return 0;
    }
}
