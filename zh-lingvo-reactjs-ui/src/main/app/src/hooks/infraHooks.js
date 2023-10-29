import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { useHistory } from 'react-router-dom';

import { getCookie, setCookie } from '../utils/cookies';
import * as actions from '../store/actions';

export const useActionOnMount = (action, ...deps) => {
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(action);
    }, [dispatch].concat(deps));
};

export const useConditionalActionOnMount = (action, condition, ...deps) => {
    const dispatch = useDispatch();

    useEffect(() => {
        if (condition)
            dispatch(action);
    }, [dispatch].concat(deps));
}

export const useAutofocus = ref => {
    useEffect(() => {
        if (ref && ref.current)
            ref.current.focus();
    }, [ref]);
};

export const useLastVisitedPage = () => {
    const history = useHistory();
    const dispatch = useDispatch();
    useEffect(() => {
        const pathname = history.location.pathname;
        if (pathname === '/') {
          const lastVisited = getCookie('lastVisited');
          dispatch(actions.navigateTo(lastVisited));
        } else {
            setCookie('lastVisited', pathname);
        }
        dispatch(actions.finishStartingUp());
    }, []);
};

export const useModal = (show, onClosed, content, deps = []) => {
    const dispatch = useDispatch();
    useEffect(() => {
        if (show)
            dispatch(actions.setModal(onClosed, content));
        else
            dispatch(actions.clearModal());
    }, [show, ...deps]);
};
