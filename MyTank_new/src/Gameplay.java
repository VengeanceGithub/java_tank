import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Gameplay extends JPanel implements ActionListener {
    private PlayerTank player;
    private BotTank bot;
    private Brick br;
    private Timer timer;
    private boolean gameRunning = true;
    private int gameState = 0; // 0=playing, 1=won, 2=lost
     private boolean showCollisionDebug = false;

    public Gameplay() {
        player = new PlayerTank(400, 550);
        bot = new BotTank(400, 50);
        br = new Brick();
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                player.handleKeyPress(e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_R && !gameRunning) {
                    resetGame();
                }
                  if (e.getKeyCode() == KeyEvent.VK_F1) {
            showCollisionDebug = !showCollisionDebug;
        }
                
            }
            
            public void keyReleased(KeyEvent e) {
                player.handleKeyRelease(e.getKeyCode());
                
            }
        });
        
        setFocusable(true);
        timer = new Timer(16, this);
        timer.start();
    }

public void actionPerformed(ActionEvent e) {
    if (gameRunning) {
        player.update(br);
        bot.update(br);
        checkCollisions();
        
        // Update hit animations
        player.updateHitAnimation();
        bot.updateHitAnimation();
    }
    repaint();
}

    private void checkCollisions() {
        // Player bullet hits bot
        if(player.checkBulletCollision(bot, br)) {
            bot.takeDamage(1);
            if(bot.isDestroyed()) {
                gameState = 1;
                gameRunning = false;
            }
        }

        // Bot bullet hits player
        if(bot.checkBulletCollision(player, br)) {
            player.takeDamage(1);
            if(player.isDestroyed()) {
                gameState = 2;
                gameRunning = false;
            }
        }
    }

    private void resetGame() {
        player.reset(400, 550);
        bot.reset(400, 50);
        br.reset();
        gameRunning = true;
        gameState = 0;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw background
        g2d.setColor(new Color(30, 30, 30));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw bricks
        br.draw(g2d);

        // Draw tanks
        player.draw(g2d);
        bot.draw(g2d);

        // Draw HUD
        drawHUD(g2d);

        if (!gameRunning) {
            drawGameOverScreen(g2d);
        }
    }

   private void drawHUD(Graphics2D g2d) {
    g2d.setFont(new Font("Arial", Font.BOLD, 20));
    
    // Player health
    String playerHealthText = "Player Health: " + player.getHealth();
    g2d.setColor(new Color(0, 0, 0, 150)); // semi-transparent black background
    g2d.fillRect(15, 20, g2d.getFontMetrics().stringWidth(playerHealthText) + 10, 25);
    g2d.setColor(Color.GREEN);
    g2d.drawString(playerHealthText, 20, 40);
    
    // Bot health
    String botHealthText = "Bot Health: " + bot.getHealth();
    g2d.setColor(new Color(0, 0, 0, 150));
    g2d.fillRect(15, 50, g2d.getFontMetrics().stringWidth(botHealthText) + 10, 25);
    g2d.setColor(Color.RED);
    g2d.drawString(botHealthText, 20, 70);
    
    // Debug mode text
    if (showCollisionDebug) {
        String debugText = "DEBUG MODE";
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(15, 80, g2d.getFontMetrics().stringWidth(debugText) + 10, 25);
        g2d.setColor(Color.WHITE);
        g2d.drawString(debugText, 20, 100);
    }
}


    private void drawGameOverScreen(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        String text = gameState == 1 ? "YOU WON!" : "GAME OVER";
        Color textColor = gameState == 1 ? Color.GREEN : Color.RED;
        
        g2d.setColor(textColor);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        g2d.drawString(text, (getWidth()-textWidth)/2, 300);
        
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        String restartText = "Press R to restart";
        int restartWidth = g2d.getFontMetrics().stringWidth(restartText);
        g2d.drawString(restartText, (getWidth()-restartWidth)/2, 350);
    }}