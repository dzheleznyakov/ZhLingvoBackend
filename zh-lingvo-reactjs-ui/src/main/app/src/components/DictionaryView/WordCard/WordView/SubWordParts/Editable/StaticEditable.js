import React, { useRef, useState } from 'react';
import { useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './Editable.module.scss';

import ButtonBox from './ButtonBox';
import * as selectors from '../../../../../../store/selectors';

const StaticEditable = props => {
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
    const tagRef = useRef();

    const onHovered = isEditing ? () => {
        setHovered(true);
        const boundingRect = tagRef.current.getBoundingClientRect()//event.nativeEvent.target.getBoundingClientRect();
        const { y, right } = boundingRect
        setButtonsCoordinates({ x: right - 52, y: y - 25 });
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

    const className = isEditing ? classes.StaticEditable : null;

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

StaticEditable.propTypes = {
    children: PropTypes.node.isRequired,
    editModalType: PropTypes.string,
    deleteModalType: PropTypes.string,
    newModalType: PropTypes.string,
    path: PropTypes.arrayOf(PropTypes.string),
    block: PropTypes.bool,
};

StaticEditable.defaultProps = {
    path: [],
};

export default StaticEditable;
