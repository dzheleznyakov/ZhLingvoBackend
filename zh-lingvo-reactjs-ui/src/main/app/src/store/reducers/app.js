import * as actionTypes from '../actionTypes/app';

const initialState = {
    showSidebar: false,
    errors: [],
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

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SHOW_SIDEBAR: return showSidebar(state, action);
        case actionTypes.ADD_ERROR: return addError(state, action);
        default: return state;
    }
};

export default reducer;
