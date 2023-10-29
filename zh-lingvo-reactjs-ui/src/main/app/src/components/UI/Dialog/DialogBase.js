import React from 'react';
import PropTypes from 'prop-types';

import classes from './Dialog.module.scss';
import DialogButtonBox, { dialogButtonConfigType } from './DialogButtonBox';

const DialogBase = props => {
    const { children, buttons } = props;

    return (
        <div className={classes.Dialog}>
            {children}
            <DialogButtonBox buttons={buttons} />
        </div>
    );
};

DialogBase.propTypes = {
    children: PropTypes.node,
    buttons: PropTypes.arrayOf(dialogButtonConfigType)
};

DialogBase.defaultProps = {
    buttons: [],
};

export default DialogBase;
