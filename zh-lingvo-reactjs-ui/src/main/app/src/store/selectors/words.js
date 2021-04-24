import _ from 'lodash';

export const wordsListSelector = state => state.words.wordsList;
export const selectedWordIndexSelector = state => state.words.selectedWordIndex;
export const wordIsLoadingSelector = state => state.words.loading;
export const loadedWordSelector = state => state.words.loadedWord;
export const isEditingSelector = state => state.words.isEditing;
export const updatedWordSelector = state => state.words.updatedWord;
export const shouldShowWordEditModalSelector = state => state.words.shouldShowWordEditModal;
export const wordEditModalTypeSelector = state => state.words.wordEditModalType;
export const wordEditPathSelector = state => state.words.wordEditPath;

export const mainFormToUpdateSelector = state => _.get(state, 'words.updatedWord[0].mainForm', '');
export const transcriptionToUpdateSelectorFactory = path =>
    state => _.get(updatedWordSelector(state), path, '');
