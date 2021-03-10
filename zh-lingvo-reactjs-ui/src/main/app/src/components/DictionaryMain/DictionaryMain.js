import React from 'react';

import classes from './DictionaryMain.module.scss';

import DictionariesTable from './DictionariesTable/DictionariesTable';
import { useUsername } from '../../hooks';

const DictionaryList = () => {
    const username = useUsername();

    return (
        <div className={classes.DictionaryListWrapper}>
            <h1>Welcome, {username}!</h1>
            <h2>Your dictionaries:</h2>
            <DictionariesTable />
        </div>
    );
};

export default DictionaryList;
