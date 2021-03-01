import * as actionTypes from '../actionTypes/control';

export const setBreadcrumbs = breadcrumbs => ({
    type: actionTypes.SET_BREADCRUMBS,
    breadcrumbs,
});
