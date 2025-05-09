package me.zyouime.eventnotifier.util;

import java.awt.*;

public enum EventDisplayInfo {

    BOSS(new Color(209, 2, 250), 0, 0, "Босс"),
    TIPO(Color.GREEN, 1400,655, "Опытный Тыпо"),
    SHIP(new Color(252, 127, 3), 620, 0, "Корабль"),
    CONTAINER(Color.RED, 0, 1300, "Контейнер"),
    FEVER(Color.YELLOW, 620, 1300, "Золотая лихорадка"),
    CARGO(new Color(170, 119, 85), 0, 655, "Груз"),
    GLADE( new Color(169, 252, 3), 620, 655, "Цветочная поляна"),
    DEATH_MINE(new Color(156, 34, 34), 1400, 0, "Смертельная шахта"),
    PARCEL(Color.CYAN, 1400, 1300, "Посылка"),
    VOTE(Color.PINK, 0, 0, "Голосование", "textures/vote.png"),
    UNKNOWN(Color.WHITE, 0, 0, "Неизвестно", "textures/unknown.png"),
    WIDTH(Color.WHITE, 0, 0, "Ширина", "textures/size.png"),
    HEIGHT(Color.WHITE, 0, 0, "Максимальная высота", "textures/size.png");

    public final Color color;
    public final int u;
    public final int v;
    public final String settingName;
    public final String texturePath;

    EventDisplayInfo(Color color, int u, int v, String settingName) {
        this.color = color;
        this.u = u;
        this.v = v;
        this.settingName = settingName;
        this.texturePath = "textures/icons.png";
    }

    EventDisplayInfo(Color color, int u, int v, String settingName, String texturePath) {
        this.color = color;
        this.u = u;
        this.v = v;
        this.settingName = settingName;
        this.texturePath = texturePath;
    }
}
