import * as actionTypes from '../actionTypes/app';

export const setUsername = username => ({
    type: actionTypes.SET_USERNAME,
    username,
})

export const clearUsername = () => ({
    type: actionTypes.CLEAR_USERNAME,
});
