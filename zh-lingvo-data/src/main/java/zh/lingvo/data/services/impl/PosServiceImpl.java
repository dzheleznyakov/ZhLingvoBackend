package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Service;
import zh.lingvo.core.LanguageDescriptorManager;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.services.PosService;

import java.util.List;
import java.util.Set;

@Service
public class PosServiceImpl implements PosService {
    private final LanguageDescriptorManager languageDescriptorManager;

    public PosServiceImpl(LanguageDescriptorManager languageDescriptorManager) {
        this.languageDescriptorManager = languageDescriptorManager;
    }

    @Override
    public Set<PartOfSpeech> findAll() {
        return ImmutableSet.copyOf(PartOfSpeech.values());
    }

    @Override
    public List<PartOfSpeech> findAll(String code) {
        return languageDescriptorManager.get(code)
                .getPartsOfSpeech();
    }

    @Override
    public String getShortName(PartOfSpeech pos) {
        return pos.getShortName();
    }

    @Override
    public String getShortNativeName(PartOfSpeech pos, String code) {
        return languageDescriptorManager.get(code)
                .getPartOfSpeechNativeShortName(pos);
    }

    @Override
    public String getNativeName(PartOfSpeech pos, String code) {
        return languageDescriptorManager.get(code)
                .getPartOfSpeechNativeName(pos);
    }
}
