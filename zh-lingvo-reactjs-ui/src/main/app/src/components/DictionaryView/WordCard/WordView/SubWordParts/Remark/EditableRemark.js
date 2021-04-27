import React, { Fragment } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import { IconButton, iconButtonTypes, buttonSizes } from '../../../../../UI';
import { REMARK_NEW, REMARK_EDIT, REMARK_DELETE } from '../../../../../../static/constants/wordEditModalTypes';
import Editing from '../Editing';
import Remark from './Remark';
import * as selectors from '../../../../../../store/selectors';
import * as actions from '../../../../../../store/actions';

const EditableRemark = props => {
    const { value: remark, path } = props;
    const isEditing = useSelector(selectors.isEditingSelector);
    const dispatch = useDispatch();

    if (isEditing && !remark) {
        const onNew = () => {
            dispatch(actions.shouldShowWordEditModal(true));
            dispatch(actions.setWordEditModalType(REMARK_NEW, path));
        };
        return <Fragment>
            <IconButton
                type={iconButtonTypes.NEW}
                size={buttonSizes.SMALL}
                clicked={onNew}
            />
            {' '}
        </Fragment>
    }

    return (
        <Editing
            path={path}
            editModalType={REMARK_EDIT}
            deleteModalType={REMARK_DELETE}
        >
            <Remark value={remark} />
        </Editing>
    );
};

EditableRemark.propTypes = {
    value: PropTypes.string,
    path: PropTypes.arrayOf(PropTypes.string),
};

EditableRemark.defaultProps = {
    path: [],
};

export default EditableRemark;
