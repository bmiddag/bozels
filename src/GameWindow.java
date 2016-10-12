package bozels;

import bozels.elements.Bozel;
import bozels.elements.Element;
import bozels.elements.WhiteBozel;
import bozels.elements.YellowBozel;
import bozels.models.BonusModel;
import bozels.models.ConfigModel;
import bozels.models.MainModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Bart Middag
 */
public class GameWindow extends JPanel implements MouseListener, MouseMotionListener, ChangeListener {

    private int w;
    private int h;
    private final Font f;
    private final MainModel model;
    private final ConfigModel configModel;
    private Graphics2D g2dp;
    private long nextSecond = System.currentTimeMillis() + 1000;
    private int frameInLastSecond = 0;
    private int framesInCurrentSecond = 0;
    private boolean canDrag;
    private float dragFromX = 0;
    private float dragFromY = 0;
    private boolean paused;
    private boolean showCentroid;
    private boolean markSleeping;
    private boolean showSpeed;
    private boolean showRays;
    private boolean thrown;
    private final Paint normalPausePaint;
    private final RadialGradientPaint nightSkyGradient;
    private final RadialGradientPaint sunsetSkyGradient;
    private java.util.List<int[]> rayList;
    private boolean showScenery;
    private int targetAmount;
    private final BonusModel bonusModel;
    private final TexturePaint starsPaint;
    private float starsX;
    private final Paint sceneryPausePaint;
    private final BufferedImage moonImage;
    private final BufferedImage moonSwirlImage;
    private final TexturePaint seaPaint;
    private final TexturePaint mount1Paint;
    private final TexturePaint mount2Paint;
    private final TexturePaint mount3Paint;
    private final BufferedImage shadowImage;
    private final BufferedImage logoImage;
    private float[][] cloudPos = new float[4][3];
    private final BufferedImage cloud1Image;
    private final BufferedImage cloud2Image;
    private final BufferedImage cloud3Image;
    private final BufferedImage cloud4Image;
    private final BufferedImage catapultImage;
    private final BufferedImage explosionImage;
    private int[] explosion = new int[3];
    private int environment;
    private final TexturePaint wavesPaint;
    private final BufferedImage pirateShipImage;
    private int winState;
    private boolean powerActive;

    public GameWindow(int width, int height, MainModel model) {
        powerActive = false;
        rayList = new ArrayList<int[]>();
        winState = 2;
        targetAmount = 0;
        thrown = false;
        w = width;
        h = height;
        f = new Font("SansSerif", Font.BOLD, 18);
        paused = true;
        setPreferredSize(new Dimension(w, h));
        this.model = model;
        configModel = model.getConfigModel();
        bonusModel = model.getBonusModel();

        // Initiate scenery
        setBackground(Color.WHITE);
        showScenery = configModel.isShowScenery();
        environment = configModel.getEnvironment();
        normalPausePaint = new GradientPaint(0, 0, Color.BLACK, 0, h, Color.WHITE, true);
        sceneryPausePaint = Color.BLACK;
        float circleRadius = (float) Math.sqrt(Math.pow(w / 2, 2) + Math.pow(h, 2));
        float[] dist = {0.0f, 0.25f, 0.6f, 1.0f};
        Color[] colors = {Color.WHITE, new Color(121, 161, 237), new Color(0, 22, 64), Color.BLACK};
        nightSkyGradient = new RadialGradientPaint(w / 2, h, circleRadius, dist, colors);
        float[] dist2 = {0.0f, 0.2f, 0.5f, 0.7f, 1.0f};
        Color[] colors2 = {Color.WHITE, new Color(255, 185, 103), new Color(255, 68, 3), new Color(158, 20, 14), Color.BLACK};
        sunsetSkyGradient = new RadialGradientPaint(w / 2, h, circleRadius, dist2, colors2);
        starsPaint = new TexturePaint(bonusModel.getStarsImage(), new Rectangle(512, 512));
        seaPaint = new TexturePaint(bonusModel.getSeaImage(), new Rectangle(143, 169));
        wavesPaint = new TexturePaint(bonusModel.getWavesImage(), new Rectangle(260, 159));
        mount1Paint = new TexturePaint(bonusModel.getMount1Image(), new Rectangle(478, 330));
        mount2Paint = new TexturePaint(bonusModel.getMount2Image(), new Rectangle(502, 218));
        mount3Paint = new TexturePaint(bonusModel.getMount3Image(), new Rectangle(466, 122));
        moonImage = bonusModel.getMoonImage();
        moonSwirlImage = bonusModel.getMoonSwirlImage();
        shadowImage = bonusModel.getShadowImage();
        cloud1Image = bonusModel.getCloud1Image();
        cloud2Image = bonusModel.getCloud2Image();
        cloud3Image = bonusModel.getCloud3Image();
        cloud4Image = bonusModel.getCloud4Image();
        logoImage = bonusModel.getLogoImage();
        pirateShipImage = bonusModel.getPirateShipImage();
        explosionImage = bonusModel.getExplosionImage();
        catapultImage = bonusModel.getCatapultImage();
        explosion[0] = 11;
        explosion[1] = 0;
        explosion[2] = 0;
        starsX = 0f;
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            cloudPos[i][0] = random.nextInt(10000);
            cloudPos[i][1] = random.nextInt(150);
            cloudPos[i][2] = random.nextInt(50);
        }


        // Add the mouse listeners.
        addMouseListener(this);
        addMouseMotionListener(this);
        model.addChangeListener(this);
        configModel.addChangeListener(this);
    }

    /**
     * Draws the game on the screen
     *
     * @author Bart Middag
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        w = getWidth();
        h = getHeight();

        if (showScenery) {
            if (environment == 0 || environment == 1) {
                // Draw starry sky
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.translate(w / 2 - 512, 0);
                if (environment == 0) {
                    g2dp.setPaint(nightSkyGradient);
                } else {
                    g2dp.setPaint(sunsetSkyGradient);
                }
                g2dp.fillRect(-(w / 2 - 512), 0, w, h);
                g2dp.setPaint(starsPaint);
                g2dp.translate(w / 2, h);
                g2dp.rotate(starsX);
                int starR = (int) Math.round(Math.sqrt(Math.pow(w, 2) + Math.pow(h / 2, 2)));
                g2dp.fillRect(-starR, -starR, starR * 2, starR * 2);
            }
            if (environment == 1) {
                // Draw pirate ship
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2dp.translate(((int) (starsX * 3000) % (w + 360)) - 180, 381 + (int) Math.round(Math.sin(starsX * 500) * 10));
                g2dp.rotate(Math.toRadians(Math.sin(starsX * 500) * 8));
                g2dp.drawImage(pirateShipImage, -100, -140, this);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                // Draw sea
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.translate((int) (starsX * 5000) % 260, 321 + (int) Math.round(Math.sin(starsX * 500) * 10));
                g2dp.setPaint(wavesPaint);
                g2dp.fillRect(-260, 0, w + 260, 159);
            }
            if (environment == 0) {
                // Draw moonlight
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2dp.translate(w - 200, 100);
                g2dp.rotate(-starsX * 12);
                g2dp.drawImage(moonSwirlImage, -150, -120, this);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                // Draw moon
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
                g2dp.translate(w - 200, 100);
                g2dp.drawImage(moonImage, -75, -75, this);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                // Draw background elements
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.translate((starsX * 1000) % 143, 255);
                g2dp.setPaint(seaPaint);
                g2dp.fillRect(-143, 0, w + 143, 169);
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.translate(0, 120);
                g2dp.setPaint(mount1Paint);
                g2dp.fillRect(0, 0, w, 330);
                g2dp.translate(0, 112);
                g2dp.setPaint(mount2Paint);
                g2dp.fillRect(0, 0, w, 218);
                g2dp.translate(0, 96);
                g2dp.setPaint(mount3Paint);
                g2dp.fillRect(0, 0, w, 122);
            }
            if (environment == 0 || environment == 1) {
                // Draw the two background clouds
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - (cloudPos[0][2] / 100f)));
                g2dp.translate(((cloudPos[0][0] / 3) % (w + 213)) - 213, cloudPos[0][1]);
                g2dp.drawImage(cloud3Image, 0, 0, this);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - (cloudPos[1][2] / 100f)));
                g2dp.translate(((cloudPos[1][0] / 3) % (w + 82)) - 82, cloudPos[1][1]);
                g2dp.drawImage(cloud4Image, 0, 0, this);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
            if (environment == 0) {
                // Draw moonlight shadow
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f));
                g2dp.drawImage(shadowImage, 0, 0, this);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
            // Draw catapult
            g2dp = (Graphics2D) g.create();
            g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
            g2dp.drawImage(catapultImage, Math.round(model.getCatapultX() * 7f) - 35, 450 - Math.round(model.getCatapultY() * 7f) - 16, this);
            g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            // Do calculations for the starry sky (water, etc work with this number)
            starsX += 0.0001f;
            for (int i = 0; i < 4; i++) {
                cloudPos[i][0]++;
            }
        }

        if (canDrag) {
            // Draw catapult line
            g2dp = (Graphics2D) g.create();
            g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Bozel bo = model.getCurrentBozel();
            if (showScenery) {
                g2dp.setColor(Color.WHITE);
                g2dp.drawLine((int) Math.round(bo.getX() * 7f), 450 - (int) Math.round(bo.getY() * 7f), (int) Math.round(model.getCatapultX() * 7f) - 25, 450 - (int) Math.round(model.getCatapultY() * 7f));
                g2dp.drawLine((int) Math.round(bo.getX() * 7f), 450 - (int) Math.round(bo.getY() * 7f), (int) Math.round(model.getCatapultX() * 7f) + 25, 450 - (int) Math.round(model.getCatapultY() * 7f));
            } else {
                g2dp.setColor(Color.BLACK);
                g2dp.drawLine((int) Math.round(bo.getX() * 7f), 450 - (int) Math.round(bo.getY() * 7f), (int) Math.round(model.getCatapultX() * 7f), 450 - (int) Math.round(model.getCatapultY() * 7f));
            }
        }
        if (showRays) {
            // Draw rays
            g2dp = (Graphics2D) g.create();
            g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2dp.setColor(Color.PINK);;
            for (int[] ray : rayList) {
                g2dp.drawLine(ray[0], ray[1], ray[2], ray[3]);
            }
        }
        // Draw elements
        for (int i = 0; i < model.getElements().size(); i++) {
            Element el = model.getElements().get(i);
            g2dp = (Graphics2D) g.create();
            g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2dp.translate((el.getX() * 7f), 450 - (el.getY() * 7f));
            g2dp.rotate(Math.PI - el.getAngle());
            if ((markSleeping) && (!el.isAwake())) {
                // Draw them transparently
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            el.draw(g2dp, showScenery);
            if ((markSleeping) && (!el.isAwake())) {
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        }
        if ((showCentroid) || (showSpeed)) {
            for (int i = 0; i < model.getElements().size(); i++) {
                Element el = model.getElements().get(i);
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.translate(0, 0);
                g2dp.rotate(0f);
                if ((showSpeed) && (el.isAwake())) {
                    // Draw speed line
                    g2dp.setColor(Color.BLACK);
                    g2dp.drawLine((int) Math.round(el.getX() * 7f), 450 - (int) Math.round(el.getY() * 7f), (int) Math.round(el.getSpeedX() * 7f), 450 - (int) Math.round(el.getSpeedY() * 7f));
                }
                g2dp.setColor(Color.WHITE);
                //g2dp.setXORMode(Color.BLACK); // XOR'ing would make things ugly - and gray elements would stay gray!
                g2dp.translate((el.getX() * 7f), 450 - (el.getY() * 7f));
                if (showCentroid) {
                    // Draw centroid
                    g2dp.fillOval(-1, -1, 2, 2);
                }
            }
        }
        if (showScenery) {
            // Draw the two foreground clouds
            g2dp = (Graphics2D) g.create();
            g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - (cloudPos[2][2] / 100f)));
            g2dp.translate(((cloudPos[2][0] / 2) % (w + 216)) - 216, cloudPos[2][1]);
            g2dp.drawImage(cloud1Image, 0, 0, this);
            g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2dp = (Graphics2D) g.create();
            g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - (cloudPos[3][2] / 100f)));
            g2dp.translate(((cloudPos[3][0] / 2) % (w + 283)) - 283, cloudPos[3][1]);
            g2dp.drawImage(cloud2Image, 0, 0, this);
            g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            if (environment == 0) {
                // Draw a second moonlight shadow
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f));
                g2dp.drawImage(shadowImage, 0, 0, 378, h, this);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
            if (environment == 1) {
                // Draw the foreground sea
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.translate((int) (starsX * 10000) % 260, 430 + (int) Math.round(Math.sin(starsX * 750) * 10));
                g2dp.setPaint(wavesPaint);
                g2dp.fillRect(-260, 0, w + 260, 159);
            }
            // Draw explosion image
            if (explosion[0] <= 10) {
                g2dp = (Graphics2D) g.create();
                g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - (((float) explosion[0]) / 20)));
                g2dp.translate(explosion[1], explosion[2]);
                g2dp.drawImage(explosionImage, -Math.round(2.5f * (int) Math.pow(explosion[0], 2)), -Math.round(2.325f * (int) Math.pow(explosion[0], 2)), Math.round(5 * (int) Math.pow(explosion[0], 2)), Math.round(4.65f * (int) Math.pow(explosion[0], 2)), this);
                g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }

        }
        // Counter must be outside of showScenery block
        explosion[0] += 1;
        if (paused) {
            // Draw pause screen
            g2dp = (Graphics2D) g.create();
            g2dp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            if (showScenery) {
                g2dp.setPaint(sceneryPausePaint);
            } else {
                g2dp.setPaint(normalPausePaint);
            }
            g2dp.fillRect(0, 0, w, h);
            g2dp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            if (showScenery) {
                // Draw logo + text
                g2dp.translate(w / 2, 7 * h / 12);
                g2dp.drawImage(logoImage, -335, -188, this);
                g2dp.setColor(Color.WHITE);
                g2dp.setFont(f);
                g2dp.translate(-w / 2, 40);
                drawCenteredString(model.getLocaleBundle().getString("CLICK_TO_START"), w, 20, g2dp);
            } else {
                // Draw text only
                g2dp.setColor(Color.WHITE);
                g2dp.setFont(f);
                drawCenteredString(model.getLocaleBundle().getString("CLICK_TO_START"), w, h, g2dp);
            }
        }

        // Record current time for FPS
        long currentTime = System.currentTimeMillis();
        if (currentTime > nextSecond) {
            nextSecond += 1000;
            frameInLastSecond = framesInCurrentSecond;
            framesInCurrentSecond = 0;
        }
        framesInCurrentSecond++;
        // Draw useful information
        if (showScenery) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawString(model.getLevelName() + ": " + frameInLastSecond + " fps", 20, 20);
        g.drawString(model.getLocaleBundle().getString("TARGETS_LEFT") + targetAmount, 20, 40);
        if (powerActive) {
            g.drawString(model.getLocaleBundle().getString("CLICK_TO_ACTIVATE"), 20, 60);
        }


    }

    /**
     * Sets the explosion counter to 0 so a new explosion image is drawn
     */
    public void startExplode(int x, int y) {
        explosion[0] = 0;
        if (showScenery) {
            bonusModel.playSound(0);
        }
        explosion[1] = x;
        explosion[2] = y;
    }

    /**
     * Draw a string centered over a given width and height
     */
    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }

    /**
     * Tell us a Bozel has been thrown
     */
    public void setThrown(boolean thrown) {
        this.thrown = thrown;
        if(thrown == false) powerActive = false;
    }

    /**
     * Mouse has been clicked
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // Unpause the game when you click
        model.setPaused(false);
    }

    /**
     * Mouse has been pressed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // Unpause the game when you click
        model.setPaused(false);
        // Bozel has been thrown - do special action
        if (thrown == true) {
            if((model.getCurrentBozel() instanceof YellowBozel) && (powerActive == true)) {
                model.getCurrentBozel().doSpecial();
                if ((showScenery) && (model.getCurrentBozel() instanceof YellowBozel)) {
                    // Play boost sound
                    bonusModel.playSound(1);
                }
                powerActive = false;
            }
            if ((model.getCurrentBozel() instanceof WhiteBozel)) {
                model.getCurrentBozel().doSpecial();
                // Next bozel pls.
                model.nextBozel();
                powerActive = false;
                thrown = false;
            }
        }
        float mX = e.getX();   // Save the x coord of the click
        float mY = e.getY();   // Save the y coord of the click
        canDrag = false;
        if (model.getCurrentBozel() != null) {
            if ((model.getCurrentBozel().isInRange(mX, mY)) && (thrown == false)) {
                // Drag bozel from the place where you grabbed it
                canDrag = true;
                dragFromX = mX - (model.getCurrentBozel().getX() * 7f);  // how far from left
                dragFromY = mY - (450 - (model.getCurrentBozel().getY() * 7f));  // how far from top
            }
        }
    }

    /**
     * Mouse has been released
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (canDrag) {
            // Unpause the game and throw dat bozel!
            model.setPaused(false);
            model.addInit(model.getCurrentBozel());
            canDrag = false;
            if (showScenery) {
                // Scream, bozel, scream!
                bonusModel.playScream();
            }
            // Wait for user to do special action
            if ((model.getCurrentBozel() instanceof WhiteBozel) || (model.getCurrentBozel() instanceof YellowBozel)) powerActive = true;
            // You've thrown the bozel!
            thrown = true;
        }
    }

    /**
     * Mouse has entered the screen - unused
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Mouse has quit the screen - unused
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Mouse has been dragged
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (canDrag) {
            // Unpause the game if you're dragging a bozel
            model.setPaused(false);
            float mX = e.getX() - dragFromX;   // Save the x coord of the click
            float mY = e.getY() - dragFromY;   // Save the y coord of the click
            // Set position of dat bozel
            model.getCurrentBozel().setMousePos(mX, mY);
        }
    }

    /**
     * Mouse has moved - unused
     */
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Show a message
     */
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(model.getWindow(), msg, msg, JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean isThrown() {
        return thrown;
    }

    /**
     * Update values
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(model)) {
            paused = model.isPaused();
            rayList = model.getRays();
            if (model.getTargetAmount() != targetAmount) {
                targetAmount = model.getTargetAmount();
            }
            if (winState != model.getWinState()) {
                // Update winning state
                winState = model.getWinState();
                if (winState == 1) {
                    if (showScenery) {
                        bonusModel.playJingle(0);
                    }
                    // You win! Woo!
                    showMessage(model.getLocaleBundle().getString("MESSAGE_WIN"));
                } else if (winState == -1) {
                    if (showScenery) {
                        bonusModel.playJingle(1);
                    }
                    // You loser, you totally lost.
                    showMessage(model.getLocaleBundle().getString("MESSAGE_LOSE"));
                }
            }
        }
        if (e.getSource().equals(configModel)) {
            showCentroid = configModel.isShowCentroid();
            markSleeping = configModel.isMarkSleeping();
            showSpeed = configModel.isShowSpeed();
            showRays = configModel.isShowRays();
            showScenery = configModel.isShowScenery();
            environment = configModel.getEnvironment();
        }
    }
}
