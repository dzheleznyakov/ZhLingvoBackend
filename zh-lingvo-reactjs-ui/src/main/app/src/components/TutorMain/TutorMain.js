import React from 'react';

import classes from './TutorMain.module.scss';

import { useUsername } from '../../hooks';
import Greeting from '../Common/Greeting/Greeting';
import QuizzesTable from './QuizzesTable/QuizzesTable';

const TutorMain = () => {
    const username = useUsername();
    return (
        <div className={classes.TutorListWrapper}>
            <Greeting 
                title={`Welcome, ${username}!`}
                subtitle="Your quizzes:"
            />
            <QuizzesTable />
        </div>
    );
};

export default TutorMain;
