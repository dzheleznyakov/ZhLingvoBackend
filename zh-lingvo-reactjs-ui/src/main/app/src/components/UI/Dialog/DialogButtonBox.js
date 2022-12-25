import React from 'react';
import PropTypes from 'prop-types';

import classes from './Dialog.module.scss';

import { ActionButton } from '../';
import * as actionButtonTypes from '../Button/actionTypes';

const DialogButtonBox = props => {
    const { buttons } = props;

    return (
        <div className={classes.ButtonBox}>
            {buttons.map(({type, onClicked, disabled, label}) => (
                <ActionButton 
                    key={label}
                    type={type}
                    clicked={onClicked}
                    disabled={disabled}
                >
                    {label}
                </ActionButton>
            ))}
        </div>
    );
};

const actionButtonTypesArray = Object.keys(actionButtonTypes)
    .map(key => actionButtonTypes[key]);

DialogButtonBox.propTypes = {
    buttons: PropTypes.arrayOf(
        PropTypes.shape({
            type: PropTypes.oneOf(actionButtonTypesArray).isRequired,
            onClicked: PropTypes.func.isRequired,
            disabled: PropTypes.bool,
            label: PropTypes.string,
        }),
    ),
};

DialogButtonBox.defaultProps = {
    buttons: [],
};

export default DialogButtonBox;
