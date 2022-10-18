import React from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';

import { loadedQuizRecordSelector } from '../../../../../store/selectors';
import { Dialog } from '../../../../UI';
import * as actions from '../../../../../store/actions';

const DeleteRecordDialog = props => {
    const { close } = props;
    const { qid: quizId, rid: recordId } = useParams();
    const record = useSelector(loadedQuizRecordSelector);
    const dispatch = useDispatch();

    const { translations = [], wordMainForm } = record;
    const hint = translations.map(({ value }) => value).join(', ')
    
    const onConfirm = () => 
        quizId && recordId && dispatch(actions.deleteQuizRecord(+quizId, +recordId));

    const hintText = hint.length === 0 ? '' : ` (${hint})`
    const text = record
        ? `Are you sure you want to delete: ${wordMainForm}${hintText}`
        : null;
    
        return (
            <Dialog close={close} confirmed={onConfirm}>
                <div>{text}</div>
            </Dialog>
        );
};

DeleteRecordDialog.propTypes = {
    close: PropTypes.func.isRequired,
};

export default DeleteRecordDialog;
