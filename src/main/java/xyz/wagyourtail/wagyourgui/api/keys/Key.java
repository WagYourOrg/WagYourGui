package xyz.wagyourtail.wagyourgui.api.keys;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Key {

    Key UNKNOWN = new Key() {
        @Override
        public int getKeyCode() {
            return -1;
        }

        @Override
        public String name() {
            return "UNKNOWN";
        }

        @Override
        public String toString() {
            return "UNKNOWN";
        }
    };

    public enum Keyboard implements Key {
        SPACE(32),
        APOSTROPHE(39),
        COMMA(44),
        MINUS(45),
        PERIOD(46),
        SLASH(47),
        KEYBOARD_0(48),
        KEYBOARD_1(49),
        KEYBOARD_2(50),
        KEYBOARD_3(51),
        KEYBOARD_4(52),
        KEYBOARD_5(53),
        KEYBOARD_6(54),
        KEYBOARD_7(55),
        KEYBOARD_8(56),
        KEYBOARD_9(57),
        SEMICOLON(59),
        EQUAL(61),
        A(65),
        B(66),
        C(67),
        D(68),
        E(69),
        F(70),
        G(71),
        H(72),
        I(73),
        J(74),
        K(75),
        L(76),
        M(77),
        N(78),
        O(79),
        P(80),
        Q(81),
        R(82),
        S(83),
        T(84),
        U(85),
        V(86),
        W(87),
        X(88),
        Y(89),
        Z(90),
        LEFT_BRACKET(91),
        BACKSLASH(92),
        RIGHT_BRACKET(93),
        GRAVE_ACCENT(96),
        WORLD_1(161),
        WORLD_2(162),
        ESCAPE(256),
        ENTER(257),
        TAB(258),
        BACKSPACE(259),
        INSERT(260),
        DELETE(261),
        RIGHT(262),
        LEFT(263),
        DOWN(264),
        UP(265),
        PAGE_UP(266),
        PAGE_DOWN(267),
        HOME(268),
        END(269),
        CAPS_LOCK(280),
        SCROLL_LOCK(281),
        NUM_LOCK(282),
        PRINT_SCREEN(283),
        PAUSE(284),
        F1(290),
        F2(291),
        F3(292),
        F4(293),
        F5(294),
        F6(295),
        F7(296),
        F8(297),
        F9(298),
        F10(299),
        F11(300),
        F12(301),
        F13(302),
        F14(303),
        F15(304),
        F16(305),
        F17(306),
        F18(307),
        F19(308),
        F20(309),
        F21(310),
        F22(311),
        F23(312),
        F24(313),
        F25(314),
        KP_0(320),
        KP_1(321),
        KP_2(322),
        KP_3(323),
        KP_4(324),
        KP_5(325),
        KP_6(326),
        KP_7(327),
        KP_8(328),
        KP_9(329),
        KP_DECIMAL(330),
        KP_DIVIDE(331),
        KP_MULTIPLY(332),
        KP_SUBTRACT(333),
        KP_ADD(334),
        KP_ENTER(335),
        KP_EQUAL(336),
        LEFT_SHIFT(340),
        LEFT_CONTROL(341),
        LEFT_ALT(342),
        LEFT_SUPER(343),
        RIGHT_SHIFT(344),
        RIGHT_CONTROL(345),
        RIGHT_ALT(346),
        RIGHT_SUPER(347),
        MENU(348),
        LAST(Key.Keyboard.MENU);


        private final String name = name();
        private final int code;

        private static final Set<Key> keys = Arrays.stream(Keyboard.values()).collect(Collectors.toSet());
        private static final Map<Integer, Key> byCode = keys.stream().collect(Collector.of(HashMap::new, (m, k) -> m.put(k.getKeyCode(), k), (m1, m2) -> {
            if (m1.size() > m2.size()) {
                m1.putAll(m2);
                return m1;
            } else {
                m2.putAll(m1);
                return m2;
            }
        }));
        private static final Map<String, Key> byName = keys.stream().collect(Collectors.toMap(Key::name, k -> k));

        Keyboard(int code) {
            this.code = code;
        }

        Keyboard(Key key) {
            this.code = key.getKeyCode();
        }

        public int getKeyCode() {
            return code;
        }

        public static Key getKey(int code) {
            return byCode.getOrDefault(code, UNKNOWN);
        }

        public static Key getKey(String name) {
            Key key = byName.getOrDefault(name, UNKNOWN);
            if (key == UNKNOWN) {
                if (name.startsWith(Keyboard.class.getSimpleName().toUpperCase() + "_")) {
                    return getKey(name.substring(Keyboard.class.getSimpleName().length() + 1));
                }
            }
            return key;
        }
    }

    public enum Mouse implements Key {
        MOUSE_1(0),
        MOUSE_2(1),
        MOUSE_3(2),
        MOUSE_4(3),
        MOUSE_5(4),
        MOUSE_6(5),
        MOUSE_7(6),
        MOUSE_8(7),
        LEFT(Key.Mouse.MOUSE_1),
        RIGHT(Key.Mouse.MOUSE_2),
        MIDDLE(Key.Mouse.MOUSE_3),
        LAST(Key.Mouse.MOUSE_8);


        private final String name = name();
        private final int code;

        private static final Set<Key> keys = Arrays.stream(Mouse.values()).collect(Collectors.toSet());
        private static final Map<Integer, Key> byCode = keys.stream().collect(Collector.of(HashMap::new, (m, k) -> m.put(k.getKeyCode(), k), (m1, m2) -> {
            if (m1.size() > m2.size()) {
                m1.putAll(m2);
                return m1;
            } else {
                m2.putAll(m1);
                return m2;
            }
        }));
        private static final Map<String, Key> byName = keys.stream().collect(Collectors.toMap(Key::name, k -> k));

        Mouse(int code) {
            this.code = code;
        }

        Mouse(Key key) {
            this.code = key.getKeyCode();
        }

        @Override
        public int getKeyCode() {
            return code;
        }

        public static Key getKey(int code) {
            return byCode.getOrDefault(code, UNKNOWN);
        }

        public static Key getKey(String name) {
            Key key = byName.getOrDefault(name, UNKNOWN);
            if (key == UNKNOWN) {
                if (name.startsWith(Mouse.class.getSimpleName().toUpperCase() + "_")) {
                    return getKey(name.substring(Mouse.class.getSimpleName().length() + 1));
                }
            }
            return key;
        }
    }

    Set<Key> keys = Stream.of(Keyboard.keys, Mouse.keys).flatMap(Set::stream).collect(Collectors.toSet());

    Map<Integer, Key> byCode = Stream.of(Keyboard.byCode, Mouse.byCode).flatMap(m -> m.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    Map<String, Key> byName = Stream.of(Keyboard.byName, Mouse.byName).reduce(new HashMap<>(), (r, a) -> {
                for (Map.Entry<String, Key> key : a.entrySet()) {
                    if (r.containsKey(key.getKey())) {
                        // resolve conflicts
                        if (r.get(key.getKey()).getKeyCode() == key.getValue().getKeyCode()) {
                            // same key, no conflict
                            continue;
                        }
                        // different key, conflict
                        // resolve by prepending the string name with the class name
                        String aName = r.get(key.getKey()).getClass().getSimpleName().toUpperCase() + "_" + key.getKey();
                        String bName = key.getValue().getClass().getSimpleName().toUpperCase() + "_" + key.getKey();
                        if (aName.equals(bName)) throw new IllegalStateException("Duplicate key name: " + aName);
                        Key aKey = r.remove(key.getKey());
                        r.put(aName, aKey);
                        r.put(bName, key.getValue());
                    } else {
                        r.put(key.getKey(), key.getValue());
                    }
                }
                return r;
            }
    );


    int getKeyCode();

    String name();

    static Key getKey(int code) {
        return byCode.get(code);
    }

    static Key getKey(String name) {
        return byName.get(name.toLowerCase());
    }
}
