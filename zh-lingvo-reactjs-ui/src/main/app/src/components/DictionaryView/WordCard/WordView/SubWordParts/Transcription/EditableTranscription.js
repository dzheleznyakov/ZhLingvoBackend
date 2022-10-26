import React from 'react';
import { useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import * as selectors from '../../../../../../store/selectors';
import OnHoverEditable from '../Editable/OnHoverEditable';
import NewPartButton from '../NewPartButton';
import Transcription from './Transcription';
import { TRANSCRIPTION_DELETE, TRANSCRIPTION_EDIT, TRANSCRIPTION_NEW } from '../../../../../../static/constants/wordEditModalTypes';

const EditableTranscription = props => {
    const { children: transcription, path } = props;
    const isEditing = useSelector(selectors.isEditingSelector);

    if (isEditing && !transcription)
        return <NewPartButton
            label="transcription"
            modalType={TRANSCRIPTION_NEW}
            path={path}
        />;

    return (
        <OnHoverEditable
            editModalType={TRANSCRIPTION_EDIT}
            deleteModalType={TRANSCRIPTION_DELETE}
            path={path}
            block={true}
        >
            <Transcription>{transcription}</Transcription>
        </OnHoverEditable>
    );
};

EditableTranscription.propTypes = {
    children: PropTypes.string,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default EditableTranscription;
