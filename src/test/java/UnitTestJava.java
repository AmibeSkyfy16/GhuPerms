import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;

public class UnitTestJava {

    public static abstract class GenericClass<T> {
        public Class<T> getParameterClass() {
            return (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0];
        }
    }
    public static class ConcreteClass extends GenericClass<String> {
    }
    @Test
    public void shouldGetGenericType() {

        var sb = new StringBuilder("command");
        sb.replace(0, 0, "mc:");
        System.out.println(sb.toString());

    }

}
