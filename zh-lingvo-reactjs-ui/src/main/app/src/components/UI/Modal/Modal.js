import React from 'react';
import PropTypes from 'prop-types';
import { ENTERING, EXITING, ENTERED } from 'react-transition-group/Transition';

import classes from './Modal.module.scss';

import { Backdrop } from '../';
import withTransition from '../../../hoc/withTransition/withTransition';

const getAnimationClass = transitionState => {
    switch (transitionState) {
        case ENTERING: return classes['Content--showing'];
        case EXITING: return classes['Content--hiding'];
        case ENTERED: return classes['Content--shown'];
        default: return classes['Content--hidden'];
    }
};

const Modal = props => {
    const { show, close, animationClass, children, nodeRef } = props;
    const className = [classes.Content, animationClass].join(' ');

    return (
        <div
            className={classes.ModalWrapper}
            ref={nodeRef}
        >
            <div className={className}>{children}</div>
            <Backdrop show={show} clicked={close} />
        </div>
    );
};

Modal.propTypes = {
    show: PropTypes.bool.isRequired,
    close: PropTypes.func.isRequired,
    animationClass: PropTypes.string.isRequired,
    children: PropTypes.node,
    nodeRef: PropTypes.object,
};

const mapPropsToTransitionProps = props => ({
    in: props.show,
    timeout: 400,
    mountOnEnter: true,
    unmountOnExit: true,
});

const mapTransitionStateToProps = transitionState => ({
    animationClass: getAnimationClass(transitionState),
});

export default withTransition(mapPropsToTransitionProps, mapTransitionStateToProps)(Modal);
