import { useEffect } from 'react';
import { useDispatch } from 'react-redux';

export const useActionOnMount = action => {
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(action);
    }, [dispatch]);
};

export const useAutofocus = ref => {
    useEffect(() => {
        if (ref)
            ref.current.focus();
    }, [ref]);
};
