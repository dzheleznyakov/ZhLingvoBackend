import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { selectedQuizSelector } from '../../../../store/selectors';
import { TUTOR_QUIZ as quizUrlPattern } from '../../../../static/constants/paths';
import * as actions from '../../../../store/actions';
import NewQuizDialog from './NewQuizDialog/NewQuizDialog';
import EditQuizDialog from './EditQuizDialog/EditQuizDialog';
import DeleteQuizDialog from './DeleteQuizDialog/DeleteQuizDialog';
import ControlBox, { MODAL_TYPES } from '../../../Common/ControlBox/ControlBox';
import QuizSettingsDialog from './QuizSettingsDialog/QuizSettingsDialog';

const TableControl = () => {
    const selectedQuiz = useSelector(selectedQuizSelector);
    const quizIsSelected = selectedQuiz.id !== null && selectedQuiz.id !== undefined;
    const dispatch = useDispatch();

    const onForward = () => {
        if (!quizIsSelected) return;
        const path = quizUrlPattern.replace(/\/:\w+/g, (param) => {
            switch (param) {
                case '/:qid': return `/${selectedQuiz.id}`;
                default: return '';
            }
        });
        dispatch(actions.navigateTo(path));
    };

    return <ControlBox 
        panelKeyPrefix="quizzed_table_control-"
        disabled={!quizIsSelected}
        panels={[
            {
                modalType: MODAL_TYPES.NEW,
                panel: NewQuizDialog,
                disabled: false,
            },
            {
                modalType: MODAL_TYPES.SETTINGS,
                panel: QuizSettingsDialog,
            },
            {
                modalType: MODAL_TYPES.EDIT,
                panel: EditQuizDialog,
            },
            {
                modalType: MODAL_TYPES.DELETE,
                panel: DeleteQuizDialog,
            },
            {
                modalType: MODAL_TYPES.FORWARD,
                clicked: onForward,
            },
        ]}
    />;
};

export default TableControl;
