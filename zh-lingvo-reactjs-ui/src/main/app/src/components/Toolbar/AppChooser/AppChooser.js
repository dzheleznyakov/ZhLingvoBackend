import React from 'react';

import classes from './AppChooser.module.scss';

const APPS = {
    DICTIONARY: 'Dictionary',
    TUTOR: 'Tutor',
};

const AppChooser = () => {
    return (
        <select className={classes.AppChooser} defaultValue={APPS.DICTIONARY}>
            <option>{APPS.DICTIONARY}</option>
            <option>{APPS.TUTOR}</option>
        </select>
    );
};

export default AppChooser;
