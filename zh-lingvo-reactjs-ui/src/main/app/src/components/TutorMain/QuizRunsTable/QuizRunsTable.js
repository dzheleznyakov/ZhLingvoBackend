import React from 'react';
import { useSelector } from 'react-redux';

import classes from './QuizRunsTable.module.scss';
import { Greeting } from '../../UI';
import { quizzesSelector, selectedQuizIndexSelector, selectedQuizSelector } from '../../../store/selectors';
import { useActionOnMount, useConditionalActionOnMount } from '../../../hooks';
import * as actions from '../../../store/actions';

const QuizRunsTable = () => {
    const selectedQuizIndex = useSelector(selectedQuizIndexSelector);
    const { id: quizId } = useSelector(selectedQuizSelector);

    useConditionalActionOnMount(actions.fetchAllQuizRuns(quizId), quizId != null, quizId);

    return (
        <div className={classes.QuizRunsTableWrapper}>
            <Greeting subtitle="Quiz runs:" />
            {selectedQuizIndex}
        </div>
    );
};

export default QuizRunsTable;
