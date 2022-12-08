import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import * as selectors from '../../../../../../store/selectors';
import * as actions from '../../../../../../store/actions';
import {OnHoverEditableBase} from '../../../../../UI';

const OnHoverEditable = props => {
    const { 
        children, 
        editModalType, 
        deleteModalType,
        newModalType,
        path, 
        block,
        isEditing: isEditingProps,
    } = props;
    const dispatch = useDispatch();
    const isEditingDefault = useSelector(selectors.isEditingSelector);
    const isEditing = isEditingProps === null ? isEditingDefault : isEditingProps;


    return (
        <OnHoverEditableBase
            newModalType={newModalType}
            editModalType={editModalType}
            deleteModalType={deleteModalType}
            block={block}
            isEditing={isEditing}
            modalTypeToAction={modalType => {
                dispatch(actions.shouldShowWordEditModal(true));
                dispatch(actions.setWordEditModalType(modalType, path));
            }}
        >
            {children}
        </OnHoverEditableBase>
    );
};

OnHoverEditable.propTypes = {
    children: PropTypes.node.isRequired,
    editModalType: PropTypes.string,
    deleteModalType: PropTypes.string,
    newModalType: PropTypes.string,
    path: PropTypes.arrayOf(PropTypes.string),
    block: PropTypes.bool,
    isEditing: PropTypes.bool,
};

OnHoverEditable.defaultProps = {
    path: [],
    isEditing: null,
};

export default OnHoverEditable;
