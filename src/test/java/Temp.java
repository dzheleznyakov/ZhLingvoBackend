import org.junit.Test;
import zh.lingvo.TestEnum;

public class Temp {
    @Test
    public void name() {
        accept(TestEnum.class);
        accept(Enum.class);
    }

    private <E extends Enum<E>> void accept(Class<E> eClass) {
        System.out.println(eClass);
    }
}
