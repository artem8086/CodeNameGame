package art.soft;

import art.soft.console.Console;
import art.soft.stages.Intro;
import art.soft.stages.Stage;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Артём Святоха
 */
public class Game extends Frame
            implements Runnable, ComponentListener, KeyListener {

    public static final String GAME_TITLE = "Какая-то тестовая штука";
    public static final String VERSION_TEXT = "version: 0.0.5";
    
    private static final int SET_GAME_WIDTH = 1024;
    private static final int SET_GAME_HEIGHT = 640;

    public static final int PREFER_GAME_WIDTH = 800;
    public static final int PREFER_GAME_HEIGHT = 640;
    
    public static final int TIME_FRAME = 33;
    
    public static final String DATA_DIR = "data/";

    // Для обработчика клавиатуры
    private final static int PRESSED_KEY_BIT  = 0x80000000;
    private final static int PRESSED_KEY_MASK = ~ PRESSED_KEY_BIT;
    private final static int KEY_BUFFER_SIZE = 512;
    private final int keyBuffer[] = new int[KEY_BUFFER_SIZE]; // буфер клавишь
    private int curKey = -1;

    private Loader loader;
    private Console console;

    // Всё для рендера
    private Image buffer;
    private Graphics graphics;
    private Graphics2D graphics2D;
    public int width, height;
    private AffineTransform transform;

    public Game() {
        super(GAME_TITLE);
    }

    private void init() {
        loader = new Loader(this);

            // Только для версии разработчика
        console = new Console(loader);
        console.start();

        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent ev){
                System.exit(0);
            }
        });
        
        addKeyListener(this);

        GraphicsDevice myDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode dMode = myDevice.getDisplayMode();
        width = dMode.getWidth();
        height = dMode.getHeight();
        if (loader.settings.fullscreen && myDevice.isFullScreenSupported()) {
            // Вход в полноэкранный режим
            setUndecorated(true);
            setResizable(false);
            setSize(width, height);
            myDevice.setFullScreenWindow((Window) this);
        } else {
            setResizable(true);
            setSize(SET_GAME_WIDTH, SET_GAME_HEIGHT);
            setLocation((width - SET_GAME_WIDTH) >> 1, (height - SET_GAME_HEIGHT) >> 1);
        }
        setFocusable(true);
        setVisible(true);
        //
        // Init graphics
        buffer = createImage(width, height);
        graphics = buffer.getGraphics();
        graphics2D = (Graphics2D) graphics;
        transform = graphics2D.getTransform();
        resizeTransform(getWidth(), getHeight());
        loader.settings.applyRender(graphics);
        //
        loader.init();
        //
        addComponentListener(this);
        //
        Intro intro = new Intro(loader);
        setStage(intro);
        //
        Thread thread = new Thread(this);
        thread.start();
    }

    private Stage curStage, newStage;

    public void setStage(Stage stage) {
        if (stage != curStage) newStage = stage;
    }

    private void setStage() {
        curStage = newStage;
        curStage.init();
        newStage = null;
            // Очистка буфера клавиатуры
        curKey = -1;
    }

    public Stage getStage() {
        return curStage;
    }

    public void log(String message) {
        if (console != null) {
            console.outln(message);
        } else {
            System.out.println(message);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.init();
    }

    @Override
    public void run() {
        boolean frameSkip = false;
        do {
            long cycleTime = System.currentTimeMillis();
            if (newStage != null) setStage();
            // Обработка клавиатуры
            if (curKey >= 0) {
                synchronized (keyBuffer) {
                    for (int i = 0; i <= curKey; i ++) {
                        int key = keyBuffer[i];
                        if (key < 0) 
                            curStage.keyPressed(key & PRESSED_KEY_MASK);
                        else curStage.keyReleased(key);
                    }
                    curKey = -1;
                }
            }
            //
            curStage.act();
            //
            if (!frameSkip) {
                graphics2D.setTransform(transform);
                curStage.draw(graphics);
                //
                //repaint();
                getGraphics().drawImage(buffer, 0, 0, this);
            }
            //
            cycleTime += TIME_FRAME - System.currentTimeMillis();
            if (cycleTime > 0) {
                try{
                    Thread.sleep(cycleTime);
                }catch(InterruptedException e) {}
                frameSkip = false;
            } else
                frameSkip = true;
        } while (true);
    }

    public Graphics getGameGraphics() {
        return graphics;
    }
    
    @Override
    public void update(Graphics g){
	paint(g);
    }

    @Override
    public void paint(Graphics g){
        g.drawImage(buffer, 0, 0, this);
    }

    private void resizeTransform(int w, int h) {
        if (((float) w / h) > 2f) {
            w = h << 1;
            setSize(w, h);
        }
        double sy = (double) h / PREFER_GAME_HEIGHT;
        width = (int) (w / sy);
        //log("width = " + width + ", w = " + w + ", h = " + h);
        height = PREFER_GAME_HEIGHT;
        transform.setToScale(sy, sy);
        //graphics2D.setTransform(transform);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        resizeTransform(getWidth(), getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        if (isUndecorated()) setLocation(0, 0);
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        curKey ++;
        if (curKey == KEY_BUFFER_SIZE) curKey = KEY_BUFFER_SIZE - 1;
        keyBuffer[curKey] = e.getKeyCode() | PRESSED_KEY_BIT;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (console != null && key == KeyEvent.VK_F1) {
            console.startConsole();
        }
        curKey ++;
        if (curKey == KEY_BUFFER_SIZE) curKey = KEY_BUFFER_SIZE - 1;
        keyBuffer[curKey] = key;
    }
}
