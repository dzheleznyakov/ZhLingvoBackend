import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './Transcription.module.scss';

import { IconButton, iconButtonTypes, buttonSizes } from '../../../../../UI';
import * as selectors from '../../../../../../store/selectors';
import * as actions from '../../../../../../store/actions';
import Editing from '../Editing';
import { TRANSCRIPTION_DELETE, TRANSCRIPTION_EDIT, TRANSCRIPTION_NEW } from '../../../../../../static/constants/wordEditModalTypes';

const Transcription = props => {
    const { children: transcription, parentPath } = props;
    const isEditing = useSelector(selectors.isEditingSelector);
    const dispatch = useDispatch();

    const path = [...parentPath, 'transcription'];

    const onNew = isEditing && !transcription ? () => {
        dispatch(actions.shouldShowWordEditModal(true));
        dispatch(actions.setWordEditModalType(TRANSCRIPTION_NEW, path));
    } : null;

    if (isEditing && !transcription)
        return <IconButton
            type={iconButtonTypes.NEW}
            size={buttonSizes.SMALL}
            clicked={onNew}
        />;

    return transcription && (
        <Editing
            editModalType={TRANSCRIPTION_EDIT}
            deleteModalType={TRANSCRIPTION_DELETE}
            path={path}
        >
            <div className={classes.Transcription}>
                [{transcription}]
            </div>
        </Editing>
    );
};

Transcription.propTypes = {
    children: PropTypes.string,
    parentPath: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default Transcription;
