package zh.lingvo.data.services;

import zh.lingvo.core.PartOfSpeech;

import java.util.Set;

public interface PosService {
    Set<PartOfSpeech> findAll();
}
