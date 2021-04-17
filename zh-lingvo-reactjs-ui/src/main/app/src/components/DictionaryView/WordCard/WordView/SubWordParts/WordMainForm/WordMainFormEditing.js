import React from 'react';
import { useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

import * as actions from '../../../../../../store/actions';
import Input from '../Input';


const WordMainFormEditing = props => {
    const { children } = props;
    const dispatch = useDispatch();

    return <Input 
        label="Main form"
        defaultValue={children}
        submit={event => dispatch(actions.updateWordMainForm(event.target.value))}
    />;
};

WordMainFormEditing.propTypes = {
    children: PropTypes.string.isRequired,
};

export default WordMainFormEditing;
