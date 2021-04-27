import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './Editing.module.scss';

import { IconButton, iconButtonTypes, buttonSizes } from '../../../../UI';
import * as selectors from '../../../../../store/selectors';
import * as actions from '../../../../../store/actions';

const Editing = props => {
    const { children, editModalType, deleteModalType, path, block } = props;
    const isEditing = useSelector(selectors.isEditingSelector);
    const [hovered, setHovered] = useState(false);
    const [buttonsCoordinates, setButtonsCoordinates] = useState({ x: 0, y: 0});
    const dispatch = useDispatch();

    const onHovered = isEditing ? event => {
        setHovered(true);
        const { clientX: x, clientY: y } = event;
        setButtonsCoordinates({ x, y });
    } : null;

    const onUnhovered = isEditing ? () => {
        setHovered(false);
    } : null;

    const onEdit = isEditing && editModalType ? () => {
        dispatch(actions.shouldShowWordEditModal(true));
        dispatch(actions.setWordEditModalType(editModalType, path));
        setHovered(false);
    } : null;

    const onDelete = isEditing && deleteModalType ? () => {
        dispatch(actions.shouldShowWordEditModal(true));
        dispatch(actions.setWordEditModalType(deleteModalType, path));
        setHovered(false);
    } : null;

    const editButton = hovered && editModalType && (
        <IconButton 
            type={iconButtonTypes.EDIT} 
            size={buttonSizes.SMALL}
            clicked={onEdit}
        />
    );

    const deleteButton = hovered && deleteModalType && (
        <IconButton 
            type={iconButtonTypes.DELETE} 
            size={buttonSizes.SMALL}
            clicked={onDelete}
        />
    );

    const buttonPostioning = {
        left: `${buttonsCoordinates.x}px`,
        top: `${buttonsCoordinates.y}px`,
    };

    const buttons = hovered && (
        <div className={classes.ButtonBox} style={buttonPostioning}>
            {editButton}
            {deleteButton}
        </div>
    );

    const className = isEditing ? classes.Editing : null;

    const Tag = block ? 'div' : 'span'
    return (
        <Tag
            className={className}
            onMouseEnter={onHovered}
            onMouseLeave={onUnhovered}
        >
            {children}
            {buttons}
        </Tag>
    );
};

Editing.propTypes = {
    children: PropTypes.node.isRequired,
    editModalType: PropTypes.string,
    deleteModalType: PropTypes.string,
    path: PropTypes.arrayOf(PropTypes.string),
    block: PropTypes.bool,
};

Editing.defaultProps = {
    path: [],
};

export default Editing;
