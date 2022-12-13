import React from 'react';
import { useParams } from 'react-router';
import { useSelector } from 'react-redux';

import classes from './QuizRecordCard.module.scss';

import * as selectors from '../../../store/selectors';
import * as actions from '../../../store/actions';
import { useConditionalActionOnMount } from '../../../hooks';
import { Spinner } from '../../UI';
import { WordMainForm } from './parts';
import QuizRecordView from './QuizRecordView/QuizRecordView';
import QuizRecordControl from './QuizRecordControl/QuizRecordControl';
import QuizRecordEditModal from './QuizRecordEditModal/QuizRecordEditModal';

const NULL_QUIZ_RECORD = {};

const QuizRecordCard = () => {
    const { qid, rid } = useParams();
    const quizId = +qid;
    const recordId = +rid;
    const quizIsLoading = useSelector(selectors.quizIsLoadingSelector);
    const loadedQuizRecord = useSelector(selectors.loadedQuizRecordSelector) || NULL_QUIZ_RECORD;
    const updatedQuizRecord = useSelector(selectors.updatedQuizRecordSelector) || NULL_QUIZ_RECORD;
    const isEditing = useSelector(selectors.quizRecordIsEditingSelector);
    
    const quizRecord = isEditing ? updatedQuizRecord : loadedQuizRecord;
    const { wordMainForm } = quizRecord;

    useConditionalActionOnMount(
        actions.fetchQuizRecord(quizId, recordId),
        !Number.isNaN(quizId) && !Number.isNaN(recordId),
        quizId, recordId);

    switch (true) {
        case quizIsLoading: return <Spinner />;
        case !!rid && !!wordMainForm: return (
            <div className={classes.QuizRecordCardWrapper}>
                <div className={classes.QuizRecordViewWrapper}>
                    <WordMainForm>{wordMainForm}</WordMainForm>
                {quizRecord !== NULL_QUIZ_RECORD && <QuizRecordView quizRecord={quizRecord} />}
                </div>
            {quizRecord !== NULL_QUIZ_RECORD && <QuizRecordControl />}
            <QuizRecordEditModal />
            </div>
        );
        default: return null;
    }
};

export default QuizRecordCard;
