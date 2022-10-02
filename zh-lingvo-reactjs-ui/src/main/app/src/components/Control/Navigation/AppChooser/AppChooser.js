import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';

import classes from './AppChooser.module.scss';

import { appSelector } from '../../../../store/selectors';
import * as actions from '../../../../store/actions';
import * as APPS from '../../../../static/constants/apps';
import * as paths from '../../../../static/constants/paths';

const AppChooser = () => {
    let app = useSelector(appSelector);
    const dispatch = useDispatch();

    const history = useHistory();
    if (app === null) {
        const pathname = history.location.pathname;
        if (pathname.match(`${paths.DICTIONARIES_ROOT}.*`))
            app = APPS.DICTIONARY;
        else if (pathname.match(`${paths.TUTOR_ROOT}.*`))
            app = APPS.TUTOR;
        else
            app = APPS.DICTIONARY;
    }

    const onAppChange = event => {
        const newApp = event.target.value;
        dispatch(actions.setApp(newApp));
    }

    return (
        <select className={classes.AppChooser} defaultValue={app} onChange={onAppChange}>
            <option>{APPS.DICTIONARY}</option>
            <option>{APPS.TUTOR}</option>
        </select>
    );
};

export default AppChooser;
