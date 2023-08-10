import React from 'react';
import PropTypes from 'prop-types';

import classes from './RecordListItem.module.scss';
import Dotdotdot from 'react-dotdotdot';

const RecordListItem = props => {
    const { quizRecord } = props;
    const { translations = [], wordMainForm, currentScore } = quizRecord;
    const hint = translations.map(({ value }) => value).join(', ')
    const currentScorePct = Math.round(currentScore * 100);
    return (
        <>
            <div className={classes.MainWrapper}>
                <span>{wordMainForm}</span>
                <span className={classes.CurrentScore}>{currentScorePct}%</span>
            </div>
            <div className={classes.Hint}>
                <Dotdotdot clamp={2}>{hint}</Dotdotdot>
            </div>
        </>
    );
};

RecordListItem.propTypes = {
    quizRecord: PropTypes.shape({
        wordMainForm: PropTypes.string.isRequired,
        translations: PropTypes.arrayOf(PropTypes.shape({
            value: PropTypes.string.isRequired,
        })),
    }).isRequired,
};

export default RecordListItem;
