import { useEffect } from 'react';
import { useDispatch } from 'react-redux';

import { setBreadcrumbs } from '../store/actions';

export const useBreadcrumbs = (...breadcrumbs) => {
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(setBreadcrumbs(breadcrumbs));
    }, [dispatch]);
};

export const useDynamicBreadcrumbs = (deps, ...breadcrumbs) => {
    const dispatch = useDispatch();
    const dependencies = [dispatch].concat(deps);
    useEffect(() => {
        dispatch(setBreadcrumbs(breadcrumbs));
    }, dependencies);
}
