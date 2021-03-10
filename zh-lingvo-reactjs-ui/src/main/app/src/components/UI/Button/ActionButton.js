import React from 'react';
import PropTypes from 'prop-types';

import classes from './ActionButton.module.scss';

import * as types from './actionTypes';
import * as sizes from './sizes';

const typeToClass = type => {
    switch (type) {
        case types.CONFIRM: return classes.Confirm;
        case types.CANCEL: return classes.Cancel;
        default: return '';
    }
};

const sizeToClass = size => {
    switch (size) {
        case sizes.LARGE: return classes.Large;
        case sizes.MEDIUM: return classes.Medium;
        default: return '';
    }
};

const ActionButton = props => {
    const { children, type, size, clicked } = props;
    const classNames = [classes.Button, typeToClass(type), sizeToClass(size)].join(' ');
    return (
        <button
            className={classNames}
            onClick={clicked}
        >
            {children}
        </button>
    );
};

const typesArray = Object.keys(types).map(key => types[key]);
const sizesArray = Object.keys(sizes).map(key => sizes[key]);

ActionButton.propTypes = {
    children: PropTypes.node,
    type: PropTypes.oneOf(typesArray),
    size: PropTypes.oneOf(sizesArray),
    clicked: PropTypes.func,
};

ActionButton.defaultProps = {
    type: types.CONFIRM,
    size: sizes.MEDIUM,
};

export default ActionButton;
