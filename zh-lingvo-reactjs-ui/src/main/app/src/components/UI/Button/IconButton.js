import React from 'react';
import PropTypes from 'prop-types';

import classes from './IconButton.module.scss';

import * as buttonTypes from './iconTypes';
import * as buttonSizes from './sizes';

const typeToClass = type => {
    switch (type) {
        case buttonTypes.OPTIONS: return classes.Options;
        case buttonTypes.NEW: return classes.New;
        case buttonTypes.EDIT: return classes.Edit;
        case buttonTypes.DELETE: return classes.Delete;
        case buttonTypes.FORWARD: return classes.Forward;
        default: return '';
    }
};

const sizeToClass = size => {
    switch (size) {
        case buttonSizes.LARGE: return classes.Large;
        case buttonSizes.MEDIUM: return classes.Medium;
        case buttonSizes.SMALL: return classes.Small;
        default: return '';
    }
};

const IconButton = props => {
    const { clicked, type, size, disabled } = props;

    const buttonClasses = [classes.Button, sizeToClass(size)];
    if (disabled)
        buttonClasses.push(classes.Disabled);

    return (
        <button 
            type="button" 
            disabled={disabled}
            className={buttonClasses.join(' ')}
            onClick={clicked}
        >
            <div 
                className={typeToClass(type)} 
            >
                <div className={classes.IconCover} />
            </div>
        </button>
    );
};

const buttonTypesArray = Object.keys(buttonTypes)
    .map(key => buttonTypes[key]);

const buttonSizesArray = Object.keys(buttonSizes)
    .map(key => buttonSizes[key]);

IconButton.propTypes = {
    clicked: PropTypes.func,
    type: PropTypes.oneOf(buttonTypesArray),
    size: PropTypes.oneOf(buttonSizesArray),
    disabled: PropTypes.bool,
};

IconButton.defaultProps = {
    clicked: () => {},
    type: buttonTypes.NEW,
    size: buttonSizes.MEDIUM,
    disabled: false,
};

export default IconButton;
