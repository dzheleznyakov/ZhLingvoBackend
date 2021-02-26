import React from 'react';
import PropTypes from 'prop-types';

import classes from './ErrorContainer.module.scss';

const ErrorContainer = props => {
    const { className, message } = props;

    return (
        <div
            className={[classes.ErrorContainer, className].join(' ')}
        >
            {message}
        </div>
    );
};

ErrorContainer.propTypes = {
    className: PropTypes.string,
    message: PropTypes.string.isRequired,
};

ErrorContainer.defaultProps = {
    className: '',
};

export default ErrorContainer;
