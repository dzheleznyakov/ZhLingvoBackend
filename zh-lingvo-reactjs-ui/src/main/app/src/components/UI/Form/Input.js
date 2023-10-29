import React, { useEffect } from 'react';
import PropTypes from 'prop-types';

import * as inputTypes from './inputTypes';
import { validationType } from './formTypes';
import { refType } from '../../../static/types/generalTypes';
import LocaleInput from './LocaleInput/LocaleInput';

const inputTypesArray = Object.keys(inputTypes).map(key => inputTypes[key]);

const resizeTextArea = (elem) => {
    const str = elem.value;
    const cols = elem.cols;

    let linecount = 0;
    str.split('\n').forEach(line => {
        linecount += Math.ceil(line.length / cols);
    });
    elem.rows = linecount + 1;
};

const Input = props => {
    const { 
        id, 
        type, 
        defaultValue, 
        values, 
        disabled, 
        autocomplete,
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

    useEffect(() => {
        if (type === inputTypes.TEXT_AREA && forwardRef && forwardRef.current && defaultValue)
            resizeTextArea(forwardRef.current);
    }, [type, forwardRef, defaultValue]);

    switch (type) {
        case inputTypes.TEXT:
            return (
                <input
                    id={id}
                    type="text"
                    defaultValue={defaultValue}
                    disabled={disabled}
                    autoComplete={autocomplete === false ? 'off' : null}
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
                    autoComplete={autocomplete === false ? 'off' : null}
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
                    autoComplete={autocomplete === false ? 'off' : null}
                    ref={forwardRef}
                    { ...listeners }
                >
                    {options}
                </select>
            );
        case inputTypes.TEXT_AREA:
            return (
                <textarea 
                    id={id}
                    defaultValue={defaultValue}
                    disabled={disabled}
                    autoComplete={autocomplete === false ? 'off' : null}
                    ref={forwardRef}
                    rows={3}
                    onChange={() => {
                        validate && validate();
                        resizeTextArea(forwardRef.current);
                    }}
                    { ...listeners }
                />
            );
        case inputTypes.LOCALE_TEXT:
            return <LocaleInput { ...props } />;
        default: return null;
    }
};

Input.propTypes = {
    id: PropTypes.string.isRequired,
    type: PropTypes.oneOf(inputTypesArray).isRequired,
    defaultValue: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    forwardRef: refType,
    values: PropTypes.arrayOf(PropTypes.string),
    disabled: PropTypes.bool,
    autocomplete: PropTypes.bool,
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
