import React from 'react';
import PropTypes from 'prop-types';

import { Dialog } from '../../UI';
import * as actions from '../../../store/actions';
import { useDispatch } from 'react-redux';

const DeleteDictionaryDialog = props => {
    const { close, dictionary } = props;
    const dispatch = useDispatch();

    const onConfirm = () => dispatch(actions.deleteDictionary(dictionary && dictionary.id || -1));
    const text = dictionary && dictionary.language
        ? `Are you sure you want to delete ${dictionary.language.name} dicitonary "${dictionary.name}"?`
        : null;

    return (
            <Dialog close={close} confirmed={onConfirm}>
                <div>{text}</div>
            </Dialog>
    );
};

DeleteDictionaryDialog.propTypes = {
    close: PropTypes.func.isRequired,
    dictionary: PropTypes.shape({}), // TODO: define type properly
};

export default DeleteDictionaryDialog;
