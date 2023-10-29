import React from 'react';
import PropTypes from 'prop-types';

import classes from './EditableBase.module.scss';
import { buttonSizes, IconButton, iconButtonTypes } from '..';

const ButtonBox = props => {
    const {
        newModalType,
        editModalType,
        deleteModalType,
        redirectModalType,
        show,
        buttonsCoordinates,
        afterActionCb,
        modalTypeToAction,
        isEditing,
    } = props;

    const getOnAction = modalType =>
        isEditing && modalType ? () => {
            modalTypeToAction(modalType);
            afterActionCb(false);
        } : null;

    const onNew = getOnAction(newModalType);
    const onEdit = getOnAction(editModalType);
    const onDelete = getOnAction(deleteModalType);
    const onRedirect = getOnAction(redirectModalType);

    const newButton = show && newModalType && (
        <IconButton
            type={iconButtonTypes.NEW}
            size={buttonSizes.SMALL}
            clicked={onNew}
        />
    );
    const editButton = show && editModalType && (
        <IconButton
            type={iconButtonTypes.EDIT}
            size={buttonSizes.SMALL}
            clicked={onEdit}
        />
    );
    const deleteButton = show && deleteModalType && (
        <IconButton
            type={iconButtonTypes.DELETE}
            size={buttonSizes.SMALL}
            clicked={onDelete}
        />
    );

    const redirectButton = show && redirectModalType && (
        <IconButton
            type={iconButtonTypes.REDIRECT}
            size={buttonSizes.SMALL}
            clicked={onRedirect}
        />
    );

    const { x, y } = buttonsCoordinates;
    const buttonPositioning = {
        left: `${x}px`,
        top: `${y}px`,
    };

    return show && (
        <div className={classes.ButtonBox} style={buttonPositioning}>
            {newButton}
            {editButton}
            {deleteButton}
            {redirectButton}
        </div>
    );
};

ButtonBox.propTypes = {
    newModalType: PropTypes.string,
    editModalType: PropTypes.string,
    deleteModalType: PropTypes.string,
    redirectModalType: PropTypes.string,
    show: PropTypes.bool,
    afterActionCb: PropTypes.func.isRequired,
    buttonsCoordinates: PropTypes.shape({
        x: PropTypes.number.isRequired,
        y: PropTypes.number.isRequired,
    }),
    modalTypeToAction: PropTypes.func.isRequired,
    isEditing: PropTypes.bool,
};

ButtonBox.defaultProps = {
    show: false,
    buttonsCoordinates: { x: 0, y: 0 },
    isEditing: false,
};

export default ButtonBox;

