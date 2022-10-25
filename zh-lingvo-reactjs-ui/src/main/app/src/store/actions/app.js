import * as actionTypes from '../actionTypes/app';

export const finishStartingUp = () => ({
    type: actionTypes.FINISH_STARTING_UP,
});

export const showSidebar = show => ({
    type: actionTypes.SHOW_SIDEBAR,
    show,
});

export const addError = (error, descr) => ({
    type: actionTypes.ADD_ERROR,
    error,
    descr,
});

export const navigateTo = url => ({
    type: actionTypes.NAVIGATE_TO,
    url,
});

export const setApp = app => ({
    type: actionTypes.SET_APP,
    app,
})

export const fetchAllLanguages = () => ({
    type: actionTypes.FETCH_ALL_LANGUAGES,
});

export const fetchAllLanguagesSuccess = languages => ({
    type: actionTypes.FETCH_ALL_LANGUAGES_SUCCESS,
    languages,
});

export const fetchPos = lang => ({
    type: actionTypes.FETCH_POS,
    lang,
});

export const setPos = posList => ({
    type: actionTypes.SET_POS,
    posList,
});
