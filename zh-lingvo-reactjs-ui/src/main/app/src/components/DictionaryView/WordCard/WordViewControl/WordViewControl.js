import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router';

import classes from './WordViewControl.module.scss';

import { IconButton, iconButtonTypes, ActionButton, actionButtonTypes } from '../../../UI';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';
import { 
    MEANING_TO_QUIZ_RECORD__SELECT_MEANING
} from '../../../../static/constants/wordEditModalTypes';

const WordViewControl = () => {
    const isEditing = useSelector(selectors.isEditingSelector);
    const dispatch = useDispatch();
    const { id: dictionaryId } = useParams();

    const onEdit = () => dispatch(actions.setWordEditing(true));
    const onSendToTutor = () => {
        dispatch(actions.shouldShowWordEditModal(true));
        dispatch(actions.setWordEditModalType(MEANING_TO_QUIZ_RECORD__SELECT_MEANING, []));
    };
    const onConfirm = () => dispatch(actions.updateWord(dictionaryId));
    const onCancel = () => dispatch(actions.setWordEditing(false));

    const editButton = !isEditing && (
        <IconButton type={iconButtonTypes.EDIT} clicked={onEdit} />
    );
    const sendToTutorButton = !isEditing && (
        <IconButton type={iconButtonTypes.REDIRECT} clicked={onSendToTutor} />
    );
    const okButton = isEditing && (
        <ActionButton type={actionButtonTypes.CONFIRM} clicked={onConfirm}>
            OK
        </ActionButton>
    );
    const cancelButton = isEditing && (
        <ActionButton type={actionButtonTypes.CANCEL} clicked={onCancel}>
            Cancel
        </ActionButton>
    );

    return (
        <div className={classes.ButtonBox}>
            {editButton}
            {sendToTutorButton}
            {okButton}
            {cancelButton}
        </div>
    );
};

export default WordViewControl;
