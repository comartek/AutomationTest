package core.enums;

import java.awt.event.KeyEvent;


public enum KeyEnum {
    ENTER(KeyEvent.VK_ENTER),
    HOME(KeyEvent.VK_HOME),
    END(KeyEvent.VK_END),
    PLUS(KeyEvent.VK_ADD),
    ALT(KeyEvent.VK_ALT),
    F3(KeyEvent.VK_F3),
    F4(KeyEvent.VK_F4),
    F5(KeyEvent.VK_F5),
    F6(KeyEvent.VK_F6),
    F9(KeyEvent.VK_F9),
    F10(KeyEvent.VK_F10),
    F12(KeyEvent.VK_F12),
    ESCAPE(KeyEvent.VK_ESCAPE),
    KEY0(KeyEvent.VK_0),
    KEY1(KeyEvent.VK_1),
    KEY2(KeyEvent.VK_2),
    KEY3(KeyEvent.VK_3),
    KEY4(KeyEvent.VK_4),
    KEY5(KeyEvent.VK_5),
    KEY6(KeyEvent.VK_6),
    KEY7(KeyEvent.VK_7),
    KEY8(KeyEvent.VK_8),
    KEY9(KeyEvent.VK_9),
    UP(KeyEvent.VK_UP),
    DOWN(KeyEvent.VK_DOWN),
    TAB(KeyEvent.VK_TAB),
    CTRL(KeyEvent.VK_CONTROL),
    SHIFT(KeyEvent.VK_SHIFT),
    KEYA(KeyEvent.VK_A),
    KEYD(KeyEvent.VK_D),
    KEYE(KeyEvent.VK_E),
    KEYM(KeyEvent.VK_M),
    KEYO(KeyEvent.VK_O),
    KEYS(KeyEvent.VK_S),
    DELETE(KeyEvent.VK_DELETE),
    PGUP(KeyEvent.VK_PAGE_UP),
    PGDOWN(KeyEvent.VK_PAGE_DOWN),
    SPACE(KeyEvent.VK_SPACE),
    RIGHT(KeyEvent.VK_RIGHT),;

    private int value;

    KeyEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

