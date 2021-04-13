import React, { useRef } from 'react';
import { useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './WordMainForm.module.scss';

import withEditing from '../../hoc/withEditing';
import * as actions from '../../../../../store/actions';

const ENTER_KEY_CODE = 13;

const WordMainFormRegular = props => {
    const { children } = props;
    return <div className={classes.WordMainForm}>{children}</div>;
};

const WordMainFormEditing = props => {
    const { children } = props;
    const dispatch = useDispatch();
    const inputRef = useRef();

    const onBlur = event => {
        dispatch(actions.updateWordMainForm(event.target.value));
    };
    const onEnterKeyPressed = event => {
        if (event.keyCode === ENTER_KEY_CODE) {
            dispatch(actions.updateWordMainForm(event.target.value));
            inputRef && inputRef.current.blur();
        }
    };

    return (
        <div>
            <input 
                type="text"
                defaultValue={children}
                onKeyUp={onEnterKeyPressed}
                ref={inputRef}
                onBlur={onBlur}
            />
        </div>
    );
};

WordMainFormRegular.propTypes = {
    children: PropTypes.string.isRequired,
};

WordMainFormEditing.propTypes = {
    children: PropTypes.string.isRequired,
};

export default withEditing(WordMainFormRegular, WordMainFormEditing);
