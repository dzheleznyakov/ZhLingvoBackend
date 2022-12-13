import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';

import classes from './QuizRecordControl.module.scss';

import { ActionButton, actionButtonTypes, ControlBox, MODAL_TYPES } from '../../../UI';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const QuizRecordControl = () => {
    const { qid } = useParams();
    const quizId = +qid;
    const dispatch = useDispatch();
    const isEditing = useSelector(selectors.quizRecordIsEditingSelector);

    if (!isEditing) 
        return <ControlBox
            panelKeyPrefix="quiz_record_view_control-"
            disabled={false}
            panels={[
                {
                    modalType: MODAL_TYPES.EDIT,
                    clicked: () => dispatch(actions.setQuizRecordEditing(true)),
                },
            ]}
        />;
    
    const okButton = (
        <ActionButton 
            type={actionButtonTypes.CONFIRM} 
            clicked={() => dispatch(actions.updateQuizRecord(quizId))}
        >
            OK
        </ActionButton>
    );
    const cancelButton = (
        <ActionButton 
            type={actionButtonTypes.CANCEL} 
            clicked={() => dispatch(actions.setQuizRecordEditing(false))}
        >
            Cancel
        </ActionButton>
    );

    return (
        <div className={classes.EditingButtonBox}>
            {okButton}
            {cancelButton}
        </div>
    )
};

export default QuizRecordControl;
