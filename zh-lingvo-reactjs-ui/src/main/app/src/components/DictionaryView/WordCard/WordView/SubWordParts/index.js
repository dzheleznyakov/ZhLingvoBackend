import WordMainFormComponent from './WordMainForm/WordMainForm';

import TranscriptionComponent from './Transcription/Transcription';
import EditableTranscriptionComponent from './Transcription/EditableTranscription';

import RemarkComponent from './Remark/Remark';
import EditableRemarkComponent from './Remark/EditableRemark';

import TranslationComponent from './Translation/Translation';
import EditableTranslationComponent, { NULL_TRANSLATION as NULL_TRANSLATION_OBJECT } from './Translation/EditableTranslation';


export const WordMainForm = WordMainFormComponent;

export const Transcription = TranscriptionComponent;
export const EditableTranscription = EditableTranscriptionComponent;

export const Remark = RemarkComponent;
export const EditableRemark = EditableRemarkComponent;

export const Translation = TranslationComponent;
export const EditableTranslation = EditableTranslationComponent;
export const NULL_TRANSLATION = NULL_TRANSLATION_OBJECT;
