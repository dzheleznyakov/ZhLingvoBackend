import React from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';

import DictionaryForm from '../DictionaryForm/DictionaryForm';
import * as actions from '../../../store/actions';
import { currentDictionarySelector } from '../../../store/selectors';

const EditDictionaryDialog = props => {
    const { close } = props;
    const dispatch = useDispatch();
    const dictionary = useSelector(currentDictionarySelector);

    const confirmed = (name, language) => dispatch(actions.updateDictionary(name, language));

    return (
        <DictionaryForm 
            title="Edit Dictionary" 
            close={close} 
            confirmed={confirmed}
            dictionary={dictionary}
            disabledInputs={{ language: true }}
        />
    );
};

EditDictionaryDialog.propTypes = {
    close: PropTypes.func.isRequired,
};

export default EditDictionaryDialog;
