import React from 'react';
import PropTypes from 'prop-types';

import classes from './WordCard.module.scss';

const WordCard = props => {
    const { wordMainForm } = props;
    return wordMainForm ? (
        <div className={classes.WordCardWrapper}>
            <div className={classes.WordMainForm}>{wordMainForm}</div>
        </div>
    ) : null;
};

WordCard.propTypes = {
    wordMainForm: PropTypes.string,
};

export default WordCard;
