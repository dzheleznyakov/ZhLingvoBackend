import React from 'react';
import { useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';
import { ControlBox, MODAL_TYPES } from '../../../UI';

import DeleteRecordDialog from './DeleteRecordDialog/DeleteRecordDialog';
import NewRecordDialog from './NewRecordDialog/NewRecordDialog';
import QuizSettingsDialog from './QuizSettingsDialog/QuizSettingsDialog';

import * as actions from '../../../../store/actions';

const RecordListControl = () => {
    const { rid: recordId, qid: quizId } = useParams();
    const noRecordSelected = recordId === null || recordId === undefined;

    const dispatch = useDispatch();

    return <ControlBox
        panelKeyPrefix="record_list_control-"
        disabled={noRecordSelected}
        panels={[
            {
                modalType: MODAL_TYPES.NEW,
                panel: NewRecordDialog,
                panelProps: { quizId },
                disabled: false,
            },
            {
                modalType: MODAL_TYPES.SETTINGS,
                panel: QuizSettingsDialog,
                panelProps: { quizId },
                disabled: false,
            },
            {
                modalType: MODAL_TYPES.DELETE,
                panel: DeleteRecordDialog,
                panelProps: { quizId, recordId },
            },
            {
                modalType: MODAL_TYPES.PLAY,
                clicked: () => dispatch(actions.navigateTo(`/quiz/${quizId}/run`)),
                disabled: false,
            }
        ]}
    />;
};

export default RecordListControl;
