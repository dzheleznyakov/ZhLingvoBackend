import React from 'react';

import classes from './DictionaryMain.module.scss';

import Greeting from '../Common/Greeting/Greeting';
import DictionariesTable from './DictionariesTable/DictionariesTable';
import { useUsername } from '../../hooks';

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
