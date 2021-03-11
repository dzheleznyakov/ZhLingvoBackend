import React from 'react';
import PropTypes from 'prop-types';

import { Modal } from '../../UI';
import DictionaryForm from '../DictionaryForm/DictionaryForm';
import * as actions from '../../../store/actions';
import { useActionOnMount } from '../../../hooks';

const NewDictionaryDialog = props => {
    const { close } = props;
    useActionOnMount(actions.fetchAllLanguages());

    return <DictionaryForm title="New Dictionary" close={close} />;
};

NewDictionaryDialog.propTypes = {
    close: PropTypes.func.isRequired,
};

export default NewDictionaryDialog;
