import withEditing from '../../hoc/withEditing';
import WordMainFormRegular from './WordMainForm/WordMainFormRegular';
import WordMainFormEditing from './WordMainForm/WordMainFormEditing';
import TranscriptionRegular from './Transcription/TranscriptionRegular';
import TranscriptionEditing from './Transcription/TrascriptionEditing';


export const WordMainForm = withEditing(WordMainFormRegular, WordMainFormEditing);
export const Transcription = withEditing(TranscriptionRegular, TranscriptionEditing);
