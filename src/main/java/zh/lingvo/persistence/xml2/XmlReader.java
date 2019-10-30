package zh.lingvo.persistence.xml2;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.Reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class XmlReader implements Reader {
    private XmlMapper xmlMapper = new XmlMapper();

    @Override
    public Dictionary loadDictionary(String fileName) throws Exception {
        return null;
    }

    @Override
    public Dictionary loadDictionary(File dictionaryFile) throws IOException {
        Dictionary dictionary;
        try (InputStream in = new BufferedInputStream(new FileInputStream(dictionaryFile))) {
            dictionary = loadEntity(in, Dictionary.class);
        }
        return dictionary;
    }

    <E> E loadEntity(InputStream in, Class<E> entityClass) throws IOException {
        return xmlMapper.readValue(in, entityClass);
    }
}
