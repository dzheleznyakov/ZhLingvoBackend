import React from 'react';
import PropTypes from 'prop-types';

import classes from './Dialog.module.scss';
import DialogButtonBox, { dialogButtonConfigType } from './DialogButtonBox';

const DialogBase = props => {
    const { children, buttons, className } = props;

    const cssClasses = [classes.Dialog];
    if (className)
        cssClasses.push(className)

    return (
        <div className={cssClasses.join(' ')}>
            {children}
            <DialogButtonBox buttons={buttons} />
        </div>
    );
};

DialogBase.propTypes = {
    children: PropTypes.node,
    buttons: PropTypes.arrayOf(dialogButtonConfigType),
    className: PropTypes.string,
};

DialogBase.defaultProps = {
    buttons: [],
};

export default DialogBase;
