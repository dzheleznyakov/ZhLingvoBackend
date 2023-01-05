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

export const fetchMatchingRegimes = () => ({
    type: actionTypes.FETCH_MATCHING_REGIMES,
});

export const fetchMatchingRegimesSuccess = matchingRegimes => ({
    type: actionTypes.FETCH_MATCHING_REGIMES_SUCCESS,
    matchingRegimes,
});

export const fetchQuizRegimes = () => ({
    type: actionTypes.FETCH_QUIZ_REGIMES,
});

export const fetchQuizRegimesSuccess = quizRegimes => ({
    type: actionTypes.FETCH_QUIZ_REGIMES_SUCCESS,
    quizRegimes,
});

export const fetchQuizSettings = quizId => ({
    type: actionTypes.FETCH_QUIZ_SETTINGS,
    quizId,
});

export const fetchQuizSettingsSuccess = (quizId, settings) => ({
    type: actionTypes.FETCH_QUIZ_SETTINGS_SUCCESS,
    quizId,
    settings,
});

export const updateQuizSettings = (quizId, settings) => ({
    type: actionTypes.UPDATE_QUIZ_SETTINGS,
    quizId,
    settings
});

export const updateQuizSettingsSuccess = (quizId, settings) => ({
    type: actionTypes.UPDATE_QUIZ_SETTINGS_SUCCESS,
    quizId,
    settings,
});

export const fetchAllQuizzesByLanguage = language => ({
    type: actionTypes.FETCH_ALL_QUIZZES_BY_LANGUAGE,
    language,
});

export const fetchAllQuizzesByLanguageStart = () => ({
    type: actionTypes.FETCH_ALL_QUIZZES_BY_LANGUAGE_START,
});

export const fetchAllQuizzesByLanguageSuccess = quizzes => ({
    type: actionTypes.FETCH_ALL_QUIZZES_BY_LANGUAGE_SUCCESS,
    quizzes,
});

export const fetchAllQuizzesByLanguageFailure = () => ({
    type: actionTypes.FETCH_ALL_QUIZZES_BY_LANGUAGE_FAILURE,
});

export const createQuizForMeaningToQuizRecord = (name, targetLanguage) => ({
    type: actionTypes.CREATE_QUIZ_FOR_MEANING_TO_QUIZ_RECORD,
    name,
    targetLanguage,
});

export const createQuizForMeaningToQuizRecordSuccess = quiz => ({
    type: actionTypes.CREATE_QUIZ_FOR_MEANING_TO_QUIZ_RECORD_SUCCESS,
    quiz,
});

export const navigateToQuiz = quizId => ({
    type: actionTypes.NAVIGATE_TO_QUIZ,
    quizId,
});
