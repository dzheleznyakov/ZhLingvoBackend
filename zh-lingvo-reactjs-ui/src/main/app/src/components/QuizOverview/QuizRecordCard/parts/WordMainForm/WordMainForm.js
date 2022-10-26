import React from 'react';
import PropTypes from 'prop-types';

import classes from './WordMainForm.module.scss';

const WordMainForm = props => {
    const { children: mainForm } = props;
    return (
        <div className={classes.WordMainForm}>
            {mainForm}
        </div>
    );
};

WordMainForm.propTypes = {
    children: PropTypes.string.isRequired,
};

export default WordMainForm;
