import React from 'react';
import PropTypes from 'prop-types';

import classes from './Dialog.module.scss';

import { ActionButton, actionButtonTypes } from '../';

const Dialog = props => {
    const { children, close, confirmed, cancelled, disabled } = props;

    const onConfirm = event => {
        confirmed && confirmed(event);
        close();
    };

    const onCancel = event => {
        cancelled && cancelled(event);
        close();
    };

    const { CANCEL, CONFIRM } = actionButtonTypes;

    return (
        <div className={classes.Dialog}>
            {children}
            <div className={classes.ButtonBox}>
                <ActionButton type={CONFIRM} clicked={onConfirm} disabled={disabled}>OK</ActionButton>
                <ActionButton type={CANCEL} clicked={onCancel}>Cancel</ActionButton>
            </div>
        </div>
    );
};

Dialog.propTypes = {
    close: PropTypes.func.isRequired,
    confirmed: PropTypes.func,
    cancelled: PropTypes.func,
    disabled: PropTypes.bool,
    children: PropTypes.node,
};

Dialog.defaultProps = {
    confirmed: () => {},
    cancelled: () => {},
    disabled: false,
    children: null,
};

export default Dialog;
