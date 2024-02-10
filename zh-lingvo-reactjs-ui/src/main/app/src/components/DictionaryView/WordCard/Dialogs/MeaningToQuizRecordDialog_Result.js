import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classes from './MeaningToQuizRecordDialog.module.scss';

import { actionButtonTypes, DialogBase, Spinner } from '../../../UI';
import { 
    convertingMeaningToQuizRecordSelector,
    meaningToQuizRecordSelector,
    wordEditPathSelector,
    meaningToConvertToQuizRecordSelector,
} from '../../../../store/selectors';
import { useActionOnMount } from '../../../../hooks';
import * as actions from '../../../../store/actions';

const MeaningToQuizRecordDialog_Result = () => {
    const path = useSelector(wordEditPathSelector);
    const meaning = useSelector(meaningToConvertToQuizRecordSelector);
    const converting = useSelector(convertingMeaningToQuizRecordSelector);
    const { targetQuiz } = useSelector(meaningToQuizRecordSelector);
    const dispatch = useDispatch();

    useActionOnMount(actions.convertMeaningToQuizRecord(meaning));

    let body = (
        <div className={classes.Spinner}>
            <Spinner />
        </div>
    );
    if (!converting) {
        body = <div>New quiz record is created. Do you want to go to the quiz?</div>
    }

    const onOk = () => dispatch(actions.navigateToQuiz(targetQuiz.id));
    const onNo = () => dispatch(actions.shouldShowWordEditModal(false));
    const { CONFIRM, CANCEL } = actionButtonTypes;
    const buttons = [
        { type: CONFIRM, onClicked: onOk, disabled: converting, label: 'OK' },
        { type: CANCEL, onClicked: onNo, disabled: converting, label: 'No' },
    ];

    return (
        <DialogBase buttons={buttons}>
            {body}
        </DialogBase>
    );
};

export default MeaningToQuizRecordDialog_Result;
