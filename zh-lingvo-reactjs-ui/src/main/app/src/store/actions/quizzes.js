import * as actionTypes from '../actionTypes/quizzes';

export const fetchAllQuizzes = () => ({
    type: actionTypes.FETCH_ALL_QUIZZES,
});

export const fetchAllQuizzesStart = () => ({
    type: actionTypes.FETCH_ALL_QUIZZES_START,
});

export const fetchAllQuizzesSuccess = quizzes => ({
    type: actionTypes.FETCH_ALL_QUIZZES_SUCCESS,
    quizzes,
});

export const fetchAllQuizzesFailure = () => ({
    type: actionTypes.FETCH_ALL_QUIZZES_FAILURE,
});

export const selectQuiz = index => ({
    type: actionTypes.SELECT_QUIZ,
    index,
});
