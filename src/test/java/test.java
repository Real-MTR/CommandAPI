import com.google.common.base.Joiner;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {

    public static void main(String[] args) {
        new test().ok();

    }

    private final List<String> test = new ArrayList<>();

    @SneakyThrows
    public void ok() {
        List<String> arr = (List<String>) this.getClass().getDeclaredMethod("test").invoke(this);
        arr.add("test");
        arr.add("i love niggers");

        System.out.println(test);
    }

    private List<String> test() {
        return test;
    }
}
