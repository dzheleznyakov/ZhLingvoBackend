import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classes from './TableControl.module.scss';

import { selectedQuizSelector } from '../../../../store/selectors';
import { TUTOR_QUIZ as quizUrlPattern } from '../../../../static/constants/paths';
import * as actions from '../../../../store/actions';
import { Modal, IconButton, iconButtonTypes } from '../../../UI';
import NewQuizDialog from './NewQuizDialog/NewQuizDialog';
import EditQuizDialog from './EditQuizDialog/EditQuizDialog';
import DeleteQuizDialog from './DeleteQuizDialog/DeleteQuizDialog';

const MODAL_TYPES = {
    NEW: 'NEW',
    EDIT: 'EDIT',
    DELETE: 'DELETE',
    NONE: 'NONE',
};

const TableControl = () => {
    const selectedQuiz = useSelector(selectedQuizSelector);
    const quizIsSelected = selectedQuiz !== null;
    const [modalType, setModalType] = useState(MODAL_TYPES.NONE);
    const showModal = modalType !== MODAL_TYPES.NONE;
    const dispatch = useDispatch();

    const closeModal = () => setModalType(MODAL_TYPES.NONE);
    const onNew = () => setModalType(MODAL_TYPES.NEW);
    const onEdit = () => setModalType(MODAL_TYPES.EDIT);
    const onDelete = () => setModalType(MODAL_TYPES.DELETE);
    const onForward = () => {
        if (!quizIsSelected) return;
        const path = quizUrlPattern.replace(/\/:\w+/g, (param) => {
            switch (param) {
                case '/:id': return `/${selectedQuiz.id}`;
                default: return '';
            }
        });
        dispatch(actions.navigateTo(path));
    };

    let panel;
    switch (modalType) {
        case MODAL_TYPES.NEW: 
            panel = <NewQuizDialog close={closeModal}/>; break;
        case MODAL_TYPES.EDIT:
            panel = <EditQuizDialog close={closeModal} />; break;
        case MODAL_TYPES.DELETE:
            panel = <DeleteQuizDialog close={closeModal} />; break;
        default: panel = null;
    }

    return (
        <>
            <div className={classes.ButtonBox}>
                <IconButton type={iconButtonTypes.NEW} clicked={onNew} />
                <IconButton type={iconButtonTypes.EDIT} clicked={onEdit} disabled={!quizIsSelected} />
                <IconButton type={iconButtonTypes.DELETE} clicked={onDelete} disabled={!quizIsSelected} />
                <IconButton type={iconButtonTypes.FORWARD} clicked={onForward} disabled={!quizIsSelected} />
            </div>
            <Modal show={showModal} close = {closeModal}>{panel}</Modal>
        </>
    );
};

export default TableControl;
