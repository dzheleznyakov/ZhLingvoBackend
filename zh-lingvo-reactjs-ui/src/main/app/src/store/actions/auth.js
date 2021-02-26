import * as actionTypes from '../actionTypes/auth';

export const signIn = username => ({
    type: actionTypes.SIGN_IN,
    username,
});

export const signUp = username => ({
    type: actionTypes.SIGN_UP,
    username,
});

export const setUsername = username => ({
    type: actionTypes.SET_USERNAME,
    username,
})

export const clearUsername = () => ({
    type: actionTypes.CLEAR_USERNAME,
});

export const setLoginError = error => ({
    type: actionTypes.SET_LOGIN_ERROR,
    error,
})
