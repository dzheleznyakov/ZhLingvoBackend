import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import { IconButton, iconButtonTypes, buttonSizes } from '../../../../../UI';
import * as selectors from '../../../../../../store/selectors';
import * as actions from '../../../../../../store/actions';
import Editing from '../Editing';
import Transcription from './Transcription';
import { TRANSCRIPTION_DELETE, TRANSCRIPTION_EDIT, TRANSCRIPTION_NEW } from '../../../../../../static/constants/wordEditModalTypes';

const EditableTranscription = props => {
    const { children: transcription, path } = props;
    const isEditing = useSelector(selectors.isEditingSelector);
    const dispatch = useDispatch();

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
            <Transcription>{transcription}</Transcription>
        </Editing>
    );
};

EditableTranscription.propTypes = {
    children: PropTypes.string,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default EditableTranscription;
