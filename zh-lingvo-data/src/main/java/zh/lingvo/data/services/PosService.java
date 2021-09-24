package zh.lingvo.data.services;

import zh.lingvo.core.domain.PartOfSpeech;

import java.util.List;
import java.util.Set;

public interface PosService {
    Set<PartOfSpeech> findAll();

    List<PartOfSpeech> findAll(String code);

    String getShortName(PartOfSpeech pos);

    String getShortNativeName(PartOfSpeech pos, String code);

    String getNativeName(PartOfSpeech pos, String code);
}
