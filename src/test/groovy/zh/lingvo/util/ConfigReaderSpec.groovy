package zh.lingvo.util

import spock.lang.Specification
import spock.lang.Unroll
import zh.lingvo.TestEnum

import static zh.lingvo.util.ConfigReader.DEFAULT_PATH

class ConfigReaderSpec extends Specification {
    private ConfigReader config = new ConfigReader('test-lingvo')

    @Unroll
    def "Config can check paths: #path"() {
        expect: 'path existence to be checked correctly'
        config.hasPath(path) == exists

        where: 'the parameters are'
        path               || exists
        'intValue'         || true
        'charValue'        || false
        'objValue.name'    || true
        'objValue.surname' || false
    }

    @Unroll
    def "Config can return primitive values: #method"() {
        when: 'a primitive value is requested'
        def value = config."$method"(path)

        then: 'the expected value is returned'
        value.class == expectedClass
        value == expectedValue

        where: 'the parameters are'
        method         | path          || expectedValue | expectedClass
        'getAsInt'     | 'intValue'    || 42            | Integer
        'getAsShort'   | 'intValue'    || 42            | Short
        'getAsByte'    | 'intValue'    || 42            | Byte
        'getAsLong'    | 'intValue'    || 42            | Long
        'getAsDouble'  | 'intValue'    || 42            | Double
        'getAsFloat'   | 'intValue'    || 42            | Float
        'getAsBoolean' | 'boolValue'   || true          | Boolean
        'getString'    | 'stringValue' || 'abc'         | String
    }

    @Unroll
    def "Config can return default primitive values if path does not resolve: #method"() {
        when: '"getOrDefault" primitive value is requested for non-existing path'
        def value = config."$method"(path, defaultValue)

        then: 'the default value is returned'
        value == defaultValue

        where: 'the parameters are'
        method               | path              | defaultValue
        'getStringOrDefault' | 'nonExistingPath' | 'default string'
    }

    def "Config can return objects"() {
        when: 'an object is requested'
        def value = config.getAsObject('objValue', { new Fruit(it) })

        then: 'the returned object is as expected'
        value == new Fruit('apple', Colour.GREEN, 2.4)
    }

    @Unroll
    def "Config cat return a list of primitive values: #method"() {
        when: 'a list of primitive values is requested'
        def list = config."$method"(path)

        then: 'the expected value is returned'
        list.forEach { assert it.class == expectedClass }
        list == expectedValue

        where: 'the parameters are'
        method             | path              || expectedClass | expectedValue
        'getAsIntList'     | 'intListValue'    || Integer       | [1, 2, 3]
        'getAsLongList'    | 'intListValue'    || Long          | [1, 2, 3]
        'getAsDoubleList'  | 'intListValue'    || Double        | [1, 2, 3]
        'getAsStringList'  | 'stringListValue' || String        | ['abc', 'def']
        'getAsBooleanList' | 'boolListValue'   || Boolean       | [false, true, false]
    }

    def "Config can return a list of objects"() {
        when: 'the config list is extracted'
        def list = config.getList('intListValue') { it.getAsInt(DEFAULT_PATH) }

        then: 'the values in the list are as in the file'
        list == [1, 2, 3]
    }

    def "Config can return a list of objects in a given order"() {
        when: 'the config list is extracted with the comparator'
        def list = config.getList('intListValue') { it.getAsInt(DEFAULT_PATH) } { a, b -> b - a }

        then: 'the values in the list are as in the file'
        list == [3, 2, 1]
    }

    def "Config can return enums"() {
        when: 'an enum value is requested'
        def value = config.getEnum('enumValue', TestEnum)

        then: 'the returned value is correct'
        value == TestEnum.FORTY_TWO
    }

    def "Config can return a list of enums"() {
        when: 'a list of enums is requested'
        def value = config.getEnumList('enumListValue', TestEnum)

        then: 'the returned list is correct'
        value == [TestEnum.FORTY_THREE, TestEnum.FORTY_TWO]
    }

    def "Config can turn lists of objects to maps"() {
        when: 'a map is requested'
        def value = config.getMap('mapValue') { it.getString('name') } { new Fruit(it) }

        then: 'the returned map is correct'
        value == [
                apple: new Fruit('apple', Colour.GREEN, 2.4),
                orange: new Fruit('orange', Colour.ORANGE, 3.5)
        ]
    }

    private class Fruit {
        private final String name
        private final Colour colour
        private final double weight

        Fruit(ConfigReader config) {
            name = config.getString('name')
            colour = config.getEnum('colour', Colour)
            weight = config.getAsDouble('weight')
        }

        Fruit(String name, Colour colour, double weight) {
            this.name = name
            this.colour = colour
            this.weight = weight
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            Fruit fruit = (Fruit) o

            if (Double.compare(fruit.weight, weight) != 0) return false
            if (colour != fruit.colour) return false
            if (name != fruit.name) return false

            return true
        }

        int hashCode() {
            int result
            long temp
            result = (name != null ? name.hashCode() : 0)
            result = 31 * result + (colour != null ? colour.hashCode() : 0)
            temp = weight != +0.0d ? Double.doubleToLongBits(weight) : 0L
            result = 31 * result + (int) (temp ^ (temp >>> 32))
            return result
        }
    }

    private enum Colour {
        GREEN, ORANGE
    }
}
