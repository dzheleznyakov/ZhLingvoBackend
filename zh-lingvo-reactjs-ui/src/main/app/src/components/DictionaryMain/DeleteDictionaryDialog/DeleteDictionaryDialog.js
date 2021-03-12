import React from 'react';
import PropTypes from 'prop-types';

import classes from './DeleteDictionaryDialog.module.scss';

import { Dialog } from '../../UI';
import * as actions from '../../../store/actions';
import { useDispatch } from 'react-redux';

const DeleteDictionaryDialog = props => {
    const { close, dictionary } = props;
    const dispatch = useDispatch();

    const onConfirm = () => dispatch(actions.deleteDictionary(dictionary && dictionary.id || -1));
    const text = dictionary && dictionary.language
        ? `Are you sure you want to delete ${dictionary.language.name} dicitonary "${dictionary.name}"`
        : null;

    return (
            <Dialog close={close} confirmed={onConfirm}>
                <div>{text}</div>
            </Dialog>
    );
};

DeleteDictionaryDialog.propTypes = {};

DeleteDictionaryDialog.defaultProps = {};

export default DeleteDictionaryDialog;
