import React from 'react';
import PropTypes from 'prop-types';

import classes from './Dialog.module.scss';

import { ActionButton, actionButtonTypes } from '../';
import DialogButtonBox from './DialogButtonBox';

const Dialog = props => {
    const { children, close, confirmed, cancelled, disabled, chained } = props;

    const onConfirm = event => {
        confirmed && confirmed(event);
        !chained && close();
    };

    const onCancel = event => {
        cancelled && cancelled(event);
        close();
    };

    const { CANCEL, CONFIRM } = actionButtonTypes;

    const buttons = [
        { type: CONFIRM, onClicked: onConfirm, disabled: disabled, label: 'OK' },
        { type: CANCEL, onClicked: onCancel, label: 'Cancel' },
    ];

    return (
        <div className={classes.Dialog}>
            {children}
            <DialogButtonBox buttons={buttons} />
        </div>
    );
};

Dialog.propTypes = {
    close: PropTypes.func.isRequired,
    confirmed: PropTypes.func,
    cancelled: PropTypes.func,
    disabled: PropTypes.bool,
    chained: PropTypes.bool,
    children: PropTypes.node,
};

Dialog.defaultProps = {
    confirmed: () => {},
    cancelled: () => {},
    disabled: false,
    chained: false,
    children: null,
};

export default Dialog;
