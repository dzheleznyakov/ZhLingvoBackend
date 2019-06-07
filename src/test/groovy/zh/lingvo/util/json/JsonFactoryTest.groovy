package zh.lingvo.util.json


import spock.lang.Specification
import spock.lang.Unroll
import zh.lingvo.rest.entities.JsonEntity

class JsonFactoryTest extends Specification {
    @Unroll
    def "Primitive value [#value as #type] is converted to string"() {
        when: 'a primitive value is converted to json'
        String json = JsonFactory.toJson(value)

        then: 'it is simply converted to a string'
        json == String.valueOf(value)

        where: 'parameters are'
        value          | type
        101            | 'int'
        10.2           | 'double'
        10.5 as float  | 'float'
        101 as short   | 'short'
        101 as byte    | 'byte'
        101 as long    | 'long'
        true           | 'boolean'
        false          | 'boolean'
        null           | 'null'
    }

    @Unroll
    def "Character value [#value as #type] is wrapped in double quotation marks"() {
        when: 'a primitive value is converted to json'
        String json = JsonFactory.toJson(value)

        then: 'it is simply converted to a string'
        json == result

        where: 'parameters are'
        value         | type      || result
        'a' as char   | 'char'    || '"a"'
        'aa'          | 'string'  || '"aa"'
    }

    @Unroll
    def "Array of type #type is converted to json string"() {
        when: 'an array is converted to json'
        String json = JsonFactory.toJson(array)

        then: 'each element is converted separately'
        json == result

        where: 'parameters are'
        array                          | type          || result
        [1, 2, 3] as int[]             | 'int[]'       || '[1,2,3]'
        [1.0, 2.22, 3] as double[]     | 'double[]'    || '[1.0,2.22,3.0]'
        [1.0, 2.22, 3] as float[]      | 'float[]'     || '[1.0,2.22,3.0]'
        [1, 2, 3] as short[]           | 'short[]'     || '[1,2,3]'
        [1, 2, 3] as byte[]            | 'byte[]'      || '[1,2,3]'
        [1, 2, 3] as long[]                                                          | 'long[]'               || '[1,2,3]'
        [1, 2, 3] as Integer[]                                                       | 'Integer[]'            || '[1,2,3]'
        [true, false] as boolean[]                                                   | 'boolean[]'            || '[true,false]'
        [true, false] as Boolean[]                                                   | 'Boolean[]'            || '[true,false]'
        ['a', 'b', 'c'] as char[]                                                    | 'char[]'               || '["a","b","c"]'
        ['a', 'b', 'c'] as Character[]                                               | 'Character[]'          || '["a","b","c"]'
        ["aa", "bb", "cc"] as String[]                                               | 'String[]'             || '["aa","bb","cc"]'
        [new TestableJsonEntity(), new TestableJsonEntity()] as TestableJsonEntity[] | 'TestableJsonEntity[]' || "[$TestableJsonEntity.expectedJson,$TestableJsonEntity.expectedJson]"
    }

    @Unroll
    def "Iterable [#iterable of type #type] should be converted to json"() {
        when: 'an iterable is converted to json'
        String json = JsonFactory.toJson(iterable)

        then: 'elements are wrapped in json array'
        json == result
        true

        where: 'parameters are'
        type   | iterable                                                  || result
        'Set'  | [1, 2, 3] as HashSet<Integer>                             || '[1,2,3]'
        'Set'  | ['a' as char, 'b' as char, 'c' as char] as Set<Character> || '["a","b","c"]'
        'Set'  | ['a', 'b', 'c'] as Set<String>                            || '["a","b","c"]'
        'List' | [true, false, true] as List<Boolean>                      || '[true,false,true]'
        'List' | [1.0, 2.22, 3d] as List<Double>                           || '[1.0,2.22,3.0]'
    }

    def "Map should be converted to json"() {
        given: 'a map'
        Map<Integer, JsonEntity> map = [1: new TestableJsonEntity(), 2: new TestableJsonEntity()]

        when: 'it is converted to json'
        String json = JsonFactory.toJson(map)

        then: 'it is converted to json object'
        json == "{\"1\":$TestableJsonEntity.expectedJson,\"2\":$TestableJsonEntity.expectedJson}"
    }

    def "Iterator should be converted to json"() {
        given: 'an iterator'
        Iterator<Integer> iterator = [1, 2, 3].iterator()

        when: 'it is converted to json'
        String json = JsonFactory.toJson(iterator)

        then: 'it is converted to json array'
        json == '[1,2,3]'
    }

    def "With iterator, only remaining elements go into json"() {
        given: 'an iterator that is already started'
        Iterator<Integer> iterator = [1, 2, 3].iterator()
        iterator.next()

        when: 'it is converted to json'
        String json = JsonFactory.toJson(iterator)

        then: 'only remaining elements go into json'
        json == '[2,3]'
    }

    def "Only fields annotated as Persistable are going into json"() {
        given: 'a JsonEntity'
        JsonEntity obj = new TestableJsonEntity()

        when: 'it is sent to JsonFactory'
        String json = JsonFactory.toJson(obj)

        then: 'only persistable fields are serialised'
        json == TestableJsonEntity.expectedJson
    }

    private static class TestableJsonEntity implements JsonEntity {
        static final String expectedJson = '{"intField":10,"boolField":true,"stringField":"abc"}'

        @Persistable
        private int intField = 10

        @Persistable
        private boolean boolField = true

        @Persistable
        private String stringField = 'abc'

        private int intField2 = 20

        int getIntField() {
            return intField
        }

        boolean isBoolField() {
            return boolField
        }

        String getStringField() {
            return stringField
        }

        int getIntField2() {
            return intField2
        }
    }
}
