package xyz.wagyourtail.wagyourgui.api.render;

import java.util.ArrayList;
import java.util.List;

public class ColoredString {
    public final List<Pair> pairs = new ArrayList<>();

    public ColoredString append(String string, int color) {
        pairs.add(new Pair(string, color));
        return this;
    }

    public void visit(ColoredStringVisitor visitor) {
        for (Pair pair : pairs) {
            visitor.visit(pair.string, pair.color);
        }
    }

    public boolean isEmpty() {
        return pairs.isEmpty();
    }

    @FunctionalInterface
    public interface ColoredStringVisitor {
        void visit(String string, int color);
    }

    private static class Pair {
        private String string;
        private int color;

        Pair(String string, int color) {
            this.string = string;
            this.color = color;
        }
    }
}
