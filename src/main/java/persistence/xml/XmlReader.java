package persistence.xml;

import persistence.PersistenceException;
import persistence.Reader;
import zh.lingvo.domain.Dictionary;
import persistence.xml.WordFactory;
import persistence.xml.entities.DictionaryXmlEntity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlReader implements Reader {
    @Override
    public Dictionary loadDictionary(String fileName) {
        try {
            return loadDictionary(new File(fileName));
        } catch (JAXBException e) {
            throw new PersistenceException(String.format("Exception when loading dictionary from [%s]", fileName), e);
        }
    }

    @Override
    public Dictionary loadDictionary(File dictionaryFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(DictionaryXmlEntity.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        DictionaryXmlEntity xmlDictionary = (DictionaryXmlEntity) unmarshaller.unmarshal(dictionaryFile);
        return WordFactory.create(xmlDictionary);
    }
}
