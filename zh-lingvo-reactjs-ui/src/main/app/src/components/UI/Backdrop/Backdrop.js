import React from 'react';
import PropTypes from 'prop-types';

import classes from './Backdrop.module.scss';

const Backdrop = props => {
    const { show, clicked, zIndex } = props;
    return show ? (
        <div 
            className={classes.Backdrop}
            onClick={clicked}
            zIndex={zIndex}
        />
    ) : null ;
};

Backdrop.propTypes = {
    show: PropTypes.bool.isRequired,
    clicked: PropTypes.func.isRequired,
    zIndex: PropTypes.number,
};

Backdrop.defaultProps = {
    zIndex: 100,
};

export default Backdrop;
