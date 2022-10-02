import { useEffect } from 'react';
import { useDispatch } from 'react-redux';

export const useActionOnMount = action => {
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(action);
    }, [dispatch]);
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
