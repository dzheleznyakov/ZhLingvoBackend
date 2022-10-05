import * as actionTypes from '../actionTypes/app';

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
