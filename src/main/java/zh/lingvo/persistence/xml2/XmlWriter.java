package zh.lingvo.persistence.xml2;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.PersistenceException;
import zh.lingvo.persistence.Writer;

import java.io.IOException;
import java.io.OutputStream;

public class XmlWriter implements Writer {
    private XmlMapper xmlMapper = new XmlMapper();

    @Override
    public void saveDictionary(Dictionary dictionary, String fileName) throws PersistenceException {

    }

    @Override
    public void close() throws IOException {

    }

    <E> void toOutputStream(E entity, OutputStream out) throws IOException {
        xmlMapper.writeValue(out, entity);
    }
}
