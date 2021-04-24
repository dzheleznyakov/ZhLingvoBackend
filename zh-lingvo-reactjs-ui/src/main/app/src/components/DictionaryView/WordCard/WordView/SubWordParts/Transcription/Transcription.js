import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './Transcription.module.scss';

import {IconButton, iconButtonTypes, buttonSizes } from '../../../../../UI';
import * as selectors from '../../../../../../store/selectors';
import * as actions from '../../../../../../store/actions';
import { TRANSCRIPTION_DELETE, TRANSCRIPTION_EDIT, TRANSCRIPTION_NEW } from '../../../../../../static/constants/wordEditModalTypes';

const Transcription = props => {
    const { children: transcription, parentPath } = props;
    const isEditing = useSelector(selectors.isEditingSelector);
    const [hovered, setHovered] = useState(false);
    const [buttonsCoordinates, setButtonsCoordinates] = useState({ x: 0, y: 0});
    const dispatch = useDispatch();

    const path = [...parentPath, 'transcription'];

    const classNames = [classes.Transcription];
    if (isEditing)
        classNames.push(classes.Editing);

    const onHovered = isEditing ? event => {
        setHovered(true);
        const { clientX: x, clientY: y } = event;
        setButtonsCoordinates({ x, y });
    } : null;
    const onUnhovered = isEditing ? () => setHovered(false) : null;
    const onEdit = isEditing ? () => {
        dispatch(actions.shouldShowWordEditModal(true));
        dispatch(actions.setWordEditModalType(TRANSCRIPTION_EDIT, path));
        setHovered(false);
    } : null;
    const onDelete = isEditing ? () => {
        dispatch(actions.shouldShowWordEditModal(true));
        dispatch(actions.setWordEditModalType(TRANSCRIPTION_DELETE, path));
        setHovered(false);
    } : null;
    const onNew = isEditing && !transcription ? () => {
        dispatch(actions.shouldShowWordEditModal(true));
        dispatch(actions.setWordEditModalType(TRANSCRIPTION_NEW, path));
    } : null;

    const editButton = hovered && (
        <IconButton 
            type={iconButtonTypes.EDIT} 
            size={buttonSizes.SMALL}
            clicked={onEdit}
        />
    );
    const deleteButton = hovered && (
        <IconButton 
            type={iconButtonTypes.DELETE} 
            size={buttonSizes.SMALL}
            clicked={onDelete}
        />
    );

    const buttonsStyle = {
        left: `${buttonsCoordinates.x}px`,
        top: `${buttonsCoordinates.y}px`,
    };
    const buttons = hovered && (
        <div className={classes.ButtonBox} style={buttonsStyle}>
            {editButton}
            {deleteButton}
        </div>
    );

    if (isEditing && !transcription) {
        return <IconButton
            type={iconButtonTypes.NEW}
            size={buttonSizes.SMALL}
            clicked={onNew}
        />;
    }

    return transcription &&  (
        <div 
            className={classNames.join(' ')}
            onMouseEnter={onHovered}
            onMouseLeave={onUnhovered}
        >
            [{transcription}]
            {buttons}
        </div>
    );
};

Transcription.propTypes = {
    children: PropTypes.string,
    parentPath: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default Transcription;
