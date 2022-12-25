import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import * as selectors from '../../../../store/selectors';
import * as actions from '../../../../store/actions';
import * as modalTypes from '../../../../static/constants/wordEditModalTypes';
import * as dialogs from '../Dialogs';
import { useModal } from '../../../../hooks';

const WordEditModal = () => {
    const showWordEditModal = useSelector(selectors.shouldShowWordEditModalSelector);
    const wordEditModalType = useSelector(selectors.wordEditModalTypeSelector);
    const dispatch = useDispatch();

    let panel;
    switch (wordEditModalType) {
        case modalTypes.MAIN_FORM: panel = <dialogs.MainFormDialog />; break;
        case modalTypes.TRANSCRIPTION_NEW: panel = <dialogs.TranscriptionEditDialog />; break;
        case modalTypes.TRANSCRIPTION_EDIT: panel = <dialogs.TranscriptionEditDialog editing />; break;
        case modalTypes.TRANSCRIPTION_DELETE: panel = <dialogs.TranscriptionDeleteDialog />; break;
        case modalTypes.REMARK_NEW: panel = <dialogs.RemarkEditDialog />; break;
        case modalTypes.REMARK_EDIT: panel = <dialogs.RemarkEditDialog editing />; break;
        case modalTypes.REMARK_DELETE: panel = <dialogs.RemarkDeleteDialog />; break;
        case modalTypes.TRANSLATION_NEW: panel = <dialogs.TranslationEditDialog />; break;
        case modalTypes.TRANSLATION_EDIT: panel = <dialogs.TranslationEditDialog editing />; break;
        case modalTypes.TRANSLATION_DELETE: panel = <dialogs.TranslationDeleteDialog />; break;
        case modalTypes.EXAMPLE_NEW: panel = <dialogs.ExampleEditDialog />; break;
        case modalTypes.EXAMPLE_EDIT: panel = <dialogs.ExampleEditDialog editing />; break;
        case modalTypes.EXAMPLE_DELETE: panel = <dialogs.ExampleDeleteDialog />; break;
        case modalTypes.MEANING_NEW: panel = <dialogs.MeaningEditDialog />; break;
        case modalTypes.MEANING_DELETE: panel = <dialogs.MeaningDeleteDialog />; break;
        case modalTypes.MEANING_TO_QUIZ_RECORD__CONFIRM_RECORD: 
            panel = <dialogs.MeaningToQuizRecordDialog />; 
            break;
        case modalTypes.MEANING_TO_QUIZ_RECORD__CHOOSE_QUIZ: 
            panel = <dialogs.MeaningToQuizRecordDialog_ChooseQuiz />; 
            break;
        case modalTypes.SEM_BLOCK_NEW: panel = <dialogs.SemanticBlockEditDialog />; break;
        case modalTypes.SEM_BLOCK_EDIT: panel = <dialogs.SemanticBlockEditDialog editing />; break;
        case modalTypes.SEM_BLOCK_DELETE: panel = <dialogs.SemanticBlockDeleteDialog />; break;
        default: panel = null;
    }

    useModal(
        showWordEditModal, 
        () => dispatch(actions.shouldShowWordEditModal(false)), 
        panel,
        [wordEditModalType]);

    return <></>;
};

export default WordEditModal;
