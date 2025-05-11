import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class PlayerTank extends Tank {
    private boolean[] keys = new boolean[256];

    public PlayerTank(int x, int y) {
        super(x, y, Color.YELLOW, "tankYellow.png");
    }
  public void reset(int x, int y) {
        super.reset(x, y);
        direction = "up";
        angle = 0;
    }
    public void handleKeyPress(int keyCode) {
        keys[keyCode] = true;
        if(keyCode == KeyEvent.VK_SPACE) shoot();
    }

    public void handleKeyRelease(int keyCode) {
        keys[keyCode] = false;
    }

    public void update(Brick bricks) {
        int newX = x;
        int newY = y;
        
        // Update direction and angle based on key pressed
        if(keys[KeyEvent.VK_W]) {
            newY -= speed;
            direction = "up";
            angle = 0;
        }
        if(keys[KeyEvent.VK_S]) {
            newY += speed;
            direction = "down";
            angle = 180;
        }
        if(keys[KeyEvent.VK_A]) {
            newX -= speed;
            direction = "left";
            angle = -90;
        }
        if(keys[KeyEvent.VK_D]) {
            newX += speed;
            direction = "right";
            angle = 90;
        }
          // Check collision in 4 directions separately
        boolean xCollision = bricks.checkTankCollision(newX, y);
        boolean yCollision = bricks.checkTankCollision(x, newY);
        boolean xyCollision = bricks.checkTankCollision(newX, newY);
        
        if (!xyCollision) {
            x = newX;
            y = newY;
        } else if (!xCollision) {
            x = newX; // Allow X movement
        } else if (!yCollision) {
            y = newY; // Allow Y movement
        }
        
        // Screen boundaries
      // To (account for collision box size):
newX = Math.max(-Tank.COLLISION_OFFSET_X, 
               Math.min(800 - Tank.COLLISION_WIDTH - Tank.COLLISION_OFFSET_X, newX));
newY = Math.max(-Tank.COLLISION_OFFSET_Y, 
               Math.min(600 - Tank.COLLISION_HEIGHT - Tank.COLLISION_OFFSET_Y, newY));
        
        if(!bricks.checkTankCollision(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    public void draw(Graphics2D g2d) {
        AffineTransform old = g2d.getTransform();
        g2d.rotate(Math.toRadians(angle), x + 25, y + 25);
        g2d.drawImage(image, x, y, 50, 50, null);
        g2d.setTransform(old);
        if(bullet != null) bullet.draw(g2d);
        
        
         if (isHit) {
        drawHitEffect(g2d);
    }
    
   
    
   
    
    if (bullet != null) bullet.draw(g2d);
    }
}