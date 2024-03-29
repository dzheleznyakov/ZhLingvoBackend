import React from 'react';

import classes from './QuizRecordExample.module.scss';

import { quizRecordExampleType } from '../../types';
import { Remark } from '../../../../DictionaryView/WordCard/WordView/SubWordParts';

const QuizRecordExample = props => {
    const { entry: example } = props;
    const { remark, expression, explanation } = example;

    return (
        <div className={classes.ExampleWrapper}>
            {remark && <Remark value={remark} />}
            <span className={classes.Example}>
                {expression} - {explanation}
            </span>
        </div>
    );
};

QuizRecordExample.propTypes = {
    entry: quizRecordExampleType.isRequired,
};

export default QuizRecordExample;
