import React, { useEffect } from 'react';
import { useHistory } from 'react-router';
import { useSelector } from 'react-redux';

import { navigationDestinationSelector } from '../../store/selectors';

const RouterHelper = () => {
    const url = useSelector(navigationDestinationSelector);
    const history = useHistory();

    useEffect(() => {
        url && history.push(url);
    }, [history, url])

    return <div id="router-helper" />;
};

export default RouterHelper;
