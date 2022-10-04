import React from 'react';
import PropTypes from 'prop-types';

import { Dialog } from '../../UI';
import * as actions from '../../../store/actions';
import { useDispatch, useSelector } from 'react-redux';
import { selectedDictionarySelector } from '../../../store/selectors';

const DeleteDictionaryDialog = props => {
    const { close } = props;
    const dispatch = useDispatch();
    const dictionary = useSelector(selectedDictionarySelector);

    const dictionaryId = (dictionary && dictionary.id) || -1;
    const onConfirm = () => dispatch(actions.deleteDictionary(dictionaryId));
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
};

export default DeleteDictionaryDialog;
