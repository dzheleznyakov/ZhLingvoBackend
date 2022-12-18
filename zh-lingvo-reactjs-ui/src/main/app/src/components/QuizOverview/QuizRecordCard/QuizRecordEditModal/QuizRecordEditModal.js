import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { quizRecordEditModalTypeSelector, showQuizRecordEditModalSelector } from '../../../../store/selectors';
import * as modalTypes from '../../../../static/constants/quizRecordEditModalTypes';
import * as dialogs from '../Dialogs';
import { Modal } from '../../../UI';
import { shouldShowQuizRecordEditModal } from '../../../../store/actions';

const QuizRecordEditModal = () => {
    const showModal = useSelector(showQuizRecordEditModalSelector);
    const modalType = useSelector(quizRecordEditModalTypeSelector);
    const dispatch = useDispatch();

    let panel;
    switch (modalType) {
        case modalTypes.MAIN_FORM: panel = <dialogs.MainFormDialog />; break;
        case modalTypes.TRANSCRIPTION_NEW: panel = <dialogs.TranscriptionEditDialog />; break;
        case modalTypes.TRANSCRIPTION_EDIT: panel = <dialogs.TranscriptionEditDialog editing />; break;
        case modalTypes.TRANSCRIPTION_DELETE: panel = <dialogs.TranscriptionDeleteDialog />; break;
        case modalTypes.TRANSLATION_NEW: panel = <dialogs.TranslationEditDialog />; break;
        case modalTypes.TRANSLATION_EDIT: panel = <dialogs.TranslationEditDialog editing />; break;
        case modalTypes.TRANSLATION_DELETE: panel = <dialogs.TranslationDeleteDialog />; break;
        default: panel = null;
    }

    return (
        <Modal
            show={showModal}
            close={() => dispatch(shouldShowQuizRecordEditModal(false))}
        >
            {panel}
        </Modal>
    );
};

export default QuizRecordEditModal;
