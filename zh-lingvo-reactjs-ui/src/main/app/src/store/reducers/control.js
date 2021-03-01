import * as actionTypes from '../actionTypes/control';

const initialState = {
    breadcrumbs: [],
};

const setBreadcrumbs = (state, action) => ({ ...state, breadcrumbs: action.breadcrumbs });

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SET_BREADCRUMBS: return setBreadcrumbs(state, action);
        default: return state;
    }
};

export default reducer;
