import React from 'react';

import classes from './TutorMain.module.scss';

import { useUsername } from '../../hooks';
import QuizzesTable from './QuizzesTable/QuizzesTable';
import { Greeting } from '../UI';

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
