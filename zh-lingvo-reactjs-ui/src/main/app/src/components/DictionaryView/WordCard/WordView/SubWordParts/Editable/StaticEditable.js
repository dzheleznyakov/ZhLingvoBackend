import React, {  } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import * as selectors from '../../../../../../store/selectors';
import * as actions from '../../../../../../store/actions';
import { StaticEditableBase } from '../../../../../UI';

const StaticEditable = props => {
    const { 
        children, 
        newModalType,
        editModalType, 
        deleteModalType,
        redirectModalType,
        path, 
        block,
    } = props;
    const dispatch = useDispatch();
    const isEditing = useSelector(selectors.isEditingSelector);
    return (
        <StaticEditableBase
            newModalType={newModalType}
            editModalType={editModalType}
            deleteModalType={deleteModalType}
            redirectModalType={redirectModalType}
            block={block}
            isEditing={isEditing}
            modalTypeToAction={modalType => {
                dispatch(actions.shouldShowWordEditModal(true));
                dispatch(actions.setWordEditModalType(modalType, path));
            }}
        >
            {children}
        </StaticEditableBase>
    );
};

StaticEditable.propTypes = {
    children: PropTypes.node.isRequired,
    newModalType: PropTypes.string,
    editModalType: PropTypes.string,
    deleteModalType: PropTypes.string,
    redirectModalType: PropTypes.string,
    path: PropTypes.arrayOf(PropTypes.string),
    block: PropTypes.bool,
};

StaticEditable.defaultProps = {
    path: [],
};

export default StaticEditable;
