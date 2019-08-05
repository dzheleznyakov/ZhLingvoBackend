package zh.lingvo.persistence.xml;

import zh.lingvo.persistence.PersistenceException;
import zh.lingvo.persistence.Writer;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.xml.entities.DictionaryXmlEntity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class XmlWriter implements Writer {
    @Override
    public void saveDictionary(Dictionary dictionary, String fileName) throws PersistenceException {
        File dictionaryFile = new File(fileName);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DictionaryXmlEntity.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            marshaller.marshal(new DictionaryXmlEntity(dictionary), dictionaryFile);
        } catch (JAXBException e) {
            throw new PersistenceException(String.format("Error when persisting the dictionary at [%s]", fileName), e);
        }
    }
}
