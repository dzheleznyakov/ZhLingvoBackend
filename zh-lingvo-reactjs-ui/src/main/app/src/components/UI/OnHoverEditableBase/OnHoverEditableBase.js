import React, { useState } from 'react';
import PropTypes from 'prop-types';

import classes from './OnHoverEditableBase.module.scss';
import ButtonBox from './ButtonBox';

const OnHoverEditableBase = props => {
    const {
        children,
        editModalType,
        deleteModalType,
        newModalType,
        block,
        isEditing,
        modalTypeToAction,
    } = props;
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
        show={hovered}
        buttonsCoordinates={buttonsCoordinates}
        afterActionCb={() => setHovered(false)}
        modalTypeToAction={modalTypeToAction}
        isEditing={isEditing}
    />;

    const className = isEditing ? classes.OnHoverEditableBase : null;
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

OnHoverEditableBase.propTypes = {
    children: PropTypes.node.isRequired,
    editModalType: PropTypes.string,
    deleteModalType: PropTypes.string,
    newModalType: PropTypes.string,
    block: PropTypes.bool,
    isEditing: PropTypes.bool,
    modalTypeToAction: PropTypes.func.isRequired,
};

OnHoverEditableBase.defaultProps = {
    isEditing: false,
};

export default OnHoverEditableBase;
