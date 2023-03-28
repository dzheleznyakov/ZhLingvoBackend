import _ from 'lodash';

import quizRegimes from '../../../../../static/constants/quizRegimes';
import matchingRegimes from '../../../../../static/constants/matchingRegimes';

const quizRecord1 = {
    id: 42,
    wordMainForm: 'word',
    translations: ['abcd', 'bcde', 'cdef'],
    currentScore: 0.0,
    numberOfRuns: 5,
    numberOfSuccesses: 1,
};

const quizRecord2 = {
    id: 52,
    wordMainForm: 'main',
    translations: ['defg', 'dfgh', 'fghi'],
    currentScore: 0.3,
    numberOfRuns: 15,
    numberOfSuccesses: 5,
};

const quizRecord3 = {
    id: 1000,
    wordMainForm: 'form',
    translations: ['ghij', 'hijk', 'ijkl'],
    currentScore: 0.9,
    numberOfRuns: 15,
    numberOfSuccesses: 10,
};

const quizRecord4 = {
    id: 1001,
    wordMainForm: 'quiz',
    translations: ['jklm', 'klmn', 'lmno'],
    currentScore: 1,
    numberOfRuns: 15,
    numberOfSuccesses: 10,
};

export const QUIZ_RECORDS = () => _.cloneDeep([
    quizRecord1,
    quizRecord2,
    quizRecord3,
    quizRecord4,
]);

const quiz = {
    id: 101,
    maxScore: 10,
    quizRegime: quizRegimes.FORWARD,
    matchingRegime: matchingRegimes.STRICT,
    records: QUIZ_RECORDS(),
};

export const QUIZ = () => _.cloneDeep(quiz);
