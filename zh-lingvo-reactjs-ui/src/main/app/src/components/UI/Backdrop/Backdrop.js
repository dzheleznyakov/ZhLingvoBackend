import React from 'react';
import PropTypes from 'prop-types';
import { ENTERING, EXITING, ENTERED } from 'react-transition-group/Transition';

import classes from './Backdrop.module.scss';
import withTransition from '../../../hoc/withTransition/withTransition';

const Backdrop = props => {
    const { animationClass, clicked, nodeRef } = props;
    const className = [classes.Backdrop, animationClass].join(' ');
    return <div className={className} onClick={clicked} ref={nodeRef} />;
};

Backdrop.propTypes = {
    show: PropTypes.bool.isRequired,
    clicked: PropTypes.func.isRequired,
    animationClass: PropTypes.string.isRequired,
    nodeRef: PropTypes.object,
};

const mapPropsToTransitionProps = props => ({
    in: props.show,
    timeout: 300,
    mountOnEnter: true,
    unmountOnExit: true,
});

const getAnimationClass = transitionState => {
    switch (transitionState) {
        case ENTERING: return classes['Backdrop--showing'];
        case EXITING: return classes['Backdrop--hiding'];
        case ENTERED: return classes['Backdrop--shown'];
        default: return classes['Backdrop--hidden'];
    }
};

const mapTransitionStateToProps = transitionState => ({
    animationClass: getAnimationClass(transitionState),
});

export default withTransition(mapPropsToTransitionProps, mapTransitionStateToProps)(Backdrop);
