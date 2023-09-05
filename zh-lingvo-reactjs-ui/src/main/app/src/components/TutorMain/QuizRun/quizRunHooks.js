import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import _ from 'lodash';

import QuizRunner from './QuizRunner/QuizRunner';
import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';

export const useQuizRunner = (quizSettings, records) => {
    const [currentQuizRunner, setQuizRunner] = useState();
    const { qid: quizId, runid: quizRunId } = useParams();
    const fetchedQuizRun = useSelector(selectors.quizRunSelector);
    const dispatch = useDispatch();

    useEffect(() => {
        if (quizRunId == null && quizSettings != null && currentQuizRunner == null && records.length > 0) {
            const quizRunner = buildQuizRunner(quizSettings, records);
            quizRunner.initRun();
            setQuizRunner(quizRunner);
            dispatch(actions.createQuizRun(quizRunner.toQuizRun(), quizId));
        } else if (quizRunId != null && currentQuizRunner && currentQuizRunner.id == null) {
            currentQuizRunner.setId(quizRunId);
        } else if (quizRunId != null && currentQuizRunner == null && fetchedQuizRun == null) {
            dispatch(actions.fetchQuizRun(quizId, quizRunId));
        } else if (quizRunId != null && currentQuizRunner == null && fetchedQuizRun != null) {
            const quizRunner = QuizRunner.fromQuizRun(fetchedQuizRun, { records });
            setQuizRunner(quizRunner);
        }
    }, [quizSettings, currentQuizRunner, records, quizId, quizRunId, dispatch, fetchedQuizRun]);

    return currentQuizRunner;
};

function buildQuizRunner(quizSettings, records) {
    const quiz = _.cloneDeep(quizSettings)
    quiz.id = quiz.quizId;
    delete quiz.quizId;
    quiz.records = records.map(record => ({
        id: record.id,
        wordMainForm: record.wordMainForm,
        currentScore: record.currentScore,
        numberOfRuns: record.numberOfRuns,
        numberOfSuccesses: record.numberOfSuccesses,
        translations: record.translations.map(({ value, elaboration }) => {
            const result = [value];
            if (elaboration)
                result.push(elaboration);
            return result
                .join(' ')
                .replaceAll(/,/g, '');
        }),
    }));
    return QuizRunner.fromQuiz(quiz);
}