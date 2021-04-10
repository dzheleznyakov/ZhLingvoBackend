import * as actionTypes from '../actionTypes/app';
import { SIGN_OUT } from '../actionTypes';

const initialState = {
    showSidebar: false,
    errors: [],
    navigationDestination: null,
};

const gen = (function* idGen() {
    let id = 1;
    while (true) {
        yield id;
        ++id;
    }
})();
const nextId = () => gen.next().value;

const showSidebar = (state, action) => ({
    ...state,
    showSidebar: action.show,
});

const addError = (state, action) => {
    const updatedErrors = [].concat(state.errors);
    updatedErrors.push({
        id: nextId(),
        ...action,
    });
    return {
        ...state,
        errors: updatedErrors,
    }
};

const navigateTo = (state, action) => ({
    ...state,
    navigationDestination: action.url,
});

const signOut = (state) => ({
    ...state,
    showSidebar: false,
    errors: [],
    navigationDestination: null,
});

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SHOW_SIDEBAR: return showSidebar(state, action);
        case actionTypes.ADD_ERROR: return addError(state, action);
        case actionTypes.NAVIGATE_TO: return navigateTo(state, action);
        case SIGN_OUT: return signOut(state, action);
        default: return state;
    }
};

export default reducer;
