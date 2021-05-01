import WordMainFormComponent from './WordMainForm/WordMainForm';

import TranscriptionComponent from './Transcription/Transcription';
import EditableTranscriptionComponent from './Transcription/EditableTranscription';

import RemarkComponent from './Remark/Remark';
import EditableRemarkComponent from './Remark/EditableRemark';

import TranslationComponent from './Translation/Translation';
import EditableTranslationComponent, { NULL_TRANSLATION as NULL_TRANSLATION_OBJECT } from './Translation/EditableTranslation';

import ExampleComponent from './Example/Example';
import EditableExampleComponent, { NULL_EXAMPLE as NULL_EXAMPLE_OBJECT } from './Example/EditableExample';

import MeaningComponent from './Meaning/Meaning';
import EditableMeaningComponent, { NULL_MEANING as NULL_MEANING_OBJECT } from './Meaning/EditableMeaning';


export const WordMainForm = WordMainFormComponent;

export const Transcription = TranscriptionComponent;
export const EditableTranscription = EditableTranscriptionComponent;

export const Remark = RemarkComponent;
export const EditableRemark = EditableRemarkComponent;

export const Translation = TranslationComponent;
export const EditableTranslation = EditableTranslationComponent;
export const NULL_TRANSLATION = NULL_TRANSLATION_OBJECT;

export const Example = ExampleComponent;
export const EditableExample = EditableExampleComponent;
export const NULL_EXAMPLE = NULL_EXAMPLE_OBJECT;

export const Meaning = MeaningComponent;
export const EditableMeaning = EditableMeaningComponent;
export const NULL_MEANING = NULL_MEANING_OBJECT;
