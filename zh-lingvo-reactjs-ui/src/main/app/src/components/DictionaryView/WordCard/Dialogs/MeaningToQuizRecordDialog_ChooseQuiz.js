import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classes from './MeaningToQuizRecordDialog.module.scss';

import { useActionOnMount } from '../../../../hooks';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';
import { actionButtonTypes, DialogBase, ListView, Spinner } from '../../../UI';
import { 
    MEANING_TO_QUIZ_RECORD__CONFIRM_RECORD, 
    MEANING_TO_QUIZ_RECORD__NEW_QUIZ,
    MEANING_TO_QUIZ_RECORD__RESULT, 
} from '../../../../static/constants/wordEditModalTypes';

const MeaningToQuizRecordDialog_ChooseQuiz = () => { 
    const { language } = useSelector(selectors.loadedDictionarySelector);
    const { loadingQuizzes, quizzes } = useSelector(selectors.meaningToQuizRecordSelector);
    const [selectedQuizIndex, setSelectedQuizIndex] = useState(-1);
    const path = useSelector(selectors.wordEditPathSelector);
    const dispatch = useDispatch();

    useActionOnMount(actions.fetchAllQuizzesByLanguage(language));

    let body = (
        <div className={classes.Spinner}>
            <Spinner />
        </div>
    );
    if (!loadingQuizzes) {
        const items = quizzes.map(({ name }) =>({ key: name, node: name }));
        body = <ListView
            items={items}
            onItemClick={i => () => setSelectedQuizIndex(i)}
            selectedIndex={selectedQuizIndex}
            height="20vh"
        />;
    }

    const onBack = () => dispatch(actions.setWordEditModalType(MEANING_TO_QUIZ_RECORD__CONFIRM_RECORD, path));
    const onConfirm = () => {
        const selectedQuiz = quizzes[selectedQuizIndex];
        dispatch(actions.createQuizForMeaningToQuizRecordSuccess(selectedQuiz));
        dispatch(actions.setWordEditModalType(MEANING_TO_QUIZ_RECORD__RESULT, path))
    };
    const onNew = () => dispatch(actions.setWordEditModalType(MEANING_TO_QUIZ_RECORD__NEW_QUIZ, path));
    const onClosed = () => dispatch(actions.shouldShowWordEditModal(false));
    const { CONFIRM, CANCEL } = actionButtonTypes
    const buttons = [
        { type: CANCEL, onClicked: onBack, label: 'Back'},
        { type: CONFIRM, onClicked: onConfirm, disabled: selectedQuizIndex < 0, label: 'OK' },
        { type: CONFIRM, onClicked: onNew, disabled: loadingQuizzes, label: 'New' },
        { type: CANCEL, onClicked: onClosed, label: 'Cancel' },
    ];

    return (
        <DialogBase buttons={buttons}>
            <h2>{language.name} quizzes:</h2>
            {body}
        </DialogBase>
    );
};

export default MeaningToQuizRecordDialog_ChooseQuiz;
