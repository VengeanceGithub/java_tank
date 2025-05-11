import java.awt.*;
import java.awt.geom.Point2D;
import javax.swing.ImageIcon;

public abstract class Tank {
    protected int x, y;
    protected Image image;
    protected Bullet bullet;
    protected double angle; // Add angle field
    protected int speed = 3;
    protected Color bulletColor;
    protected String direction = "up";
      protected int health = 3;
       protected boolean isHit = false;
    protected int hitTimer = 0;
    protected static final int HIT_ANIMATION_DURATION = 10; // frames
     // Add collision box constants
    protected static final int COLLISION_WIDTH = 40;
    protected static final int COLLISION_HEIGHT = 40;
    protected static final int COLLISION_OFFSET_X = 5; // (50 - 40)/2
    protected static final int COLLISION_OFFSET_Y = 5;

    public Rectangle getBounds() {
        return new Rectangle(
            x + COLLISION_OFFSET_X,
            y + COLLISION_OFFSET_Y,
            COLLISION_WIDTH,
            COLLISION_HEIGHT
        );
    }
    public int getX() { return x; }
    public int getY() { return y; }
    
    public Tank(int x, int y, Color bulletColor, String imagePath) {
        this.x = x;
        this.y = y;
          this.angle = 0; // Initialize angle
        this.bulletColor = bulletColor;
        this.image = new ImageIcon(imagePath).getImage();
    }
      
    
    public int getHealth() { return health; }
    
    public boolean isDestroyed() {
        return health <= 0;
    }
    
    public void reset(int x, int y) {
        this.x = x;
        this.y = y;
        this.health = 3;
        this.bullet = null;
    }

    public void draw(Graphics2D g2d) {
          // Draw image
    g2d.drawImage(image, x, y, 50, 50, null);
    
    // Draw collision outline (smaller than image)
   
    Rectangle bounds = getBounds();
    g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

        protected Point calculateBulletStart() {
        double radianAngle = Math.toRadians(angle);
        int bulletX = x + 25 + (int)(25 * Math.cos(radianAngle));
        int bulletY = y + 25 + (int)(25 * Math.sin(radianAngle));
        return new Point(bulletX, bulletY);
    }

     
    
    public void shoot() {
        if(bullet == null) {
            int bulletX = x + 25;
            int bulletY = y + 25;
            
            switch(direction) {
                case "up": 
                    bulletY = y;
                    break;
                case "down": 
                    bulletY = y + 50;
                    break;
                case "left": 
                    bulletX = x;
                    break;
                case "right": 
                    bulletX = x + 50;
                    break;
            }
            
            bullet = new Bullet(bulletX, bulletY, direction, bulletColor);
        }
    }

    public boolean checkBulletCollision(Tank target, Brick bricks) {
        if (bullet != null) {
            bullet.update();
            
            // Check brick collisions
            if(bricks.checkCollision(bullet.getX(), bullet.getY())) {
                bullet = null;
                return false;
            }
            
            // Check tank collision
            if (bullet.checkCollision(target.getBounds())) {
                bullet = null;
                return true;
            }
            
            // Check bounds
            if(bullet.isOutOfBounds()) {
                bullet = null;
            }
        }
        return false;
    }

  

      public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
        isHit = true;
        hitTimer = HIT_ANIMATION_DURATION;
    }
    
    protected void drawHitEffect(Graphics2D g2d) {
        if (!isHit) return;
        
        // Create glowing effect
        float radius = 40;
        Point2D center = new Point2D.Float(x + 25, y + 25);
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(255, 100, 100, 150), new Color(255, 50, 50, 0)};
        
        RadialGradientPaint paint = new RadialGradientPaint(
            center, radius, dist, colors
        );
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setPaint(paint);
        g2d.fillOval(x - 15, y - 15, 80, 80);
        g2d.setComposite(AlphaComposite.SrcOver);
    }
    
    public void updateHitAnimation() {
        if (hitTimer > 0) {
            hitTimer--;
        } else {
            isHit = false;
        }
    }

    public abstract void update(Brick bricks);
}