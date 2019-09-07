package zh.lingvo.persistence.xml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.PersistenceException;
import zh.lingvo.persistence.Reader;
import zh.lingvo.persistence.xml.entities.DictionaryXmlEntity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Service
public class XmlReader implements Reader {
    @Autowired
    private XmlWordFactory xmlWordFactory;

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
        return xmlWordFactory.create(xmlDictionary);
    }
}
