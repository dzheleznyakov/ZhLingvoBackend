import * as actionTypes from '../actionTypes/auth';

export const signIn = username => ({
    type: actionTypes.SIGN_IN,
    username,
});

export const signUp = username => ({
    type: actionTypes.SIGN_UP,
    username,
});

export const autoSignIn = () => ({
    type: actionTypes.AUTO_SIGN_IN,
});

export const setUsername = username => ({
    type: actionTypes.SET_USERNAME,
    username,
})

export const signOut = () => ({
    type: actionTypes.SIGN_OUT,
});

export const setLoginError = error => ({
    type: actionTypes.SET_LOGIN_ERROR,
    error,
})
