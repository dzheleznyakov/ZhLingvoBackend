import React, { useRef } from 'react';
import PropTypes from 'prop-types';

import classes from './Editing.module.scss';

const ENTER_KEY_CODE = 13;

const Input = props => {
    const { label, defaultValue, submit } = props;
    const inputRef = useRef();

    const onBlur = submit;
    const onKeyPressed = event => {
        if (event.keyCode === ENTER_KEY_CODE)
            inputRef && inputRef.current.blur();
    };

    return (
        <div>
            <label className={classes.Label}>{label}:</label>
            <input
                className={classes.Input}
                type="text"
                defaultValue={defaultValue}
                ref={inputRef}
                onKeyUp={onKeyPressed}
                onBlur={onBlur}
            />
        </div>
    );
};

Input.propTypes = {
    label: PropTypes.string.isRequired,
    submit: PropTypes.func,
    defaultValue: PropTypes.string,
};

Input.defaultProps = {
    submit: () => {},
    defaultValue: '',
};

export default Input;
