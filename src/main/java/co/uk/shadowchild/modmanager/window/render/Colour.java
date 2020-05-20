package co.uk.shadowchild.modmanager.window.render;

import co.uk.shadowchild.modmanager.MathUtil;

public class Colour {

    public float r, g, b, a;

    public Colour(float r, float g, float b, float a) {

        this.r = MathUtil.clamp(r, 0, 1);
        this.g = MathUtil.clamp(g, 0, 1);
        this.b = MathUtil.clamp(b, 0, 1);
        this.a = MathUtil.clamp(a, 0, 1);
    }

    public Colour(float r, float g, float b) {

        this(r, g, b, 1);
    }

    public String toString() {

        return "(" +
                r + ", " +
                g + ", " +
                b + ", " +
                a +
                ")";
    }

    public static final Colour BLACK = new Colour(0, 0, 0, 1);
    public static final Colour WHITE = new Colour(1, 1, 1, 1);
    public static final Colour RED = new Colour(1, 0, 0, 1);
    public static final Colour GREEN = new Colour(0, 1, 0, 1);
    public static final Colour BLUE = new Colour(0, 0, 1, 1);
    public static final Colour GREY = new Colour(0.5f, 0.5f, 0.5f, 1);
    public static final Colour YELLOW = new Colour(1, 1, 0, 1);
    public static final Colour MAGENTA = new Colour(1, 0, 1, 1);
    public static final Colour CYAN = new Colour(0, 1, 1, 1);


}
