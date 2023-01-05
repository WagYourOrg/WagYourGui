package xyz.wagyourtail.wagyourgui.api.container.impl;

import xyz.wagyourtail.wagyourgui.api.container.DropDownElementContainer;
import xyz.wagyourtail.wagyourgui.api.element.impl.Button;
import xyz.wagyourtail.wagyourgui.api.render.ColoredString;

import java.util.List;
import java.util.function.Consumer;

public class StringSelector extends DropDownElementContainer {

    protected Object current;
    protected List<Object> options;
    protected Consumer<StringSelector> onChange;

    public StringSelector(int x, int y, int width, int height, int maxHeight, ColoredString selected, List<ColoredString> options, Consumer<StringSelector> onChange) {
        super(x, y, width, height, maxHeight, new Button(x, y, width, height, selected, null));
        this.current = selected;
        this.options = (List) options;
        this.onChange = onChange;
        updateOptions();
    }

    public StringSelector(int x, int y, int width, int height, int maxHeight, String selected, List<String> options, Consumer<StringSelector> onChange) {
        super(x, y, width, height, maxHeight, new Button(x, y, width, height, selected, null));
        this.current = selected;
        this.options = (List) options;
        this.onChange = onChange;
        updateOptions();
    }

    public void setOptions(String current, List<String> options) {
        this.current = current;
        dropDown.setText(current);
        this.options = (List) options;
        updateOptions();
    }

    public void setOptions(ColoredString current, List<ColoredString> options) {
        this.current = current;
        dropDown.setText(current);
        this.options = (List) options;
        updateOptions();
    }

    protected void updateOptions() {
        clearScrollContainer();
        int y = this.y + dropDown.getHeight();
        for (Object s : options) {
            if (s == current) continue;
            Button b;
            if (s instanceof ColoredString) {
                b = new Button(x, y, width, height, (ColoredString) s, null);
            } else {
                b = new Button(x, y, width, height, s.toString(), null);
            }
            b.setOnClick((b1) -> {
                current = s;
                dropDown.setText(s.toString());
                toggle();
                onChange.accept(this);
            });
            addToScrollContainer(b);
            y += b.getHeight();
        }
    }

    @Override
    protected void toggle(Button b) {
        super.toggle(b);
        if (!elements.contains(scrollContainer)) {
            updateOptions();
        }
    }

    public Object getCurrent() {
        return current;
    }
}
