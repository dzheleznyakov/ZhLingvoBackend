import React, { useState } from 'react';
import { useParams } from 'react-router-dom';

import { IconButton, iconButtonTypes, Modal } from '../../../UI';
import DeleteRecordDialog from './DeleteRecordDialog/DeleteRecordDialog';
import NewRecordDialog from './NewRecordDialog/NewRecordDialog';

import classes from './RecordListControl.module.scss';

const MODAL_TYPES = {
    NEW: 'NEW',
    DELETE: 'DELETE',
    NONE: 'NONE',
};

const RecordListControl = () => {
    const { rid: recordId } = useParams();
    const noRecordSelected = recordId === null || recordId === undefined;
    const [modalType, setModalType] = useState(MODAL_TYPES.NONE);
    const showModal = modalType !== MODAL_TYPES.NONE;

    const onNew = () => setModalType(MODAL_TYPES.NEW);
    const onDelete = () => setModalType(MODAL_TYPES.DELETE);
    const closeModal = () => setModalType(MODAL_TYPES.NONE);

    let panel;
    switch (modalType) {
        case MODAL_TYPES.NEW: panel = <NewRecordDialog  close={closeModal} />; break;
        case MODAL_TYPES.DELETE: panel = <DeleteRecordDialog close={closeModal} />; break;
        default: panel = null;
    }

    return (
        <>
            <div className={classes.ButtonBox}>
                <IconButton type={iconButtonTypes.NEW} clicked={onNew} />
                <IconButton type={iconButtonTypes.DELETE} clicked={onDelete} disabled={noRecordSelected} />
            </div>
            <Modal show={showModal} close={closeModal}>{panel}</Modal>
        </>
    );
};

export default RecordListControl;
