import React from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';

import { quizRecordIsEditingSelector } from '../../../../../store/selectors';
import { OnHoverEditableBase } from '../../../../UI';
import { setQuizRecordEditModalType, shouldShowQuizRecordEditModal } from '../../../../../store/actions';

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
    const isEditingDefault = useSelector(quizRecordIsEditingSelector);
    const isEditing = isEditingProps === null ? isEditingDefault : isEditingProps;

    return (
        <OnHoverEditableBase 
            newModalType={newModalType}
            editModalType={editModalType}
            deleteModalType={deleteModalType}
            block={block}
            isEditing={isEditing}
            modalTypeToAction={modalType => {
                dispatch(shouldShowQuizRecordEditModal(true));
                dispatch(setQuizRecordEditModalType(modalType, path))
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
