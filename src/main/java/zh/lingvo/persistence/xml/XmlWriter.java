package zh.lingvo.persistence.xml;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.PersistenceException;
import zh.lingvo.persistence.Writer;
import zh.lingvo.persistence.xml.entities.DictionaryXmlEntity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class XmlWriter implements Writer {
    private PublishSubject<Payload> saveDictionarySubject = PublishSubject.create();
    private Disposable saveDictionarySubscription;

    public XmlWriter() {
        initSubscription();
    }

    private void initSubscription() {
        saveDictionarySubscription = saveDictionarySubject.subscribeOn(Schedulers.io())
//                .throttleLatest(5, TimeUnit.SECONDS)
                .subscribe(this::doSaveDictionary);
    }


    @Override
    public void saveDictionary(Dictionary dictionary, String fileName) throws PersistenceException {
        saveDictionarySubject.onNext(new Payload(dictionary, fileName));
    }

    private void doSaveDictionary(Payload payload) throws PersistenceException {
        Dictionary dictionary = payload.dictionary;
        String fileName = payload.fileName;

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

    @Override
    public void close() throws IOException {
        if (saveDictionarySubscription != null)
            saveDictionarySubscription.dispose();
    }

    private static class Payload {
        final Dictionary dictionary;
        final String fileName;

        private Payload(Dictionary dictionary, String fileName) {
            this.dictionary = dictionary;
            this.fileName = fileName;
        }
    }
}
