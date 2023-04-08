import { useEffect, useState } from 'react';
import _ from 'lodash';
import QuizRunner from './QuizRunner/QuizRunner';

export const useQuizRunner = (quizSettings, records) => {
    const [quizRunner, setQuizRunner] = useState();

    useEffect(() => {
        if (quizSettings != null && quizRunner == null && records.length > 0) {
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
                    return result;
                }),
            }));
            const qr = QuizRunner.fromQuiz(quiz);
            qr.initRun();
            setQuizRunner(qr);
        }
    }, [quizSettings, quizRunner, records]);

    return quizRunner;
};