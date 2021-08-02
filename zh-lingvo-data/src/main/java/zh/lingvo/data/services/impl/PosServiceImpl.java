package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Service;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.services.PosService;

import java.util.Set;

@Service
public class PosServiceImpl implements PosService {
    @Override
    public Set<PartOfSpeech> findAll() {
        return ImmutableSet.copyOf(PartOfSpeech.values());
    }
}
