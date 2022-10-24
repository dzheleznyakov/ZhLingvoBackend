import React, { useEffect } from 'react';
import PropTypes from 'prop-types';

import * as inputTypes from './inputTypes';
import { validationType } from './formTypes';

const inputTypesArray = Object.keys(inputTypes).map(key => inputTypes[key]);

const Input = props => {
    const { 
        id, 
        type, 
        defaultValue, 
        values, 
        disabled, 
        listeners, 
        forwardRef,
        validation,
        onValidate,
    } = props;

    const validate = validation && validation.length ? () => {
        const errors = validation
            .filter(({ validate }) => !validate(forwardRef))
            .map(({ failureMessage }) => failureMessage);
        onValidate(errors);
    } : null;

    useEffect(() => {
        if (validate && forwardRef && forwardRef.current)
            validate(forwardRef);
    }, [forwardRef]);

    switch (type) {
        case inputTypes.TEXT:  
            return (
                <input
                    id={id}
                    type="text"
                    defaultValue={defaultValue}
                    disabled={disabled}
                    ref={forwardRef}
                    onKeyUp={validate}
                    { ...listeners }
                />
            );
        case inputTypes.NUMBER:
            return (
                <input
                    id={id}
                    type="number"
                    defaultValue={defaultValue}
                    disabled={disabled}
                    ref={forwardRef}
                    onChange={validate}
                    { ...listeners }
                />
            );
        case inputTypes.SELECT: 
            const options = values.map(value => (
                <option key={`${id}-op-${value}`}>{value}</option>
            ));
            return (
                <select 
                    id={id}
                    defaultValue={defaultValue}
                    disabled={disabled}
                    ref={forwardRef}
                    { ...listeners }
                >
                    {options}
                </select>
            );
        default: return null;
    }
};

Input.propTypes = {
    id: PropTypes.string.isRequired,
    type: PropTypes.oneOf(inputTypesArray).isRequired,
    defaultValue: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    forwardRef: PropTypes.object.isRequired,
    values: PropTypes.arrayOf(PropTypes.string),
    disabled: PropTypes.bool,
    validation: PropTypes.arrayOf(validationType),
    onValidate: PropTypes.func.isRequired,
    listeners: PropTypes.objectOf(PropTypes.func),
};

Input.defaultProps = {
    values: [],
    disabled: false,
    listeners: {},
    validation: [],
};

export default Input;
