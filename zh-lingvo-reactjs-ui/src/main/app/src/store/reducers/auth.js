import * as actionTypes from '../actionTypes/auth';

const initialState = {
    username: null,
    error: null,
};

const setUsername = (state, action) => ({ ...state, username: action.username, error: null });

const clearUsername = (state, action) => ({ ...state, username: null });

const setLoginError = (state, action) => ({ ...state, error: action.error });

const reducer = (state = initialState, action) => {
    switch(action.type) {
        case actionTypes.SET_USERNAME: return setUsername(state, action);
        case actionTypes.CLEAR_USERNAME: return clearUsername(state, action);
        case actionTypes.SET_LOGIN_ERROR: return setLoginError(state, action);
        default: return state;
    }
};

export default reducer;
