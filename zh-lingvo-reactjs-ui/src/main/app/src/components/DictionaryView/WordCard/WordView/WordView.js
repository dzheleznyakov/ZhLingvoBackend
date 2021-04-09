import React from 'react';
import PropTypes from 'prop-types';

import classes from './WordView.module.scss';

const WordView = props => {
    return (
        <div>Word View
            <div>{JSON.stringify(props)}</div>
        </div>
    );
};

WordView.propTypes = {};

WordView.defaultProps = {};

export default WordView;
