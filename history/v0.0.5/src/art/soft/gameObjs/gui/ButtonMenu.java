package art.soft.gameObjs.gui;

import art.soft.Loader;
import art.soft.Settings;
import static art.soft.Settings.GAME_DOWN;
import static art.soft.Settings.GAME_SELECT;
import static art.soft.Settings.GAME_UP;
import static art.soft.Settings.SETTINGS_FILE;
import art.soft.animation.Animation;
import art.soft.gameObjs.GameKeyListener;
import art.soft.gameObjs.gameObj;
import com.sun.glass.events.KeyEvent;
import java.awt.Graphics;
import java.io.File;

/**
 *
 * @author Артём Святоха
 */
public class ButtonMenu extends gameObj implements GameKeyListener {

    private static final int NUM_OF_MENU = 8;
    
    public static final int MENU_START = 0;
    private static final int MENU_OPTIONS = 1;
    private static final int MENU_CONTROLS = 2;
    private static final int MENU_GRAPHICS = 3;
    private static final int MENU_PLAYER1_KEYS = 4;
    private static final int MENU_PLAYER2_KEYS = 5;
    private static final int MENU_COMMON_KEYS = 6;
    public static final int MENU_IN_GAME = 7;
    
    static Animation checkAnim, keyAnim;

    private static final String[] START_MENU = {
        "1 игрок", "2 игрока", "Настройки", "Выход"
    };
    
    private static final String[] OPTIONS_MENU = {
        "Управление", "Графика", "Сброс настроек", "Назад"
    };

    private static final String[] CONTROLS_MENU = {
        "1-ый игрок", "2-ой игрок", "Общее", "Назад"
    };

    private static final String[] GRAPHICS_MENU = {
        "Полный экран",
        "Альфа интерполяция",
        "Сглаживание",
        "Цветопередача",
        "Смещение",
        "Дробные показатели",
        "Интерполяция",
        "Качественный рендер",
        "Stroke control",
        "Сглаживание текста",
        "Назад"
    };

    private static final String[] IN_GAME_MENU = {
        "Продолжить", "Уровень сначала", "Настройки", "В главное меню"
    };

    private static final String[] PLAYERS_KEYS_MENU = {
        "Вверх", "Вниз", "Влево", "Вправо", "Огонь", "Прыжок", "Взять", "Назад"
    };

    private static final String[] COMMON_KEYS_MENU = {
        "Меню вверх", "Меню вниз", "Выбрать", "Пауза", "Назад"
    };

    public Loader loader;

    public final MenuSelector toMainMenu = new MenuSelector(MENU_START);
    
    private final Button backButton = new Button(this, null, 0, 0);

    private Button[] curMenu;
    private int curButton;

    public Button buttonListener;
    
    private Button[][] menu;

    public void setMenu(int menu) {
        curMenu = this.menu[menu];
        curButton = 0;
    }

    public void load(Loader loader) {
        this.loader = loader;
        //
        checkAnim = new Animation(loader.loadAnimation("check"));
        keyAnim = new Animation(loader.loadAnimation("key"));
        //
        menu = new Button[NUM_OF_MENU][];
        MenuSelector toOptions = new MenuSelector(MENU_OPTIONS);
        // Главное меню
        Button[] buttons = menu[MENU_START] = createCenteredButtons(START_MENU, 400, 400, 24);
        buttons[0].setListener(new StartGame(1));
        buttons[1].setListener(new StartGame(2));
        buttons[2].setListener(toOptions);
        buttons[3].setListener((button) -> System.exit(0));
        // Меню настроек
        buttons = menu[MENU_OPTIONS] = createCenteredButtons(OPTIONS_MENU, 400, 400, 24);
        buttons[0].setListener(new MenuSelector(MENU_CONTROLS));
        buttons[1].setListener(new MenuSelector(MENU_GRAPHICS));
        buttons[2].setListener((Button button) -> {
            new File(SETTINGS_FILE).delete();
            System.exit(0);
        });
        buttons[3].setListener(toMainMenu);
        // Настройки управления
        buttons = menu[MENU_CONTROLS] = createCenteredButtons(CONTROLS_MENU, 400, 400, 24);
        buttons[0].setListener(new MenuSelector(MENU_PLAYER1_KEYS));
        buttons[1].setListener(new MenuSelector(MENU_PLAYER2_KEYS));
        buttons[2].setListener(new MenuSelector(MENU_COMMON_KEYS));
        buttons[3].setListener(toOptions);
        // Настройки графики
        menu[MENU_GRAPHICS] = createGraphicsButtons(GRAPHICS_MENU, 360, 360, 24);
        // Управление
        menu[MENU_PLAYER1_KEYS] = createKeysButtons(PLAYERS_KEYS_MENU,
                loader.settings.playerKeys[0], 330, 360, 30);
        menu[MENU_PLAYER2_KEYS] = createKeysButtons(PLAYERS_KEYS_MENU,
                loader.settings.playerKeys[1], 330, 360, 30);
        menu[MENU_COMMON_KEYS] = createKeysButtons(COMMON_KEYS_MENU,
                loader.settings.gameKeys, 330, 400, 30);
        // Внутри игровое меню
        buttons = menu[MENU_IN_GAME] = createCenteredButtons(IN_GAME_MENU, 400, 400, 24);
        buttons[0].setListener((button) -> loader.level.unpause());
        buttons[1].setListener((button) -> {
            setMenu(MENU_IN_GAME);
            loader.level.unpause();
            loader.engine.restartLevel();
        });
        buttons[2].setListener(toOptions);
        buttons[3].setListener((button) -> loader.game.setStage(loader.menu));
    }

    public Button[] createCenteredButtons(String[] caps, int x, int y, int step) {
        Button[] buttons = new Button[caps.length];
        for (int i = 0; i < buttons.length; i ++) {
            String text = caps[i];
            buttons[i] = new Button(this, text, x - (loader.stringWidth(text) >> 1), y);
            y += step;
        }
        return buttons;
    }

    public Button[] createGraphicsButtons(String[] caps, int x, int y, int step) {
        Settings settings = loader.settings;
        Button[] buttons = new Button[caps.length];
        buttons[0] = new CheckButton(this, caps[0], x, y, settings.fullscreen,
            (Button button) -> {
                CheckButton checkBut = (CheckButton) button;
                loader.settings.fullscreen = checkBut.check ^= true;
                loader.settings.Save();
            });
        y += step;
        int i = 0;
        int[] map = settings.mapRender;
        for (; i < map.length; i ++) {
            buttons[i + 1] = new CheckButton(this, caps[i + 1], x, y, map[i] != 0,
                    new MapRenderSetter(i));
            y += step;
        }
        buttons[i + 1] = new Button(this, caps[i + 1], x, y,
            new MenuSelector(MENU_OPTIONS));

        return buttons;
    }

    public Button[] createKeysButtons(String[] caps, int[] keys, int x, int y, int step) {
        Button[] buttons = new Button[caps.length];
        int i = 0;
        for (; i < keys.length; i ++) {
            KeyButton key = new KeyButton(this, caps[i], x, y);
            key.setKeys(keys, i);
            buttons[i] = key;
            y += step;
        }
        buttons[i] = new Button(this, caps[i], x + 68, y,
            new MenuSelector(MENU_CONTROLS));
        return buttons;
    }

    @Override
    public void init(Loader loader) {
        this.loader = loader;
        curButton = 0;
    }

    @Override
    public boolean act() {
        return false;
    }

    @Override
    public void draw(Graphics g) {
        for (int i = 0; i < curMenu.length; i ++) {
            curMenu[i].draw(g, i == curButton);
        }
    }

    @Override
    public void pool(Loader loader) {
        curMenu = null;
        menu = null;
    }

    public void prevButton() {
        curMenu[curButton].press = false;
        if (curButton > 0) curButton --;
    }

    public void nextButton() {
        curMenu[curButton].press = false;
        if ((curButton + 1) < curMenu.length) {
            curButton ++;
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        if (buttonListener != null) {
            buttonListener.keyPressed(keyCode);
        } else {
            int[] gameKeys = loader.settings.gameKeys;
            if (keyCode == gameKeys[GAME_UP]) {
                prevButton();
            }
            if (keyCode == gameKeys[GAME_DOWN]) {
                nextButton();
            }
            if (keyCode == gameKeys[GAME_SELECT]) {
                curMenu[curButton].keyPressed(keyCode);
            }
        }
    }

    @Override
    public void keyReleased(int keyCode) {
        if (buttonListener != null) {
            buttonListener.keyReleased(keyCode);
        } else {
            if (keyCode == KeyEvent.VK_ESCAPE) {
                toMainMenu.pressed(backButton);
            } else
            if (keyCode == loader.settings.gameKeys[GAME_SELECT]) {
                curMenu[curButton].keyReleased(keyCode);
            }
        }
    }
}
