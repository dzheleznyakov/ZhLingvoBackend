import React, { useEffect, useRef, useState } from 'react';
import { useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

import classes from '../Editing.module.scss';

import * as actions from '../../../../../../store/actions';

const ENTER_KEY_CODE = 13;

const WordMainFormEditing = props => {
    const { children } = props;
    const inputRef = useRef();
    const dispatch = useDispatch();

    const onBlur = event => {
        dispatch(actions.updateWordMainForm(event.target.value));
    };
    const onKeyPressed = event => {
        if (event.keyCode === ENTER_KEY_CODE)
            inputRef && inputRef.current.blur();
    };

    return (
        <div>
            <label className={classes.Label}>Main form:</label>
            <input
                className={classes.Input}
                type="text"
                defaultValue={children}
                ref={inputRef}
                onKeyUp={onKeyPressed}
                onBlur={onBlur}
            />
        </div>
    );
};

WordMainFormEditing.propTypes = {
    children: PropTypes.string.isRequired,
};

export default WordMainFormEditing;
