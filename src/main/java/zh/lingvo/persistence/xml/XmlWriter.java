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
import java.util.Objects;

@Service
class XmlWriter implements Writer {
    private DictionaryXmlFactory dictionaryXmlFactory;

    private XmlMapper xmlMapper = new XmlMapper();
    private String currentFileName;
    private OutputStream outputStream;

    public XmlWriter(DictionaryXmlFactory dictionaryXmlFactory) {
        this.dictionaryXmlFactory = dictionaryXmlFactory;
    }

    @Override
    public void saveDictionary(Dictionary dictionary, String fileName) throws PersistenceException {
        try(OutputStream out = getOutputStream(fileName)) {
            String declaration = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<!DOCTYPE dictionary SYSTEM \"dictionary.dtd\">\n\n";
            out.write(declaration.getBytes());

            DictionaryXml dictionaryXml = dictionaryXmlFactory.getDictionaryXml(dictionary);
            toOutputStream(dictionaryXml, out);
        } catch (IOException e) {
            throw new PersistenceException(String.format("Failed to save the dictionary [%s]", fileName), e);
        }
    }

    private OutputStream getOutputStream(String fileName) throws IOException {
        if (Objects.equals(fileName, currentFileName))
            return outputStream;

        if (outputStream != null)
            outputStream.close();
        currentFileName = fileName;
        outputStream = new BufferedOutputStream(new FileOutputStream(fileName));
        return outputStream;
    }

    @Override
    public void close() {
        try {
            currentFileName = null;
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
        } catch (IOException e) {
            throw new PersistenceException("Failed to close XmlWriter", e);
        }
    }


    <E> void toOutputStream(E entity, OutputStream out) throws IOException {
        xmlMapper.writeValue(out, entity);
    }
}
