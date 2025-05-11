
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Bullet {
    private int x, y;
    private String direction;
    private final int speed = 8;
    private final Color color;

    public Bullet(int x, int y, String direction, Color color) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.color = color;
    }

    public void update() {
        switch(direction) {
            case "up": y -= speed; break;
            case "down": y += speed; break;
            case "left": x -= speed; break;
            case "right": x += speed; break;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect(x-2, y-2, 4, 4);
    }
    

    public boolean checkCollision(Rectangle bounds) {
        return bounds.intersects(new Rectangle(x-2, y-2, 4, 4));
    }

    public boolean isOutOfBounds() {
        return x < 0 || x > 800 || y < 0 || y > 600;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}