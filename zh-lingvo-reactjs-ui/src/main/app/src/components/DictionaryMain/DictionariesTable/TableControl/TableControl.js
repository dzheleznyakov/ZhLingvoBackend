import React, { useState } from 'react';
import { useSelector } from 'react-redux';

import classes from './TableControl.module.scss';

import { Modal, IconButton, iconButtonTypes } from '../../../UI';
import NewDictionaryDialog from '../../NewDictionaryDialog/NewDictionaryDialog';
import { dictionarySelectedSelector } from '../../../../store/selectors';

const TableControl = () => {
    const dictionarySelected = useSelector(dictionarySelectedSelector);
    const [showModal, setShowModal] = useState(false);

    return (
        <div className={classes.ButtonBox}>
            <IconButton type={iconButtonTypes.NEW} clicked={() => setShowModal(true)} />
            <IconButton type={iconButtonTypes.EDIT} disabled={!dictionarySelected} clicked={() => {}} />
            <IconButton type={iconButtonTypes.DELETE} disabled={!dictionarySelected} clicked={() => {}} />
            <NewDictionaryDialog show={showModal} close={() => setShowModal(false)}>Modal</NewDictionaryDialog>
        </div>
    );
};

export default TableControl;
