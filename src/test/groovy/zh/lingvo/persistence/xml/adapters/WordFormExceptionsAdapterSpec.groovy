package zh.lingvo.persistence.xml.adapters

import spock.lang.Specification
import zh.lingvo.persistence.xml.entities.XmlEntity

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement

class WordFormExceptionsAdapterSpec extends Specification {
    def "blah"() {
        given: ''
        def context = JAXBContext.newInstance(XmlEntityTestClass)
//        def context = JAXBContext.newInstance Map<PartOfSpeech, Map<Enum, String>>
        def marshaller = context.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false)
        def file = new File("test.xml")
        marshaller.marshal(new XmlEntityTestClass(42, 'The answer to the question'), file)
        println file
    }

    @XmlRootElement(name = "test")
    @XmlAccessorType(XmlAccessType.PROPERTY)
    static class XmlEntityTestClass implements XmlEntity {
        private int number
        private String text

        XmlEntityTestClass() {
        }

        XmlEntityTestClass(int number, String text) {
            this.number = number
            this.text = text
        }

        @XmlAttribute
        int getNumber() {
            return number
        }

        void setNumber(int number) {
            this.number = number
        }

        String getText() {
            return text
        }

        void setText(String text) {
            this.text = text
        }
    }
}
