import React from 'react';
import PropTypes from 'prop-types';
import Transition, { ENTERING, EXITING } from 'react-transition-group/Transition';

import classes from './Backdrop.module.scss';

const Backdrop = props => {
    const { show, clicked } = props;

    const getAnimationClass = transitionState => {
        switch (transitionState) {
            case ENTERING: return classes['Backdrop--showing'];
            case EXITING: return classes['Backdrop--hiding'];
            default: return '';
        }
    };

    return <Transition 
        in={show} 
        timeout={300}
        mountOnEnter
        unmountOnExit
    >
        {transitionState => {
            let animationClass = getAnimationClass(transitionState);
            return (
                <div 
                    className={[classes.Backdrop, animationClass].join(' ')}
                    onClick={clicked}
                />
            )
        }}
    </Transition>;
};

Backdrop.propTypes = {
    show: PropTypes.bool.isRequired,
    clicked: PropTypes.func.isRequired,
};

export default Backdrop;
