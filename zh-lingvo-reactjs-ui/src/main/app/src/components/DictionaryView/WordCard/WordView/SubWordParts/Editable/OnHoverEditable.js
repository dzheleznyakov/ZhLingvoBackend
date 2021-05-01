import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './Editable.module.scss';

import ButtonBox from './ButtonBox';
import * as selectors from '../../../../../../store/selectors';

const OnHoverEditable = props => {
    const { 
        children, 
        editModalType, 
        deleteModalType,
        newModalType,
        path, 
        block,
    } = props;
    const isEditing = useSelector(selectors.isEditingSelector);
    const [hovered, setHovered] = useState(false);
    const [buttonsCoordinates, setButtonsCoordinates] = useState({ x: 0, y: 0});

    const onHovered = isEditing ? event => {
        setHovered(true);
        const { clientX: x, clientY: y } = event;
        setButtonsCoordinates({ x, y });
    } : null;

    const onUnhovered = isEditing ? () => {
        setHovered(false);
    } : null;

    const buttons = <ButtonBox
        newModalType={newModalType}
        editModalType={editModalType}
        deleteModalType={deleteModalType}
        path={path}
        show={hovered}
        buttonsCoordinates={buttonsCoordinates}
        afterActionCb={() => setHovered(false)}
    />

    const className = isEditing ? classes.OnHoverEditable : null;

    const Tag = block ? 'div' : 'span';
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

OnHoverEditable.propTypes = {
    children: PropTypes.node.isRequired,
    editModalType: PropTypes.string,
    deleteModalType: PropTypes.string,
    newModalType: PropTypes.string,
    path: PropTypes.arrayOf(PropTypes.string),
    block: PropTypes.bool,
};

OnHoverEditable.defaultProps = {
    path: [],
};

export default OnHoverEditable;
