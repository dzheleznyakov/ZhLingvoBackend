import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classes from './TableControl.module.scss';

import { Modal, IconButton, iconButtonTypes } from '../../../UI';
import NewDictionaryDialog from '../../NewDictionaryDialog/NewDictionaryDialog';
import EditDictionaryDialog from '../../EditDictionaryDialog/EditDictionaryDialog';
import DeleteDictionaryDialog from '../../DeleteDictionaryDialog/DeleteDictionaryDialog';
import { dictionarySelectedSelector, selectedDictionarySelector } from '../../../../store/selectors';
import { DICTIONARY as dictionaryUrlPattern } from '../../../../static/constants/paths';
import * as actions from '../../../../store/actions';

const MODAL_TYPES = {
    NEW: 'NEW',
    EDIT: 'EDIT',
    DELETE: 'DELETE',
    NONE: null,
};

const TableControl = () => {
    const dictionaryIsSelected = useSelector(dictionarySelectedSelector);
    const selectedDictionary = useSelector(selectedDictionarySelector);
    const [modalType, setModalType] = useState(MODAL_TYPES.NONE);
    const showModal = modalType !== MODAL_TYPES.NONE;
    const dispath = useDispatch();

    const closeModal = () => setModalType(MODAL_TYPES.NONE);
    const onNew = () => setModalType(MODAL_TYPES.NEW);
    const onEdit = () => setModalType(MODAL_TYPES.EDIT);
    const onDelete = () => setModalType(MODAL_TYPES.DELETE);
    const onForward = () => {
        const selectedDictionaryId = selectedDictionary.id;
        const path = dictionaryUrlPattern.replace(/:(\w)+/g, (param) => {
            switch (param) {
                case ':id': return selectedDictionaryId;
                default: return '';
            }
        });
        dispath(actions.navigateTo(path));
    };

    let panel;
    switch (modalType) {
        case MODAL_TYPES.NEW: panel = (
            <NewDictionaryDialog 
                close={closeModal} 
            />
        ); 
        break;
        case MODAL_TYPES.EDIT: panel = (
            <EditDictionaryDialog
                close={closeModal}
            />
        );
        break;
        case MODAL_TYPES.DELETE: panel = (
            <DeleteDictionaryDialog 
                close={closeModal}  
            />
        ); break;
        default: panel = null;
    }

    return (
        <>
            <div className={classes.ButtonBox}>
                <IconButton type={iconButtonTypes.NEW} clicked={onNew} />
                <IconButton type={iconButtonTypes.EDIT} disabled={!dictionaryIsSelected} clicked={onEdit} />
                <IconButton type={iconButtonTypes.DELETE} disabled={!dictionaryIsSelected} clicked={onDelete} />
                <IconButton type={iconButtonTypes.FORWARD} disabled={!dictionaryIsSelected} clicked={onForward} />
            </div>
            <Modal show={showModal} close={closeModal}>{panel}</Modal>
        </>
    );
};

export default TableControl;
