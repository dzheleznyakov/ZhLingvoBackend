import * as actionTypes from '../actionTypes/app';

const initialState = {
    showSidebar: false,
};

const showSidebar = (state, action) => ({
    ...state,
    showSidebar: action.show,
});

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SHOW_SIDEBAR: return showSidebar(state, action);
        default: return state;
    }
};

export default reducer;
