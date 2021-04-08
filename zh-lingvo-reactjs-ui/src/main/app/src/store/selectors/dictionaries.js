import { createSelector } from 'reselect';

export const loadingDictionariesSelector = state => state.dictionaries.loading;

export const dictionariesSelector = state => state.dictionaries.dictionaries;

export const selectedDictionaryIndexSelector = state => state.dictionaries.selectedDictionaryIndex;

export const dictionarySelectedSelector = state => state.dictionaries.selectedDictionaryIndex >= 0;

export const loadedDictionarySelector = state => state.dictionaries.loadedDictionary;

export const selectedDictionarySelector = state => {
    const { selectedDictionaryIndex, dictionaries } = state.dictionaries;
    return selectedDictionaryIndex < 0
        ? { language: {} }
        : dictionaries[selectedDictionaryIndex];
};

export const dictionariesTableDataSelector = createSelector(
    dictionariesSelector,
    dictionaries => dictionaries.map(dic => ({ 
        name: { 
            value: dic.name,
            id: dic.id,
        },
        language: { value: dic.language.name }
    }))
);

export const languagesSelector = state => state.dictionaries.languages;
