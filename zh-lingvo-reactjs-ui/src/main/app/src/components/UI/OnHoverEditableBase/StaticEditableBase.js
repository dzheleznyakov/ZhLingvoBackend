import React, { useRef, useState } from 'react';
import PropTypes from 'prop-types';

import classes from './EditableBase.module.scss';
import ButtonBox from './ButtonBox';

const StaticEditableBase = props => {
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
    const tagRef = useRef();

    const onHovered = isEditing ? () => {
        setHovered(true);
        const boundingRect = tagRef.current.getBoundingClientRect();
        const { y, right } = boundingRect;
        setButtonsCoordinates({ x: right - 52, y: y - 25 });
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

    const className = isEditing ? classes.StaticEditableBase : null;
    const Tag = block ? 'div' : 'span';

    return (
        <Tag
            ref={tagRef}
            className={className}
            onMouseEnter={onHovered}
            onMouseLeave={onUnhovered}
        >
            {children}
            {buttons}
        </Tag>
    );
};

StaticEditableBase.propTypes = {
    children: PropTypes.node.isRequired,
    editModalType: PropTypes.string,
    deleteModalType: PropTypes.string,
    newModalType: PropTypes.string,
    block: PropTypes.bool,
    isEditing: PropTypes.bool,
    modalTypeToAction: PropTypes.func.isRequired,
};

StaticEditableBase.defaultProps = {
    isEditing: false,
};

export default StaticEditableBase;
