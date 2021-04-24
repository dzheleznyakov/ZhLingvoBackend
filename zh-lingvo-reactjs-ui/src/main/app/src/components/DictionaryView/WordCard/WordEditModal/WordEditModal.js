import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { Modal } from '../../../UI';
import * as selectors from '../../../../store/selectors';
import * as actions from '../../../../store/actions';
import * as modalTypes from '../../../../static/constants/wordEditModalTypes';
import * as dialogs from '../Dialogs';

const WordEditModal = () => {
    const showWordEditModal = useSelector(selectors.shouldShowWordEditModalSelector);
    const wordEditModalType = useSelector(selectors.wordEditModalTypeSelector);
    const dispatch = useDispatch();

    let panel;
    switch (wordEditModalType) {
        case modalTypes.MAIN_FORM: panel = <dialogs.MainFormDialog />; break;
        case modalTypes.TRANSCRIPTION_NEW: panel = <dialogs.TranscriptionEditDialog />; break;
        case modalTypes.TRANSCRIPTION_EDIT: panel = <dialogs.TranscriptionEditDialog editting />; break;
        case modalTypes.TRANSCRIPTION_DELETE: panel = <dialogs.TranscriptionDeleteDialog />; break;
        default: panel = null;
    }

    return (
        <Modal 
            show={showWordEditModal}
            close={() => dispatch(actions.shouldShowWordEditModal(false))}
        >
            {panel}
        </Modal>
    );
};

export default WordEditModal;
