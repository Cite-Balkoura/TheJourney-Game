package fr.grimtown.journey.game.classes;

public enum Universe {
    SKY_LAND(1),
    FAR_LANDS(2),
    MATRIX(3),
    APOCALYPSE(4);

    public final int level;
    Universe(int level) { this.level = level; }

    public int getLevel() {
        return level;
    }

    public Universe getNext() {
        switch (level) {
            case 1 -> {
                return FAR_LANDS;
            }
            case 2 -> {
                return MATRIX;
            }
            case 3 -> {
                return APOCALYPSE;
            }
        }
        return SKY_LAND;
    }
}
