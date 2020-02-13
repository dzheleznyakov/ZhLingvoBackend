package zh.lingvo.persistence.xml2;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.Reader;
import zh.lingvo.persistence.xml2.entities.DictionaryXml;

import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class XmlReader implements Reader {
    private XmlMapper xmlMapper = new XmlMapper();
    private DictionaryFactory dictionaryFactory;

    public XmlReader(DictionaryFactory dictionaryFactory) {
        this.dictionaryFactory = dictionaryFactory;
    }

    @Override
    @Nullable
    public Dictionary loadDictionary(String fileName) throws IOException {
        File dictionaryFile = new File(fileName);
        return dictionaryFile.exists() ? loadDictionary(dictionaryFile) : null;
    }

    @Override
    public Dictionary loadDictionary(File dictionaryFile) throws IOException {
        Dictionary dictionary;
        try (InputStream in = new BufferedInputStream(new FileInputStream(dictionaryFile))) {
            DictionaryXml dictionaryXml = loadEntity(in, DictionaryXml.class);
            dictionary = dictionaryFactory.getDictionary(dictionaryXml);
        }
        return dictionary;
    }

    <E> E loadEntity(InputStream in, Class<E> entityClass) throws IOException {
        return xmlMapper.readValue(in, entityClass);
    }
}
