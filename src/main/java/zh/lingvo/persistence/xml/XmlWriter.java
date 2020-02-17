package zh.lingvo.persistence.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.PersistenceException;
import zh.lingvo.persistence.Writer;
import zh.lingvo.persistence.xml.entities.DictionaryXml;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
class XmlWriter implements Writer {
    private DictionaryXmlFactory dictionaryXmlFactory;

    private XmlMapper xmlMapper = new XmlMapper();

    public XmlWriter(DictionaryXmlFactory dictionaryXmlFactory) {
        this.dictionaryXmlFactory = dictionaryXmlFactory;
    }

    @Override
    public void saveDictionary(Dictionary dictionary, String fileName) throws PersistenceException {
        try (OutputStream out = getOutputStream(fileName)) {
            String declaration = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<!DOCTYPE dictionary SYSTEM \"dictionary.dtd\">\n\n";
            out.write(declaration.getBytes());

            DictionaryXml dictionaryXml = dictionaryXmlFactory.getDictionaryXml(dictionary);
            toOutputStream(dictionaryXml, out);
        } catch (IOException e) {
            throw new PersistenceException(String.format("Failed to save the dictionary [%s]", fileName), e);
        }
    }

    private OutputStream getOutputStream(String fileName) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(fileName));
    }

    <E> void toOutputStream(E entity, OutputStream out) throws IOException {
        xmlMapper.writeValue(out, entity);
    }
}
