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
