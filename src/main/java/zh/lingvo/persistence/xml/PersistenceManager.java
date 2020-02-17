package zh.lingvo.persistence.xml;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.springframework.stereotype.Service;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.Reader;
import zh.lingvo.persistence.Writer;
import zh.lingvo.util.Pair;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Service
public class PersistenceManager implements Reader, Writer {
    private XmlReader xmlReader;
    private XmlWriter xmlWriter;

    private PublishSubject<Pair<Dictionary, String>> saveDictionarySubject = PublishSubject.create();
    private Disposable saveDictionarySubscription;

    public PersistenceManager(XmlReader xmlReader, XmlWriter xmlWriter) {
        this.xmlReader = xmlReader;
        this.xmlWriter = xmlWriter;
        subscribe();
    }

    private void subscribe() {
        close();
        saveDictionarySubscription = saveDictionarySubject.subscribeOn(Schedulers.single())
                .throttleLatest(5, TimeUnit.SECONDS)
                .subscribe(savingPayload -> {
                    Dictionary dictionary = savingPayload.getFirst();
                    String filePath = savingPayload.getSecond();
                    xmlWriter.saveDictionary(dictionary, filePath);
                }, e -> {
                    System.out.println("Save dictionary subscription failed");
                    e.printStackTrace();
                    subscribe();
                });
    }

    @Override
    public Dictionary loadDictionary(String fileName) {
        return xmlReader.loadDictionary(fileName);
    }

    @Override
    public Dictionary loadDictionary(File dictionaryFile) {
        return xmlReader.loadDictionary(dictionaryFile);
    }

    @Override
    public void saveDictionary(Dictionary dictionary, String fileName) {
        saveDictionarySubject.onNext(new Pair<>(dictionary, fileName));
    }

    @Override
    public void close() {
        xmlWriter.close();
        if (saveDictionarySubscription != null && !saveDictionarySubscription.isDisposed()) {
            saveDictionarySubscription.dispose();
        }
    }


}
