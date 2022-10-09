import * as actionTypes from '../actionTypes/app';
import { SIGN_OUT } from '../actionTypes';

const initialState = {
    isStartingUp: true,
    showSidebar: false,
    errors: [],
    navigationDestination: null,
    app: null,
    languages: [],
};

const gen = (function* idGen() {
    let id = 1;
    while (true) {
        yield id;
        ++id;
    }
})();
const nextId = () => gen.next().value;

const finishStartingUp = state => ({
    ...state,
    isStartingUp: false,
});

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

const setApp = (state, action) => ({
    ...state,
    app: action.app,
});

const fetchAllLanguages = (state, action) => ({
    ...state,
    languages: action.languages || [],
});

const signOut = (state) => ({
    ...state,
    showSidebar: false,
    errors: [],
    navigationDestination: null,
});

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FINISH_STARTING_UP: console.log(state); return finishStartingUp(state, action);
        case actionTypes.SHOW_SIDEBAR: return showSidebar(state, action);
        case actionTypes.ADD_ERROR: return addError(state, action);
        case actionTypes.NAVIGATE_TO: return navigateTo(state, action);
        case actionTypes.SET_APP: return setApp(state, action);
        case actionTypes.FETCH_ALL_LANGUAGES_SUCCESS: return fetchAllLanguages(state, action);
        case SIGN_OUT: return signOut(state, action);
        default: return state;
    }
};

export default reducer;
