package zh.lingvo.persistence.xml2;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.Reader;
import zh.lingvo.persistence.xml2.entities.DictionaryXml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

@Service
public class XmlReader implements Reader {
    private XmlMapper xmlMapper = new XmlMapper();
    private WordFactory wordFactory;

    public XmlReader(WordFactory wordFactory) {
        this.wordFactory = wordFactory;
    }

    @Override
    public Dictionary loadDictionary(String fileName) throws IOException, URISyntaxException {
        URL fileUrl = getClass().getClassLoader().getResource(fileName);
        if (fileUrl == null)
            return null;
        File dictionaryFile = new File(fileUrl.toURI());
        return loadDictionary(dictionaryFile);
    }

    @Override
    public Dictionary loadDictionary(File dictionaryFile) throws IOException {
        Dictionary dictionary = null;
        try (InputStream in = new BufferedInputStream(new FileInputStream(dictionaryFile))) {
            DictionaryXml dictionaryXml = loadEntity(in, DictionaryXml.class);
            dictionary = wordFactory.getDictionary(dictionaryXml);
        }
        return dictionary;
    }

    <E> E loadEntity(InputStream in, Class<E> entityClass) throws IOException {
        return xmlMapper.readValue(in, entityClass);
    }
}
