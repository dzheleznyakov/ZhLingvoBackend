import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import _ from 'lodash';

import QuizRunner from './QuizRunner/QuizRunner';
import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';

const NULL_QUIZ = { id: -1, name: '', targetLanguage: {}};

export const useQuizRunner = () => {
    const records = useSelector(selectors.allQuizRecordsSelector);
    const quiz = useSelector(selectors.loadedQuizSelector) || NULL_QUIZ;
    const quizSettings = useSelector(selectors.quizSettingsSelector)[quiz.id];
    const [currentQuizRunner, setQuizRunner] = useState();
    const { qid: quizId, runid: quizRunId } = useParams();
    const fetchedQuizRun = useSelector(selectors.quizRunSelector);
    const dispatch = useDispatch();

    const dataIsFetched = 
        records && records.length > 0 
        && quiz && quiz.id >= 0 
        && quizSettings != null && quizSettings.quizId >= 0;

    useEffect(() => {
        if (quizRunId == null && currentQuizRunner == null && !dataIsFetched) {
            dispatch(actions.fetchQuizRunData(quizId));
        } else if (quizRunId == null && currentQuizRunner == null && dataIsFetched) {
            const quizRunner = buildNewQuizRunner(quizSettings, records);
            quizRunner.initRun();
            setQuizRunner(quizRunner);
            dispatch(actions.createQuizRun(quizRunner.toQuizRun(), quizId));
        } else if (quizRunId != null && currentQuizRunner != null && currentQuizRunner.id == null && dataIsFetched) {
            currentQuizRunner.setId(quizRunId);
            setQuizRunner(currentQuizRunner);
        } else if (quizRunId != null && currentQuizRunner == null && !dataIsFetched) {
            dispatch(actions.fetchQuizRun(quizId, quizRunId));
        } else if (quizRunId != null && currentQuizRunner == null && dataIsFetched) {
            const quizRunner = QuizRunner.fromQuizRun(
                fetchedQuizRun, 
                { records: getQuizRunnerRecords(records) },
            );
            setQuizRunner(quizRunner);
        }
    }, [
        quizId,
        quizRunId,
        currentQuizRunner,
        dataIsFetched,
        dispatch,
        quizSettings,
        records,
    ]);

    return currentQuizRunner;
};

function buildNewQuizRunner(quizSettings, records) {
    const quiz = _.cloneDeep(quizSettings)
    quiz.id = quiz.quizId;
    delete quiz.quizId;
    quiz.records = getQuizRunnerRecords(records);
    return QuizRunner.fromQuiz(quiz);
}

function getQuizRunnerRecords(records = []) {
    return records.map(record => ({
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
}