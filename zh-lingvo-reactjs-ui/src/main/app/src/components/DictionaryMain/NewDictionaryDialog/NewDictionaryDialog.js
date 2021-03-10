import React from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import classes from './NewDictionaryDialog.module.scss';

import { Modal } from '../../UI';
import DictionaryForm from '../DictionaryForm/DictionaryForm';
import * as actions from '../../../store/actions';
import { languagesSelector } from '../../../store/selectors';
import { useActionOnMount } from '../../../hooks';

const NewDictionaryDialog = props => {
    const { show, close } = props;
    useActionOnMount(actions.fetchAllLanguages());

    return (
        <Modal show={show} close={close}>
            <DictionaryForm title="New Dictionary" close={close} />
        </Modal>
    );
};

NewDictionaryDialog.propTypes = {};

NewDictionaryDialog.defaultProps = {};

export default NewDictionaryDialog;
