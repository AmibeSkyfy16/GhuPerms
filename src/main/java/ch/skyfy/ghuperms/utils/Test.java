package ch.skyfy.ghuperms.utils;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Test {

    public static final Map<String, Data> MAP = new HashMap<>();
    public static final Map<LiteralArgumentBuilder<?>, Data> MAP2 = new HashMap<>();
    public static final Map<LiteralCommandNode<?>, Data2> TEST = new HashMap<>();

    public static final Map<String, String> COMMANDS = new HashMap<>();

    public static final Map<String, String> CREATED_ALIASES = new HashMap<>();

//    public static final AtomicBoolean shouldReturn = new AtomicBoolean(false);

    public static class Data {
        public Class<?> declaringClass;

        public Data(Class<?> declaringClass) {
            this.declaringClass = declaringClass;
        }
    }

    public static class Data2 {
        public String literal;

        public LiteralArgumentBuilder<?> literalArgumentBuilder;

        public Data data;

        public Data2(String literal, LiteralArgumentBuilder<?> literalArgumentBuilder, Data data) {
            this.literal = literal;
            this.literalArgumentBuilder = literalArgumentBuilder;
            this.data = data;
        }
    }

}
