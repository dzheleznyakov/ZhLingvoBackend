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

export const createQuiz = (name, targetLanguage) => ({
    type: actionTypes.CREATE_QUIZ,
    name,
    targetLanguage,
});

export const updateQuiz = name => ({
    type: actionTypes.UPDATE_QUIZ,
    name,
});

export const deleteQuiz = id => ({
    type: actionTypes.DELETE_QUIZ,
    id,
});

export const fetchQuiz = id => ({
    type: actionTypes.FETCH_QUIZ,
    id,
});

export const fetchQuizStart = () => ({
    type: actionTypes.FETCH_QUIZ_START,
});

export const fetchQuizSuccess = quiz => ({
    type: actionTypes.FETCH_QUIZ_SUCCESS,
    quiz,
});

export const fetchQuizFailure = () => ({
    type: actionTypes.FETCH_QUIZ_FAIURE,
});
