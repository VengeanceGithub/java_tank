import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Random;

public class BotTank extends Tank {
    private Random rand = new Random();
    private int moveTimer = 0;
    private double angle = 0;

    public BotTank(int x, int y) {
        super(x, y, Color.RED, "enemy.png");
    }

      public void reset(int x, int y) {
        super.reset(x, y);
        direction = "down";
        angle = 180;
    }
    
    public void update(Brick bricks) {
        if(moveTimer <= 0) {
            int dir = rand.nextInt(4);
            switch(dir) {
                case 0: 
                    direction = "up";
                    angle = 0;
                    break;
                case 1: 
                    direction = "right";
                    angle = 90;
                    break;
                case 2: 
                    direction = "down";
                    angle = 180;
                    break;
                case 3: 
                    direction = "left";
                    angle = -90;
                    break;
            }
            moveTimer = rand.nextInt(60) + 30;
        }
        
        int newX = x;
        int newY = y;
        
        switch(direction) {
            case "up": newY -= speed; break;
            case "down": newY += speed; break;
            case "left": newX -= speed; break;
            case "right": newX += speed; break;
        }
        
      // To (account for collision box size):
newX = Math.max(-Tank.COLLISION_OFFSET_X, 
               Math.min(800 - Tank.COLLISION_WIDTH - Tank.COLLISION_OFFSET_X, newX));
newY = Math.max(-Tank.COLLISION_OFFSET_Y, 
               Math.min(600 - Tank.COLLISION_HEIGHT - Tank.COLLISION_OFFSET_Y, newY));
        
        if(!bricks.checkTankCollision(newX, newY)) {
            x = newX;
            y = newY;
        }
        
        if(rand.nextInt(100) < 20) shoot();
        moveTimer--;
    }

   public void draw(Graphics2D g2d) {
    // Different color for bot hit effect
    if (isHit) {
        Color[] colors = {new Color(255, 255, 255, 150), new Color(255, 255, 255, 0)};
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Float(x + 25, y + 25), 40, 
            new float[]{0.0f, 1.0f}, colors
        );
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setPaint(paint);
        g2d.fillOval(x - 15, y - 15, 80, 80);
        g2d.setComposite(AlphaComposite.SrcOver);
    }
    
    AffineTransform old = g2d.getTransform();
    g2d.rotate(Math.toRadians(angle), x + 25, y + 25);
    g2d.drawImage(image, x, y, 50, 50, null);
    g2d.setTransform(old);
    
    
    
    if(bullet != null) bullet.draw(g2d);
}
}