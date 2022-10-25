import React from 'react';

import classes from './DictionaryMain.module.scss';

import DictionariesTable from './DictionariesTable/DictionariesTable';
import { useUsername } from '../../hooks';
import { Greeting } from '../UI';

const DictionaryList = () => {
    const username = useUsername();

    return (
        <div className={classes.DictionaryListWrapper}>
            <Greeting 
                title={`Welcome, ${username}!`} 
                subtitle="Your dictionaries:"
            />
            <DictionariesTable />
        </div>
    );
};

export default DictionaryList;
