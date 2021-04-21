import React, { useRef } from 'react';
import PropTypes from 'prop-types';
import { useDispatch } from 'react-redux';

import DictionaryForm from '../DictionaryForm/DictionaryForm';
import { Form, formInputTypes } from '../../UI';
import * as actions from '../../../store/actions';
import { useActionOnMount } from '../../../hooks';

const NewDictionaryDialog = props => {
    const { close } = props;
    useActionOnMount(actions.fetchAllLanguages());
    const dispatch = useDispatch();
    
    const confirmed = (name, language) => dispatch(actions.createDictionary(name, language));

    return <DictionaryForm title="New Dictionary" close={close} confirmed={confirmed} />;
};

NewDictionaryDialog.propTypes = {
    close: PropTypes.func.isRequired,
};

export default NewDictionaryDialog;
