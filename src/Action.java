public class Action {

    private boolean mine;
    private int turn; //1 right -1 left
    private boolean move;

    private boolean coal;

    private boolean emerald;
    public void move() {
        move = true;
    }

    public void turnRight() {
        turn = 1;
    }

    public void turnLeft() {
        turn = -1;
    }

    public void mine(boolean mine) {
        mine = true;
    }

    public void coal(boolean coal) {
        coal = true;
    }

    public void emerald(boolean emerald) {
        emerald = true;
    }

    public boolean getMove() {
        return move;
    }
    public int getTurn() {
        return turn;
    }
    public boolean isMine() {
        return mine;
    }

    public boolean isCoal() {
        return coal;
    }

    public boolean isEmerald() {
        return emerald;
    }

    public Action()
    {
        move=false;
        turn = 0;
        mine=false;
        coal=false;
        emerald=false;
    }




}
