import React, { Fragment } from 'react';
import { useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import { REMARK_NEW, REMARK_EDIT, REMARK_DELETE } from '../../../../../../static/constants/wordEditModalTypes';
import Editing from '../Editing';
import NewPartButton from '../NewPartButton';
import Remark from './Remark';
import * as selectors from '../../../../../../store/selectors';

const EditableRemark = props => {
    const { value: remark, path } = props;
    const isEditing = useSelector(selectors.isEditingSelector);

    if (isEditing && !remark)
        return (<Fragment>
            <NewPartButton
                label="remark"
                modalType={REMARK_NEW}
                path={path}
            />
            {' '}
        </Fragment>)

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
