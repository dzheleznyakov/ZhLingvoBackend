import org.junit.Test;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.Reader;
import zh.lingvo.persistence.xml.XmlReader;
import zh.lingvo.rest.entities.DictionaryRestEntity;
import zh.lingvo.util.json.JsonFactory;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class Temp {
    @Test
    public void start() throws Exception {
        Reader reader = new XmlReader();
        Dictionary dictionary = reader.loadDictionary("en_dictionary.xml");
        DictionaryRestEntity restDictionary = new DictionaryRestEntity(dictionary);
        String json = JsonFactory.toJson(restDictionary);
        System.out.println("---------");
        System.out.println(json);
    }

    @Test
    public void streamArrayTest() {
        Integer[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        Arrays.stream(array)
                .map(n -> n * n)
                .forEach(System.out::println);
        System.out.println("---------");
        StreamSupport.stream(new ArraySpliterator(array), false)
                .map(n -> n * n)
                .forEach(System.out::println);
        System.out.println("---------");
        StreamSupport.stream(new ArraySpliterator(array), true)
                .map(n -> n * n)
                .forEach(System.out::println);
    }
}

class ArraySpliterator implements Spliterator<Integer> {
    private final Integer[] elements;
    private final int fence;
    private int nextIndex;

    ArraySpliterator(Integer[] elements) {
        this.elements = elements;
        nextIndex = 0;
        fence = elements.length;
    }

    ArraySpliterator(Integer[] elements, int nextIndex, int fence) {
        this.elements = elements;
        this.fence = fence;
        this.nextIndex = nextIndex;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Integer> action) {
        if (nextIndex < fence) {
            action.accept(elements[nextIndex]);
            nextIndex++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Spliterator<Integer> trySplit() {
        int lo = nextIndex;
        int mid = (fence - lo) / 2;
        if (lo < mid) {
            nextIndex = mid;
            return new ArraySpliterator(elements, lo, mid);
        } else {
            return null;
        }
    }

    @Override
    public long estimateSize() {
        return fence - nextIndex;
    }

    @Override
    public int characteristics() {
        return ORDERED | SIZED | IMMUTABLE | SUBSIZED;
    }
}
