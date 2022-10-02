import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classes from './AppChooser.module.scss';

import { appSelector } from '../../../../store/selectors';
import * as actions from '../../../../store/actions';
import * as APPS from '../../../../static/constants/apps';

const AppChooser = () => {
    const app = useSelector(appSelector) || APPS.DICTIONARY;
    const dispatch = useDispatch();

    const onAppChange = event => {
        const app = event.target.value;
        dispatch(actions.setApp(app));
    }

    return (
        <select className={classes.AppChooser} defaultValue={app} onChange={onAppChange}>
            <option>{APPS.DICTIONARY}</option>
            <option>{APPS.TUTOR}</option>
        </select>
    );
};

export default AppChooser;
