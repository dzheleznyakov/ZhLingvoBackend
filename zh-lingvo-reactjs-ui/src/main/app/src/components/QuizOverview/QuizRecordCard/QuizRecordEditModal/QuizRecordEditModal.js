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
