import * as actionTypes from '../actionTypes/app';

const initialState = {
    username: null,
};

const setUsername = (state, action) => ({ ...state, username: action.username });

const clearUsername = (state, action) => ({ ...state, username: null });

const reducer = (state = initialState, action) => {
    switch(action.type) {
        case actionTypes.SET_USERNAME: return setUsername(state, action);
        case actionTypes.CLEAR_USERNAME: return clearUsername(state, action);
        default: return state;
    }
};

export default reducer;
