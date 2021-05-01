import React from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';

import classes from './Editable.module.scss';

import { IconButton, iconButtonTypes, buttonSizes } from '../../../../../UI';
import * as actions from '../../../../../../store/actions';
import * as selectors from '../../../../../../store/selectors';

const ButtonBox = props => {
    const { 
        newModalType, 
        editModalType, 
        deleteModalType, 
        path, 
        show, 
        buttonsCoordinates,
        afterActionCb,
    } = props;
    const isEditing = useSelector(selectors.isEditingSelector);
    const dispatch = useDispatch();

    const getOnAction = modalType => 
        isEditing && modalType ? () => {
            dispatch(actions.shouldShowWordEditModal(true));
            dispatch(actions.setWordEditModalType(modalType, path));
            afterActionCb(false);
        } : null;

    const onNew = getOnAction(newModalType);
    const onEdit = getOnAction(editModalType);
    const onDelete = getOnAction(deleteModalType);

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

    const buttonPostioning = {
        left: `${buttonsCoordinates.x}px`,
        top: `${buttonsCoordinates.y}px`,
    };

    return show && (
        <div className={classes.ButtonBox} style={buttonPostioning}>
            {newButton}
            {editButton}
            {deleteButton}
        </div>
    );
};

ButtonBox.propTypes = {
    editModalType: PropTypes.string,
    deleteModalType: PropTypes.string,
    newModalType: PropTypes.string,
    show: PropTypes.bool,
    afterActionCb: PropTypes.func.isRequired,
    path: PropTypes.arrayOf(PropTypes.string),
    buttonsCoordinates: PropTypes.shape({
        x: PropTypes.number.isRequired,
        y: PropTypes.number.isRequired,
    }),
};

ButtonBox.defaultProps = {
    show: false,
    path: [],
    buttonsCoordinates: { x: 0, y: 0 },
};

export default ButtonBox;
