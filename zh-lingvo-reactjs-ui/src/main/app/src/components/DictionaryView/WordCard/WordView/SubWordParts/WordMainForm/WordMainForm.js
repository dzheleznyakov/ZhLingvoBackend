import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './WordMainForm.module.scss';

import { IconButton, iconButtonTypes, buttonSizes } from '../../../../../UI';
import * as selectors from '../../../../../../store/selectors';
import * as actions from '../../../../../../store/actions';
import { MAIN_FORM } from '../../../../../../static/constants/wordEditModalTypes';

const WordMainForm = props => {
    const { children: mainForm } = props;
    const isEditing = useSelector(selectors.isEditingSelector);
    const [hovered, setHovered] = useState(false);
    const [buttonsCoordinates, setButtonsCoordinates] = useState({ x: 0, y: 0 });
    const dispatch = useDispatch();

    const classNames = [classes.WordMainForm];
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
        dispatch(actions.setWordEditModalType(MAIN_FORM));
        setHovered(false);
    } : null;

    const editButton = hovered && (
        <IconButton 
            type={iconButtonTypes.EDIT} 
            size={buttonSizes.SMALL}
            clicked={onEdit}
        />
    );

    const buttonsStyle = { 
        left: `${buttonsCoordinates.x}px`, 
        top: `${buttonsCoordinates.y}px`,
    };
    const buttons = hovered && (
        <div className={classes.ButtonBox} style={buttonsStyle}>
            {editButton}
        </div>
    );

    return (
        <div 
            className={classNames.join(' ')}
            onMouseEnter={onHovered}
            onMouseLeave={onUnhovered}
        >
            {mainForm}
            {buttons}
        </div>
    );
};

WordMainForm.propTypes = {
    children: PropTypes.string.isRequired,
};

export default WordMainForm;
