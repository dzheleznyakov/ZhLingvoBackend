import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import { Dialog } from '../../../UI';
import * as selectors from '../../../../store/selectors';
import * as actions from '../../../../store/actions';

const DeleteWordDialog = props => {
    const { close, wordMainForm, dictionaryId } = props;
    const dispatch = useDispatch();
    const word = useSelector(selectors.loadedWordSelector);
    const onConfirm = () => word && dispatch(actions.deleteSelectedWord(dictionaryId));

    const text = word
        ? `Are you sure you want to delete word "${wordMainForm}"?`
        : null;

    return (
        <Dialog close={close} confirmed={onConfirm}>
            <div>{text}</div>
        </Dialog>
    );
};

DeleteWordDialog.propTypes = {
    close: PropTypes.func.isRequired,
    dictionaryId: PropTypes.string.isRequired,
    wordMainForm: PropTypes.string.isRequired,
};

export default DeleteWordDialog;
