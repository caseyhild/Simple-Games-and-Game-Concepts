public class Piece
{
    public int row;
    public int col;
    public String color;
    public String type;

    public Piece(int row, int col, String color, String type)
    {
        this.row = row;
        this.col = col;
        this.color = color;
        this.type = type;
    }

    public Piece(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public void move(int r, int c)
    {
        row = r;
        col = c;
    }

    public boolean equals(Piece p)
    {
        return row == p.row && col == p.col;
    }

    public String toString()
    {
        return row + " " + col + " " + color + " " + type;
    }
}