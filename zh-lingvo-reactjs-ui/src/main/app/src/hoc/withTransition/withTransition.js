import React, { useRef } from 'react';
import Transition from 'react-transition-group/Transition';

const withTransition = (mapPropsToTrProps, mapTrStateToProps) => Component => props => {
    const trProps = typeof mapPropsToTrProps === 'function' 
        ? mapPropsToTrProps(props) 
        : mapPropsToTrProps || {};
    const nodeRef = useRef();
    return (
        <Transition {...trProps} nodeRef={nodeRef}>
            {transitionState => {
                const cProps = typeof mapTrStateToProps === 'function'
                    ? mapTrStateToProps(transitionState)
                    : mapTrStateToProps || {}
                return <Component {...props} {...cProps} nodeRef={nodeRef} />;
            }}
        </Transition>
    );
};

export default withTransition;