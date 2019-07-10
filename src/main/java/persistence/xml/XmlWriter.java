package persistence.xml;

import persistence.Writer;
import zh.lingvo.domain.Dictionary;
import persistence.xml.entities.DictionaryXmlEntity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class XmlWriter implements Writer {
    @Override
    public void saveDictionary(Dictionary dictionary, String fileName) throws JAXBException {
        File dictionaryFile = new File(fileName);
        JAXBContext jaxbContext = JAXBContext.newInstance(DictionaryXmlEntity.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.marshal(new DictionaryXmlEntity(dictionary), dictionaryFile);
    }
}
