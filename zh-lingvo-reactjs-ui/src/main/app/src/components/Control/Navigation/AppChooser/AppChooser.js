import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';

import classes from './AppChooser.module.scss';

import { appSelector } from '../../../../store/selectors';
import * as actions from '../../../../store/actions';
import * as APPS from '../../../../static/constants/apps';
import { getApp } from '../../../../utils/appUtils';

const AppChooser = () => {
    const app = useSelector(appSelector);
    const dispatch = useDispatch();
    const history = useHistory();
    const pathname = history.location.pathname;

    useEffect(() => {
        const newApp = getApp(pathname);
        if (app !== newApp)
            dispatch(actions.setApp(newApp));
    }, [pathname]); // eslint-disable-line react-hooks/exhaustive-deps

    const onAppChange = event => {
        const newApp = event.target.value;
        dispatch(actions.setApp(newApp));
    }

    const options = Object.keys(APPS)
        .map(key => <option key={key}>{APPS[key]}</option>)

    return (
        <select className={classes.AppChooser} onChange={onAppChange} value={app || ''}>
            {options}
        </select>
    );
};

export default AppChooser;
