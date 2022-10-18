import React, { useState } from 'react';
import { useSelector } from 'react-redux';

import classes from './WordListControl.module.scss';

import { Modal, IconButton, iconButtonTypes } from '../../../UI';
import NewWordDialog from '../NewWordDialog/NewWordDialog';
import DeleteWordDialog from '../DeleteWordDialog/DeleteWordDialog';
import * as selectors from '../../../../store/selectors';

const MODAL_TYPES = {
    NEW: 'NEW',
    DELETE: 'DELETE',
    NONE: null,
};

const WordListControl = () => {
    const nothingIsSelected = useSelector(selectors.selectedWordIndexSelector) < 0;
    const [modalType, setModalType] = useState(MODAL_TYPES.NONE);
    const showModal = modalType !== MODAL_TYPES.NONE;

    const onNew = () => setModalType(MODAL_TYPES.NEW);
    const onDelete = () => setModalType(MODAL_TYPES.DELETE);
    const closeModal = () => setModalType(MODAL_TYPES.NONE);

    let panel;
    switch (modalType) {
        case MODAL_TYPES.NEW: panel = <NewWordDialog close={closeModal} />; break;
        case MODAL_TYPES.DELETE: panel = <DeleteWordDialog close={closeModal} />; break;
        default: panel = null;
    }

    return (
        <>
            <div className={classes.ButtonBox}>
                <IconButton type={iconButtonTypes.NEW} clicked={onNew} />
                <IconButton type={iconButtonTypes.DELETE} disabled={nothingIsSelected} clicked={onDelete} />
            </div>
            <Modal show={showModal} close={closeModal}>{panel}</Modal>
        </>
    );
};

export default WordListControl;
